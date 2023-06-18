# Tournois_sportif_G1

## Rep checkerProf
dossier pour executer le chekerChampionat
pour l'executer depuis le terminal en utilsant la dernierre verssion java (la même que l'ide):
sudo ~/.jdks/openjdk-20.0.1/bin/java -jar CheckerChampionnat.jar à adapté celons votre systeme d'exploitation


fichierInstance.txt et fichierInstance_sol.txt sont automatiquement écrasé par la dernierre instance traité via le solveur cplex

le dosier docup sert à drag and drop les instance que l'on veux traité via chekerChampionat avec l'option -all

## Rep instances
sert à stoquer un ensemble de instance de notre choix 

## Rep instanceTestUnitaire
sert à stocker les instances des tests unitaire soit par contrainte et nombre d'équipe

## Rep instanceViableCplex
sert hystoriquement à stocker les instance solvable directement par cplex respectant à la fois la viabilité de la solution et les contraintes dures.

## Rep ModelsCplex
sert à stocker les model generé par cplex sert avant tout pour le debug

## Rep Resultat
toute instance traité par testAllSolveur ou testCplex à sa solution enregistré dans un sous repertoir nomé selon le solveur utilisé 

## Src
code source

### solveur
#### repAbandoned
enciens solveurs vetustes
#### Meilleur Insertion V2
realise sucessivement les operations d'insertion realisable prioritaire en terme de marge et ayant le meilleur cout tant qu'il en à
forte proba d'amener à une solution partielle non viable
#### solveur Iter
realise iterativement un solveur designé et retourne la solution au meilleur cout
le nombre d'iteration peux étre passé en parametre du constructeur sinon voir l'atribut par defaut
#### solveur cplex
solveur utilisant cplex d'IBM
à plusieur atribut de parametrage

| atribut      | fonction                                                                                          |
|--------------|---------------------------------------------------------------------------------------------------|
| watchdog     | delay maximum d'execution                                                                         |
| minimiseDure | rend permissif les contraintes dure et essaye de minimiser leurs nombre (prioritaire sur le cout) |
| minimiseCout | definit les equoition de cout des contrainte souple et cherche à le minimiser                     |

### tests
#### rep hold
enciens test 
#### test all solveur
permé d'executé un ensemble de solveur sur un ensemble d'instance
dipose de quelque parametrage:

| ligne | parametrage                                  |
|-------|----------------------------------------------|
| 368   | dossier des instances à traiter              |
| 369   | dossier principal de sauvegarde des solution |
| 67    | initialisation des solveur à traiter         |

#### test solveur
permé d'excuté un solveur unique sur une instance unique

| ligne | parametrage                                  |
|-------|----------------------------------------------|
| 29    | initialisation du solveur                    |
| 24    | dossier principal de sauvegarde des solution |
| 24    | instance à traiter                           |


