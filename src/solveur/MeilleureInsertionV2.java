package solveur;

import instance.Instance;
import operateur.OperateurInsertion;
import solution.Rencontre;
import solution.Solution;

import java.util.ArrayList;
import java.util.Collections;

/** classe définissant MeilleureInsertion (implémentant l'interface Solveur)
 * @author Engloo Benjamin
 * @author Morcq Alexandre
 * @author Sueur Jeanne
 * @author Lux Hugo
 * @version 1.0
 */
public class MeilleureInsertionV2 implements Solveur{
    @Override
    public String getNom() {
        return "Meilleure Insertion V2";
    }

    @Override
    public Solution solve(Instance instance) {
        Solution solution = new Solution(instance);
        ArrayList<OperateurInsertion> list = new ArrayList<OperateurInsertion>(solution.getInsertionMinMarge());
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
                //System.err.println("situation blocante");
                return solution;//la meilleur insertion simple peux ammener à une situation bloquante on transmet alor la solution incomplete
            }else{
                if(!o.doMouvementIfRealisable()){
                    //System.err.println("situation blocante2");
                    return solution;
                }else{
                    solution.updateMageJournee(o);
                    //System.out.println(solution.nbMargineString());
                    list = new ArrayList<OperateurInsertion>(solution.getInsertionMinMarge());
                };
            }
        }
        //if(!solution.check()) return null;
        return solution;
    }
}
