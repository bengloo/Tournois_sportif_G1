package tests.hold;

import instance.Instance;
import io.InstanceReader;
import io.exception.ReaderException;
import solution.Solution;

/** classe définissant TestChampionnat (pour tester la classe Solution)
 * @author Engloo Benjamin
 * @author Morcq Alexandre
 * @author Sueur Jeanne
 * @author Lux Hugo
 * @version 1.0
 */
public class TestChampionnat {
    public static void main(String[] args) {
        String path="instances/instance_test_sansContrainte_20Equipe.txt";
        try {
            InstanceReader reader = new InstanceReader(path);
            Instance i= reader.readInstance();
            Solution s = new Solution(i);
            System.out.println(s.toString());
            System.out.println(s.getContraintes().toString());
        } catch (ReaderException ex) {
            System.out.println(ex.getMessage());
        }

    }
}
