package solution;

import instance.Instance;
import instance.modele.contrainte.*;
import operateur.OperateurInsertion;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
/** Class definissant Solution(Championnat).
 * @author Engloo Benjamin
 * @author Morcq Alexandre
 * @author Sueur Jeanne
 * @author Lux Hugo
 * @version 0.5
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
    public String getIDRencontre(Integer IDequipeDomicile,Integer IDequipeExterne){
        return IDequipeDomicile+"-"+IDequipeExterne;
    }

    private boolean addEquipe(int id){
        if(id < 0 ||this.equipes.containsKey(id))return false;
        this.equipes.put(id, new Equipe(id));
        return true;
    }
    private boolean addRencontre(Equipe equipe,Equipe equipeAdverse){

        if(equipe==null||equipeAdverse==null)return false;
        String id= getIDRencontre(equipe.getId(),equipeAdverse.getId());
        if(this.rencontres.containsKey(id))return false;
        this.rencontres.put(id, new Rencontre(equipeAdverse,equipe));
        return true;
    }

    private boolean addJournee(int id){
        if(id < 0 ||this.journees.containsKey(id))return false;
        this.journees.put(id, new Journee(id));
        return true;
    }

    /**
     * Fonction permettant de recuperer la rencontre retour en passant
     * en paramètre la rencontre allée
     * @Param r la rencontre ciblée
     * @return  la rencontre du match retour.
     */
    public Rencontre getMatchRencontre(Rencontre r){
        return this.rencontres.get(r.getLabelRetour());
    }
    /**
     * Fonction permettant de recuperer l'instance
     * @return  une instance.
     */
    public static Instance getInstance() {
        return instance;
    }

    public Integer getPhase(Journee journee){
        if(journee==null)return null;
        return journee.getId()+1>getNbJournee()/2?2:1;
    }
    /**
     * Fonction permettant de recuperer le nombre de journées
     * @return  le nombre de journées.
     */
    public int getNbJournee(){
        return getNBEquipe()*2-2;
    }
    /**
     * Fonction permettant de recuperer le nombre de rencontres
     * @return  le nombre de rencontres.
     */
    public int getNBRencontreJournee(){
        return getNBEquipe()/2;
    }
    /**
     * Fonction permettant de recuperer le nombre d'équipes
     * @return  le nombre d'équipes.
     */
    public int getNBEquipe(){
        return getInstance().getNbEquipes();
    }
    /**
     * Fonction permettant de recuperer les journées
     * @return  l'ensemble des journée sous un Map.
     */
    public Map<Integer, Journee> getJournees() {
        return journees;
    }
    /**
     * Fonction permettant de recuperer les rencontres
     * @return  l'ensemble des rencontres sous un Map.
     */
    public Map<String, Rencontre> getRencontres() {
        return rencontres;
    }
    /**
     * Fonction permettant de recuperer les équipes
     * @return  l'ensemble des équipes sous un Map.
     */
    public Map<Integer, Equipe> getEquipes() {
        return equipes;
    }
    /**
     * Fonction permettant de recuperer le cout total
     * @return  le cout .
     */
    public Integer getCoutTotal() {
        return coutTotal;
    }


    public Map<Contrainte, Integer> getCoefContraintes() {
        return coefContraintes;
    }

    public LinkedList<? extends Contrainte>  getContraintes(TypeContrainte type){
        return instance.getContraintes(type);
    }

    public LinkedList<Contrainte>  getContraintes(){
        return instance.getContraintes();
    }

    /*
    * check quantiativement les journee et rencontre creer
    * */

    public void addCoefCoutContrainte(Contrainte c,Integer DeltaCoef,Integer delatCout){
        //update du coef contrainte
        coefContraintes.put(c,coefContraintes.get(c)+DeltaCoef);
        //update du cout contrainte
        coutTotal+=delatCout;

    }

    public boolean check(){
        if(!checkIntegriteeChampionat())return false;
        return checkAllContrainte();
    }

    public boolean checkIntegriteeChampionat(){
        for(Journee j :journees.values()){
            if(j.getRencontres().size()>getNBRencontreJournee()){
                System.err.println("La journee "+j.getId()+" à un nombre de rencontre execif: "+j.getRencontres().toString());
                return false;
            }
        }
        for(Rencontre r:rencontres.values()){
            if(getPhase(r.getJournee())==getPhase(rencontres.get(r.getLabelRetour()).getJournee())){
                int a=getPhase(r.getJournee());
                int b= getPhase(rencontres.get(r.getLabelRetour()).getJournee());
                Rencontre ra=rencontres.get(r.getLabelRetour());
                System.err.println("la rencontre :"+r.toString()+"à son matche retour dans la même phase");
                return false;
            }
        }
        return true;
    }

    public boolean checkAllContrainte(){
        for(Contrainte c:getContraintes()){
            if(!c.checkContrainte(this)){
                System.err.println("contrainte non respecté: "+c.toString());
                return false;
            }
        }

        return true;
    }

    public void addCoutTotal(Integer cout) {
        this.coutTotal += cout;
    }
            /**
             * Fonction permettant de recuperer la meilleure insertion
             * @Param r la rencontre ciblée
             * @return  un Operateur Insertion.
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
     * Fonction permettant de recuperer la première insertion
     * @Param r la rencontre ciblée
     * @return  un Operateur Insertion.
     */
    public OperateurInsertion getPremiereInsertion(Rencontre r) {
        for(Journee j: this.journees.values()) {
            OperateurInsertion o = new OperateurInsertion(this,j,r);
            if(o != null && o.isMouvementRealisable()) {
                return o;
            }
        }
        System.err.println("il n'y a plus d'insertion valide");
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

