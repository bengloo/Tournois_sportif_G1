package tests;

import instance.Instance;
import io.InstanceReader;
import io.exception.ReaderException;
import solution.Solution;
import solveur.Solveur;
import solveur.SolveurCplex;
import solveur.SolveurIter;

/** classe définissant TestInsertionSimple (pour tester la classe InsertionSimple)
 * @author Engloo Benjamin
 * @author Morcq Alexandre
 * @author Sueur Jeanne
 * @author Lux Hugo
 * @version 1.0
 */
public class TestSolveur {
    public static void main(String[] args) {
        String path="instancesViablesCplex/instance_ITC2021_Test_5.txt";
        //String path="instanceTestUnitaire/instance_test_ContraintePlacement_4Equipe.txt";
        //String path="instancesViablesCplex/instance_ITC2021_Early_3.txt";
        try {
            InstanceReader reader = new InstanceReader(path);
            Instance i= reader.readInstance();
            //System.out.println(i);
            //Solveur solveur = new SolveurCplex();
            Solveur solveur = new SolveurIter( new SolveurCplex(600,false,false,false),100);

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
}
