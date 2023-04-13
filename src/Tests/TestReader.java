package Tests;

import io.InstanceReader;
import io.exception.ReaderException;

public class TestReader {
    public static void main(String[] args) {
        try {
            InstanceReader reader = new InstanceReader("instances/instance_ITC2021_Test_4.txt");
            reader.readInstance();
            System.out.println("Instance lue avec success !");
        } catch (ReaderException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
