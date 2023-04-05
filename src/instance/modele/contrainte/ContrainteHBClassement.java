package instance.modele.contrainte;

import instance.modele.Equipe;
import instance.modele.Journee;

import java.util.Map;

public class ContrainteHBClassement {
    private Equipe equipe;
    private Map<Integer, Journee> journees;
    private Map<Integer, Equipe> equipesAdverses;
    private TypeMode mode;
    private Integer max;

    private Integer penalite;

    public ContrainteHBClassement(Equipe equipe, Map<Integer, Journee> journees, Map<Integer, Equipe> equipesAdverses, TypeMode mode, Integer max, Integer penalite) {
        this.equipe = equipe;
        this.journees = journees;
        this.equipesAdverses = equipesAdverses;
        this.mode = mode;
        this.max = max;
        this.penalite = penalite;
    }
}
