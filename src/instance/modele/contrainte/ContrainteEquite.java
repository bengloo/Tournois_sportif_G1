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

    public boolean addEquipe(Equipe equipeToAdd){
        if(equipeToAdd == null) return false;
        this.equipes.put(equipeToAdd.getId(), equipeToAdd);
        return true;
    }
    public boolean addJournee(Journee journeeToAdd){
        if(journeeToAdd == null) return false;
        this.journees.put(journeeToAdd.getId(), journeeToAdd);
        return true;
    }

    public boolean addEquipeAdverse(Equipe equipeToAdd){
        if(equipeToAdd == null) return false;
        this.equipesAdverses.put(equipeToAdd.getId(), equipeToAdd);
        return true;
    }

    @Override
    public int TestContrainte(Championnat championnat) {
        return 0;
    }
}
