package io;

import instance.Instance;
import instance.modele.contrainte.ContraintePlacement;
import instance.modele.contrainte.TypeMode;
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

public class InstanceReader {
    /**
     * Le fichier contenant l'instance.
     */
    private File instanceFile;

    /**
     * Constructeur par donnee du chemin du fichier d'instance.
     * @param inputPath le chemin du fichier d'instance, qui doit se terminer
     * par l'extension du fichier (.xml).
     * @throws ReaderException lorsque le fichier n'est pas au bon format ou
     * ne peut pas etre ouvert.
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
     * @throws ReaderException lorsque les donnees dans le fichier d'instance
     * sont manquantes ou au mauvais format.
     */
    public Instance readInstance() throws ReaderException {
        try{
            FileReader f = new FileReader(this.instanceFile.getAbsolutePath());
            BufferedReader br = new BufferedReader(f);
            String nom = lireNom(br);
            int nbEquipes = lireNbEquipes(br);

            Instance i = new Instance(nom,nbEquipes);





            // TO CHECK : constructeur de la classe Instance
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
     * La ligne dans le fichier doit commencer par "NAME :"
     * @param br lecteur courant du fichier d'instance
     * @return le nom de l'instance
     * @throws IOException
     */
    private String lireNom(BufferedReader br) throws IOException {
        String ligne = br.readLine();
        while(!ligne.contains("// Nom")) {
            ligne = br.readLine();
        }
        ligne = br.readLine();
        return ligne;
    }


    private void lireContraintesDures(BufferedReader br, Instance i) throws IOException {
        String ligne = br.readLine();
        while(!ligne.contains("// Contraintes dures")) {
            ligne = br.readLine();
        }

        System.out.println("\n--------------- CONTRAINTES DURES ---------------");
        i.addContrainte(this.lireContraintesPlacement(br,i));
        this.lireContraintesHBClassement(br);
        this.lireContraintesRencontres(br);
        this.lireContraintesPauseEquipes(br);
        this.lireContraintesPauseGlobale(br);
    }




    private ContraintePlacement lireContraintesPlacement(BufferedReader br,Instance instance) throws IOException {
        String ligne = br.readLine();
        String[] tokens, tokensJours;
        int idEquipe, max, nbContraintesPlacement;
        String idJour;
        String maxStr;
        String mode;
        String equipeStr;
        ContraintePlacement contraintePlacement = null;



        while(!ligne.contains("// Contraintes de placement")) {
            ligne = br.readLine();
        }

        while(!ligne.contains("// Nombre")) {
            ligne = br.readLine();
        }

        ligne = br.readLine();
        ligne = ligne.trim();
        nbContraintesPlacement = Integer.parseInt(ligne);


        while(!ligne.contains("// Contraintes")) {
            ligne = br.readLine();
        }

        for(int i =0; i<nbContraintesPlacement;i++){
            ligne = br.readLine();
            System.out.println(ligne);

            tokens = ligne.split("\t");
            equipeStr = tokens[0].split("=")[1];

            idJour = tokens[1].split("=")[1];

            mode = tokens[2].split("=")[1];
            maxStr = tokens[3].split("=")[1];

            idEquipe = Integer.parseInt(equipeStr);
            max = Integer.parseInt(maxStr);

            contraintePlacement = new ContraintePlacement(instance.getEquipeById(idEquipe), castModeToTypeEnum(mode), max);

            tokensJours = idJour.split(";");
            for (int j = 0; j < tokensJours.length; j++) {
                contraintePlacement.addJournee(instance.getJourneeById(Integer.parseInt(tokensJours[j])));
            }

            System.out.println("\nCONTRAINTES PLACEMENT");
            System.out.println("La valeur de l'équipe est : " + idEquipe);
            System.out.println("La liste des jours est : " + idJour);
            System.out.println("Le mode est : " + mode);
            System.out.println("La valeur du max est : " +max);

        }
        return contraintePlacement;
    }

    private void lireContraintesHBClassement(BufferedReader br) throws IOException {
        String ligne = br.readLine();
        int nbContraintesHBClassement, idEquipe, max;
        String equipeStr, mode, idJour, idEquipeAdverse, maxStr;
        String[] tokens;

        while(!ligne.contains("// Contraintes d'equipes en haut et bas de classement")) {
            ligne = br.readLine();
        }

        while(!ligne.contains("// Nombre")) {
            ligne = br.readLine();
        }

        ligne = br.readLine();
        ligne = ligne.trim();
        nbContraintesHBClassement = Integer.parseInt(ligne);


        while(!ligne.contains("// Contraintes")) {
            ligne = br.readLine();
        }

        for(int i =0; i<nbContraintesHBClassement;i++){
            ligne = br.readLine();
            System.out.println(ligne);

            tokens = ligne.split("\t");
            equipeStr = tokens[0].split("=")[1];
            idJour = tokens[1].split("=")[1];
            idEquipeAdverse = tokens[2].split("=")[1];
            mode = tokens[3].split("=")[1];
            maxStr = tokens[4].split("=")[1];

            idEquipe = Integer.parseInt(equipeStr);
            max = Integer.parseInt(maxStr);

            System.out.println("\nCONTRAINTES HB CLASSEMENT");
            System.out.println("La valeur de l'équipe est : " + idEquipe);
            System.out.println("La liste des jours est : " + idJour);
            System.out.println("La Liste des équipes adverses sont : " +idEquipeAdverse);
            System.out.println("Le mode est : " + mode);
            System.out.println("La valeur du max est : " +max);

        }
    }

    private void lireContraintesRencontres(BufferedReader br) throws IOException {
        String ligne = br.readLine();
        int nbContraintesRencontre, max, min;
        String idJour, idRencontre, maxStr, minStr;
        String[] tokens;

        while(!ligne.contains("// Contraintes de rencontres")) {
            ligne = br.readLine();
        }

        while(!ligne.contains("// Nombre")) {
            ligne = br.readLine();
        }

        ligne = br.readLine();
        ligne = ligne.trim();
        nbContraintesRencontre = Integer.parseInt(ligne);


        while(!ligne.contains("// Contraintes")) {
            ligne = br.readLine();
        }

        for(int i =0; i<nbContraintesRencontre;i++){
            ligne = br.readLine();
            System.out.println(ligne);

            tokens = ligne.split("\t");

            idJour = tokens[0].split("=")[1];
            idRencontre = tokens[1].split("=")[1];
            minStr = tokens[2].split("=")[1];
            maxStr = tokens[3].split("=")[1];

            min = Integer.parseInt(minStr);
            max = Integer.parseInt(maxStr);

            System.out.println("\nCONTRAINTES RENCONTRES");
            System.out.println("La liste des jours est : " + idJour);
            System.out.println("La Liste des équipes adverses sont : " +idRencontre);
            System.out.println("La valeur du min est : " + min);
            System.out.println("La valeur du max est : " +max);

        }
    }

    private void lireContraintesPauseEquipes(BufferedReader br) throws IOException {
        String ligne = br.readLine();
        int nbContraintesPauseEquipes, max, idEquipe;
        String idJour,maxStr, equipeStr, mode;
        String[] tokens;

        while(!ligne.contains("// Contraintes de pause par equipe")) {
            ligne = br.readLine();
        }

        while(!ligne.contains("// Nombre")) {
            ligne = br.readLine();
        }

        ligne = br.readLine();
        ligne = ligne.trim();
        nbContraintesPauseEquipes = Integer.parseInt(ligne);


        while(!ligne.contains("// Contraintes")) {
            ligne = br.readLine();
        }

        for(int i =0; i<nbContraintesPauseEquipes;i++){
            ligne = br.readLine();
            System.out.println(ligne);

            tokens = ligne.split("\t");

            equipeStr = tokens[0].split("=")[1];
            idJour = tokens[1].split("=")[1];
            mode = tokens[2].split("=")[1];
            maxStr = tokens[3].split("=")[1];

            idEquipe = Integer.parseInt(equipeStr);
            max = Integer.parseInt(maxStr);

            System.out.println("\nCONTRAINTES PAUSE EQUIPES");
            System.out.println("La Liste des équipes adverses sont : " +idEquipe);
            System.out.println("La liste des jours est : " + idJour);
            System.out.println("La valeur du min est : " + mode);
            System.out.println("La valeur du max est : " +max);

        }
    }

    private void lireContraintesPauseGlobale(BufferedReader br) throws IOException {
        String ligne = br.readLine();
        int nbContraintesPauseGlobale, max;
        String idJour,maxStr, equipeStr;
        String[] tokens;

        while(!ligne.contains("// Contraintes de pause globale")) {
            ligne = br.readLine();
        }

        while(!ligne.contains("// Nombre")) {
            ligne = br.readLine();
        }

        ligne = br.readLine();
        ligne = ligne.trim();
        nbContraintesPauseGlobale = Integer.parseInt(ligne);


        while(!ligne.contains("// Contraintes")) {
            ligne = br.readLine();
        }

        for(int i =0; i<nbContraintesPauseGlobale;i++){
            ligne = br.readLine();
            System.out.println(ligne);

            tokens = ligne.split("\t");

            equipeStr = tokens[0].split("=")[1];
            idJour = tokens[1].split("=")[1];
            maxStr = tokens[2].split("=")[1];


            max = Integer.parseInt(maxStr);

            System.out.println("\nCONTRAINTES PAUSE GLOBALE");
            System.out.println("La Liste des équipes adverses sont : " +equipeStr);
            System.out.println("La liste des jours est : " + idJour);
            System.out.println("La valeur du max est : " +max);

        }
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////
    //CONTRAINTES SOUPLES
    ////////////////////////////////////////////////////////////////////////////////////////////////////


    private void lireContraintesSouples(BufferedReader br, Instance i) throws IOException {
        String ligne = br.readLine();
        while(!ligne.contains("// Contraintes souples")) {
            ligne = br.readLine();
        }
        System.out.println("\n--------------- CONTRAINTES SOUPLES ---------------");
        this.lireContraintesPlacementSouple(br);
        this.lireContraintesHBClassementSouple(br);
        this.lireContraintesRencontresSouple(br);
        this.lireContraintesPauseEquipesSouple(br);
        this.lireContraintesPauseGlobaleSouple(br);
        this.lireContraintesEquite(br);
        this.lireContraintesSeparation(br);
    }




    private void lireContraintesPlacementSouple(BufferedReader br) throws IOException {
        String ligne = br.readLine();
        String[] tokens;
        int idEquipe, max, nbContraintesPlacement,penalite;
        String idJour, maxStr, mode, equipeStr,penaliteStr;



        while(!ligne.contains("// Contraintes de placement")) {
            ligne = br.readLine();
        }

        while(!ligne.contains("// Nombre")) {
            ligne = br.readLine();
        }

        ligne = br.readLine();
        ligne = ligne.trim();
        nbContraintesPlacement = Integer.parseInt(ligne);


        while(!ligne.contains("// Contraintes")) {
            ligne = br.readLine();
        }

        for(int i =0; i<nbContraintesPlacement;i++){
            ligne = br.readLine();
            System.out.println(ligne);

            tokens = ligne.split("\t");
            equipeStr = tokens[0].split("=")[1];
            idJour = tokens[1].split("=")[1];
            mode = tokens[2].split("=")[1];
            maxStr = tokens[3].split("=")[1];
            penaliteStr = tokens[4].split("=")[1];


            idEquipe = Integer.parseInt(equipeStr);
            max = Integer.parseInt(maxStr);
            penalite = Integer.parseInt(penaliteStr);

            System.out.println("\nCONTRAINTES PLACEMENT SOUPLE");
            System.out.println("La valeur de l'équipe est : " + idEquipe);
            System.out.println("La liste des jours est : " + idJour);
            System.out.println("Le mode est : " + mode);
            System.out.println("La valeur du max est : " +max);
            System.out.println("La valeur de la penalite est : " +penalite);


        }
    }

    private void lireContraintesHBClassementSouple(BufferedReader br) throws IOException {
        String ligne = br.readLine();
        int nbContraintesHBClassement, idEquipe, max, penalite;
        String equipeStr, mode, idJour, idEquipeAdverse, maxStr, penaliteStr;
        String[] tokens;

        while(!ligne.contains("// Contraintes d'equipes en haut et bas de classement")) {
            ligne = br.readLine();
        }

        while(!ligne.contains("// Nombre")) {
            ligne = br.readLine();
        }

        ligne = br.readLine();
        ligne = ligne.trim();
        nbContraintesHBClassement = Integer.parseInt(ligne);


        while(!ligne.contains("// Contraintes")) {
            ligne = br.readLine();
        }

        for(int i =0; i<nbContraintesHBClassement;i++){
            ligne = br.readLine();
            System.out.println(ligne);

            tokens = ligne.split("\t");
            equipeStr = tokens[0].split("=")[1];
            idJour = tokens[1].split("=")[1];
            idEquipeAdverse = tokens[2].split("=")[1];
            mode = tokens[3].split("=")[1];
            maxStr = tokens[4].split("=")[1];
            penaliteStr = tokens[5].split("=")[1];

            idEquipe = Integer.parseInt(equipeStr);
            max = Integer.parseInt(maxStr);
            penalite = Integer.parseInt(penaliteStr);

            System.out.println("\nCONTRAINTES HB CLASSEMENT SOUPLE");
            System.out.println("La valeur de l'équipe est : " + idEquipe);
            System.out.println("La liste des jours est : " + idJour);
            System.out.println("La Liste des équipes adverses sont : " +idEquipeAdverse);
            System.out.println("Le mode est : " + mode);
            System.out.println("La valeur du max est : " +max);
            System.out.println("La valeur de la penalite est : " +penalite);

        }
    }

    private void lireContraintesRencontresSouple(BufferedReader br) throws IOException {
        String ligne = br.readLine();
        int nbContraintesRencontre, max, min,penalite;
        String idJour, idRencontre, maxStr, minStr,penaliteStr;
        String[] tokens;

        while(!ligne.contains("// Contraintes de rencontres")) {
            ligne = br.readLine();
        }

        while(!ligne.contains("// Nombre")) {
            ligne = br.readLine();
        }

        ligne = br.readLine();
        ligne = ligne.trim();
        nbContraintesRencontre = Integer.parseInt(ligne);


        while(!ligne.contains("// Contraintes")) {
            ligne = br.readLine();
        }

        for(int i =0; i<nbContraintesRencontre;i++){
            ligne = br.readLine();
            System.out.println(ligne);

            tokens = ligne.split("\t");

            idJour = tokens[0].split("=")[1];
            idRencontre = tokens[1].split("=")[1];
            minStr = tokens[2].split("=")[1];
            maxStr = tokens[3].split("=")[1];
            penaliteStr = tokens[4].split("=")[1];

            min = Integer.parseInt(minStr);
            max = Integer.parseInt(maxStr);
            penalite = Integer.parseInt(penaliteStr);

            System.out.println("\nCONTRAINTES RENCONTRES SOUPLE");
            System.out.println("La liste des jours est : " + idJour);
            System.out.println("La Liste des rencontres sont : " +idRencontre);
            System.out.println("La valeur du min est : " + min);
            System.out.println("La valeur du max est : " +max);
            System.out.println("La valeur de la penalite est : " +penalite);

        }
    }

    private void lireContraintesPauseEquipesSouple(BufferedReader br) throws IOException {
        String ligne = br.readLine();
        int nbContraintesRencontre, max, idEquipe,penalite;
        String idJour,maxStr, equipeStr, mode,penaliteStr;
        String[] tokens;

        while(!ligne.contains("// Contraintes de pause par equipe")) {
            ligne = br.readLine();
        }

        while(!ligne.contains("// Nombre")) {
            ligne = br.readLine();
        }

        ligne = br.readLine();
        ligne = ligne.trim();
        nbContraintesRencontre = Integer.parseInt(ligne);


        while(!ligne.contains("// Contraintes")) {
            ligne = br.readLine();
        }

        for(int i =0; i<nbContraintesRencontre;i++){
            ligne = br.readLine();
            System.out.println(ligne);

            tokens = ligne.split("\t");

            equipeStr = tokens[0].split("=")[1];
            idJour = tokens[1].split("=")[1];
            mode = tokens[2].split("=")[1];
            maxStr = tokens[3].split("=")[1];
            penaliteStr = tokens[4].split("=")[1];


            idEquipe = Integer.parseInt(equipeStr);
            max = Integer.parseInt(maxStr);
            penalite = Integer.parseInt(penaliteStr);

            System.out.println("\nCONTRAINTES PAUSE EQUIPES SOUPLE");
            System.out.println("La Liste des équipes adverses sont : " +idEquipe);
            System.out.println("La liste des jours est : " + idJour);
            System.out.println("La valeur du min est : " + mode);
            System.out.println("La valeur du max est : " +max);
            System.out.println("La valeur de la penalite est : " +penalite);


        }
    }

    private void lireContraintesPauseGlobaleSouple(BufferedReader br) throws IOException {
       String ligne = br.readLine();
        int nbContraintesRencontre, max, penalite;
        String idJour,maxStr, equipeStr,penaliteStr;
        String[] tokens;

        while(!ligne.contains("// Contraintes de pause globale")) {
            ligne = br.readLine();
        }

        while(!ligne.contains("// Nombre")) {
            ligne = br.readLine();
        }

        ligne = br.readLine();
        ligne = ligne.trim();
        nbContraintesRencontre = Integer.parseInt(ligne);


        while(!ligne.contains("// Contraintes")) {
            ligne = br.readLine();
        }

        for(int i =0; i<nbContraintesRencontre;i++){
            ligne = br.readLine();
            System.out.println(ligne);

            tokens = ligne.split("\t");

            equipeStr = tokens[0].split("=")[1];
            idJour = tokens[1].split("=")[1];
            maxStr = tokens[2].split("=")[1];
            penaliteStr = tokens[3].split("=")[1];



            max = Integer.parseInt(maxStr);
            penalite= Integer.parseInt(penaliteStr);

            System.out.println("\nCONTRAINTES PAUSE GLOBALE SOUPLE");
            System.out.println("La Liste des équipes adverses sont : " +equipeStr);
            System.out.println("La liste des jours est : " + idJour);
            System.out.println("La valeur du max est : " +max);
            System.out.println("La valeur du max est : " +penalite);


        }
    }


    private void lireContraintesEquite(BufferedReader br) throws IOException {
        String ligne = br.readLine();
        int nbContraintesRencontre, max, penalite;
        String idJour,maxStr, equipeStr, penaliteStr;
        String[] tokens;

        while(!ligne.contains("// Contraintes d'equite")) {
            ligne = br.readLine();
        }

        while(!ligne.contains("// Nombre")) {
            ligne = br.readLine();
        }

        ligne = br.readLine();
        ligne = ligne.trim();
        nbContraintesRencontre = Integer.parseInt(ligne);


        while(!ligne.contains("// Contraintes")) {
            ligne = br.readLine();
        }


        for(int i =0; i<nbContraintesRencontre;i++){
            ligne = br.readLine();
            System.out.println(ligne);

            tokens = ligne.split("\t");

            equipeStr = tokens[0].split("=")[1];
            idJour = tokens[1].split("=")[1];
            maxStr = tokens[2].split("=")[1];
            penaliteStr = tokens[3].split("=")[1];



            max = Integer.parseInt(maxStr);
            penalite = Integer.parseInt(penaliteStr);

            System.out.println("\nCONTRAINTES EQUITE SOUPLE");
            System.out.println("La Liste des équipes adverses sont : " +equipeStr);
            System.out.println("La liste des jours est : " + idJour);
            System.out.println("La valeur du max est : " +max);
            System.out.println("La valeur de la penalite est : " +penalite);


        }
    }



    private void lireContraintesSeparation(BufferedReader br) throws IOException {
        String ligne = br.readLine();
        int nbContraintesRencontre, min,penalite;
        String idEquipe, minStr,penaliteStr;
        String[] tokens;

        while(!ligne.contains("// Contraintes de separation")) {
            ligne = br.readLine();
        }

        while(!ligne.contains("// Nombre")) {
            ligne = br.readLine();
        }

        ligne = br.readLine();
        ligne = ligne.trim();
        nbContraintesRencontre = Integer.parseInt(ligne);


        while(!ligne.contains("// Contraintes")) {
            ligne = br.readLine();
        }

        for(int i =0; i<nbContraintesRencontre;i++){
            ligne = br.readLine();
            System.out.println(ligne);

            tokens = ligne.split("\t");

            idEquipe = tokens[0].split("=")[1];
            minStr = tokens[1].split("=")[1];
            penaliteStr = tokens[2].split("=")[1];

            min = Integer.parseInt(minStr);

            penalite = Integer.parseInt(penaliteStr);

            System.out.println("\nCONTRAINTES SEPARATION SOUPLE");
            System.out.println("La liste des jours est : " + idEquipe);
            System.out.println("La valeur du min est : " + min);
            System.out.println("La valeur de la penalite est : " +penalite);

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
        while(!ligne.contains("// Nombre d'equipes")) {
            ligne = br.readLine();
        }
        ligne = br.readLine();
        ligne = ligne.trim();
        int nbEquipes = Integer.parseInt(ligne);
        return nbEquipes;
    }

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
     * Test de lecture d'une instance.
     * @param args
     */
    public static void main(String[] args) {
        try {
            InstanceReader reader = new InstanceReader("instances/instance_ITC2021_Test_1.txt");
            reader.readInstance();
            System.out.println("Instance lue avec success !");
        } catch (ReaderException ex) {
            System.out.println(ex.getMessage());
        }
    }
}

