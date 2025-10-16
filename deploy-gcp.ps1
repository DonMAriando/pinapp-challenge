# GCP Deployment Script for PinApp Challenge (Windows PowerShell)
# This script automates the deployment of the application to Google Cloud Run

$ErrorActionPreference = "Stop"

Write-Host "===========================================
PinApp Challenge - GCP Deployment Script
===========================================" -ForegroundColor Cyan
Write-Host ""

# Check if gcloud is installed
try {
    $null = gcloud version 2>$null
} catch {
    Write-Host "❌ Error: gcloud CLI is not installed" -ForegroundColor Red
    Write-Host "Please install it from: https://cloud.google.com/sdk/docs/install" -ForegroundColor Yellow
    exit 1
}

# Get project ID
$PROJECT_ID = gcloud config get-value project 2>$null
if ([string]::IsNullOrEmpty($PROJECT_ID)) {
    Write-Host "❌ Error: No GCP project configured" -ForegroundColor Red
    Write-Host "Run: gcloud config set project YOUR_PROJECT_ID" -ForegroundColor Yellow
    exit 1
}

Write-Host "✅ Using project: $PROJECT_ID" -ForegroundColor Green
Write-Host ""

# Variables
$REGION = "us-central1"
$SERVICE_NAME = "pinapp-challenge"
$REPO_NAME = "pinapp-repo"
$IMAGE_NAME = "us-central1-docker.pkg.dev/$PROJECT_ID/$REPO_NAME/${SERVICE_NAME}:latest"

# Ask for Cloud SQL password if not set
if ([string]::IsNullOrEmpty($env:DB_PASSWORD)) {
    Write-Host "WARNING: Database password not set" -ForegroundColor Yellow
    $securePassword = Read-Host "Enter Cloud SQL password (will be saved to Secret Manager)" -AsSecureString
    $BSTR = [System.Runtime.InteropServices.Marshal]::SecureStringToBSTR($securePassword)
    $DB_PASSWORD = [System.Runtime.InteropServices.Marshal]::PtrToStringAuto($BSTR)
} else {
    $DB_PASSWORD = $env:DB_PASSWORD
}

Write-Host ""
Write-Host "Deployment Configuration:" -ForegroundColor Cyan
Write-Host "   Project ID: $PROJECT_ID"
Write-Host "   Region: $REGION"
Write-Host "   Service: $SERVICE_NAME"
Write-Host "   Image: $IMAGE_NAME"
Write-Host ""

$confirmation = Read-Host "Continue with deployment? (y/n)"
if ($confirmation -ne 'y' -and $confirmation -ne 'Y') {
    Write-Host "Deployment cancelled" -ForegroundColor Yellow
    exit 0
}

Write-Host ""
Write-Host "[Step 1/6] Enabling required APIs..." -ForegroundColor Cyan
gcloud services enable run.googleapis.com sqladmin.googleapis.com secretmanager.googleapis.com artifactregistry.googleapis.com cloudbuild.googleapis.com

Write-Host ""
Write-Host "[Step 2/6] Setting up Cloud SQL instance..." -ForegroundColor Cyan
try {
    $null = gcloud sql instances describe pinapp-db 2>$null
    Write-Host "Cloud SQL instance already exists" -ForegroundColor Green
} catch {
    Write-Host "Creating Cloud SQL instance (this may take 5-10 minutes)..." -ForegroundColor Yellow
    gcloud sql instances create pinapp-db `
      --database-version=POSTGRES_15 `
      --tier=db-f1-micro `
      --region=$REGION `
      --root-password="$DB_PASSWORD" `
      --storage-size=10GB `
      --storage-type=HDD
    
    Write-Host "Creating database..." -ForegroundColor Yellow
    gcloud sql databases create clientdb --instance=pinapp-db
}

$CONNECTION_NAME = gcloud sql instances describe pinapp-db --format='value(connectionName)'
Write-Host "Cloud SQL connection name: $CONNECTION_NAME" -ForegroundColor Green

Write-Host ""
Write-Host "[Step 3/6] Setting up secrets in Secret Manager..." -ForegroundColor Cyan

# Create db-password secret
try {
    $null = gcloud secrets describe db-password 2>$null
    Write-Host "db-password secret already exists" -ForegroundColor Green
} catch {
    Write-Host "Creating db-password secret..." -ForegroundColor Yellow
    $DB_PASSWORD | gcloud secrets create db-password --data-file=-
}

# Create api-password secret
try {
    $null = gcloud secrets describe api-password 2>$null
    Write-Host "api-password secret already exists" -ForegroundColor Green
} catch {
    Write-Host "Creating api-password secret..." -ForegroundColor Yellow
    '$2a$10$1AuLGVy1VJjdHCZTJYvpm.XtdDHUZEkmv22cEsnnCLo0YzUbaDXMK' | gcloud secrets create api-password --data-file=-
}

# Grant permissions
$PROJECT_NUMBER = gcloud projects describe $PROJECT_ID --format='value(projectNumber)'
Write-Host "Granting secret access to Cloud Run service account..." -ForegroundColor Yellow

gcloud secrets add-iam-policy-binding db-password `
  --member="serviceAccount:${PROJECT_NUMBER}-compute@developer.gserviceaccount.com" `
  --role="roles/secretmanager.secretAccessor" --quiet

gcloud secrets add-iam-policy-binding api-password `
  --member="serviceAccount:${PROJECT_NUMBER}-compute@developer.gserviceaccount.com" `
  --role="roles/secretmanager.secretAccessor" --quiet

Write-Host ""
Write-Host "[Step 4/6] Creating Artifact Registry repository..." -ForegroundColor Cyan
try {
    $null = gcloud artifacts repositories describe $REPO_NAME --location=$REGION 2>$null
    Write-Host "Repository already exists" -ForegroundColor Green
} catch {
    Write-Host "Creating repository..." -ForegroundColor Yellow
    gcloud artifacts repositories create $REPO_NAME `
      --repository-format=docker `
      --location=$REGION `
      --description="PinApp Challenge Docker images"
}

# Configure Docker authentication
Write-Host "Configuring Docker authentication..." -ForegroundColor Yellow
gcloud auth configure-docker us-central1-docker.pkg.dev --quiet

Write-Host ""
Write-Host "[Step 5/6] Building Docker image..." -ForegroundColor Cyan
Write-Host "Using Cloud Build (no local Docker required)..." -ForegroundColor Yellow
gcloud builds submit --tag $IMAGE_NAME

Write-Host ""
Write-Host "[Step 6/6] Deploying to Cloud Run..." -ForegroundColor Cyan

# Construct the datasource URL (use single quotes to avoid PowerShell escaping issues)
$DATASOURCE_URL = 'jdbc:postgresql:///' + $CONNECTION_NAME + '/clientdb?cloudSqlInstance=' + $CONNECTION_NAME + '&socketFactory=com.google.cloud.sql.postgres.SocketFactory&user=postgres'

gcloud run deploy $SERVICE_NAME `
  --image $IMAGE_NAME `
  --platform managed `
  --region $REGION `
  --allow-unauthenticated `
  --add-cloudsql-instances $CONNECTION_NAME `
  --set-env-vars "SPRING_PROFILES_ACTIVE=prod" `
  --set-env-vars "SPRING_DATASOURCE_URL=$DATASOURCE_URL" `
  --set-env-vars "SPRING_DATASOURCE_USERNAME=postgres" `
  --set-secrets "SPRING_DATASOURCE_PASSWORD=db-password:latest" `
  --set-secrets "API_PASSWORD=api-password:latest" `
  --set-env-vars "API_USERNAME=admin" `
  --set-env-vars "API_ROLE=USER" `
  --memory 1Gi `
  --cpu 2 `
  --timeout 300 `
  --min-instances 0 `
  --max-instances 10 `
  --port 8080 `
  --cpu-boost

Write-Host ""
Write-Host "Deployment complete!" -ForegroundColor Green
Write-Host ""

# Get service URL
$SERVICE_URL = gcloud run services describe $SERVICE_NAME --region $REGION --format='value(status.url)'

Write-Host "Service URL: $SERVICE_URL" -ForegroundColor Cyan
Write-Host ""
Write-Host "Quick tests:" -ForegroundColor Cyan
Write-Host "   Health check:" -ForegroundColor Yellow
Write-Host "   curl $SERVICE_URL/actuator/health"
Write-Host ""
Write-Host "   API (auth required):" -ForegroundColor Yellow
Write-Host "   curl -u admin:password123 $SERVICE_URL/api/clients"
Write-Host ""
Write-Host "   Swagger UI (open in browser):" -ForegroundColor Yellow
Write-Host "   $SERVICE_URL/swagger-ui.html"
Write-Host ""
Write-Host "View logs:" -ForegroundColor Cyan
Write-Host "   gcloud run services logs read $SERVICE_NAME --region $REGION --limit 50"
Write-Host ""
Write-Host "Done!" -ForegroundColor Green

