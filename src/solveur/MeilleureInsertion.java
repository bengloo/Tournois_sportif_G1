package solveur;

import instance.Instance;
import operateur.OperateurInsertion;
import solution.Rencontre;
import solution.Solution;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** classe définissant MeilleureInsertion (implémentant l'interface Solveur)
 * @author Engloo Benjamin
 * @author Morcq Alexandre
 * @author Sueur Jeanne
 * @author Lux Hugo
 * @version 1.0
 */
public class MeilleureInsertion implements Solveur{
    @Override
    public String getNom() {
        return "Meilleure Insertion";
    }

    @Override
    public Solution solve(Instance instance) {
        Solution solution = new Solution(instance);
        List<Rencontre> list = new ArrayList<Rencontre>(solution.getRencontres().values());
        Collections.shuffle(list);
        for(Rencontre r:list){
            System.out.println(solution);
            OperateurInsertion o= solution.getMeilleureInsertionRencontre(r);
            if(o==null){
                System.err.println("situation blocante");
                return solution;//la meilleur insertion simple peux ammener à une situation bloquante on transmet alor la solution incomplete
            }else{
                if(!o.doMouvementIfRealisable()){
                    System.err.println("situation blocante2");
                    return solution;
                }else{
                    solution.updateMageJournee(o);
                    System.out.println(solution.nbMargineString());
                };
            }
        }
        //if(!solution.check()) return null;
        return solution;
    }
}
