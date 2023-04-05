package instance.modele.contrainte;

import instance.modele.Equipe;
import instance.modele.Journee;

import java.util.Map;

public class ContrainteEquite {

    private Map<Integer, Equipe> equipes;
    private Map<Integer, Journee> journees;
    private Map<Integer, Equipe> equipesAdverses;
    private Integer max;
    private Integer penalite;
}
