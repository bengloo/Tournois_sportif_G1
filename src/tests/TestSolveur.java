package tests;

import instance.Instance;
import io.InstanceReader;
import io.exception.ReaderException;
import solution.Solution;
import solveur.Solveur;
import solveur.SolveurCplex;
import solveur.SolveurIter;

/** classe définissant TestCplex (pour tester la classe SplveurCplex)
 * @author Engloo Benjamin
 * @author Morcq Alexandre
 * @author Sueur Jeanne
 * @author Lux Hugo
 * @version 1.0
 */
public class TestSolveur {
    public static void main(String[] args) {
        String path="instancesViablesCplex/saufPause/instance_ITC2021_Test_7.txt";
        try {
            InstanceReader reader = new InstanceReader(path);
            Instance i= reader.readInstance();
            Solveur solveur = new SolveurIter( new SolveurCplex(1000,false,false,true),150);
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
}
