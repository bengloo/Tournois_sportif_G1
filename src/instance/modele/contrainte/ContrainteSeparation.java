package instance.modele.contrainte;

import operateur.Operateur;
import operateur.OperateurInsertion;
import solution.Rencontre;
import solution.Solution;

import java.util.TreeSet;

/** classe définissant ContrainteSeparation (hérite de Contrainte)
 * @author Engloo Benjamin
 * @author Morcq Alexandre
 * @author Sueur Jeanne
 * @author Lux Hugo
 * @version 1.0
 */
public class ContrainteSeparation extends Contrainte{
    private TreeSet<Integer> equipes;
    private Integer min;

    public ContrainteSeparation(Integer min, Integer penalite) {

        super(penalite);
        this.equipes = new TreeSet<>();
        this.min = min;

    }

    /**
     * Ajoute une équipe à la liste des équipes de la contrainte
     * @param id l'ID de l'équipe à ajouter
     * @return true si l'ajout a réussi, false sinon
     */
    public boolean addEquipe(Integer id){
        return equipes.add(id);
    }

    @Override
    public TypeContrainte getTypeContrainte() {
        return TypeContrainte.SEPARATION;
    }

    @Override
    public int getCoutTotal(Solution championnat) {
        /*//TODO tchequer contrainte inerante si oui renvoyer max integer

        //le nombre de rencontres jouées par l’équipe de la contrainte selon un mode sur l’ensemble des journées
        int valc=0;

        //pour toutes les rencontres
        for(Rencontre r:championnat.getRencontres().values()){
            //pour toutes les équpes et journees concernées par la contrainte
           // valc += Math.max(0, parcoursJournees(championnat, r) - this.max);
        }

        return this.penalite * valc;*/
        return 0;
    }

    @Override
    public int evalDeltatCoef(Solution championnat, Operateur o) {
        /*
        int valcDelta=0;
        if(o instanceof OperateurInsertion) {
            Rencontre r = o.getRencontre();
            valcDelta = parcoursJournees(championnat, r);
        }
        return valcDelta;*/
        return 0;
    }

    /**
     * ??????????? TODO: compléter
     * @param championnat la solution
     * @param r la rencontre concernée
     * @return le nombre entier du compteur
     */
    private int parcoursJournees(Solution championnat, Rencontre r) { //Factorisation du code
        /*int valcDelta=0;
        // TODO: réunion de groupe pour discuter de la manière dont cela va être développé
        for (Integer eID : this.equipes) {
            for (Integer eIDAdverse : this.equipes) {
                //si notre équipe courante joue à domicile sur cette rencontre et que la journee courante contient la rencontre
                // if (r.getDomicile().equals(eID) && championnat.getJournees().get(jID).getRencontres().containsKey(r)) {
                if (championnat.getEquipes().equals(eID) && championnat.getEquipes().equals(eIDAdverse)) {
                    valcDelta++;
                }
            }
        }
        return valcDelta;*/
        return 0;
    }

    @Override
    public int evalDeltatCout(Solution championnat, Operateur o) {
        /*Integer valcDelta=evalDeltatCoef(championnat,o);
        return evalDeltatCout(championnat, o, valcDelta);*/
        return 0;
    }

    @Override
    public int evalDeltatCout(Solution championnat, Operateur o, Integer valcDelta) {
        /*//TODO tchequer contrainte inerante si oui renvoyer max integer
        if(o instanceof OperateurInsertion){

            if(championnat.getCoefContraintes().get(this)+valcDelta>max){
                if (estDure()) return Integer.MAX_VALUE;
                //au dela du max le cout suit une relation lineaire le deltat cout est donc proportionel
                return this.penalite *(valcDelta);
            }else return 0;
        }
        //TODO d'autre operation implique d'autre cout*/
        return 0;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ContrainteSeparation{");
        sb.append("equipes=").append(equipes).append(", ");
        sb.append("min=").append(min).append(", ");
        sb.append("penalite=").append(penalite).append(", ");
        sb.append("dure=").append(estDure()).append("}");
        return sb.toString();
    }
}
