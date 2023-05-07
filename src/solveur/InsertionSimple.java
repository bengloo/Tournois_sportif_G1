package solveur;

import instance.Instance;
import operateur.OperateurInsertion;
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
            OperateurInsertion o= solution.getPremiereInsertion(r);
            if(o==null){
                return solution;//l'insertion simple peux ammener à une situation bloquante on transmet alor la solution incomplete
            }else{
                o.doMouvementIfRealisable();
            }
        }
        if(!solution.check()) return null;
        return solution;
    }
}
