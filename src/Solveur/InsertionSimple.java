package Solveur;

import instance.Instance;
import solution.Rencontre;
import solution.Solution;

/** classe définissant InsertionSimple (implémentant l'interface Solveur)
 * @author Engloo Benjamin
 * @author Morcq Alexandre
 * @author Sueur Jeanne
 * @author Lux Hugo
 * @version 1.0
 */
public class InsertionSimple implements Solveur{

    @Override
    public String getNom() {
        return "InsertionSimple";
    }

    @Override
    public Solution solve(Instance instance) {
        Solution solution = new Solution(instance);
        for(Rencontre r:solution.getRencontres().values()){
            System.out.println(solution);
            solution.getPremiereInsertion(r).doMouvementIfRealisable();
        }
        if(!solution.check()) return null;
        return solution;
    }
}
