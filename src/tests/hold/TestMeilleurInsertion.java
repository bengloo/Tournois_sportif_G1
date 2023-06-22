package tests.hold;

import instance.Instance;
import io.InstanceReader;
import io.exception.ReaderException;
import solution.Solution;
import solveur.abandonne.MeilleureInsertionV2;

/** classe définissant TestInsertionSimple (pour tester la classe InsertionSimple)
 * @author Engloo Benjamin
 * @author Morcq Alexandre
 * @author Sueur Jeanne
 * @author Lux Hugo
 * @version 1.0
 */
public class TestMeilleurInsertion {
    public static void main(String[] args) {
        String path="instances/instance_ITC2021_Early_1.txt";
        try {
            InstanceReader reader = new InstanceReader(path);
            Instance i= reader.readInstance();

            MeilleureInsertionV2 solveur = new MeilleureInsertionV2();
            Solution s = solveur.solve(i);

            s.writeSolution(solveur.getNom());
            System.out.println(s.toString());
            System.out.println(s.check());
        } catch (ReaderException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
