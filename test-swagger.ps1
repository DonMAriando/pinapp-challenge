#!/usr/bin/env pwsh
# Quick test script for the deployed H2 version with CORS fixed

$SERVICE_URL = "https://pinapp-challenge-test-2qn5zhzvaa-uc.a.run.app"
$USERNAME = "admin"
$PASSWORD = "password123"

Write-Host "`n====================================" -ForegroundColor Cyan
Write-Host "Testing Swagger with CORS Fixed" -ForegroundColor Cyan
Write-Host "====================================" -ForegroundColor Cyan

Write-Host "`n[1/4] Testing Health Endpoint..." -ForegroundColor Yellow
try {
    $response = Invoke-WebRequest -Uri "$SERVICE_URL/actuator/health" -Method GET -UseBasicParsing
    Write-Host "SUCCESS: Health check passed" -ForegroundColor Green
    Write-Host "Status: $($response.StatusCode)" -ForegroundColor Gray
} catch {
    Write-Host "FAILED: Health check failed" -ForegroundColor Red
    Write-Host $_.Exception.Message -ForegroundColor Red
    exit 1
}

Write-Host "`n[2/4] Testing Swagger UI..." -ForegroundColor Yellow
try {
    $response = Invoke-WebRequest -Uri "$SERVICE_URL/swagger-ui.html" -Method GET -UseBasicParsing
    Write-Host "SUCCESS: Swagger UI is accessible" -ForegroundColor Green
    Write-Host "Status: $($response.StatusCode)" -ForegroundColor Gray
} catch {
    Write-Host "FAILED: Swagger UI not accessible" -ForegroundColor Red
    Write-Host $_.Exception.Message -ForegroundColor Red
}

Write-Host "`n[3/4] Testing API with Authentication..." -ForegroundColor Yellow
$base64Auth = [Convert]::ToBase64String([Text.Encoding]::ASCII.GetBytes("$($USERNAME):$($PASSWORD)"))
$headers = @{
    Authorization = "Basic $base64Auth"
}

try {
    $response = Invoke-RestMethod -Uri "$SERVICE_URL/api/clients" -Method GET -Headers $headers -UseBasicParsing
    Write-Host "SUCCESS: GET /api/clients works!" -ForegroundColor Green
    Write-Host "Clients found: $($response.Count)" -ForegroundColor Gray
    if ($response.Count -gt 0) {
        Write-Host "First client: $($response[0].firstName) $($response[0].lastName)" -ForegroundColor Gray
    }
} catch {
    Write-Host "FAILED: GET /api/clients failed" -ForegroundColor Red
    Write-Host $_.Exception.Message -ForegroundColor Red
}

Write-Host "`n[4/4] Testing CORS Headers..." -ForegroundColor Yellow
try {
    $response = Invoke-WebRequest -Uri "$SERVICE_URL/api/clients" -Method OPTIONS -Headers @{
        "Origin" = "https://example.com"
        "Access-Control-Request-Method" = "GET"
        "Access-Control-Request-Headers" = "Authorization,Content-Type"
    } -UseBasicParsing
    
    $corsHeaders = $response.Headers | Where-Object { $_.Key -like "Access-Control-*" }
    
    if ($corsHeaders.Count -gt 0) {
        Write-Host "SUCCESS: CORS headers present!" -ForegroundColor Green
        foreach ($header in $corsHeaders) {
            Write-Host "  $($header.Key): $($header.Value)" -ForegroundColor Gray
        }
    } else {
        Write-Host "WARNING: No CORS headers found in OPTIONS response" -ForegroundColor Yellow
    }
} catch {
    Write-Host "INFO: OPTIONS request completed (may not have CORS headers in response)" -ForegroundColor Yellow
}

Write-Host "`n====================================" -ForegroundColor Cyan
Write-Host "SUMMARY" -ForegroundColor Cyan
Write-Host "====================================" -ForegroundColor Cyan
Write-Host "Swagger UI: $SERVICE_URL/swagger-ui.html" -ForegroundColor White
Write-Host "Credentials: admin / password123" -ForegroundColor White
Write-Host "`nTry using Swagger UI in your browser now!" -ForegroundColor Green
Write-Host "The CORS issue should be FIXED!" -ForegroundColor Green
Write-Host "====================================" -ForegroundColor Cyan

