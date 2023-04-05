package instance;

import instance.modele.Equipe;
import instance.modele.Journee;
import instance.modele.contrainte.*;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class Instance {

    private String nom;
    private Map<Integer, Equipe> equipes;

    private Map<Integer, Journee> journees;
    private LinkedList<ContraintePlacement> contraintesPlacement;
    private LinkedList<ContrainteEquite> contraintesEquite;
    private LinkedList<ContrainteRencontres> contraintesRencontre;
    private LinkedList<ContraintePauseEquipe> contraintesPauseEquipe;
    private LinkedList<ContraintePauseGlobale> contraintesPauseGlobale;
    private LinkedList<ContrainteHBClassement>contraintesHBClassement;
    private LinkedList<ContrainteHBClassement>contraintesSeparation;

    public Instance(String nom) {
        this.nom = nom;
        equipes =new HashMap<>();
        journees =new HashMap<>();
        contraintesEquite =new LinkedList<>();
        contraintesHBClassement =new LinkedList<>();
        contraintesRencontre =new LinkedList<>();
        contraintesPlacement =new LinkedList<>();
        contraintesPauseGlobale =new LinkedList<>();
        contraintesPauseEquipe =new LinkedList<>();
        contraintesSeparation =new LinkedList<>();
    }

    public boolean addEquipe(Equipe equipeToAdd){
        if(equipeToAdd == null) return false;
        this.equipes.put(equipeToAdd.getId(), equipeToAdd);
        return true;
    }
    public boolean addJournee(Journee journeeToAdd){
        if(journeeToAdd == null) return false;
        this.journees.put(journeeToAdd.getId(), journeeToAdd);
        return true;
    }

    public boolean addContrainte(Contrainte contrainteToAdd){
        if(contrainteToAdd == null) return false;
        switch ( contrainteToAdd.getTypeContrainte()){
            case EQUITE: this.contraintesEquite.add((ContrainteEquite)contrainteToAdd);
            case HBCLASSEMENT: this.contraintesHBClassement.add((ContrainteHBClassement)contrainteToAdd);
            case PAUSEEQUIPE: this.contraintesPauseEquipe.add((ContraintePauseEquipe) contrainteToAdd);
            case PAUSEGLOBALE: this.contraintesPauseGlobale.add((ContraintePauseGlobale) contrainteToAdd);
            case PLACEMENT: this.contraintesPlacement.add((ContraintePlacement) contrainteToAdd);
            case RENCONTRES: this.contraintesRencontre.add((ContrainteRencontres) contrainteToAdd);
            case SEPARATION: this.contraintesSeparation.add((ContrainteSeparation)contrainteToAdd);
        }


        return true;
    }
}
