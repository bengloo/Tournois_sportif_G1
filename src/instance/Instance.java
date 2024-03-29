package instance;

import instance.modele.contrainte.*;

import java.util.LinkedList;

/** classe définissant Instance (complétée à la lecture du fichier)
 * @author Engloo Benjamin
 * @author Morcq Alexandre
 * @author Sueur Jeanne
 * @author Lux Hugo
 * @version 1.0
 */
public class Instance {
    private String nom;

    private String chemin;

    private int nbEquipes;

    private LinkedList<ContraintePlacement> contraintesPlacement;
    private LinkedList<ContrainteEquite> contraintesEquite;
    private LinkedList<ContrainteRencontres> contraintesRencontre;
    private LinkedList<ContraintePauseEquipe> contraintesPauseEquipe;
    private LinkedList<ContraintePauseGlobale> contraintesPauseGlobale;
    private LinkedList<ContrainteHBClassement>contraintesHBClassement;
    private LinkedList<ContrainteSeparation>contraintesSeparation;

    /**
     * constructeur par defaut de l'instance
     * @param nom nom de l'instance
     * @param nbEquipes nb Equipes de l'instance
     * @param chemin chemin absolut de l'instance
     */
    public Instance(String nom,int nbEquipes,String chemin) {
        this.nom = nom;
        this.nbEquipes=nbEquipes;
        contraintesEquite =new LinkedList<>();
        contraintesHBClassement =new LinkedList<>();
        contraintesRencontre =new LinkedList<>();
        contraintesPlacement =new LinkedList<>();
        contraintesPauseGlobale =new LinkedList<>();
        contraintesPauseEquipe =new LinkedList<>();
        contraintesSeparation =new LinkedList<>();
        this.chemin=chemin;
    }

    public String getChemin() {
        return chemin;
    }

    /**
     * Récupère le nombre d'équipes contenu dans l'instance
     * @return le nombre d'équipes
     */
    public int getNbEquipes() {
        return nbEquipes;
    }

    /**
     * get le nombre de contrainte impliquant des pause
     * @param withSouple si true inclue les contrainte souple
     * @return en entier
     */
    public int getNbContraintePause(boolean withSouple){
        int res=0;
        if(withSouple){
            for(Contrainte c:contraintesPauseEquipe){
                if(c.estDure())res+=1;
            }
            for(Contrainte c:contraintesPauseGlobale){
                if(c.estDure())res+=1;
            }
        }else {
            res += contraintesPauseEquipe.size();
            res += contraintesPauseGlobale.size();
        }
        return res;
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
     * Retourne la liste des contraintes de l'instance sans distinction de type
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
     * detecte si une pause est utilisée dans les contraintes de l'instance afin de savoir s'il faut définir ces
     * équations
     * @param e une equipe liée à la pause ciblée
     * @param j une journée lié à la pause ciblée
     * @param m un mode lié à la pause ciblée
     * @param avoidPauseGlobal inclus ou non les pause globale dans le test
     * @return  un boolean indicatif si la pause a été comptabilisé au moins une fois
     */
    public boolean isPauseConcerne(int e,int j,TypeMode m,boolean avoidPauseGlobal){
        for(ContraintePauseEquipe c:contraintesPauseEquipe){
            if(c.isConserne(e,j,m))return true;
        }
        for(ContraintePauseGlobale c:contraintesPauseGlobale){
            if((!avoidPauseGlobal)&&c.isConserne(e,j))return true;
        }
        return false;
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
