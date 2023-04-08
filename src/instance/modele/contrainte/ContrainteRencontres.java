package instance.modele.contrainte;

import solution.Championnat;
import instance.modele.Journee;
import instance.modele.Rencontre;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class ContrainteRencontres extends Contrainte{
    private Map<Integer, Journee> journees;
    private LinkedList<Rencontre> rencontres;
    private Integer min;
    private Integer max;

    public ContrainteRencontres(Integer min,Integer max) {
        super(false);
        this.journees = new HashMap<>();
        this.rencontres = new LinkedList<>();
        this.min = max;
        this.max = max;
    }

    public boolean addJournee(Journee journee){
        if(journee==null) return false;
        this.journees.put(journee.getId(),journee);
        return true;
    }

    public boolean addRencontre(Rencontre rencontre){
        if(rencontre == null) return false;
        this.rencontres.add(rencontre);
        return true;
    }


    @Override
    public int getPenaliteCumulee(Championnat championnat) {
        return 0;
    }

    @Override
    public String toString() {
        return "ContrainteRencontres{" +
                "journees=" + journees +
                ", rencontres=" + rencontres +
                ", min=" + min +
                ", max=" + max +
                ", dure=" + dure +
                '}';
    }
}
