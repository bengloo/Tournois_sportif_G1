package instance;

import solution.Equipe;
import solution.Rencontre;
import instance.modele.contrainte.*;
import solution.Journee;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/** classe définissant Instance (complétée à la lecture du fichier)
 * @author Engloo Benjamin
 * @author Morcq Alexandre
 * @author Sueur Jeanne
 * @author Lux Hugo
 * @version 1.0
 */
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


    /**
     * Récupère le nombre d'équipes contenues dans l'instance
     * @return le nombre d'équipes
     */
    public int getNbEquipes() {
        return nbEquipes;
    }

    public int getNbContraintePause(){
        return contraintesPauseEquipe.size()+contraintesPauseGlobale.size();
    }

    public int getNbJournees(){
        return getNbEquipes()*2-2;
    }

    /**
     * Ajoute une contrainte à l'instance courante
     * @param contrainteToAdd la contrainte à ajouter
     * @return true si l'ajout a été fait, false sinon (type de contrainte non reconnu ou ajout échoué)
     */
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

    /**
     * Retourne la liste des contraintes d'un certain type de l'instance
     * @param type le type de contrainte à récupérer
     * @return la liste chaînée de contraintes du type demandé
     */
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

    /**
     * Retourne la liste des contraintes de l'instance sans disctinction de type
     * @return la liste chaînée de contraintes
     */
    public LinkedList<Contrainte> getContraintes(){
        LinkedList<Contrainte> contraintesAll= new LinkedList<>();
        for(TypeContrainte type:TypeContrainte.values()){
            LinkedList<? extends Contrainte> contraintes = getContraintes(type);
            contraintesAll.addAll(contraintes);
        }
        return contraintesAll;
    }

    /**
     * Retourne le nom de l'instance
     * @return la chaîne de caractères du nom
     */
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
        sb.append("\t]").append("\n");
        sb.append("}");
        return sb.toString();
    }
}
