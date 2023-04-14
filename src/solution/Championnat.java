package solution;

import instance.Instance;
import instance.modele.contrainte.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class Championnat {
    private static Instance instance;
    private Map<Integer, Journee> journees;
    private Map<String, Rencontre> rencontres;
    private Map<Integer, Equipe> equipes;

    private Integer coutTotal;
    private Map<Contrainte,Integer> coutContraintes;

    public Championnat(Instance instance) {
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
        //Les opeartion impacterons la penalitee total de chaque contrainte.
        this.coutContraintes = new HashMap<>();
        for(TypeContrainte type:TypeContrainte.values()) {
            for (Contrainte contrainte : instance.getContraintes(type)){
                coutContraintes.put(contrainte,0);
            };
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
        this.rencontres.put(id, new Rencontre(equipe,equipeAdverse));
        return true;
    }

    private boolean addJournee(int id){
        if(id < 0 ||this.journees.containsKey(id))return false;
        this.journees.put(id, new Journee(id));
        return true;
    }

    public static Instance getInstance() {
        return instance;
    }

    public int getPhase(Journee journee){
        return journee.getId()>getNbJournee()/2?2:1;
    }
    public int getNbJournee(){
        return getNBEquipe()*2-2;
    }

    public int getNBRencontre(){
        return getNBEquipe()*6-6;
    }

    public int getNBEquipe(){
        return getInstance().getNbEquipes();
    }

    public Map<Integer, Journee> getJournees() {
        return journees;
    }

    public Map<String, Rencontre> getRencontres() {
        return rencontres;
    }

    public Map<Integer, Equipe> getEquipes() {
        return equipes;
    }

    public Integer getCoutTotal() {
        return coutTotal;
    }

    public Map<Contrainte, Integer> getCoutContraintes() {
        return coutContraintes;
    }

    public LinkedList<? extends Contrainte>  getContraintes(TypeContrainte type){
        return instance.getContraintes(type);
    }

    public LinkedList<? extends Contrainte>  getContraintes(){
        LinkedList<? extends Contrainte> contraintesAll= new LinkedList<>();
        for(TypeContrainte type:TypeContrainte.values()){
            LinkedList<? extends Contrainte> contraintes = instance.getContraintes(type);
            //contraintesAll.addAll(contraintes);
        }
        return contraintesAll;
    }

    /*
    * check quantiativement les journee et rencontre creer
    * */
    public boolean checkIntegriteeChampiona(){
        return false;
    }

    public boolean checkAllContrainte(){
        return false;
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
        sb.append("\tcoutContraintes=[").append("\n");
        for(TypeContrainte type:TypeContrainte.values()){
            LinkedList<? extends Contrainte> contraintes= getInstance().getContraintes(type);
            sb.append("\t\t"+type+"\n");
            for(Contrainte c:contraintes){
                sb.append("\t\t\tcout="+this.coutContraintes.get(c)+" <= ").append(c.toString()).append("\n");
            }
        }
        sb.append("\t]").append("\n");
        sb.append("}");
        return sb.toString();
    }

}

