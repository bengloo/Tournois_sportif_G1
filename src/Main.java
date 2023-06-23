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
    public static String cheminFichier;
    private static Integer watchDog;
    private static Boolean minimiseDure;
    private static Boolean minimiseSouple;
    private static Boolean avoidContraintePauseGlobale;

    public static void main(String[] args) {
        readArgs(args);
        String path=cheminFichier;
        try {
            InstanceReader reader = new InstanceReader(path);
            Instance i= reader.readInstance();
            Solveur solveur = new SolveurIter( new SolveurCplex(watchDog,minimiseDure,minimiseSouple,avoidContraintePauseGlobale),1);
            Solution s = solveur.solve(i);
            s.writeSolution(solveur.getNom());
            s.writeSolutionCheckerProf();
            System.out.println(s.toStringSimple());
            System.out.println(s.check());
            System.out.println(s.getLog());
            s.restLog();
        } catch (ReaderException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static void readArgs(String args[]) {
        if (args.length < 4) {
            printUsage();
            System.exit(-1);
        }
        cheminFichier = args[0];
        watchDog = Integer.valueOf(args[1]);
        minimiseDure = Boolean.valueOf(args[2]);
        minimiseSouple = Boolean.valueOf(args[3]);
        avoidContraintePauseGlobale = Boolean.valueOf(args[4]);
    }
    public static void printUsage() {
        System.out.println("Usage: java -jar Tournois_sportif_G1.jar <fichier> <watchdog> <miniserDure(bool)> " +
                "<miniserSouple(bool)> <enlever Pauseglobale>(bool)");
    }
}
