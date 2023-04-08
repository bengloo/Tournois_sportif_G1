package instance.modele.contrainte;

import instance.modele.Equipe;
import solution.Championnat;
import instance.modele.Journee;

import java.util.HashMap;
import java.util.Map;

public class ContraintePauseGlobale extends Contrainte{

    private Map<Integer, Journee> journees;
    private Map<Integer, Equipe> equipes;
    private Integer max;
    private Integer penalite;

    public ContraintePauseGlobale(Integer max) {
        super(false);
        this.journees = new HashMap<>();
        this.equipes = new HashMap<>();
        this.max = max;
        this.penalite = -1;
    }
    public ContraintePauseGlobale(Integer max, Integer penalite) {
        super(false);
        this.journees = new HashMap<>();
        this.equipes = new HashMap<>();
        this.max = max;
        this.penalite = penalite;
    }

    public boolean addEquipe(Equipe equipe){
        if(equipe==null) return false;
        this.equipes.put(equipe.getId(),equipe);
        return true;
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
        return "ContraintePauseGlobale{" +
                "journees=" + journees +
                ", equipes=" + equipes +
                ", max=" + max +
                ", penalite=" + penalite +
                ", dure=" + dure +
                '}';
    }
}
