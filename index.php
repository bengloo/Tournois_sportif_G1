<!-- Source: https://codepen.io/kbocz/pen/vbBEBN -->

<!DOCTYPE html>
<meta charset="utf-8">

<title>Tournoi sportif G1  - Import instance</title>
<html>
    <head>
        <link rel="stylesheet" href="./style.css">
        <script src="jquery-3.7.0.js"></script>
    </head>
    <body>
        <div class="frame" id="content">
            <div class="center">
                <div class="title">
                   <h1>Déposez un fichier texte d'instance</h1>
                </div>

                <div class="dropzone">
                    <img src="http://100dayscss.com/codepen/upload.svg" class="upload-icon"/>
                    <span class="filename"></span>
                    <input type="file" class="upload-input" id="fileInput"/>
                </div>
                <form id="myForm" method="post" action="">
                    <input type="hidden" name="fileName" id="jsVariableInput">
                    <button type="button" class="btn" onclick="submitForm()">Soumettre</button>
                </form>
            </div> 
        </div>
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
    console.log(fileName);

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

// TODO: dès que le fichier execution_done a été créé, on va 
// rediriger vers output.php et y lister tous les fichiers dans un menu

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
                    $('.center').append("<div id='attenteImport'>Veuillez importer un fichier d'instance au format .txt</div>");
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
        echo "La valeur de la variable JavaScript est : " . $fileName;
        
        $commande = 'powershell -InputFormat none -ExecutionPolicy ByPass -NoProfile -Command "& { .\Run_program.ps1 ' . $fileName;
        $commande = $commande . '; }"';
        // echo $commande;
        echo "<pre>";
        echo Shell_Exec($commande);
        echo "</pre>";
    }
?>