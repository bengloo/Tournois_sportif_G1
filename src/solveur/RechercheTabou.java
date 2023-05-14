package solveur;

import instance.Instance;
import operateur.ListeTabou;
import operateur.OperateurLocal;
import operateur.TypeOperateurLocal;
import solution.Solution;

public class RechercheTabou implements Solveur {

    private final Solveur solveurInitial;

    public RechercheTabou(Solveur solveurInitial) {
        this.solveurInitial = solveurInitial;
    }

    @Override
    public String getNom() {
        return "RechercheTabou("+solveurInitial.getNom()+")";
    }

    @Override
    public Solution solve(Instance instance) {
        Solution sol=this.solveurInitial.solve(instance);
        Solution bestSol=new Solution(sol);
        int nbIterSansAmelioration =0;
        int nbIterMax=10000;
        ListeTabou tabou= ListeTabou.getInstance();
        tabou.vider();
        while (nbIterSansAmelioration<nbIterMax){
            OperateurLocal bestOL= OperateurLocal.getOperateurLocal(TypeOperateurLocal.ECHANGE);

            for(TypeOperateurLocal type: TypeOperateurLocal.values()){
                OperateurLocal ol = sol.getMeilleureOperateurLocal(type);
                if (ol.isMeilleur(bestOL)) {
                    bestOL = ol;
                }

            }
            if(sol.doMouvementRechercheLocale(bestOL)){
                tabou.setDelatAspiration(bestSol.getCoutTotal()-sol.getCoutTotal());
                tabou.add(bestOL);
            }
            if(sol.isMeilleure(bestSol)){
                bestSol= new Solution(sol);
                tabou.setDelatAspiration(bestSol.getCoutTotal()-sol.getCoutTotal());
                nbIterSansAmelioration=0;
            }else{
                nbIterSansAmelioration+=1;
            }

        }
        return bestSol;
    }
}
