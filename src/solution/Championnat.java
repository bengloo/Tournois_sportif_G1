package solution;

import instance.Instance;
import instance.modele.contrainte.Contrainte;
import instance.modele.contrainte.TypeContrainte;

import java.util.HashMap;
import java.util.Map;

public class Championnat {
    private static Instance instance;
    private Map<Integer, Journee> journees;

    private Map<Contrainte,Integer> coutContraintes;

    public Championnat(Instance instance,HashMap<Integer,Journee> journees) {
        this.instance = instance;
        this.journees = journees;
        this.coutContraintes = new HashMap<>();
        for(TypeContrainte type:TypeContrainte.values()) {
            for (Contrainte contrainte : instance.getContraintes(type)){
                coutContraintes.put(contrainte,0);
            };
        }
    }

    public static Instance getInstance() {
        return instance;
    }

    public int getPhase(Journee journee){
        return journee.getId()>getNbJournee()/2?2:1;
    }
    public int getNbJournee(){
        return getNBEquipe()*2-2;
    }

    public int getNBRencontre(){
        return getNBEquipe()*6-6;
    }

    public int getNBEquipe(){
        return this.instance.getEquipes().size();
    }

    /*
    * check quantiativement les journee et rencontre creer
    * */
    public boolean checkIntegriteeChampiona(){
        return false;
    }

    public boolean checkAllContrainte(){
        return false;
    }


}

