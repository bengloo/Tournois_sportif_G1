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


    @Override
    public int getPenaliteCumulee(Championnat championnat) {
        return 0;
    }
}
