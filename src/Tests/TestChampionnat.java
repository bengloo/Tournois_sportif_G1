package Tests;

import instance.Instance;
import io.InstanceReader;
import io.JourneeReader;
import io.exception.ReaderException;
import solution.Championnat;
import solution.Journee;

import java.util.HashMap;

public class TestChampionnat {
    public static void main(String[] args) {
        String path="instances/instance_ITC2021_Test_4.txt";
        try {
            InstanceReader reader = new InstanceReader(path);
            JourneeReader readerJ = new JourneeReader(path);
            HashMap<Integer, Journee> journees = new HashMap<>();
            journees= readerJ.readJournees();
            Instance i= reader.readInstance(journees);
            Championnat c= new Championnat(i,journees);
        } catch (ReaderException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
