<?php
    switch($_GET['function']) {
        case 'listFiles':
            listFiles();

        case 'getFile':
            getFile($_GET['param']);
    }

    function listFiles() {
        // TODO: changer le répertoire
        $directory = './solutions';

        // Vérifie si le répertoire existe
        if (is_dir($directory)) {
            $files = array_diff(scandir($directory), array('.', '..')); // Récupère la liste des fichiers en excluant les entrées '.' et '..'
            echo json_encode(array_values($files)); // Renvoie la liste des fichiers sous forme de tableau JSON
        } else {
            echo json_encode(array()); // Renvoie un tableau JSON vide si le répertoire n'existe pas
        }
    }

    function getFile($filename) {
        // TODO: changer le répertoire
        $directory = './solutions';
        
        $file_path = $directory . '/' . $filename;

        // Vérifie si le fichier existe
        if (file_exists($file_path)) {
            $file_lines = file($file_path); // Lit le contenu du fichier et le stocke dans un tableau
            
            // Parcours du tableau de lignes
            foreach ($file_lines as $line) {
                // Traitement de chaque ligne
                echo $line . '<br>';
            }
        } else {
            echo 'Le fichier n\'existe pas.';
        }
    }
?>