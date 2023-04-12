package io;import instance.Instance;
import instance.modele.contrainte.ContraintePlacement;
import instance.modele.contrainte.TypeMode;
import io.exception.FileExistException;
import io.exception.FormatFileException;
import io.exception.OpenFileException;
import io.exception.ReaderException;
import solution.Journee;

import java.io.*;
import java.util.HashMap;

import static instance.modele.contrainte.TypeMode.*;

public class JourneeReader {

    private HashMap<Integer,Journee> journees;
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
    public JourneeReader(String inputPath) throws ReaderException {
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
    public HashMap<Integer, Journee> readJournees() throws ReaderException {
        try{
            FileReader f = new FileReader(this.instanceFile.getAbsolutePath());
            BufferedReader br = new BufferedReader(f);
            int nbEquipes = lireNbEquipes(br);
            journees = new HashMap<>();
            for(int id=0;id<nbEquipes*2-2;id++){
                journees.put(id,new Journee(id));
            }
            br.close();
            f.close();
            return  journees;
        } catch (FileNotFoundException ex) {
            throw new FileExistException(instanceFile.getName());
        } catch (IOException ex) {
            throw new ReaderException("IO exception", ex.getMessage());
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
