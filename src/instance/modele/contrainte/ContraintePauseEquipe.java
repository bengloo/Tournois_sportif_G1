package instance.modele.contrainte;

import instance.modele.Equipe;
import instance.modele.Journee;
import instance.modele.Rencontre;

import java.util.Map;

public class ContraintePauseEquipe {
    private Equipe equipe;
    private Map<Integer, Journee> journees;
    private TypeMode mode;
    private Integer max;
    private Integer penalite;
}
