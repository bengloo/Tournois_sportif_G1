package operateur;

import instance.modele.contrainte.Contrainte;
import instance.modele.contrainte.TypeContrainte;
import instance.modele.contrainte.TypeMode;
import solution.Journee;
import solution.Rencontre;
import solution.Solution;

import java.util.HashMap;
import java.util.Map;

public class OperateurEchange extends Operateur{


    public OperateurEchange(Solution c, Rencontre r, Rencontre r2) {
        super( r.getJournee(), r,r2,r2.getJournee(),c);
    }

    @Override
    protected Integer evalDeltaCout() {
        //if(deltaCoef==null)return Integer.MAX_VALUE;
        Integer deltaCout=0;
        if(!isRealisableInital()){
            //System.out.println(this.toString());
            return Integer.MAX_VALUE;

        }
        for(TypeContrainte type:TypeContrainte.values()){
            for(Contrainte c: getChampionnat().getContraintes(type)){
                Object deltaCoef = c.evalDeltaCoef(getChampionnat(),this);
                if(isNotDelatcoefNull(type,deltaCoef))deltaCout+=c.evalDeltaCout(getChampionnat(),this,deltaCoef);
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
                if(isNotDelatcoefNull(type,deltaCoef)){
                    deltaCoefs.put(c,deltaCoef);
                }

            }
        }
        return deltaCoefs;
    }

    protected boolean isNotDelatcoefNull(TypeContrainte type,Object deltaCoef){
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
        //check nb rencontre journee
        if(this.getJournee().getRencontres().size()+1>getChampionnat().getNBRencontreJournee()){
            //System.out.println("nb max r pour j");
            return false;
        }
        //check un match par jr
        for(Rencontre r:getJournee().getRencontres().values()){
            if(r.isConcerne(getRencontre().getDomicile(), TypeMode.INDEFINI) || r.isConcerne(getRencontre().getExterieur(), TypeMode.INDEFINI)){
                //System.out.println("nb rencontre par equipe joure >1");
                return false;
            }

        }

        //match aller ou retour par phase si il existe
        if(getChampionnat().getPhase(getJournee())==getChampionnat().getPhase(getChampionnat().getRencontres().get(getRencontre().getLabelRetour()).getJournee())){
            /*int a=getChampionnat().getPhase(getJournee());
            Journee j=getJournee();
            int b=getChampionnat().getPhase(getChampionnat().getRencontres().get(getRencontre().getLabelRetour()).getJournee());
            Journee j2=getChampionnat().getRencontres().get(getRencontre().getLabelRetour()).getJournee();
            */
            //System.out.println("matche retour même phase");
            return false;
        }
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
        //TODO y'a peut étre moyen de mieux parcour un hashmap à moindre temps
        //pour chaque contrainte impacté par l'operation
        for(Contrainte c:deltaCoefs.keySet()){
            //on update le cout et les coef des contraintes
            getChampionnat().addCoefCoutContrainte(c,deltaCoefs.get(c),c.evalDeltaCout(getChampionnat(),this,deltaCoefs.get(c)));
        }
        //on update le cout total
        //getChampionnat().addCoutTotal(this.getCout());
        //on affect la rencontre à la journee
        try {
            return echangeRencontre(this);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean echangeRencontre(OperateurEchange o){
        if(o.getJournee().addRencontre(o.getRencontre2())){
            if(o.getJournee2().addRencontre(o.getRencontre()))return true;
        }
        return false;
    }

}
