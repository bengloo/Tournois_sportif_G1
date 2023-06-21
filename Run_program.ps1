<#$DocumentsPath = Join-Path $env:USERPROFILE 'Documents'
Write-Output $DocumentsPath#>
$filePath = Join-Path $PSScriptRoot "Location.txt"
$documentsPath = Get-Content -Path $filePath
$resultat = $DocumentsPath + '\' + $args[0]
$watchDog = $args[1]
$minimiseDure = $args[2]
$minimiseSouple = $args[3]
$avoidContraintePauseGlobale = $args[4]
Write-Output $resultat
java -jar Tournois_sportif_G1.jar $resultat $watchDog $minimiseDure $minimiseSouple $avoidContraintePauseGlobale
Write-Host "Execution Done! Check our webapp!!!!!!"
$documentsPath = "ExecutionDone"
$fileName = "ExecutionDone.txt"
$filePath = Join-Path (Get-Location) $fileName
Set-Content -Path $filePath -Value $documentsPath

Read-Host -Prompt "Press Enter to exit"