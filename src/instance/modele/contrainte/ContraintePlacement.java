package instance.modele.contrainte;

import instance.modele.Equipe;
import solution.Championnat;
import instance.modele.Journee;

import java.util.HashMap;
import java.util.Map;

public class ContraintePlacement extends Contrainte{
    private Equipe equipe;
    private Map<Integer, Journee> journees;
    private TypeMode mode;
    private Integer max;

    public ContraintePlacement(Equipe equipe,TypeMode mode,Integer max) {
        super(false);
        this.equipe=equipe;
        this.journees = new HashMap<>();
        this.mode = mode;
        this.max = max;
    }

    public boolean addJournee(Journee journee){
        if(journee==null) return false;
        this.journees.put(journee.getId(),journee);
        return true;
    }


    @Override
    public int getPenaliteCumulee(Championnat championnat) {
        return 0;
    }

    @Override
    public String toString() {
        return "ContraintePlacement{" +
                "equipe=" + equipe +
                ", journees=" + journees +
                ", mode=" + mode +
                ", max=" + max +
                ", dure=" + dure +
                '}';
    }
}
