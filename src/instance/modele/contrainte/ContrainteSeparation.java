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

    public boolean addEquipe(Equipe equipe){
        if(equipe==null) return false;
        this.equipes.put(equipe.getId(),equipe);
        return true;
    }


    @Override
    public int getPenaliteCumulee(Championnat championnat) {
        return 0;
    }

    @Override
    public String toString() {
        return "ContrainteSeparation{" +
                "equipes=" + equipes +
                ", min=" + min +
                ", penalite=" + penalite +
                ", dure=" + dure +
                '}';
    }
}
