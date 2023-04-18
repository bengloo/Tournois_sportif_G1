package Tests;

import Solveur.InsertionSimple;
import instance.Instance;
import io.InstanceReader;
import io.exception.ReaderException;
import solution.Solution;

/** Class permettant de tester notre Insertion.
 * @author Engloo Benjamin
 * @author Morcq Alexandre
 * @author Sueur Jeanne
 * @author Lux Hugo
 * @version 0.5
 */
public class TestInsertionSimple {
    public static void main(String[] args) {
        //String path="instances/instance_ITC2021_Test_4.txt";
        String path="instances/instance_test_sansContrainte_2Equipe.txt";
        try {
            InstanceReader reader = new InstanceReader(path);
            Instance i= reader.readInstance();
            //System.out.println(i);

            InsertionSimple solveur = new InsertionSimple();

            System.out.println(solveur.solve(i).toString());
        } catch (ReaderException ex) {
            System.out.println(ex.getMessage());
        }


    }
}
