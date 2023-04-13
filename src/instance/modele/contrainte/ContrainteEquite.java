package instance.modele.contrainte;

import instance.modele.Equipe;
import solution.Championnat;
import solution.Journee;

import java.util.HashMap;
import java.util.Map;

public class ContrainteEquite extends Contrainte {

    private Map<Integer, Equipe> equipes;
    private Map<Integer, Journee> journees;
    private Map<Integer, Equipe> equipesAdverses;
    private Integer max;
    private Integer penalite;

    public ContrainteEquite(Integer max, Integer penalite) {

        super(false);
        this.penalite = penalite;
        this.equipes = new HashMap<>();
        this.journees = new HashMap<>();
        this.equipesAdverses = new HashMap<>();
        this.max = max;

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

    public boolean addEquipeAdverse(Equipe equipe){
        if(equipe==null) return false;
        this.equipesAdverses.put(equipe.getId(),equipe);
        return true;
    }

    @Override
    public int getPenaliteCumulee(Championnat championnat) {
        return 0;
    }

    @Override
    public String toString() {
        return "ContrainteEquite{" +
                "equipes=" + equipes +
                ", journees=" + journees +
                ", equipesAdverses=" + equipesAdverses +
                ", max=" + max +
                ", penalite=" + penalite +
                ", dure=" + dure +
                '}';
    }
}
