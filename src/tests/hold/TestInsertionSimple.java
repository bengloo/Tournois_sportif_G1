package tests.hold;

import solution.Solution;
import instance.Instance;
import io.InstanceReader;
import io.exception.ReaderException;
import solveur.abandonne.InsertionSimple;

/** classe définissant TestInsertionSimple (pour tester la classe InsertionSimple)
 * @author Engloo Benjamin
 * @author Morcq Alexandre
 * @author Sueur Jeanne
 * @author Lux Hugo
 * @version 1.0
 */
public class TestInsertionSimple {
    public static void main(String[] args) {
        String path="instances/instance_test_sansContrainte_20Equipe.txt";
        try {
            InstanceReader reader = new InstanceReader(path);
            Instance i= reader.readInstance();

            InsertionSimple solveur = new InsertionSimple();
            Solution s = solveur.solve(i);
            s.writeSolution(solveur.getNom());
            System.out.println(s.toString());
        } catch (ReaderException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
