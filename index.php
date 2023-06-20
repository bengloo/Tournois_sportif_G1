<!-- Source: https://codepen.io/kbocz/pen/vbBEBN -->

<!DOCTYPE html>
<meta charset="utf-8">

<title>Tournoi sportif G1  - Import instance</title>
<html>
    <head>
        <link rel="stylesheet" href="./style_index.css">
        <script src="jquery-3.7.0.js"></script>
    </head>
    <body>
        <form id="myForm" method="post" action="">
            <div class="frame" id="content">
                <div class="center" id="import">
                    <div class="title">
                    <h1>Déposez un fichier texte d'instance</h1>
                    </div>

                    <div class="dropzone">
                        <img src="http://100dayscss.com/codepen/upload.svg" class="upload-icon"/>
                        <span class="filename"></span>
                        <input type="file" class="upload-input" id="fileInput"/>
                    </div>
                    <input type="hidden" name="fileName" id="jsVariableInput">
                    <button type="button" class="btn" onclick="submitForm()">Soumettre</button>
                </div> 
                
                <div class="center" id="options">
                    <div class="title">
                        <h1>Sélectionnez les options voulues</h1>
                    </div>

                    <label> Choisir la valeur du Watchdog en secondes :</label>
                    <input type="number" name="watchdog" class="number-input" value="600">

                    <div class="switch-container">
                        <label for="minimize-hard">Minimiser contraintes dures</label>
                        <input type="checkbox" name="minimize-hard" id="minimize-hard" class="switch">
                    </div>

                    <div class="switch-container">
                        <label for="minimize-soft">Minimiser contraintes souples</label>
                        <input type="checkbox" name="minimize-soft" id="minimize-soft" class="switch">
                    </div>

                    <div class="switch-container">
                        <label for="disable-global-pause">Désactiver la contrainte de pause globale</label>
                        <input type="checkbox" name="disable-global-pause" id="disable-global-pause" class="switch">
                    </div>
                </div>
            </div>
        </form>
    </body>
</html>

<script>

$(document).ready(function() {
    // Écoute de l'événement "change" sur l'input file
    $('#fileInput').on('change', function(e) {
        // Récupérer le fichier sélectionné
        fileName = e.target.files[0].name;

        // Remplacer l'icône d'import par le nom du fichier
        $('.filename').html(fileName);
        $('.dropzone .upload-icon').hide();

        // Réglage de la position de l'input file selon la longueur du nom de fichier
        if ($('.filename').height() > 16) {
            $('.upload-input').css('top', '-41px');
        } else {
            $('.upload-input').css('top', '-21px');
        }
    });
});

function getFileName() {
    const fileInput = document.getElementById('fileInput');
    
    if (fileInput.files[0] != null) {
        fileName = fileInput.files[0].name;
        return fileName;
    } else {
        return null;
    }
}


function submitForm() {
    let counter = 0;
    fileName = getFileName();

    if (fileName != null && fileName.split('.').pop() == "txt") {
        // Remplir un champ de formulaire invisible avec la valeur de la variable JavaScript
        document.getElementById('jsVariableInput').value = fileName;

        // Soumettre le formulaire
        document.getElementById('myForm').submit();
    } else {
        $('#attenteImport').css('color', 'red');
        $('#attenteImport').html("Erreur : aucun fichier n'a été importé ou le fichier n'est pas au format .txt");
        const interval = setInterval(function() {
            $('#attenteImport').fadeToggle(500);
            counter += 500;
            if (counter >= 2000) {
                clearInterval(interval);
            }
        }, 500);
    }
}

window.addEventListener('DOMContentLoaded', function() {
    // Vérifier périodiquement la présence du fichier
    setInterval(checkFilePresence, 1000);
});

// Vérifie la présence d'un fichier
function checkFilePresence() {
    var xhr = new XMLHttpRequest();
    xhr.onreadystatechange = function() {
        if (xhr.readyState === 4) {
            if (xhr.status === 200) {
                // Le fichier est présent, afficher la page
                window.location.href = "./output.php";
            } else {
                // Le fichier n'est pas encore présent
                if ($('#attenteImport').length == 0) {
                    $('#import').append("<div id='attenteImport'>Veuillez importer un fichier d'instance au format .txt</div>");
                }
            }
        }
    };
    xhr.open('HEAD', 'ExecutionDone.txt', true);
    xhr.send();
}
</script>

<?php
    // Permet de déclencher le script Powershell pour exécuter notre application Java
    if ($_SERVER['REQUEST_METHOD'] === 'POST' && isset($_POST['fileName'])) {
        // Récupérer la valeur de la variable JavaScript envoyée via le formulaire
        $fileName = $_POST['fileName'];
        echo '<div id="loadingOverlay"><div class="loadingSpinner"></div></div>';
        
        if (!isset($_POST['minimize-hard'])) {
            $_POST['minimize-hard'] = 'off';
        } 
        if (!isset($_POST['minimize-soft'])) {
            $_POST['minimize-soft'] = 'off';
        } 
        if (!isset($_POST['disable-global-pause'])) {
            $_POST['disable-global-pause'] = 'off';
        } 
        
        print_r($_POST);
        // echo "La valeur de la variable JavaScript est : " . $fileName;
        
        $commande = 'powershell -InputFormat none -ExecutionPolicy ByPass -NoProfile -Command "& { .\Run_program.ps1 ' . $fileName;
        $commande = $commande . '; }"';
        // echo $commande;
        echo "<pre>";
        echo Shell_Exec($commande);
        echo "</pre>";
    }
?>