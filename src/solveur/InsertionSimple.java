package solveur;

import instance.Instance;
import operateur.OperateurInsertion;
import solution.Rencontre;
import solution.Solution;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

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
        List<Rencontre> list = new ArrayList<Rencontre>(solution.getRencontres().values());
        Collections.shuffle(list);
        for(Rencontre r:list){
            System.out.println(solution);
            OperateurInsertion o= solution.getPremiereInsertion(r);
            if(o==null){
                return solution;//l'insertion simple peux ammener à une situation bloquante on transmet alor la solution incomplete
            }else{
                o.doMouvementIfRealisable();
            }
        }
        //if(!solution.check()) return null;
        return solution;
    }
}
