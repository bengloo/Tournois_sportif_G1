package instance.modele.contrainte;

import instance.modele.Equipe;

import java.util.Map;

public class ContrainteSeparation {

    private Map<Integer, Equipe> equipes;

    private Integer min;

    private Integer penalite;

    public ContrainteSeparation(Map<Integer, Equipe> equipes, Integer min, Integer penalite) {
        this.equipes = equipes;
        this.min = min;
        this.penalite = penalite;
    }
}
