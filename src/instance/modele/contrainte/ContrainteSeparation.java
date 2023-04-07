package instance.modele.contrainte;

import instance.modele.Equipe;
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

    public boolean addEquipe(Equipe equipeToAdd){
        if(equipeToAdd == null) return false;
        this.equipes.put(equipeToAdd.getId(), equipeToAdd);
        return true;
    }

}
