package solveur;

import instance.Instance;
import operateur.ListeTabou;
import operateur.OperateurLocal;
import operateur.TypeOperateurLocal;
import solution.Solution;

public class RechercheLocale implements Solveur{

    private final Solveur solveurInitial;

    public RechercheLocale(Solveur solveurInitial) {
        this.solveurInitial = solveurInitial;
    }

    @Override
    public String getNom() {
        return "Recherche Locale("+solveurInitial.getNom()+")";
    }


    @Override
    public Solution solve(Instance instance) {
        ListeTabou tabou= ListeTabou.getInstance();
        tabou.vider();
        Solution sol=this.solveurInitial.solve(instance);
        boolean improve = true;
        while (improve){
            improve=false;
            for(TypeOperateurLocal type: TypeOperateurLocal.values()){
                OperateurLocal operateur = sol.getMeilleureOperateurLocal(type);
                if (operateur.isMouvementAmeliorant()) {
                    sol.doMouvementRechercheLocale(operateur);
                    improve = true;

                }

            }
        }
        return sol;
    }
}
