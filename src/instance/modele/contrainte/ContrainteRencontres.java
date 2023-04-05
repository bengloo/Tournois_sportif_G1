package instance.modele.contrainte;

import instance.modele.Equipe;
import instance.modele.Journee;
import instance.modele.Rencontre;

import java.util.Map;

public class ContrainteRencontres {
    private Map<Integer, Journee> journees;
    private Map<Integer, Rencontre> rencontres;
    private Integer min;
    private Integer max;
}
