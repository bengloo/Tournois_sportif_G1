package instance.modele.contrainte;

import instance.modele.Equipe;
import instance.modele.Journee;
import instance.modele.Rencontre;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class ContrainteRencontres extends Contrainte{
    private Map<Integer, Journee> journees;
    private LinkedList<Rencontre> rencontres;
    private Integer min;
    private Integer max;

    public ContrainteRencontres(Integer min,Integer max) {
        super(false);
        this.journees = new HashMap<>();
        this.rencontres = new LinkedList<>();
        this.min = max;
        this.max = max;
    }

    public boolean addJournee(Journee journeeToAdd){
        if(journeeToAdd == null) return false;
        this.journees.put(journeeToAdd.getId(), journeeToAdd);
        return true;
    }

    public boolean addRencontre(Rencontre rencontreToAdd){
        if(rencontreToAdd == null) return false;
        this.rencontres.add(rencontreToAdd);
        return true;
    }
}
