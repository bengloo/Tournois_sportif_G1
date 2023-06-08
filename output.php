<!-- Source: https://www.bootdey.com/snippets/view/bs4-event-timeline -->

<meta name="viewport" content="width=device-width, initial-scale=1">
<script src="jquery-3.7.0.js"></script>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@4.4.1/dist/css/bootstrap.min.css" rel="stylesheet">
<link rel="stylesheet" type="text/css" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.12.1/css/all.min.css">
<link rel='stylesheet' type='text/css' href='./style_output.css'>

<div class="container animated animated-done bootdey" data-animate="fadeIn" data-animate-delay="0.05" style="animation-delay: 0.05s;">
    <input type="file" name="readfile" id="readfile" />

    <hr class="hr-lg mt-0 mb-2 w-10 mx-auto hr-primary">
    <div class="timeline timeline-left mx-lg-10">
      
    </div>
<div> 
    


<input type="file" id="fileInput">
<!-- <button onclick="getFileName()">Obtenir le nom du fichier</button> -->


<script>
    fileName = '';
    let file = document.getElementById("readfile");
    
    file.addEventListener("change", function () {
        var reader = new FileReader();
        reader.onload = function (progressEvent) {
            var lines = this.result.split('\n'); // Divise le contenu en lignes
            var journees = []; // Tableau pour stocker les journées
            var journee = []; // Tableau pour stocker les lignes d'une journée
            var i = 0;
            
            lines.forEach(function(line) {
                if (line.trim() !== '' && !line.startsWith("//")) { // Vérifie si la ligne n'est pas vide
                    var values = line.split('   '); // Divise la ligne en valeurs (suppose que les valeurs sont séparées par une tabulation)
                    $('#box-'+(i-1)).append('<div class="timeline-item mt-3 row text-center p-2">'+
                        '<div class="col font-weight-bold text-md-right">'+values[0]+'</div>'+
                        '<div class="col-1">vs</div>'+
                        '<div class="col font-weight-bold text-md-left">'+values[1]+'</div>'+
                    '</div>');
                    journee.push(values.map(Number)); // Ajoute la ligne sous forme de tableau de nombres dans la journée
                } else if (!line.startsWith("//")) {
                    $('.timeline').append('<div class="box" id="box-'+i+'">'+
                                            '<div class="timeline-breaker">Journée '+i+'</div>'+
                                            '</div>');
                    journees.push(journee); // Ajoute la journée au tableau des journées
                    journee = []; // Réinitialise le tableau pour la prochaine journée
                    i++;
                }                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                           
            });
            // Vérifie s'il reste des lignes non ajoutées comme une journée finale
            if (journee.length > 0) {
                journees.push(journee);
            }

            // Supprime le 1er élément du tableau car il n'y a pas encore eu de journée parcourue à ce stage
            journees.shift();

            // Affiche le tableau des journées dans la console
            console.log(journees);
        };
        reader.readAsText(this.files[0]);
    });


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



</script>


<form id="myForm" method="post" action="">
  <input type="hidden" name="fileName" id="jsVariableInput">
  <input type="button" value="Soumettre" onclick="submitForm()">
</form>



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
  
