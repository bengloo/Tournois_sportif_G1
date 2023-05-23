package tests;

import instance.Instance;
import io.InstanceReader;
import io.exception.ReaderException;
import solution.Solution;
import solveur.MeilleureInsertion;
import solveur.MeilleureInsertionV2;
import solveur.SolveurIter;

/** classe définissant TestInsertionSimple (pour tester la classe InsertionSimple)
 * @author Engloo Benjamin
 * @author Morcq Alexandre
 * @author Sueur Jeanne
 * @author Lux Hugo
 * @version 1.0
 */
public class TestMeilleurInsertionIter {
    public static void main(String[] args) {
        //String path="instances/instance_ITC2021_Early_1.txt";
        String path="instances/instance_test_sansContrainte_20Equipe.txt";
        try {
            InstanceReader reader = new InstanceReader(path);
            Instance i= reader.readInstance();
            //System.out.println(i);

            SolveurIter solveur = new SolveurIter(new MeilleureInsertionV2(),200);
            Solution s = solveur.solve(i);
            //System.out.println(s.getNBRencontreJournee());
            s.writeSolution(solveur.getNom());
            //System.out.println(s.toString());
            System.out.println(s.check());
        } catch (ReaderException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
