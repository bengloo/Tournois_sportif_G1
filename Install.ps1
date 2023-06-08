$documentsPath = [System.Environment]::GetFolderPath('MyDocuments')
$fileName = "Location.txt"
$filePath = Join-Path (Get-Location) $fileName

Set-Content -Path $filePath -Value $documentsPath