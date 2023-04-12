package instance;

import instance.modele.Equipe;
import solution.Journee;
import instance.modele.Rencontre;
import instance.modele.contrainte.*;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Instance {

    private String nom;
    private Map<Integer, Equipe> equipes;

    private Map<String, Rencontre> rencontres;

    private Map<Integer, Journee> journees;

    private LinkedList<ContraintePlacement> contraintesPlacement;
    private LinkedList<ContrainteEquite> contraintesEquite;
    private LinkedList<ContrainteRencontres> contraintesRencontre;
    private LinkedList<ContraintePauseEquipe> contraintesPauseEquipe;
    private LinkedList<ContraintePauseGlobale> contraintesPauseGlobale;
    private LinkedList<ContrainteHBClassement>contraintesHBClassement;
    private LinkedList<ContrainteSeparation>contraintesSeparation;

    public Instance(String nom,int nbEquipe,HashMap<Integer,Journee>journees) {
        this.nom = nom;
        equipes =new HashMap<>();
        for(int id=0;id<nbEquipe;id++){
            addEquipe(id);
        }
        rencontres =new HashMap<>();
        for(int id=0;id<nbEquipe;id++){
            for(int idAdverse=0;idAdverse<nbEquipe;idAdverse++){
                if(id!=idAdverse){
                    addRencontre(this.getEquipeById(id),this.getEquipeById(idAdverse));
                }
            }
        }
        journees=journees;
        contraintesEquite =new LinkedList<>();
        contraintesHBClassement =new LinkedList<>();
        contraintesRencontre =new LinkedList<>();
        contraintesPlacement =new LinkedList<>();
        contraintesPauseGlobale =new LinkedList<>();
        contraintesPauseEquipe =new LinkedList<>();
        contraintesSeparation =new LinkedList<>();
    }

    public boolean addEquipe(int id){
        if(id>=0 ||this.equipes.containsKey(id))return false;
        this.equipes.put(id, new Equipe(id));
        return true;
    }
    public boolean addRencontre(Equipe equipe,Equipe equipeAdverse){

        if(equipe==null||equipeAdverse==null)return false;
        String id= equipe.getId()+","+equipeAdverse.getId();
        if(this.rencontres.containsKey(id))return false;
        this.rencontres.put(id, new Rencontre(equipe,equipeAdverse));
        return true;
    }

    public boolean addJournee(int id){
        if(id>=0 ||this.journees.containsKey(id))return false;
        this.journees.put(id, new Journee(id));
        return true;
    }

    public boolean addContrainte(Contrainte contrainteToAdd){
        if(contrainteToAdd == null) return false;
        switch ( contrainteToAdd.getTypeContrainte()){
            case EQUITE:
                return this.contraintesEquite.add((ContrainteEquite)contrainteToAdd);
            case HBCLASSEMENT:
                return this.contraintesHBClassement.add((ContrainteHBClassement)contrainteToAdd);
            case PAUSEEQUIPE:
                return this.contraintesPauseEquipe.add((ContraintePauseEquipe) contrainteToAdd);
            case PAUSEGLOBALE:
                return this.contraintesPauseGlobale.add((ContraintePauseGlobale) contrainteToAdd);
            case PLACEMENT:
                return this.contraintesPlacement.add((ContraintePlacement) contrainteToAdd);
            case RENCONTRES:
                return this.contraintesRencontre.add((ContrainteRencontres) contrainteToAdd);
            case SEPARATION:
                return this.contraintesSeparation.add((ContrainteSeparation)contrainteToAdd);
            default: return false;
        }
    }


    public Map<Integer, Equipe> getEquipes() {
        return equipes;
    }

    public Map<Integer, Journee> getJournees() {
        return journees;
    }
    public Equipe getEquipeById(int id){
        return this.equipes.get(id);
    }
    public Rencontre getRencontreById(String id){
        return this.rencontres.get(id);
    }
    public Journee getJourneeById(int id){
        return this.journees.get(id);
    }

    public LinkedList<? extends Contrainte> getContraintes(TypeContrainte type){
        switch (type ){
            case EQUITE:
                return this.contraintesEquite;
            case HBCLASSEMENT:
                return this.contraintesHBClassement;
            case PAUSEEQUIPE:
                return this.contraintesPauseEquipe;
            case PAUSEGLOBALE:
                return this.contraintesPauseGlobale;
            case PLACEMENT:
                return this.contraintesPlacement;
            case RENCONTRES:
                return this.contraintesRencontre;
            case SEPARATION:
                return this.contraintesSeparation;
            default: return null;
        }
    }

    public Map<String, Rencontre> getRencontres() {
        return rencontres;
    }

    @Override
    public String toString() {
        return "Instance{" +
                "\nnom='" + nom + '\'' +
                "\n\tequipes=" + equipes +
                "\n\trencontres=" + rencontres +
                "\n\tjournees=" + journees +
                "\n\tcontraintesPlacement=" + contraintesPlacement +
                "\n\tcontraintesEquite=" + contraintesEquite +
                "\n\tcontraintesRencontre=" + contraintesRencontre +
                "\n\tcontraintesPauseEquipe=" + contraintesPauseEquipe +
                "\n\tcontraintesPauseGlobale=" + contraintesPauseGlobale +
                "\n\tcontraintesHBClassement=" + contraintesHBClassement +
                "\n\tcontraintesSeparation=" + contraintesSeparation +
                "\n}";
    }
}
