# 🪟 Guía de Despliegue GCP para Windows

Esta guía está optimizada para usuarios de **Windows** que quieren desplegar en Google Cloud Platform.

## 📥 Paso 1: Instalar Google Cloud CLI

### Opción A: Instalador Windows (Recomendado)

1. **Descargar:** https://cloud.google.com/sdk/docs/install#windows
2. **Ejecutar:** `GoogleCloudSDKInstaller.exe`
3. **Seguir el asistente:**
   - ✅ Aceptar todos los valores por defecto
   - ✅ Marcar "Run 'gcloud init'"
   - ✅ Reiniciar PowerShell cuando termine

4. **Verificar instalación:**
   ```powershell
   gcloud version
   ```

### Opción B: Usando Chocolatey

Si tienes [Chocolatey](https://chocolatey.org/) instalado:

```powershell
choco install gcloudsdk
```

## 🔐 Paso 2: Configurar GCP

Abre **PowerShell** como administrador:

```powershell
# Inicializar gcloud (te abrirá el navegador para autenticarte)
gcloud init

# O manualmente:
gcloud auth login
gcloud config set project YOUR_PROJECT_ID
gcloud config set compute/region us-central1
```

**💡 Obtener tu Project ID:**
1. Ve a: https://console.cloud.google.com/
2. En la barra superior, al lado de "Google Cloud", verás tu proyecto
3. Copia el **Project ID** (no el nombre)

## 🚀 Paso 3: Desplegar con el Script PowerShell

```powershell
# Navega a tu proyecto
cd C:\Users\TU_USUARIO\Projects\java\pinapp-challenge

# Ejecuta el script de despliegue
.\deploy-gcp.ps1
```

El script te pedirá:
1. ✅ Contraseña para la base de datos PostgreSQL (créala ahora, debe ser segura)
2. ✅ Confirmación para continuar

Luego hará **todo automáticamente**:
- Habilitar APIs necesarias
- Crear Cloud SQL PostgreSQL
- Configurar Secret Manager
- Construir imagen Docker (en la nube, no necesitas Docker local)
- Desplegar a Cloud Run

⏱️ **Tiempo estimado:** 10-15 minutos

## 📊 Verificar el Despliegue

Después del despliegue, el script te dará una URL. Pruébala:

```powershell
# Obtener la URL de tu servicio
$SERVICE_URL = gcloud run services describe pinapp-challenge --region us-central1 --format='value(status.url)'

# Ver la URL
Write-Host "Tu app está en: $SERVICE_URL"

# Probar health check (con curl si lo tienes, o usa el navegador)
curl "$SERVICE_URL/actuator/health"

# Abrir Swagger UI en el navegador
Start-Process "$SERVICE_URL/swagger-ui.html"
```

**Si no tienes `curl` en Windows:**
```powershell
# Instalar curl (viene por defecto en Windows 10+)
# O usar Invoke-WebRequest:
Invoke-WebRequest -Uri "$SERVICE_URL/actuator/health"

# O simplemente abre en el navegador:
Start-Process "$SERVICE_URL/actuator/health"
```

## 📝 Comandos Útiles en PowerShell

### Ver logs en tiempo real
```powershell
gcloud run services logs tail pinapp-challenge --region us-central1
```

### Ver logs recientes
```powershell
gcloud run services logs read pinapp-challenge --region us-central1 --limit 50
```

### Ver información del servicio
```powershell
gcloud run services describe pinapp-challenge --region us-central1
```

### Ver estado de Cloud SQL
```powershell
gcloud sql instances describe pinapp-db
```

### Actualizar la aplicación (después de cambios en el código)
```powershell
# Construir nueva imagen
gcloud builds submit --tag us-central1-docker.pkg.dev/$(gcloud config get-value project)/pinapp-repo/pinapp-challenge:latest

# Desplegar nueva versión
gcloud run deploy pinapp-challenge --region us-central1
```

### Prueba la API con PowerShell
```powershell
# Definir credenciales
$user = "admin"
$pass = "password123"
$pair = "${user}:${pass}"
$bytes = [System.Text.Encoding]::ASCII.GetBytes($pair)
$base64 = [System.Convert]::ToBase64String($bytes)

# Obtener URL del servicio
$SERVICE_URL = gcloud run services describe pinapp-challenge --region us-central1 --format='value(status.url)'

# Hacer request a la API
Invoke-RestMethod -Uri "$SERVICE_URL/api/clients" -Headers @{Authorization="Basic $base64"}
```

## 🛠️ Troubleshooting

### Error: "gcloud: command not found"

**Solución:** Reinicia PowerShell después de instalar gcloud CLI.

### Error: "The term 'gcloud' is not recognized"

**Solución:** 
1. Cierra y abre PowerShell
2. O agrega manualmente al PATH:
   ```powershell
   $env:Path += ";C:\Program Files (x86)\Google\Cloud SDK\google-cloud-sdk\bin"
   ```

### Error: "Cannot run script because running scripts is disabled"

**Solución:** Habilitar ejecución de scripts en PowerShell:
```powershell
# Ejecutar PowerShell como Administrador
Set-ExecutionPolicy -ExecutionPolicy RemoteSigned -Scope CurrentUser
```

### Ver logs detallados del despliegue

```powershell
# Ver logs de Cloud Build
gcloud builds list --limit 5

# Ver detalles del último build
$BUILD_ID = (gcloud builds list --limit 1 --format='value(id)')
gcloud builds log $BUILD_ID
```

### La aplicación no inicia / Error 500

```powershell
# Ver logs para encontrar el error
gcloud run services logs read pinapp-challenge --region us-central1 --limit 100 | Select-String "ERROR"

# Ver si Flyway migró correctamente
gcloud run services logs read pinapp-challenge --region us-central1 --limit 200 | Select-String "Flyway"
```

## 🧹 Limpiar Recursos (Eliminar Todo)

⚠️ **Advertencia:** Esto eliminará todos los recursos y datos.

```powershell
# Eliminar servicio de Cloud Run
gcloud run services delete pinapp-challenge --region us-central1 --quiet

# Eliminar instancia de Cloud SQL
gcloud sql instances delete pinapp-db --quiet

# Eliminar secretos
gcloud secrets delete db-password --quiet
gcloud secrets delete api-password --quiet

# Eliminar repositorio de imágenes
gcloud artifacts repositories delete pinapp-repo --location us-central1 --quiet

# Ver costos actuales
# Ve a: https://console.cloud.google.com/billing
```

## 💰 Ver Costos

```powershell
# Abrir consola de facturación
Start-Process "https://console.cloud.google.com/billing"

# Abrir consola de Cloud Run (ver uso)
Start-Process "https://console.cloud.google.com/run"
```

## 🔄 CI/CD Automatizado (Opcional)

Si quieres que se despliegue automáticamente al hacer push:

1. **Conecta tu repositorio GitHub:**
   ```powershell
   Start-Process "https://console.cloud.google.com/cloud-build/triggers"
   ```

2. **Click "CREATE TRIGGER"**

3. **Configurar:**
   - Source: GitHub
   - Repository: Tu repo
   - Branch: `^main$`
   - Configuration: Cloud Build configuration file
   - Location: `cloudbuild.yaml`

4. **Hacer push a main:**
   ```powershell
   git add .
   git commit -m "Deploy to GCP"
   git push origin main
   ```

¡Listo! Cada push a `main` desplegará automáticamente.

## 📚 Recursos Adicionales

- **Documentación completa:** Ver `README.md`
- **Guía rápida multi-plataforma:** Ver `QUICKSTART-GCP.md`
- **GCP Console:** https://console.cloud.google.com/
- **Cloud Run Docs:** https://cloud.google.com/run/docs
- **Cloud SQL Docs:** https://cloud.google.com/sql/docs

## 🆘 ¿Necesitas Ayuda?

1. **Ver logs detallados:**
   ```powershell
   gcloud run services logs tail pinapp-challenge --region us-central1
   ```

2. **Verificar servicios habilitados:**
   ```powershell
   gcloud services list --enabled
   ```

3. **Verificar permisos:**
   ```powershell
   gcloud projects get-iam-policy $(gcloud config get-value project)
   ```

4. **Contacto y documentación:**
   - GCP Support: https://cloud.google.com/support
   - Stack Overflow: https://stackoverflow.com/questions/tagged/google-cloud-platform

¡Feliz despliegue! 🚀

