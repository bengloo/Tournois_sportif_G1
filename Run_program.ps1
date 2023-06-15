<#$DocumentsPath = Join-Path $env:USERPROFILE 'Documents'
Write-Output $DocumentsPath#>
$filePath = Join-Path $PSScriptRoot "Location.txt"
$documentsPath = Get-Content -Path $filePath
$resultat = $DocumentsPath + '\' + $args[0]
Write-Output $resultat
java -jar Tournois_sportif_G1.jar $resultat 
Write-Host "Execution Done! Check our webapp!!!!!!"
$documentsPath = "ExecutionDone"
$fileName = "ExecutionDone.txt"
$filePath = Join-Path (Get-Location) $fileName
Set-Content -Path $filePath -Value $documentsPath

Read-Host -Prompt "Press Enter to exit"
<#
	public static String inFile;
	
	readArgs(args);
	
	public static void readArgs(String args[]) {
        if (args.length < 1) {
            printUsage();
            System.exit(-1);
        }

        int index = -1;

        inFile = args[++index];
        }
    public static void printUsage() {
        System.out.println("Usage: java -jar Tournois_sportif_G1.jar <input>");
    }
#>