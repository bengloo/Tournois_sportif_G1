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

    public boolean addEquipe(int id){
        if(id>=0) return false;
        this.equipes.put(id, new Equipe(id));
        return true;
    }
    public boolean addJournee(int id){
        if(id>=0) return false;
        this.journees.put(id, new Journee(id));
        return true;
    }

    public boolean addEquipeAdverse(int id){
        if(id>=0) return false;
        this.equipesAdverses.put(id, new Equipe(id));
        return true;
    }

    @Override
    public int getPenaliteCumulee(Championnat championnat) {
        return 0;
    }
}
