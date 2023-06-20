# Tournois_sportif_G1

## Rep checkerProf
Dossier pour executer le chekerChampionat
pour l'executer depuis le terminal en utilsant la dernierre verssion java (la même que l'ide):
sudo ~/.jdks/openjdk-20.0.1/bin/java -jar CheckerChampionnat.jar a adaptée celons votre systeme d'exploitation


fichierInstance.txt et fichierInstance_sol.txt sont automatiquement écrasés par la dernierre instance traitée via le solveur cplex

Le dosier docup sert à drag and drop les instances que l'on veut traiter via chekerChampionat avec l'option -all

## Rep instances
Sert à stoquer un ensemble de l'instance de notre choix 

## Rep instanceTestUnitaire
Sert à stocker les instances des tests unitaires soit par contrainte et nombre d'équipes

## Rep instanceViableCplex
Sert hystoriquement à stocker les instances solvables directement par cplex respectant à la fois la viabilité de la solution et les contraintes dures.

## Rep ModelsCplex
Sert à stocker les model generé par cplex sert avant tout pour le debug

## Rep Resultat
Toute instance traitée par testAllSolveur ou testCplex à sa solution enregistrée dans un sous repertoir nomé selon le solveur utilisé 

## Src
Code source

### solveur
#### repAbandoned
Enciens solveurs vetustes
#### Meilleure Insertion V2
Realise sucessivement les operations d'insertion realisable prioritaire en termes de marge et ayant le meilleur cout tant qu'il en à
forte proba d'amener à une solution partielle non viable
#### solveur Iter
Realise iterativement un solveur designé et retourne la solution au meilleur cout
le nombre d'iteration peux étre passé en parametre du constructeur sinon voir l'atribut par defaut
#### Solveur cplex
Solveur utilisant cplex d'IBM
à plusieur atribut de parametrage qui peuvent aussi étre placé en parametre du constructeur

| atribut      | fonction                                                                                          |
|--------------|---------------------------------------------------------------------------------------------------|
| watchdog     | delay maximum d'execution                                                                         |
| minimiseDure | rend permissif les contraintes dure et essaye de minimiser leurs nombre (prioritaire sur le cout) |
| minimiseCout | definit les equoition de cout des contrainte souple et cherche à le minimiser                     |

### tests
#### rep hold
Enciens test 
#### test all solveur
Permé d'executé un ensemble de solveur sur un ensemble d'instance
dipose de quelque parametrage:

| ligne | parametrage                                  |
|-------|----------------------------------------------|
| 368   | dossier des instances à traiter              |
| 369   | dossier principal de sauvegarde des solution |
| 67    | initialisation des solveur à traiter         |

#### Test solveur
Permé d'excuté un solveur unique sur une instance unique

| ligne | parametrage                                  |
|-------|----------------------------------------------|
| 29    | initialisation du solveur                    |
| 24    | dossier principal de sauvegarde des solution |
| 24    | instance à traiter                           |


