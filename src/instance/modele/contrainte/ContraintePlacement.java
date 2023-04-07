package instance.modele.contrainte;

import instance.modele.Equipe;
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

    public boolean addJournee(Journee journeeToAdd){
        if(journeeToAdd == null) return false;
        this.journees.put(journeeToAdd.getId(), journeeToAdd);
        return true;
    }

}
