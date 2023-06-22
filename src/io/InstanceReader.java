package io;

import instance.Instance;
import instance.modele.contrainte.*;
import io.exception.FileExistException;
import io.exception.FormatFileException;
import io.exception.OpenFileException;
import io.exception.ReaderException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;


import static instance.modele.contrainte.TypeMode.*;

/** classe définissant InstanceReader pour lire une instance
 * @author Engloo Benjamin
 * @author Morcq Alexandre
 * @author Sueur Jeanne
 * @author Lux Hugo
 * @version 1.3
 */
public class InstanceReader {
    /**
     * Le fichier contenant l'instance.
     */
    private File instanceFile;

    /**
     * Constructeur par donnée du chemin du fichier d'instance.
     * @param inputPath le chemin du fichier d'instance, qui doit se terminer par l'extension du fichier (.txt).
     * @throws ReaderException lorsque le fichier n'est pas au bon format ou ne peut pas etre ouvert.
     */
    public InstanceReader(String inputPath) throws ReaderException {
        if (inputPath == null) {
            throw new OpenFileException();
        }
        if (!inputPath.endsWith(".txt")) {
            throw new FormatFileException("txt", "txt");
        }
        String instanceName = inputPath;
        this.instanceFile = new File(instanceName);
    }

    /**
     * Methode principale pour lire le fichier d'instance.
     * @return l'instance lue
     * @throws ReaderException lorsque les donnees dans le fichier d'instance sont manquantes ou au mauvais format.
     */
    public Instance readInstance() throws ReaderException {
        try{
            FileReader f = new FileReader(this.instanceFile.getAbsolutePath());
            BufferedReader br = new BufferedReader(f);
            String nom = lireNom(br);
            int nbEquipes = lireNbEquipes(br);
            Instance i = new Instance(nom,nbEquipes,this.instanceFile.getAbsolutePath());

            this.lireContraintesDures(br, i);
            this.lireContraintesSouples(br, i);

            br.close();
            f.close();
            return i;
        } catch (FileNotFoundException ex) {
            throw new FileExistException(instanceFile.getName());
        } catch (IOException ex) {
            throw new ReaderException("IO exception", ex.getMessage());
        }
    }

    /**
     * Lecture du nom de l'instance.
     * La ligne dans le fichier doit commencer par "// Nom"
     * @param br lecteur courant du fichier d'instance
     * @return le nom de l'instance
     * @throws IOException
     */
    private String lireNom(BufferedReader br) throws IOException {
        String ligne = br.readLine();
        lecture(br, ligne, "// Nom");
        ligne = br.readLine();
        return ligne;
    }

    /**
     * Lecture des contraintes dures du fichier .txt
     * @param br lecteur courant du fichier d'instance
     * @param i l'instance à laquelle on va ajouter les contraintes dures en fonction du texte lu
     * @throws IOException
     */
    private void lireContraintesDures(BufferedReader br, Instance i) throws IOException {
        String ligne = br.readLine();
        lecture(br, ligne, "// Contraintes dures");

        lireContraintesPlacement(br,i,false);
        lireContraintesHBClassement(br,i,false);
        lireContraintesRencontres(br,i,false);
        lireContraintesPauseEquipes(br,i,false);
        lireContraintesPauseGlobale(br,i,false);
    }

    /**
     * Lecture des contraintes souples du fichier .txt
     * @param br lecteur courant du fichier d'instance
     * @param i l'instance à laquelle on va ajouter les contraintes souples en fonction du texte lu
     * @throws IOException
     */
    private void lireContraintesSouples(BufferedReader br, Instance i) throws IOException {
        String ligne = br.readLine();
        lecture(br, ligne, "// Contraintes souples");

        lireContraintesPlacement(br,i,true);
        lireContraintesHBClassement(br,i,true);
        lireContraintesRencontres(br,i,true);
        lireContraintesPauseEquipes(br,i,true);
        lireContraintesPauseGlobale(br,i,true);
        lireContraintesEquite(br,i);
        lireContraintesSeparation(br,i);
    }

    /**
     * Lecture des contraintes de placement pour les ajouter à l'instance
     * @param br lecteur courant du fichier d'instance
     * @param instance l'instance à laquelle on va ajouter les contraintes en fonction du texte lu
     * @param estSouple booléen déterminant la nature des contraintes à lire pour prendre en compte les pénalités ou non
     * @throws IOException
     */
    private void lireContraintesPlacement(BufferedReader br, Instance instance, boolean estSouple) throws IOException {
        String ligne = br.readLine();
        String[] tokens, tokensJours;
        int idEquipe, max, nbContraintesPlacement, penalite = Integer.MAX_VALUE;
        String idJour, maxStr, mode, equipeStr,penaliteStr;
        ContraintePlacement contraintePlacement = null;

        lecture(br,ligne,"// Contraintes de placement");

        lecture(br,ligne,"// Nombre");

        ligne = br.readLine();
        ligne = ligne.trim();
        nbContraintesPlacement = Integer.parseInt(ligne);

        lecture(br,ligne,"// Contraintes");

        for(int i =0; i<nbContraintesPlacement;i++){
            ligne = br.readLine();

            tokens = ligne.split("\t");
            equipeStr = tokens[0].split("=")[1];
            idJour = tokens[1].split("=")[1];
            mode = tokens[2].split("=")[1];
            maxStr = tokens[3].split("=")[1];

            idEquipe = Integer.parseInt(equipeStr);
            max = Integer.parseInt(maxStr);

            // Seulement si la contrainte est souple
            if (estSouple) {
                penaliteStr = tokens[4].split("=")[1];
                penalite = Integer.parseInt(penaliteStr);
                contraintePlacement = new ContraintePlacement(idEquipe, castModeToTypeEnum(mode), max, penalite);
            } else {
                contraintePlacement = new ContraintePlacement(idEquipe, castModeToTypeEnum(mode), max);
            }
            tokensJours = idJour.split(";");

            for (int j = 0; j < tokensJours.length; j++) {
                contraintePlacement.addJournee(Integer.parseInt(tokensJours[j]));
            }
            instance.addContrainte(contraintePlacement);
        }
    }

    /**
     * Lecture des contraintes de haut et bas de classement pour les ajouter à l'instance
     * @param br lecteur courant du fichier d'instance
     * @param instance l'instance à laquelle on va ajouter les contraintes en fonction du texte lu
     * @param estSouple booléen déterminant la nature des contraintes à lire pour prendre en compte les pénalités ou non
     * @throws IOException
     */
    private void lireContraintesHBClassement(BufferedReader br, Instance instance, boolean estSouple) throws IOException {
        String ligne = br.readLine();
        int nbContraintesHBClassement, idEquipe, max, penalite  = Integer.MAX_VALUE;
        String equipeStr, mode, idJour, idEquipeAdverse, maxStr, penaliteStr;
        String[] tokens, tokensJours, tokensEA;
        ContrainteHBClassement contrainteHBClassement = null;


            lecture(br,ligne,"// Contraintes d'equipes en haut et bas de classement");

            lecture(br,ligne,"// Nombre");

            ligne = br.readLine();
            ligne = ligne.trim();
            nbContraintesHBClassement = Integer.parseInt(ligne);


            lecture(br,ligne,"// Contraintes");

        for(int i =0; i<nbContraintesHBClassement;i++){
            ligne = br.readLine();

            tokens = ligne.split("\t");
            equipeStr = tokens[0].split("=")[1];
            idJour = tokens[1].split("=")[1];
            idEquipeAdverse = tokens[2].split("=")[1];
            mode = tokens[3].split("=")[1];
            maxStr = tokens[4].split("=")[1];

            idEquipe = Integer.parseInt(equipeStr);
            max = Integer.parseInt(maxStr);

            // Seulement si la contrainte est souple
            if (estSouple) {
                penaliteStr = tokens[5].split("=")[1];
                penalite = Integer.parseInt(penaliteStr);
                contrainteHBClassement = new ContrainteHBClassement(idEquipe, castModeToTypeEnum(mode), max, penalite);
            } else {
                contrainteHBClassement = new ContrainteHBClassement(idEquipe, castModeToTypeEnum(mode), max);
            }
            tokensJours = idJour.split(";");
            tokensEA = idEquipeAdverse.split(";");

            for (int j = 0; j < tokensJours.length; j++) {
                contrainteHBClassement.addJournee(Integer.parseInt(tokensJours[j]));
            }

            for (int k = 0; k < tokensEA.length; k++) {
                contrainteHBClassement.addEquipeAdverse(Integer.parseInt(tokensEA[k]));
            }
            instance.addContrainte(contrainteHBClassement);
        }
    }

    /**
     * Lecture des contraintes de rencontres pour les ajouter à l'instance
     * @param br lecteur courant du fichier d'instance
     * @param instance l'instance à laquelle on va ajouter les contraintes en fonction du texte lu
     * @param estSouple booléen déterminant la nature des contraintes à lire pour prendre en compte les pénalités ou non
     * @throws IOException
     */
    private void lireContraintesRencontres(BufferedReader br, Instance instance, boolean estSouple) throws IOException {
        String ligne = br.readLine();
        int nbContraintesRencontre, max, min,penalite = Integer.MAX_VALUE;
        String idJour, idRencontre, maxStr, minStr,penaliteStr;
        String[] tokens, tokensJours, tokensRencontres;
        ContrainteRencontres contrainteRencontres = null;

        lecture(br,ligne,"// Contraintes de rencontre");

        lecture(br,ligne,"// Nombre");

        ligne = br.readLine();
        ligne = ligne.trim();
        nbContraintesRencontre = Integer.parseInt(ligne);


        lecture(br,ligne,"// Contraintes");

        for(int i =0; i<nbContraintesRencontre;i++){
            ligne = br.readLine();

            tokens = ligne.split("\t");

            idJour = tokens[0].split("=")[1];
            idRencontre = tokens[1].split("=")[1];
            minStr = tokens[2].split("=")[1];
            maxStr = tokens[3].split("=")[1];

            min = Integer.parseInt(minStr);
            max = Integer.parseInt(maxStr);

            // Seulement si la contrainte est souple
            if (estSouple) {
                penaliteStr = tokens[4].split("=")[1];
                penalite = Integer.parseInt(penaliteStr);
                contrainteRencontres = new ContrainteRencontres(min, max, penalite);
            } else {
                contrainteRencontres = new ContrainteRencontres(min, max);
            }
            tokensJours = idJour.split(";");
            tokensRencontres = idRencontre.split(";");

            for (int j = 0; j < tokensJours.length; j++) {
                contrainteRencontres.addJournee(Integer.parseInt(tokensJours[j]));
            }

            for (int k = 0; k < tokensRencontres.length; k++) {
                contrainteRencontres.addRencontre(tokensRencontres[k].replace(',','-'));
            }
            instance.addContrainte(contrainteRencontres);
        }
    }

    /**
     * Lecture des contraintes de pause par équipes pour les ajouter à l'instance
     * @param br lecteur courant du fichier d'instance
     * @param instance l'instance à laquelle on va ajouter les contraintes en fonction du texte lu
     * @param estSouple booléen déterminant la nature des contraintes à lire pour prendre en compte les pénalités ou non
     * @throws IOException
     */
    private void lireContraintesPauseEquipes(BufferedReader br, Instance instance, boolean estSouple) throws IOException {
        String ligne = br.readLine();
        int nbContraintesPauseEquipes, max, idEquipe, penalite = Integer.MAX_VALUE;
        String idJour,maxStr, equipeStr, mode, penaliteStr;
        String[] tokens,tokensJours, tokensEA;
        ContraintePauseEquipe contraintePauseEquipe = null;

        lecture(br,ligne,"// Contraintes de pause par equipe");
        lecture(br,ligne,"// Nombre");

        ligne = br.readLine();
        ligne = ligne.trim();
        nbContraintesPauseEquipes = Integer.parseInt(ligne);

        lecture(br,ligne,"// Contraintes");

        for(int i =0; i<nbContraintesPauseEquipes;i++){
            ligne = br.readLine();

            tokens = ligne.split("\t");

            equipeStr = tokens[0].split("=")[1];
            idJour = tokens[1].split("=")[1];
            mode = tokens[2].split("=")[1];
            maxStr = tokens[3].split("=")[1];

            idEquipe = Integer.parseInt(equipeStr);
            max = Integer.parseInt(maxStr);

            // Seulement si la contrainte est souple
            if (estSouple) {
                penaliteStr = tokens[4].split("=")[1];
                penalite = Integer.parseInt(penaliteStr);
                contraintePauseEquipe = new ContraintePauseEquipe(idEquipe, castModeToTypeEnum(mode), max, penalite);
            } else {
                contraintePauseEquipe = new ContraintePauseEquipe(idEquipe, castModeToTypeEnum(mode), max);
            }
            tokensJours = idJour.split(";");

            for (int j = 0; j < tokensJours.length; j++) {
                contraintePauseEquipe.addJournee(Integer.parseInt(tokensJours[j]));
            }
            instance.addContrainte(contraintePauseEquipe);
        }
    }

    /**
     * Lecture des contraintes de pause globale pour les ajouter à l'instance
     * @param br lecteur courant du fichier d'instance
     * @param instance l'instance à laquelle on va ajouter les contraintes en fonction du texte lu
     * @param estSouple booléen déterminant la nature des contraintes à lire pour prendre en compte les pénalités ou non
     * @throws IOException
     */
    private void lireContraintesPauseGlobale(BufferedReader br, Instance instance, boolean estSouple) throws IOException {
        String ligne = br.readLine();
        int nbContraintesPauseGlobale, max, penalite = Integer.MAX_VALUE;
        String idJour,maxStr, equipeStr, penaliteStr;
        String[] tokens, tokensJours, tokensE;
        ContraintePauseGlobale contraintePauseGlobale = null;

        lecture(br,ligne,"// Contraintes de pause globale");

        lecture(br,ligne,"// Nombre");

        ligne = br.readLine();
        ligne = ligne.trim();
        nbContraintesPauseGlobale = Integer.parseInt(ligne);


        lecture(br,ligne,"// Contraintes");

        for(int i =0; i<nbContraintesPauseGlobale;i++) {
            ligne = br.readLine();

            tokens = ligne.split("\t");

            equipeStr = tokens[0].split("=")[1];
            idJour = tokens[1].split("=")[1];
            maxStr = tokens[2].split("=")[1];

            max = Integer.parseInt(maxStr);

            // Seulement si la contrainte est souple
            if (estSouple) {
                penaliteStr = tokens[3].split("=")[1];
                penalite = Integer.parseInt(penaliteStr);
                contraintePauseGlobale = new ContraintePauseGlobale(max, penalite);
            } else {
                contraintePauseGlobale = new ContraintePauseGlobale(max);
            }
            tokensJours = idJour.split(";");
            tokensE = equipeStr.split(";");

            for (int j = 0; j < tokensJours.length; j++) {
                contraintePauseGlobale.addJournee((Integer.parseInt(tokensJours[j])));
            }

            for (int k = 0; k < tokensE.length; k++) {
                contraintePauseGlobale.addEquipe((Integer.parseInt(tokensE[k])));
            }
            instance.addContrainte(contraintePauseGlobale);
        }
    }

    /**
     * Lecture des contraintes d'équité pour les ajouter à l'instance
     * @param br lecteur courant du fichier d'instance
     * @param instance l'instance à laquelle on va ajouter les contraintes en fonction du texte lu
     * @throws IOException
     */
    private void lireContraintesEquite(BufferedReader br, Instance instance) throws IOException {
        String ligne = br.readLine();
        int nbContraintesRencontre, max, penalite;
        String idJour,maxStr, equipeStr, penaliteStr;
        String[] tokens, tokensJours, tokensE;
        ContrainteEquite contrainteEquite = null;

        lecture(br,ligne, "// Contraintes d'equite");

        lecture(br,ligne, "// Nombre");

        ligne = br.readLine();
        ligne = ligne.trim();
        nbContraintesRencontre = Integer.parseInt(ligne);

        lecture(br,ligne, "// Contraintes");

        for(int i =0; i<nbContraintesRencontre;i++){
            ligne = br.readLine();

            tokens = ligne.split("\t");

            equipeStr = tokens[0].split("=")[1];
            idJour = tokens[1].split("=")[1];
            maxStr = tokens[2].split("=")[1];
            penaliteStr = tokens[3].split("=")[1];

            max = Integer.parseInt(maxStr);
            penalite = Integer.parseInt(penaliteStr);

            contrainteEquite = new ContrainteEquite(max, penalite);

            tokensJours = idJour.split(";");
            tokensE = equipeStr.split(";");

            for (int j = 0; j < tokensJours.length; j++) {
                contrainteEquite.addJournee((Integer.parseInt(tokensJours[j])));
            }

            for (int k = 0; k < tokensE.length; k++) {
                contrainteEquite.addEquipe((Integer.parseInt(tokensE[k])));
            }
            instance.addContrainte(contrainteEquite);
        }
    }

    /**
     * Lecture des contraintes de séparation pour les ajouter à l'instance
     * @param br lecteur courant du fichier d'instance
     * @param instance l'instance à laquelle on va ajouter les contraintes en fonction du texte lu
     * @throws IOException
     */
    private void lireContraintesSeparation(BufferedReader br,Instance instance) throws IOException {
        String ligne = br.readLine();
        int nbContraintesRencontre, min,penalite;
        String minStr,penaliteStr,equipeStr;
        String[] tokens,tokensE;
        ContrainteSeparation contrainteSeparation = null;

        lecture(br,ligne, "// Contraintes de separation");

        lecture(br,ligne, "// Nombre");

        ligne = br.readLine();
        ligne = ligne.trim();
        nbContraintesRencontre = Integer.parseInt(ligne);

        lecture(br,ligne, "// Contraintes");

        for(int i =0; i<nbContraintesRencontre;i++){
            ligne = br.readLine();

            tokens = ligne.split("\t");

            equipeStr = tokens[0].split("=")[1];
            minStr = tokens[1].split("=")[1];
            penaliteStr = tokens[2].split("=")[1];

            min = Integer.parseInt(minStr);

            penalite = Integer.parseInt(penaliteStr);

            contrainteSeparation = new ContrainteSeparation(min, penalite);

            tokensE = equipeStr.split(";");

            for (int k = 0; k < tokensE.length; k++) {
                contrainteSeparation.addEquipe((Integer.parseInt(tokensE[k])));
            }

            instance.addContrainte(contrainteSeparation);
        }
    }

    /**
     * Lecture du nombre d'équipes.
     * La ligne doit commencer par "// Nombre d'equipes"
     * @param br le lecteur courant du fichier d'instance
     * @return le nombre d'équipes
     * @throws IOException
     */
    private int lireNbEquipes(BufferedReader br) throws IOException {
        String ligne = br.readLine();
        lecture(br,ligne, "// Nombre d'equipes");
        ligne = br.readLine();
        ligne = ligne.trim();
        int nbEquipes = Integer.parseInt(ligne);
        return nbEquipes;
    }

    /**
     * Convertit la chaîne de caractères du mode extraite du fichier .txt en TypeMode (ENUM)
     * @param mode de la rencontre (domicile, extérieur ou indéfini)
     * @return le mode converti en TypeMode (ENUM)
     */
    private TypeMode castModeToTypeEnum(String mode) {
        TypeMode typeMode = null;

        switch(mode) {
            case "D":
                typeMode = DOMICILE;
                break;
            case "E":
                typeMode = EXTERIEUR;
                break;
            case "DE":
                typeMode = INDEFINI;
                break;
        }
        return typeMode;
    }

    /**
     * Lecture d'un commentaire spécifique d'une ligne du fichier d'instance sous format .txt
     * @param br le lecteur courant du fichier d'instance
     * @param ligne marquant la position actuelle de parcours du fichier
     * @param commentaire commençant par "//"
     * @throws IOException
     */
    private void lecture(BufferedReader br, String ligne, String commentaire) throws IOException {
        while (!ligne.contains(commentaire)) {
            ligne = br.readLine();
        }
    }
}

