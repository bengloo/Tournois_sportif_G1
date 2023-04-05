package instance;

import instance.modele.Equipe;
import instance.modele.Journee;
import instance.modele.contrainte.*;

import java.util.Map;

public class Instance {

    private String nom;
    private Map<Integer, Equipe> equipes;

    private Map<Integer, Journee> journees;
    private Map<Integer, ContraintePlacement> contraintesPlacements;
    private Map<Integer, ContrainteEquite> contraintesEquite;
    private Map<Integer, ContrainteRencontres> contraintesRencontre;
    private Map<Integer, ContraintePauseEquipe> contraintesPauseEquipe;
    private Map<Integer, ContraintePauseGlobale> contraintesPauseGlobale;
    private Map<Integer, ContrainteHBClassement> contraintesHBClassement;


}
