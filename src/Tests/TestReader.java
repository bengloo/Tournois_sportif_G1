package Tests;

import instance.Instance;
import io.InstanceReader;
import io.exception.ReaderException;
import solution.Journee;

import java.io.File;
import java.util.HashMap;

public class TestReader {
    public static void main(String[] args) {
        //TODO chemin relatif
        File dir  = new File("./instances");
        System.out.println(dir.toString());
        File[] liste = dir.listFiles();
        System.out.println(liste.toString());
        for(File item : liste){
            if(item.isFile()&&item.getName().contains("Test")&&!item.getName().equals("readMe.txt"))
            {
                System.out.format("Nom du fichier: %s%n", item.getName());
                String path=item.getName();
                try {
                    InstanceReader reader = new InstanceReader("instances/"+path);
                    Instance i= reader.readInstance();
                    System.out.println(i);
                    System.out.println("Instance lue avec success !");
                } catch (ReaderException ex) {
                    System.out.println(ex.getMessage());
                }
            }
            }

        }


}
