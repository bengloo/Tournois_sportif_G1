package Tests;

import io.InstanceReader;
import io.JourneeReader;
import io.exception.ReaderException;
import solution.Journee;

import java.util.HashMap;

public class TestReader {
    public static void main(String[] args) {
        String path="instances/instance_ITC2021_Test_4.txt";
        try {
            InstanceReader reader = new InstanceReader(path);
            JourneeReader readerJ = new JourneeReader(path);
            HashMap<Integer, Journee> journees = new HashMap<>();
            journees= readerJ.readJournees();
            reader.readInstance(journees);
            System.out.println("Instance lue avec success !");
        } catch (ReaderException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
