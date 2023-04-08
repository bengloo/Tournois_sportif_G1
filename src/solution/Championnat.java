package solution;

import instance.Instance;
import instance.modele.Journee;

import java.util.Map;

public class Championnat {
    private final Instance instance;
    private Map<Integer, JourneeChampionat> journees;

    public Championnat(Instance instance) {
        this.instance = instance;
        for(Map.Entry j: instance.getJournees().entrySet()){
            int id= (int)j.getKey();
            journees.put(id,new JourneeChampionat(id));
        }
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

