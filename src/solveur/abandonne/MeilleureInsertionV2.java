package solveur.abandonne;

import instance.Instance;
import operateur.OperateurInsertion;
import solution.Solution;
import solveur.Solveur;

import java.util.ArrayList;

/** classe définissant MeilleureInsertion (implémentant l'interface Solveur)
 * @author Engloo Benjamin
 * @author Morcq Alexandre
 * @author Sueur Jeanne
 * @author Lux Hugo
 * @version 1.0
 */
public class MeilleureInsertionV2 implements Solveur {
    @Override
    public String getNom() {
        return "Meilleure Insertion V2";
    }

    @Override
    public Solution solve(Instance instance) {
        Solution solution = new Solution(instance);
        ArrayList<OperateurInsertion> list = new ArrayList<OperateurInsertion>(solution.getInsertionMinMarge(solution.updateMages(
                null)));
        //Collections.shuffle(list);


        //System.out.println(solution);
        //System.out.println(solution.nbMargineString());
        //System.out.println("solution initial\n\n");
        while (list.size()>0){

            //System.out.println("RencontrePrioritaire");
            //System.out.println(list.toString());
            OperateurInsertion o= solution.getMeilleureInsertionV2(list);
            //System.out.println("operationRetenus");
            //System.out.println(o.toString());
            if(o==null){
                //System.err.println("situation bloquante");
                return solution;//la meilleure insertion simple peut amener à une situation bloquante on transmet
                // alors la solution incomplète
            }else{
                if(!o.doMouvementIfRealisable()){
                    //System.err.println("situation bloquante2");
                    return solution;
                }else{
                    solution.updateMages(o);
                    //System.out.println(solution.nbMargineString());
                    //System.out.println(solution.toString());
                    list = new ArrayList<OperateurInsertion>(solution.getInsertionMinMarge(solution.updateMages(o)));
                };
            }
        }
        //if(!solution.check()) return null;
        return solution;
    }
}
