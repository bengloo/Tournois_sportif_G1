package instance.modele.contrainte;

import instance.modele.Equipe;
import solution.Championnat;
import solution.Journee;

import java.util.HashMap;
import java.util.Map;

public class ContraintePauseGlobale extends Contrainte{

    private Map<Integer, Journee> journees;
    private Map<Integer, Equipe> equipes;
    private Integer max;
    private Integer penalite;

    public ContraintePauseGlobale(Integer max) {
        super(true);
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
        StringBuilder sb = new StringBuilder();
        sb.append("ContraintePauseGlobale{");
        sb.append("journees=").append(journees.values()).append(", ");
        sb.append("equipes=").append(equipes.values()).append(", ");
        sb.append("max=").append(max).append(", ");
        sb.append("penalite=").append(penalite).append(", ");
        sb.append("dure=").append(dure).append("}");
        return sb.toString();
    }
}
