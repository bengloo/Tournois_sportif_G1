package solveur.Abandoné;

import instance.Instance;
import solution.Solution;
import solveur.Solveur;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
/** classe définissant SolveurIterThread (implémentant l'interface Solveur)
 * @author Engloo Benjamin
 * @author Morcq Alexandre
 * @author Sueur Jeanne
 * @author Lux Hugo
 * @version 1.0
 */
public class SolveurIterThread implements Solveur {

    private final Solveur solveurInitial;

    public SolveurIterThread(Solveur solveurInitial) {
        this.solveurInitial = solveurInitial;
    }

    @Override
    public String getNom() {
        return  "SolveurIter("+solveurInitial.getNom()+")";
    }

    @Override
    public Solution solve(Instance instance) {
        List<Thread> threadList = new ArrayList<>();
        List<Solution> solutions = new ArrayList<>();
        CountDownLatch latch = new CountDownLatch(1);

        // Créez une classe de Runnable qui effectue votre traitement
        Runnable myTask = () -> {
            //code de traitement
            Solution sol = this.solveurInitial.solve(instance);
            // Vérifiez le critère d'arrêt
            boolean criteriaMet = sol.check();
            if (criteriaMet) {
                solutions.add(sol);
                latch.countDown(); // Signal pour débloquer le thread principal
            }
        };

        // Ajoutez les threads à la liste
        for (int i = 0; i < 4; i++) {
            Thread thread = new Thread(myTask);
            threadList.add(thread);
        }

        // Démarrez les threads
        for (Thread thread : threadList) {
            thread.start();
        }

        try {
            latch.await(); // Attend que le critère soit satisfait par l'un des threads
        } catch (InterruptedException e) {
            // Gérer l'interruption du thread principal si nécessaire
            Thread.currentThread().interrupt();
        }

        // Arrêtez les autres threads qui n'ont pas satisfait le critère
        for (Thread thread : threadList) {
            if (thread.isAlive()) {
                thread.interrupt();
            }
        }

        // Retourne la solution du thread qui a satisfait le critère
        if (!solutions.isEmpty()) {
            System.out.println(this.getNom()+" | "+instance.getNom());
            return solutions.get(0);
        }

        // Retourne une solution par défaut si aucun thread n'a satisfait le critère
        return null;
    }
}
