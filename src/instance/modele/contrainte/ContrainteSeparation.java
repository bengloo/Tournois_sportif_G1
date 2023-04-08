package instance.modele.contrainte;

import instance.modele.Equipe;
import solution.Championnat;

import java.util.HashMap;
import java.util.Map;

public class ContrainteSeparation extends Contrainte{

    private Map<Integer, Equipe> equipes;

    private Integer min;

    private Integer penalite;

    public ContrainteSeparation(Integer min, Integer penalite) {

        super(false);
        this.penalite = penalite;
        this.equipes = new HashMap<>();
        this.min = min;

    }

    public boolean addEquipe(int id){
        if(id>=0) return false;
        this.equipes.put(id, new Equipe(id));
        return true;
    }


    @Override
    public int getPenaliteCumulee(Championnat championnat) {
        return 0;
    }
}
