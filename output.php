<?php
	$fic = "ExecutionDone.txt";

	// Suppression du fichier execution_done pour qu'il n'interfère pas la prochaine fois qu'on ira sur index.php
	if (file_exists($fic)) {
		unlink($fic);
	}

	// Récupération du fichier le plus récent
	$dir = './resultats/SolveurIter(SolveurCplex)'; 

	// Obtient la liste des fichiers du dossier
	$files = scandir($dir);

	// Supprime les entrées "." et ".." de la liste
	$files = array_diff($files, array('.', '..'));

	$newestFile = null;
	$newestTime = 0;

	// Parcourt chaque fichier pour trouver le plus récent
	foreach ($files as $file) {
		$filePath = $dir . '/' . $file;
		
		// Obtient la date de modification du fichier
		$fileTime = filemtime($filePath);
		
		// Vérifie si le fichier est plus récent que le précédent
		if ($fileTime > $newestTime) {
			$newestFile = $filePath;
			$newestTime = $fileTime;
		}
	}
?>

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
  
	<a href="./index.php" id="redirectLink">
		<img src="./ressources/return_button.png" id="redirectIcon">
	</a>

	<div class="container animated animated-done bootdey" data-animate="fadeIn" data-animate-delay="0.05" style="animation-delay: 0.05s;">
		<div id="labelDropdown">Veuillez sélectionner une solution à afficher</div></br>
		<select id="fileDropdown">
			<option selected style="color:grey;">-- Pas de sélection --</option>
		</select>
		<hr class="hr-lg mt-0 mb-2 w-10 mx-auto hr-primary">
		<div class="timeline timeline-left mx-lg-10"></div>
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
				var newestFile = <?php echo json_encode($newestFile); ?>;
				newestFile = newestFile.split("/").pop();

				// Parcours de la liste des fichiers et ajout des options au menu déroulant
				for (var i = 0; i < response.length; i++) {
					$('#fileDropdown').append('<option value="' + response[i] + '" >' + response[i] + '</option>');
				}
				$("#fileDropdown").val(newestFile);
			
				$.ajax({			
					url: './fonctions/getFile.php',
					method: 'GET',
					data: { param: newestFile },
					success: function(response) {
						var lines = response.split('\n'); // Divise le contenu en lignes
						var journees = []; // Tableau pour stocker les journées
						var journee = []; // Tableau pour stocker les lignes d'une journée
						var i = 0;
						var dateAleatoire = genererDateAleatoire();
						var date = new Date(dateAleatoire);

						$('.timeline').empty();

						$.getJSON('./logos/correspondance.json', function(data) {
							lines.forEach(function(line) {
								if (line.trim() !== '' && !line.startsWith("//")) { // Vérifie si la ligne n'est pas vide
									var values = line.split('   '); // Divise la ligne en valeurs (suppose que les valeurs sont séparées par une tabulation)

									$('#box-'+(i-1)).append('<div class="timeline-item mt-3 row text-center p-2">'+
										'<div class="col font-weight-bold text-md-right"><img class="logo" style="float:left;" src='+data[parseInt(values[0])].logo+'>'+data[parseInt(values[0])].nom+'</div>'+
										'<div class="col-1">vs</div>'+
										'<div class="col font-weight-bold text-md-left">'+data[parseInt(values[1])].nom+'<img class="logo" style="float:right;" src='+data[parseInt(values[1])].logo+'></div>'+
									'</div>');
									journee.push(values.map(Number)); // Ajoute la ligne sous forme de tableau de nombres dans la journée
								} else if (!line.startsWith("//")) {
									dateAleatoire = date.toLocaleDateString("fr-FR");

									$('.timeline').append('<div class="box" id="box-'+i+'">'+
															'<div class="timeline-breaker">'+dateAleatoire+'</div>'+
															'</div>');
									journees.push(journee); // Ajoute la journée au tableau des journées
									journee = []; // Réinitialise le tableau pour la prochaine journée
									i++;
									date.setDate(date.getDate() + 1); // Ajouter un jour à la date
								}                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                           
							});
							// Supprime le dernier bloc de journée vide
							// (à cause d'un saut de ligne en trop à la fin du fichier solution)
							if ($('.box').length > 0) {
								if ($('.box').last().children().length === 1) {
									$('.box').last().remove();
								}
							}
						})

						// Supprime le 1er élément du tableau car il n'y a pas encore eu de journée parcourue à ce stage
						journees.shift();	
					},
					error: function(xhr){
						console.log(xhr.responseText);
					}
				});
			},
			error: function(xhr){
				console.log(xhr);
			}
		});

		// Écouteur d'événement pour détecter le changement de sélection dans le menu déroulant
		$('#fileDropdown').on('change', function() {
			var selectedFile = $(this).val();
			
			$.ajax({			
				url: './fonctions/getFile.php',
				method: 'GET',
				data: { param: selectedFile },
				success: function(response) {
					var lines = response.split('\n'); // Divise le contenu en lignes
					var journees = []; // Tableau pour stocker les journées
					var journee = []; // Tableau pour stocker les lignes d'une journée
					var i = 0;
					var dateAleatoire = genererDateAleatoire();
					var date = new Date(dateAleatoire);

					$('.timeline').empty();

					$.getJSON('./logos/correspondance.json', function(data) {
						lines.forEach(function(line) {
							if (line.trim() !== '' && !line.startsWith("//")) { // Vérifie si la ligne n'est pas vide
								var values = line.split('   '); // Divise la ligne en valeurs (suppose que les valeurs sont séparées par une tabulation)

								$('#box-'+(i-1)).append('<div class="timeline-item mt-3 row text-center p-2">'+
									'<div class="col font-weight-bold text-md-right"><img class="logo" style="float:left;" src='+data[parseInt(values[0])].logo+'>'+data[parseInt(values[0])].nom+'</div>'+
									'<div class="col-1">vs</div>'+
									'<div class="col font-weight-bold text-md-left">'+data[parseInt(values[1])].nom+'<img class="logo" style="float:right;" src='+data[parseInt(values[1])].logo+'></div>'+
								'</div>');
								journee.push(values.map(Number)); // Ajoute la ligne sous forme de tableau de nombres dans la journée
							} else if (!line.startsWith("//")) {
								dateAleatoire = date.toLocaleDateString("fr-FR");

								$('.timeline').append('<div class="box" id="box-'+i+'">'+
														'<div class="timeline-breaker">'+dateAleatoire+'</div>'+
														'</div>');
								journees.push(journee); // Ajoute la journée au tableau des journées
								journee = []; // Réinitialise le tableau pour la prochaine journée
								i++;
								date.setDate(date.getDate() + 1); // Ajouter un jour à la date
							}                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                           
						});
						// Supprime le dernier bloc de journée vide
						// (à cause d'un saut de ligne en trop à la fin du fichier solution)
						if ($('.box').length > 0) {
							if ($('.box').last().children().length === 1) {
								$('.box').last().remove();
							}
						}
					})

					// Supprime le 1er élément du tableau car il n'y a pas encore eu de journée parcourue à ce stage
					journees.shift();	
				},
				error: function(xhr){
					console.log(xhr.responseText);
				}
			});
		});
	});

	function genererDateAleatoire() {
		date = new Date(Math.random() * (new Date().getTime() - new Date(2022, 0, 1).getTime()) + new Date(2022, 0, 1).getTime());

		var jour = date.getDate();
		var mois = date.getMonth();
		var annee = date.getFullYear();
		var dateFormatee = (mois + 1) + "/" + jour + "/" + annee;

		return dateFormatee;
	}

</script>
  
