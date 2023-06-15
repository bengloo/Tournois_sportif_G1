<?php
    // TODO: changer le répertoire
    $directory = '../solutions';

    // Vérifie si le répertoire existe
    if (is_dir($directory)) {
        $files = array_diff(scandir($directory), array('.', '..')); // Récupère la liste des fichiers en excluant les entrées '.' et '..'
        echo json_encode(array_values($files)); // Renvoie la liste des fichiers sous forme de tableau JSON
    } else {
        echo json_encode(array()); // Renvoie un tableau JSON vide si le répertoire n'existe pas
    }
?>