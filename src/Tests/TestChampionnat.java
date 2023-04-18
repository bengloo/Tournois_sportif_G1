package Tests;

import instance.Instance;
import io.InstanceReader;
import io.exception.ReaderException;
import solution.Solution;

/** Class permettant de tester notre class championnat.
 * @author Engloo Benjamin
 * @author Morcq Alexandre
 * @author Sueur Jeanne
 * @author Lux Hugo
 * @version 0.5
 */
public class TestChampionnat {
    public static void main(String[] args) {
        String path="instances/instance_ITC2021_Test_4.txt";
        try {
            InstanceReader reader = new InstanceReader(path);
            Instance i= reader.readInstance();
            //System.out.println(i);
            Solution c= new Solution(i);
            System.out.println(c.toString());
            System.out.println(c.getContraintes().toString());
        } catch (ReaderException ex) {
            System.out.println(ex.getMessage());
        }

    }
}
