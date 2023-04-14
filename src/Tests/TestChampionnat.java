package Tests;

import instance.Instance;
import io.InstanceReader;
import io.exception.ReaderException;
import solution.Championnat;
import solution.Journee;

import java.util.HashMap;

public class TestChampionnat {
    public static void main(String[] args) {
        String path="instances/instance_ITC2021_Test_4.txt";
        try {
            InstanceReader reader = new InstanceReader(path);
            Instance i= reader.readInstance();
            //System.out.println(i);
            Championnat c= new Championnat(i);
            System.out.println(c.toString());
        } catch (ReaderException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
