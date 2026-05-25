<#
.SYNOPSIS
  Ejecuta el orquestador SQL para crear la base de datos y crea el usuario `banco_user`.

.NOTES
  Ejecutar desde PowerShell con permisos suficientes. Se solicitará la contraseña de root (dejar vacía si no aplica).
#>

[CmdletBinding()]
param(
    [string]$MysqlExe = "C:\\xampp\\mysql\\bin\\mysql.exe",
    [string]$BaseDir = (Get-Location).Path,
    [string]$RootPass
)

Write-Host "Orquestador - directorio: $BaseDir" -ForegroundColor Cyan

if (-not $RootPass) {
    $RootPass = Read-Host -AsSecureString "Contraseña de MySQL 'root' (se ocultará la entrada)" | 
        ForEach-Object { [Runtime.InteropServices.Marshal]::PtrToStringAuto([Runtime.InteropServices.Marshal]::SecureStringToBSTR($_)) }
}

function Execute-SourceFile($exe, $rootPass, $file) {
    $path = Join-Path $BaseDir $file
    if (-not (Test-Path $path)) { throw "Archivo no encontrado: $path" }
    # mysql -e "SOURCE /full/path/00_orquestador_mysql.sql;"
    $full = (Resolve-Path $path).ProviderPath -replace '\\','/'
    & $exe -u root --password="$rootPass" -e "SOURCE $full;"
    if ($LASTEXITCODE -ne 0) { throw "Fallo ejecutando SOURCE $file" }
}

try {
    Push-Location $BaseDir
    Write-Host "Ejecutando orquestador via SOURCE: 00_orquestador_mysql.sql" -ForegroundColor Green
    Execute-SourceFile -exe $MysqlExe -rootPass $RootPass -file '00_orquestador_mysql.sql'

    Write-Host "Creando usuario de aplicación 'banco_user'..." -ForegroundColor Green
    $createUserSql = "CREATE USER IF NOT EXISTS 'banco_user'@'localhost' IDENTIFIED BY 'Banco123'; GRANT ALL PRIVILEGES ON banco_core.* TO 'banco_user'@'localhost'; FLUSH PRIVILEGES;"
    & $MysqlExe -u root --password="$RootPass" -e $createUserSql
    if ($LASTEXITCODE -ne 0) { throw "Error creando usuario 'banco_user'." }

    Write-Host "Orquestador y creación de usuario finalizados." -ForegroundColor Cyan
} catch {
    Write-Error $_.Exception.Message
} finally {
    Pop-Location
}
