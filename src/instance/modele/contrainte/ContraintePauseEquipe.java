package instance.modele.contrainte;

import instance.modele.Equipe;
import instance.modele.Journee;

import java.util.Map;

public class ContraintePauseEquipe {
    private Equipe equipe;
    private Map<Integer, Journee> journees;
    private TypeMode mode;
    private Integer max;
    private Integer penalite;

    public ContraintePauseEquipe(Equipe equipe, Map<Integer, Journee> journees, TypeMode mode, Integer max, Integer penalite) {
        this.equipe = equipe;
        this.journees = journees;
        this.mode = mode;
        this.max = max;
        this.penalite = penalite;
    }
}
