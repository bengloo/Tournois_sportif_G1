package instance.modele.contrainte;

import instance.modele.Equipe;
import instance.modele.Journee;
import instance.modele.Rencontre;

import java.util.Map;

public class ContraintePauseGlobale {

    private Map<Integer, Journee> journees;
    private Map<Integer, Equipe> equipes;
    private Integer max;
    private Integer penalite;
}
