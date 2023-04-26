package solution;

import instance.Instance;
import instance.modele.contrainte.*;
import operateur.OperateurInsertion;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/** classe définissant Solution
 * @author Engloo Benjamin
 * @author Morcq Alexandre
 * @author Sueur Jeanne
 * @author Lux Hugo
 * @version 1.0
 */
public class Solution {
    private static Instance instance;
    private Map<Integer, Journee> journees;
    private Map<String, Rencontre> rencontres;
    private Map<Integer, Equipe> equipes;
    //update Journee.addRencontre
    private Integer coutTotal;
    private Map<Contrainte,Integer> coefContraintes;

    public Solution(Instance instance) {
        this.instance = instance;

        //on pouras faire des operation par equipes
        equipes =new HashMap<>();
        for(int id=0;id<getNBEquipe();id++){
            addEquipe(id);
        }

        //on pouras faire des operation par journee
        journees =new HashMap<>();
        for(int id=0;id<getNbJournee();id++){
            addJournee(id);
        }

        //on pouras faire des operation par rencontres
        rencontres =new HashMap<>();
        for(int id=0;id<getNBEquipe();id++){
            for(int idAdverse=0;idAdverse<getNBEquipe();idAdverse++){
                if(id!=idAdverse){
                    addRencontre(equipes.get(id),equipes.get(idAdverse));
                }
            }
        }

        coutTotal=0;

        //le calcule du cout total par contrainte n'est par continus vis à vias des coef de la contrainte comme valc
        this.coefContraintes = new HashMap<>();
        for(TypeContrainte type:TypeContrainte.values()) {
            for (Contrainte contrainte : instance.getContraintes(type)){
                coefContraintes.put(contrainte,0);
            }
        }
    }

    /**
     * Méthode permettant de récupérer l'instance de la solution
     * @return l'instance en question
     */
    public static Instance getInstance() {
        return instance;
    }

    /**
     * Méthode permettant de connaître la phase de la solution à partir d'une journée donnée
     * @param journee du championnat
     * @return 1 si on se situe dans la 1ère phase du championnat, 2 sinon
     */
    public Integer getPhase(Journee journee){
        if(journee==null)return null;
        return journee.getId()+1>getNbJournee()/2?2:1;
    }

    /**
     * Méthode permettant de récupérer le nombre de journées qui ont lieu lors du championnat
     * @return l'entier symbolisant le nombre de journées
     */
    public int getNbJournee(){
        return getNBEquipe()*2-2;
    }

    /**
     * Méthode permettant de récupérer le nombre de rencontres qui ont lieu lors du championnat
     * @return l'entier symbolisant le nombre de rencontres
     */
    public int getNBRencontreJournee(){
        return getNBEquipe()/2;
    }

    /**
     * Méthode permettant de récupérer le nombre d'équipes jouant lors du championnat
     * @return l'entier symbolisant le nombre d'équipes
     */
    public int getNBEquipe(){
        return getInstance().getNbEquipes();
    }

    /**
     * Méthode permettant de récupérer l'ensemble des journées de compétition
     * @return le tableau de l'ensemble des journées
     */
    public Map<Integer, Journee> getJournees() {
        return this.journees;
    }

    /**
     * Méthode permettant de récupérer l'ensemble des rencontres de la compétition
     * @return le tableau de l'ensemble des rencontres
     */
    public Map<String, Rencontre> getRencontres() {
        return this.rencontres;
    }

    /**
     * Méthode permettant de récupérer l'ensemble des équipes de la compétition
     * @return le tableau de l'ensemble des équipes
     */
    public Map<Integer, Equipe> getEquipes() {
        return this.equipes;
    }

    /**
     * Méthode permettant de récupérer le coût total de la solution
     * @return l'entier symbolisant le coût
     */
    public Integer getCoutTotal() {
        return this.coutTotal;
    }

    /**
     * Méthode permettant de récupérer le coeff des contraintes de la solution
     * @return le tableau de l'ensemble des coeffs de contraintes
     */
    public Map<Contrainte, Integer> getCoefContraintes() {
        return this.coefContraintes;
    }

    /**
     * Récupère la liste des contraintes d'un type donné de la solution
     * @param type des contraintes à récupérer
     * @return la liste chaînée des contraintes
     */
    public LinkedList<? extends Contrainte> getContraintes(TypeContrainte type){
        return instance.getContraintes(type);
    }

    /**
     * Récupère la liste de toutes les contraintes de la solution
     * @return la liste chaînée des contraintes
     */
    public LinkedList<Contrainte> getContraintes(){
        return instance.getContraintes();
    }

    /**
     * Indique à la fois l'ID de l'équipe jouant à domicile suivi de celui de l'équipe jouant en extérieur
     * @param IDequipeDomicile l'ID de l'équipe jouant à domicile
     * @param IDequipeExterne l'ID de l'équipe jouant en extérieur
     * @return la chaîne de caractères correspondante
     */
    public String getIDRencontre(Integer IDequipeDomicile,Integer IDequipeExterne){
        return IDequipeDomicile+"-"+IDequipeExterne;
    }

    /**
     * Ajoute une équipe à la solution finale (championnat)
     * @param id l'ID de l'équipe à ajouter
     * @return true si l'ajout a été effectué avec succès, false sinon
     */
    private boolean addEquipe(int id){
        if(id < 0 ||this.equipes.containsKey(id))return false;
        this.equipes.put(id, new Equipe(id));
        return true;
    }

    /**
     * Ajoute une rencontre entre deux équipes à la solution finale (championnat)
     * @param equipe l'ID de l'équipe à ajouter jouant à domicile lors de la rencontre
     * @param equipeAdverse l'ID de l'équipe adverse à ajouter jouant en extérieur lors de la rencontre
     * @return true si l'ajout a été effectué avec succès, false sinon
     */
    private boolean addRencontre(Equipe equipe,Equipe equipeAdverse){
        if(equipe==null||equipeAdverse==null)return false;
        String id= getIDRencontre(equipe.getId(),equipeAdverse.getId());
        if(this.rencontres.containsKey(id))return false;
        this.rencontres.put(id, new Rencontre(equipeAdverse,equipe));
        return true;
    }

    /**
     * Ajoute une journée de compétition à la solution finale (championnat)
     * @param id l'ID de la journée à ajouter
     * @return true si l'ajout a été effectué avec succès, false sinon
     */
    private boolean addJournee(int id){
        if(id < 0 ||this.journees.containsKey(id))return false;
        this.journees.put(id, new Journee(id));
        return true;
    }

    /**
     * Met à jour le coût total de la solution
     * @param cout à ajouter à la solution
     */
    public void addCoutTotal(Integer cout) {
        this.coutTotal += cout;
    }

    /**
     * Méthode permettant de récupérer la rencontre retour en connaissance de la rencontre du match aller
     * @param r la rencontre ciblée
     * @return la rencontre du match retour
     */
    public Rencontre getMatchRencontre(Rencontre r){
        return this.rencontres.get(r.getLabelRetour());
    }

    /*
    * check quantiativement les journee et rencontre creer
    * */

    /**
     * Méthode permettant d'ajouter le coeff du coût de la contrainte au coût total de la solution
     * @param c la contrainte en question
     * @param deltaCoef le delta du coeff
     * @param deltaCout le coût associé
     */
    public void addCoefCoutContrainte(Contrainte c,Integer deltaCoef,Integer deltaCout){
        //update du coef contrainte
        coefContraintes.put(c,coefContraintes.get(c)+deltaCoef);
        //update du cout contrainte
        coutTotal+=deltaCout;
    }

    /**
     * Méthode permettant de vérifier la faisabilité de l'entièreté de la solution (avec les contraintes + planning)
     * @return true si la solution est réalisable, false sinon
     */
    public boolean check(){
        if(!checkIntegriteeChampionat())return false;
        return checkAllContrainte();
    }

    /**
     * Méthode permettant de vérifier la faisabilité de la solution vis-à-vis du planning du nombre de rencontres sur chaque journée et des phases
     * @return true si ces paramètres sont réalisables, false sinon
     */
    public boolean checkIntegriteeChampionat(){
        for(Journee j :journees.values()){
            if(j.getRencontres().size()>getNBRencontreJournee()){
                System.err.println("La journée "+j.getId()+" a un nombre de rencontres égal à: "+j.getRencontres().toString());
                return false;
            }
        }
        for(Rencontre r:rencontres.values()){
            if(getPhase(r.getJournee())==getPhase(rencontres.get(r.getLabelRetour()).getJournee())){
                int a=getPhase(r.getJournee());
                int b= getPhase(rencontres.get(r.getLabelRetour()).getJournee());
                Rencontre ra=rencontres.get(r.getLabelRetour());
                System.err.println("La rencontre "+r.toString()+"a son match retour dans la même phase");
                return false;
            }
        }
        return true;
    }

    /**
     * Méthode permettant de vérifier la faisabilité de la solution vis-à-vis des contraintes
     * @return true si les contraintes sont réalisables, false sinon
     */
    public boolean checkAllContrainte(){
        for(Contrainte c:getContraintes()){
            if(!c.checkContrainte(this)){
                System.err.println("Contrainte non respectée: "+c.toString());
                return false;
            }
        }

        return true;
    }

    /**
     * Méthode permettant de récupérer la meilleure insertion d'une rencontre dans le championnat (coût le plus faible)
     * @param r la rencontre à insérer
     * @return le meilleur opérateur d'insertion
     */
    public OperateurInsertion getMeilleureInsertion(Rencontre r) {
        OperateurInsertion bestInsertion = new OperateurInsertion();
        for(Journee j: this.journees.values()) {
            OperateurInsertion o = new OperateurInsertion(this,j,r);
            if(o != null && o.isMouvementRealisable() && o.isMeilleur(bestInsertion)) {
                bestInsertion = o;
            }
        }
        return bestInsertion;
    }
    /**
     * Méthode permettant de récupérer la première tentative d'insertion d'une rencontre dans le championnat
     * @param r la rencontre ciblée
     * @return le premier opérateur d'insertion s'il est valide, null sinon
     */
    public OperateurInsertion getPremiereInsertion(Rencontre r) {
        for(Journee j: this.journees.values()) {
            OperateurInsertion o = new OperateurInsertion(this,j,r);
            if(o != null && o.isMouvementRealisable()) {
                return o;
            }
        }
        System.err.println("Il n'y a plus d'insertion valide");
        return null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Championat{\n");
        sb.append("\tInstance=").append(getInstance().getNom()).append("\n");
        sb.append("\tequipes=").append(equipes.values()).append("\n");
        sb.append("\trencontres=").append(rencontres.values()).append("\n");
        sb.append("\tjournees=[").append("\n");
        for(Journee j:journees.values()){
            sb.append("\t\t").append(j.toString()).append("\n");
        }
        sb.append("\t]").append("\n");
        sb.append("\tcoutTotal=").append(coutTotal).append("\n");
        sb.append("\tcoefContraintes=[").append("\n");
        for(TypeContrainte type:TypeContrainte.values()){
            LinkedList<? extends Contrainte> contraintes= getInstance().getContraintes(type);
            sb.append("\t\t"+type+"\n");
            for(Contrainte c:contraintes){
                sb.append("\t\t\tcoef="+this.coefContraintes.get(c)+" <= ").append(c.toString()).append("\n");
            }
        }
        sb.append("\t]").append("\n");
        sb.append("}");
        return sb.toString();
    }
}

