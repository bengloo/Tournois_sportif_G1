package tests.Hold;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TestCheckerProf {
    public static void main(String[] args) {

            String cheminJar = "checkerProf/CheckerChampionnat.jar";
            try {
                ProcessBuilder processBuilder = new ProcessBuilder("java", "-jar", cheminJar);
                Process process = processBuilder.start();

                // Capturer la sortie du processus exécutant le fichier JAR
                InputStream inputStream = process.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    // Traiter la sortie du fichier JAR
                    System.out.println(line);
                }

                // Attendre la fin de l'exécution du processus
                int exitCode = 0;
                try {
                    exitCode = process.waitFor();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("Le fichier JAR a terminé avec le code de sortie : " + exitCode);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

    }
}
