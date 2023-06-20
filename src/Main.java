import instance.Instance;
import io.InstanceReader;
import io.exception.ReaderException;
import solution.Solution;
import solveur.Solveur;
import solveur.SolveurCplex;
import solveur.SolveurIter;

/** classe Main pour generer le jar du livrable de projet
 * @author Engloo Benjamin
 * @author Morcq Alexandre
 * @author Sueur Jeanne
 * @author Lux Hugo
 * @version 1.0
 */


public class Main {
    public static String inFile;

    public static void main(String[] args) {
        String cheminFichier;
        Integer watchDog;
        Boolean minimiseDure;
        Boolean minimiseSouple;
        Boolean avoidContraintePauseGlobale;
        readArgs(args);
        cheminFichier = args[0];
        watchDog = Integer.valueOf(args[1]);
        minimiseDure = Boolean.valueOf(args[2]);
        minimiseSouple = Boolean.valueOf(args[3]);
        avoidContraintePauseGlobale = Boolean.valueOf(args[4]);
        String path=cheminFichier;
        //System.out.println(cheminFichier + watchDog + minimiseDure + minimiseSouple + avoidContraintePauseGlobale);
        //String path="instanceTestUnitaire/instance_test_ContraintePlacement_4Equipe.txt";
        //String path="instancesViablesCplex/instance_ITC2021_Early_3.txt";
        try {
            InstanceReader reader = new InstanceReader(path);
            Instance i= reader.readInstance();
            //System.out.println(i);
            //Solveur solveur = new SolveurCplex();
            Solveur solveur = new SolveurIter( new SolveurCplex(watchDog,minimiseDure,minimiseSouple,avoidContraintePauseGlobale),100);

            Solution s = solveur.solve(i);


            s.writeSolution(solveur.getNom());
            s.writeSolutionCheckerProf(solveur.getNom());

            System.out.println(s.toStringSimple());
            System.out.println(s.check());
            System.out.println(s.getLog());
            s.restLog();
            //System.out.println(s.toString());
        } catch (ReaderException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static void readArgs(String args[]) {
        if (args.length < 1) {
            printUsage();
            System.exit(-1);
        }

        int index = -1;

        inFile = args[++index];
    }
    public static void printUsage() {
        System.out.println("Usage: java -jar Tournois_sportif_G1.jar <fichier> <watchdog> <miniserDure(bool)> " +
                "<miniserSouple(bool)> <enlever Pauseglobale>");
    }
}
