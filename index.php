<!-- Source: https://codepen.io/kbocz/pen/vbBEBN -->

<!DOCTYPE html>
<meta charset="utf-8">

<title> Visualisation des solutions </title>
<html>
    <head>
        <link rel="stylesheet" href="./style.css">
    </head>
    <body>
        <div class="frame" id="content">
            <div class="center">
                <div class="title">
                   <h1>Déposez un fichier texte d'instance</h1>
                </div>

                <div class="dropzone">
                    <img src="http://100dayscss.com/codepen/upload.svg" class="upload-icon"/>
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

function getFileName() {
    const fileInput = document.getElementById('fileInput');
    fileName = fileInput.files[0].name;
}


function submitForm() {
    getFileName();

    // Remplir un champ de formulaire invisible avec la valeur de la variable JavaScript
    document.getElementById('jsVariableInput').value = fileName;

    // Soumettre le formulaire
    document.getElementById('myForm').submit();
}

////////////////////////////////////////////////////////////////////////////////  Code qui affiche un menu déroulant affichant les fichiers d'un dossier
// TODO: dès que le fichier execution_done a été créé, on va 
// lister tous les fichiers dans un menu (ou prendre le + récent?)

<!DOCTYPE html>
<html>
<head>
  <style>
    /* Style du menu déroulant */
    select {
      width: 300px;
      padding: 5px;
      font-size: 16px;
    }
  </style>
</head>
<body>
  <label for="fileSelect">Sélectionnez un fichier :</label>
  <select id="fileSelect"></select>

  <script>
    // Fonction pour charger les fichiers d'un dossier
    function loadFiles() {
      var fileSelect = document.getElementById("fileSelect");
      var folderPath = "chemin/vers/votre/dossier"; // Remplacez cela par le chemin vers votre dossier

      // Appel à une API côté serveur pour obtenir la liste des fichiers dans le dossier
      // Ici, nous utilisons l'API Fetch pour effectuer une requête AJAX
      fetch("/get-files?folderPath=" + encodeURIComponent(folderPath))
        .then(response => response.json())
        .then(data => {
          // Parcourt les fichiers retournés et ajoute-les en tant qu'options du menu déroulant
          data.forEach(file => {
            var option = document.createElement("option");
            option.value = file;
            option.text = file;
            fileSelect.appendChild(option);
          });
        })
        .catch(error => {
          console.log("Une erreur s'est produite lors du chargement des fichiers :", error);
        });
    }

    // Appel de la fonction pour charger les fichiers au chargement de la page
    window.onload = loadFiles;
  </script>
</body>
</html>
// Ce code crée un menu déroulant (select) qui sera rempli avec les noms de fichiers retournés par l'API côté serveur. 
// L'API côté serveur est responsable de récupérer la liste des fichiers dans le dossier spécifié. 
// Notez que cet exemple suppose que vous disposez d'une API côté serveur pour récupérer la liste des fichiers dans le dossier spécifié. La mise en place de cette API dépend du langage et du framework que vous utilisez côté serveur.


//////////////////////////////////////////////

window.addEventListener('DOMContentLoaded', function() {
    // Vérifier périodiquement la présence du fichier
    setInterval(checkFilePresence, 1000);
});

function checkFilePresence() {
    var xhr = new XMLHttpRequest();
    xhr.onreadystatechange = function() {
        if (xhr.readyState === 4) {
            if (xhr.status === 200) {
                // Le fichier est présent, afficher la page
                window.location.href = "./output.php";
            } else {
                // Le fichier n'est pas encore présent
                document.getElementById('content').innerHTML = "<p>Le fichier n'est pas encore présent.</p>";
            }
        }
    };
    xhr.open('HEAD', './solutions/execution_done.ps1', true);
    xhr.send();
}
</script>

<?php
    if ($_SERVER['REQUEST_METHOD'] === 'POST' && isset($_POST['fileName'])) {
        // Récupérer la valeur de la variable JavaScript envoyée via le formulaire
        $fileName = $_POST['fileName'];

        echo "La valeur de la variable JavaScript est : " . $fileName;
        
        $commande = 'powershell -InputFormat none -ExecutionPolicy ByPass -NoProfile -Command "& { .\Run_program.ps1 ' . $fileName;
        $commande = $commande . '; }"';
    // echo $commande;
        echo "<pre>";
        echo Shell_Exec($commande);
        echo "</pre>";
    }
?>