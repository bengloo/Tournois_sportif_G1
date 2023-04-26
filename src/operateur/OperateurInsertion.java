package operateur;

import instance.modele.contrainte.Contrainte;
import instance.modele.contrainte.TypeContrainte;
import solution.Journee;
import solution.Rencontre;
import solution.Solution;

import java.util.HashMap;
import java.util.Map;

/** classe définissant OperateurInsertion (hérite de Operateur)
 * @author Engloo Benjamin
 * @author Morcq Alexandre
 * @author Sueur Jeanne
 * @author Lux Hugo
 * @version 1.0
 */
public class OperateurInsertion extends Operateur{

    public OperateurInsertion() {
    }
    public OperateurInsertion(Solution c, Journee j, Rencontre r) {
        super(c, j, r);
    }

    @Override
    protected int evalDeltaCout(Map<Contrainte, Integer> delatCoef) {
        if(delatCoef==null)return Integer.MAX_VALUE;
        int deltaCout=0;
        for(TypeContrainte type:TypeContrainte.values()){
            for(Contrainte c: getChampionnat().getContraintes(type)){
                deltaCout+=c.evalDeltatCout(getChampionnat(),this);
            }
        }
        return deltaCout;
    }

    @Override
    protected Map<Contrainte, Integer> evalDeltaCoefs() {
        if(!isRealisableInital())return null;
        Map<Contrainte, Integer> delatCoefs = new HashMap<>();

        for(TypeContrainte type:TypeContrainte.values()){
            for(Contrainte c: getChampionnat().getContraintes(type)){
                int deltaCoef = c.evalDeltatCoef(getChampionnat(),this);
                if(deltaCoef!=0)delatCoefs.put(c,deltaCoef);
            }
        }
        return delatCoefs;
    }

    @Override
    protected boolean isRealisableInital() {

        if(this.getJournee().getRencontres().size()+1>getChampionnat().getNBRencontreJournee()){
            return false;
        }
        if(getChampionnat().getPhase(getJournee())==getChampionnat().getPhase(getChampionnat().getRencontres().get(getRencontre().getLabelRetour()).getJournee())){
            int a=getChampionnat().getPhase(getJournee());
            Journee j=getJournee();
            int b=getChampionnat().getPhase(getChampionnat().getRencontres().get(getRencontre().getLabelRetour()).getJournee());
            Journee j2=getChampionnat().getRencontres().get(getRencontre().getLabelRetour()).getJournee();

            return false;
        }

        return true;
    }

    @Override
    protected boolean doMouvement() {
        Map<Contrainte, Integer> DeltaCoefs= evalDeltaCoefs();
        //TODO y'a peut étre moyen de mieux parcour un hashmap à moindre temps
        //pour chaque contrainte impacté par l'operation
        for(Contrainte c:DeltaCoefs.keySet()){
            //on update le cout et les coef des contraintes
            getChampionnat().addCoefCoutContrainte(c,DeltaCoefs.get(c),c.evalDeltatCout(getChampionnat(),this,DeltaCoefs.get(c)));
        }
        //on update le cout total
        getChampionnat().addCoutTotal(this.getCout());
        //on affect la rencontre à la journee
        try {
            return getChampionnat().getJournees().get(this.getJournee().getId()).addRencontre(this.getRencontre());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
