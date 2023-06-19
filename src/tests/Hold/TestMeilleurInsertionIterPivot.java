package tests.Hold;

import instance.Instance;
import io.InstanceReader;
import io.exception.ReaderException;
import solution.Solution;
import solveur.MeilleureInsertionV2;
import solveur.Abandoné.SolveurIterPivot;

/** classe définissant TestInsertionSimple (pour tester la classe InsertionSimple)
 * @author Engloo Benjamin
 * @author Morcq Alexandre
 * @author Sueur Jeanne
 * @author Lux Hugo
 * @version 1.0
 */
public class TestMeilleurInsertionIterPivot {
    public static void main(String[] args) {
        //String path="instances/instance_ITC2021_Early_1.txt";
        String path="instances/instance_test_sansContrainte_10Equipe.txt";
        try {
            InstanceReader reader = new InstanceReader(path);
            Instance i= reader.readInstance();
            //System.out.println(i);

            SolveurIterPivot solveur = new SolveurIterPivot(new MeilleureInsertionV2(),200);
            Solution s = solveur.solve(i);
            s.writeSolution(solveur.getNom());

            System.out.println(s.check());
        } catch (ReaderException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
