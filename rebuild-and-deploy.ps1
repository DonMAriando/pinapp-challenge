# Rebuild with CORS fix and redeploy
$ErrorActionPreference = "Stop"

Write-Host "Rebuilding and Redeploying with CORS Fix" -ForegroundColor Cyan
Write-Host "=========================================" -ForegroundColor Cyan
Write-Host ""

$gcloud = "C:\Users\s7\AppData\Local\Google\Cloud SDK\google-cloud-sdk\bin\gcloud.cmd"
$PROJECT_ID = & $gcloud config get-value project 2>$null
$REGION = "us-central1"
$SERVICE_NAME = "pinapp-challenge-test"
$REPO_NAME = "pinapp-repo"
$IMAGE_NAME = "us-central1-docker.pkg.dev/$PROJECT_ID/$REPO_NAME/pinapp-challenge:latest"

Write-Host "Step 1: Building new image with CORS support..." -ForegroundColor Yellow
& $gcloud builds submit --tag $IMAGE_NAME

if ($LASTEXITCODE -ne 0) {
    Write-Host "Build failed!" -ForegroundColor Red
    exit 1
}

Write-Host ""
Write-Host "Step 2: Deploying updated version..." -ForegroundColor Yellow

& $gcloud run deploy $SERVICE_NAME `
  --image $IMAGE_NAME `
  --platform managed `
  --region $REGION `
  --allow-unauthenticated `
  --set-env-vars "SPRING_PROFILES_ACTIVE=dev" `
  --set-secrets "API_PASSWORD=api-password:latest" `
  --set-env-vars "API_USERNAME=admin,API_ROLE=USER" `
  --memory 1Gi `
  --cpu 2 `
  --timeout 300 `
  --min-instances 0 `
  --max-instances 5 `
  --port 8080 `
  --cpu-boost `
  --quiet

if ($LASTEXITCODE -eq 0) {
    Write-Host ""
    Write-Host "SUCCESS! Deployment complete" -ForegroundColor Green
    Write-Host ""
    
    $SERVICE_URL = & $gcloud run services describe $SERVICE_NAME --region $REGION --format='value(status.url)'
    Write-Host "App URL: $SERVICE_URL" -ForegroundColor Cyan
    Write-Host ""
    Write-Host "Swagger UI (with CORS fixed): $SERVICE_URL/swagger-ui.html" -ForegroundColor Green
    Write-Host ""
    Write-Host "Opening Swagger UI in 5 seconds..." -ForegroundColor Yellow
    Start-Sleep -Seconds 5
    Start-Process "$SERVICE_URL/swagger-ui.html"
    
    Write-Host ""
    Write-Host "Next: Deploy to production with Cloud SQL" -ForegroundColor Cyan
} else {
    Write-Host "Deployment failed!" -ForegroundColor Red
}

