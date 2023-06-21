# Tournois_sportif_G1

## Rep checkerProf
Dossier pour exécuter le checkerChampionnat
pour l'exécuter depuis le terminal en utilisant la dernière version java (la même que l'IDE):
sudo ~/.jdks/openjdk-20.0.1/bin/java -jar CheckerChampionnat.jar a adaptée celons votre systeme d'exploitation


fichierInstance.txt et fichierInstance_sol.txt sont automatiquement écrasés par la dernière instance traitée via le 
solveur cplex

Le dossier docup sert à drag and drop les instances que l'on veut traiter via checkerChampionnat avec l'option -all

## Rep instances
Sert à stocker l'ensemble des instances de notre choix 

## Rep instanceTestUnitaire
Sert à stocker les instances des tests unitaires soit par contraintes et nombre d'équipes

## Rep instanceViableCplex
Sert à stocker les instances respectant à la fois la viabilité de la solution et 
l'ensemble des contraintes dures.(solutions obtenues avec Cplex)
### Rep saufPause
Sert à stocker les instances respectant à la fois la viabilité de la solution et
l'ensemble des contraintes dures sauf la contrainte de Pause Globale.(solutions obtenues avec Cplex)

## Rep modeleCplex
Sert à stocker les models générés par Cplex (.lp). Cela est principalement utile pour du debug.

## Rep resultats
Toutes solutions des instances traitées par testAllSolveur ou testCplex est enregistrée dans un sous 
repertoire nommé selon le solveur utilisé. (Les dernières exécutions sont donc dans SolveurIter(.....) car
nous essayons de diminuer les coûts)

## Src
Code source

### instance

### io
Package permettant la lecture et l'écriture
### operateur
Package contenant les différents opérateurs
### solution
Package contenant l'ensemble des classes servant à décrire une solution
### solveur
#### abandonne
Anciens solveurs vétustes
#### solveur Iter
Realise itérativement un solveur designé et retourne la solution au meilleur cout
le nombre d'itérations peux-être passé en paramètre du constructeur sinon voir l'attribut par défaut
#### Solveur cplex
Solveur utilisant Cplex d'IBM à plusieurs attributs de paramétrage qui peuvent aussi
être placé en paramètre du constructeur

| attributs    | fonction                                                                                                                                               |
|--------------|--------------------------------------------------------------------------------------------------------------------------------------------------------|
| watchdog     | delai maximum d'exécution                                                                                                                              |
| minimiseDure | rend permissif les contraintes dure et essaye de minimiser leurs nombre non respectées(prioritaire sur le cout afin de traité les instance difficiles) |
| minimiseCout | définit les equations de cout des contraintes souples et cherche à les minimiser                                                                       |

### tests
#### hold
Anciens tests
#### TestAllSolveur
Permet d'exécuter un ensemble de solveurs sur un ensemble d'instances.
Il dispose aussi de quelque paramétrage :

| ligne | paramétrage                                  |
|-------|----------------------------------------------|
| 368   | dossier des instances à traiter              |
| 369   | dossier principal de sauvegarde des solution |
| 67    | initialisation des solveur à traiter         |

#### TestSolveur
Permet d'exécuter un solveur unique sur une instance unique

| ligne | paramétrage                                  |
|-------|----------------------------------------------|
| 29    | initialisation du solveur                    |
| 24    | dossier principal de sauvegarde des solution |
| 24    | instance à traiter                           |

### Main 
Contient le Main permettant l'appelle lors de l'exécution du .jar cotès serveur avec 
des paramètres : 

| attributs    | fonction                                                                                                                                              |
|--------------|-------------------------------------------------------------------------------------------------------------------------------------------------------|
| chemin       | chemin de l'instance a traiter                                                                                                                        |
| watchdog     | delai maximum d'exécution                                                                                                                             |
| minimiseDure | rend permissif les contraintes dure et essaye de minimiser leurs nombre non respectées(prioritaire sur le cout afin de traité les instance difficiles) |
| minimiseCout | définit les equations de cout des contraintes souples et cherche à les minimiser                                                                      |

