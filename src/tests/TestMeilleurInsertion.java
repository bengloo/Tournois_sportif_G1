package tests;

import instance.Instance;
import io.InstanceReader;
import io.exception.ReaderException;
import solution.Solution;
import solveur.InsertionSimple;
import solveur.MeilleureInsertion;

/** classe définissant TestInsertionSimple (pour tester la classe InsertionSimple)
 * @author Engloo Benjamin
 * @author Morcq Alexandre
 * @author Sueur Jeanne
 * @author Lux Hugo
 * @version 1.0
 */
public class TestMeilleurInsertion {
    public static void main(String[] args) {
        //String path="instances/instance_ITC2021_Test_4.txt";
        //String path="instances/instance_test_ContraintePlacement_4Equipe.txt";
        //String path="instances/instance_test_ContrainteHBClassement_4Equipes.txt";
        //String path="instances/instance_test_ContrainteRencontres_4Equipes.txt";
        String path="instances/instance_test_sansContrainte_6Equipe.txt";
        try {
            InstanceReader reader = new InstanceReader(path);
            Instance i= reader.readInstance();
            //System.out.println(i);

            MeilleureInsertion solveur = new MeilleureInsertion();
            Solution s = solveur.solve(i);
            System.out.println(s.getNBRencontreJournee());
            s.writeSolution(solveur.getNom());
            System.out.println(s.toString());
            System.out.println(s.check());
        } catch (ReaderException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
