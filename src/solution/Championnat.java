package solution;

import instance.Instance;

import java.util.Map;

public class Championnat {
    private final Instance instance;
    private Map<Integer, Journee> journees;

    public Championnat(Instance instance) {
        this.instance = instance;
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

