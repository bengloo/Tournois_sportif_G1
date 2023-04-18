package instance;

import solution.Equipe;
import solution.Rencontre;
import instance.modele.contrainte.*;
import solution.Journee;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class Instance {

    private String nom;
    private int nbEquipes;
    /*private Map<Integer, Equipe> equipes;

    private Map<String, Rencontre> rencontres;

    private Map<Integer, Journee> journees;*/

    private LinkedList<ContraintePlacement> contraintesPlacement;
    private LinkedList<ContrainteEquite> contraintesEquite;
    private LinkedList<ContrainteRencontres> contraintesRencontre;
    private LinkedList<ContraintePauseEquipe> contraintesPauseEquipe;
    private LinkedList<ContraintePauseGlobale> contraintesPauseGlobale;
    private LinkedList<ContrainteHBClassement>contraintesHBClassement;
    private LinkedList<ContrainteSeparation>contraintesSeparation;

    public Instance(String nom,int nbEquipes) {
        this.nom = nom;
        this.nbEquipes=nbEquipes;
        contraintesEquite =new LinkedList<>();
        contraintesHBClassement =new LinkedList<>();
        contraintesRencontre =new LinkedList<>();
        contraintesPlacement =new LinkedList<>();
        contraintesPauseGlobale =new LinkedList<>();
        contraintesPauseEquipe =new LinkedList<>();
        contraintesSeparation =new LinkedList<>();
    }



    public int getNbEquipes() {
        return nbEquipes;
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
                return this.contraintesSeparation.add((ContrainteSeparation) contrainteToAdd);
            default: return false;
        }
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

    public LinkedList<Contrainte>  getContraintes(){
        LinkedList<Contrainte> contraintesAll= new LinkedList<>();
        for(TypeContrainte type:TypeContrainte.values()){
            LinkedList<? extends Contrainte> contraintes = getContraintes(type);
            contraintesAll.addAll(contraintes);
        }
        return contraintesAll;
    }


    public String getNom() {
        return nom;
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Instance{\n");
        sb.append("\tnom=").append(nom).append("\n");
        sb.append("\tnbEquipes=").append(nbEquipes).append("\n");

        for(TypeContrainte type:TypeContrainte.values()){
            LinkedList<? extends Contrainte> contraintes=getContraintes(type);
            sb.append("\t\t"+type+"\n");
            for(Contrainte c:contraintes){
                sb.append("\t\t\t").append(c.toString()).append("\n");
            }
        }
        /*
        sb.append("\n\tcontraintesPlacement=").append(contraintesPlacement.size()).append("[\n");
        for (ContraintePlacement contrainte : contraintesPlacement) {
            sb.append("\t\t").append(contrainte.toString()).append("\n");
        }

        sb.append("\n\tcontraintesHBClassement=").append(contraintesHBClassement.size()).append("[\n");
        for (ContrainteHBClassement contrainte : contraintesHBClassement) {
            sb.append("\t\t").append(contrainte.toString()).append("\n");
        }

        sb.append("\n\tcontraintesRencontre=").append(contraintesRencontre.size()).append("[\n");
        for (ContrainteRencontres contrainte : contraintesRencontre) {
            sb.append("\t\t").append(contrainte.toString()).append("\n");
        }

        sb.append("\n\tcontraintesPauseEquipe=").append(contraintesPauseEquipe.size()).append("[\n");
        for (ContraintePauseEquipe contrainte : contraintesPauseEquipe) {
            sb.append("\t\t").append(contrainte.toString()).append("\n");
        }

        sb.append("\n\tcontraintesPauseGlobale=").append(contraintesPauseGlobale.size()).append("[\n");
        for (ContraintePauseGlobale contrainte : contraintesPauseGlobale) {
            sb.append("\t\t").append(contrainte.toString()).append("\n");
        }

        sb.append("\n\tcontraintesEquite=").append(contraintesEquite.size()).append("[\n");
        for (ContrainteEquite contrainte : contraintesEquite) {
            sb.append("\t\t").append(contrainte.toString()).append("\n");
        }

        sb.append("\n\tcontraintesSeparation=").append(contraintesSeparation.size()).append("[\n");
        for (ContrainteSeparation contrainte : contraintesSeparation) {
            sb.append("\t\t").append(contrainte.toString()).append("\n");
        }*/

        sb.append("\t]").append("\n");
        sb.append("}");
        return sb.toString();
    }
}
