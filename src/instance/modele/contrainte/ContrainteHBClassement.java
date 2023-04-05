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
}
