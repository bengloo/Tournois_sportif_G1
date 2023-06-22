package operateur;

import instance.modele.contrainte.Contrainte;
import instance.modele.contrainte.TypeContrainte;
import solution.Journee;
import solution.Rencontre;
import solution.Solution;

import java.util.HashMap;
import java.util.Map;

/**
 * Class de l' Operateur d'échange /!\ developement abandoné
 */
public class OperateurEchange extends Operateur{


    public OperateurEchange(Solution c, Rencontre r, Rencontre r2) {
        super( r.getJournee(), r,r2,r2.getJournee(),c);
    }

    @Override
    protected Integer evalDeltaCout() {
        Integer deltaCout=0;
        if(!isRealisableInital()){
            return Integer.MAX_VALUE;

        }
        for(TypeContrainte type:TypeContrainte.values()){
            for(Contrainte c: getChampionnat().getContraintes(type)){
                Object deltaCoef = c.evalDeltaCoef(getChampionnat(),this);
                if(isNotDeltaCoefNull(type,deltaCoef))deltaCout+=c.evalDeltaCout(getChampionnat(),this,deltaCoef);
                if(deltaCout==Integer.MAX_VALUE)return Integer.MAX_VALUE;
            }
        }
        return deltaCout;
    }

    @Override
    protected Map<Contrainte, Object> evalDeltaCoefs() {
        if(!isRealisableInital())return null;
        Map<Contrainte, Object> deltaCoefs = new HashMap<>();
        for(TypeContrainte type:TypeContrainte.values()){
            for(Contrainte c: getChampionnat().getContraintes(type)){
                Object deltaCoef = c.evalDeltaCoef(getChampionnat(),this);
                if(isNotDeltaCoefNull(type,deltaCoef)){
                    deltaCoefs.put(c,deltaCoef);
                }

            }
        }
        return deltaCoefs;
    }

    protected boolean isNotDeltaCoefNull(TypeContrainte type, Object deltaCoef){
        if(type != TypeContrainte.EQUITE){
            if((Integer) deltaCoef!=0){
                return true;
            }
        }else{
            if(((HashMap<Integer,Integer>)deltaCoef).size()!=0){
                return true;
            }
        }
        return false;
    }

    @Override
    protected boolean isRealisableInital() {
        if(!this.getRencontre().getLabel().equals(this.rencontre2.getLabelRetour()))return false;
        return true;
    }

    public Rencontre getRencontre2() {
        return rencontre2;
    }

    public  Journee getJournee2(){
        return journee2;
    }

    @Override
    protected boolean doMouvement() {
        Map<Contrainte, Object> deltaCoefs= evalDeltaCoefs();
        //pour chaque contrainte impactée par l'opération
        for(Contrainte c:deltaCoefs.keySet()){
            //on update le cout et les coeffs des contraintes
            getChampionnat().addCoefCoutContrainte(c,deltaCoefs.get(c),c.evalDeltaCout(getChampionnat(),
                    this,deltaCoefs.get(c)));
        }
        //on affecte la rencontre à la journée
        try {
            return echangeRencontre(this);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean echangeRencontre(OperateurEchange o){
        o.getJournee().removeRencontre(o.getRencontre());
        o.getJournee2().removeRencontre(o.getRencontre2());
        o.getRencontre().setJournee(null);
        o.getRencontre2().setJournee(null);
        if(o.getJournee().addRencontre(o.getRencontre2())){
            if(o.getJournee2().addRencontre(o.getRencontre()))return true;
        }

        return false;
    }

}
