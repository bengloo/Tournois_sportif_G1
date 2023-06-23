<?php
    // TODO: changer le répertoire
    $directory = '../resultats/SolveurIter(SolveurCplex)';
    $filename = $_GET['param'];
            
    $file_path = $directory . '/' . $filename;

    // Vérifie si le fichier existe
    if (file_exists($file_path)) {
        $file_lines = file($file_path); // Lit le contenu du fichier et le stocke dans un tableau
        
        // Parcours du tableau de lignes
        foreach ($file_lines as $line) {
            // Traitement de chaque ligne
            echo $line;
        }
    } else {
        echo 'Le fichier n\'existe pas.';
    }
?>