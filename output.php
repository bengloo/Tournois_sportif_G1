<!-- Source: https://www.bootdey.com/snippets/view/bs4-event-timeline -->

<!DOCTYPE html>
<meta name="viewport" content="width=device-width, initial-scale=1">

<title>Tournoi sportif G1 - Solution</title>
<html>
	<head>
		<script src="jquery-3.7.0.js"></script>
		<link href="https://cdn.jsdelivr.net/npm/bootstrap@4.4.1/dist/css/bootstrap.min.css" rel="stylesheet">
		<link rel="stylesheet" type="text/css" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.12.1/css/all.min.css">
		<link rel='stylesheet' type='text/css' href='./style_output.css'>
	</head>
  

	<div class="container animated animated-done bootdey" data-animate="fadeIn" data-animate-delay="0.05" style="animation-delay: 0.05s;">
		<select id="fileDropdown"></select>
		<hr class="hr-lg mt-0 mb-2 w-10 mx-auto hr-primary">
		<div class="timeline timeline-left mx-lg-10">
		
		</div>
	<div>
</html>
    


<script>
	$(document).ready(function() {
		// Appel AJAX pour récupérer la liste des noms de fichiers du répertoire
		$.ajax({			
			url: './fonctions/listFiles.php',
			method: 'GET',
			dataType: 'json',
			success: function(response) {
				console.log(response);
				// Parcours de la liste des fichiers et ajout des options au menu déroulant
				for (var i = 0; i < response.length; i++) {
					$('#fileDropdown').append('<option value="' + response[i] + '">' + response[i] + '</option>');
				}
			},
			error: function(xhr){
				console.log(xhr);
			}
		});

		// Écouteur d'événement pour détecter le changement de sélection dans le menu déroulant
		$('#fileDropdown').on('change', function() {
			var selectedFile = $(this).val();
			
			// TODO : requête Ajax
			$.ajax({			
				url: './fonctions/getFile.php',
				method: 'GET',
				data: { param: selectedFile },
				success: function(response) {
					var lines = response.split('\n'); // Divise le contenu en lignes
					var journees = []; // Tableau pour stocker les journées
					var journee = []; // Tableau pour stocker les lignes d'une journée
					var i = 0;

					$('.timeline').empty();
					
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
				},
				error: function(xhr){
					console.log(xhr.responseText);
				}
			});
		});
	});


</script>


<?php
	$fic = "ExecutionDone.txt";

	// Suppression du fichier execution_done pour qu'il n'interfère pas la prochaine fois qu'on ira sur index.php
	if (file_exists($fic)) {
		unlink($fic);
	}
?>
  
