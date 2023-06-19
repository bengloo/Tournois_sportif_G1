package tests;

import instance.Instance;
import io.InstanceReader;
import io.exception.ReaderException;
import solution.Solution;
import solveur.Solveur;
import solveur.SolveurCplex;

import java.net.InetAddress;
import java.net.UnknownHostException;

/** classe définissant TestInsertionSimple (pour tester la classe InsertionSimple)
 * @author Engloo Benjamin
 * @author Morcq Alexandre
 * @author Sueur Jeanne
 * @author Lux Hugo
 * @version 1.0
 */
public class TestSolveur {
    public static void main(String[] args) {
        //String path="instanceViableCplex/instance_ITC2021_Test_1.txt";
        //String path="instanceTestUnitaire/instance_test_ContraintePlacement_4Equipe.txt";
        String path="instances/instance_ITC2021_Test_7.txt";
        try {
            InstanceReader reader = new InstanceReader(path);
            Instance i= reader.readInstance();
            //System.out.println(i);
            Solveur solveur = new SolveurCplex();
            //Solveur solveur = new SolveurIter( new SolveurCplex(),1);
            long start = System.currentTimeMillis();
            Solution s = solveur.solve(i);
            long time = System.currentTimeMillis() - start;
            try {
                s.addLog("|"+ InetAddress.getLocalHost().getHostName()+"|"+System.getProperty("user.name"));
            } catch (UnknownHostException e) {
                throw new RuntimeException(e);
            }
            s.writeSolution(solveur.getNom());
            s.writeSolutionChekerProf(solveur.getNom());
            //TODO integré le cheker du prof si il peux paratager ces resultat en resource
            s.addLog("|"+time);
            s.logCheckProf();
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
