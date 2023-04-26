package instance.modele.contrainte;

import operateur.Operateur;
import operateur.OperateurInsertion;
import solution.Rencontre;
import solution.Solution;

import java.util.TreeSet;

/** classe définissant ContrainteRencontres (hérite de Contrainte)
 * @author Engloo Benjamin
 * @author Morcq Alexandre
 * @author Sueur Jeanne
 * @author Lux Hugo
 * @version 1.0
 */
public class ContrainteRencontres extends Contrainte{
    private TreeSet<Integer> journees;
    private TreeSet<String>  rencontres;
    private Integer min;
    private Integer max;

    public ContrainteRencontres(Integer min,Integer max) {
        super();
        this.journees = new TreeSet<>();
        this.rencontres = new TreeSet<>();
        this.min = max;
        this.max = max;
        this.penalite = Integer.MAX_VALUE;
    }

    public ContrainteRencontres(Integer min,Integer max, Integer penalite) {
        super(penalite);
        this.journees = new TreeSet<>();
        this.rencontres = new TreeSet<>();
        this.min = max;
        this.max = max;
    }

    /**
     * Ajoute une journée à la liste des journées de la contrainte
     * @param id l'ID de la journée à ajouter
     * @return true si l'ajout est réussi, false sinon
     */
    public boolean addJournee(Integer id){
        return journees.add(id);
    }

    /**
     * Ajoute une rencontre à la liste des rencontres de la contrainte
     * @param id l'ID de la rencontre à ajouter
     * @return true si l'ajout est réussi, false sinon
     */
    public boolean addRencontre(String id){
        return rencontres.add(id);
    }

    @Override
    public TypeContrainte getTypeContrainte() {
        return TypeContrainte.RENCONTRES;
    }

    //TODO implementer les fonction de calcule de cout en sinspirent de la contrainte de placement, réflechire si on ne peux pas factoriser du code sout des fonction comune aux contraintes
    @Override
    public int getCoutTotal(Solution championnat) {
        //le nombre de rencontres jouées par l’équipe de la contrainte selon un mode sur l’ensemble des journées
        int valc=0;

        //pour toutes les journees concernées par la contrainte
        valc = parcoursJournees(championnat, null);

        if(valc>this.max || valc < this.min) {
            if (estDure()) return Integer.MAX_VALUE;
            return this.penalite *(Math.max(0, valc-this.max) + Math.max(0, this.min - valc)); //source : https://docs.oracle.com/javase/8/docs/api/java/lang/Math.html
        }
        return 0;
    }

    @Override
    public int evalDeltaCoef(Solution championnat, Operateur o) {
        int valcDelta=0;
        if(o instanceof OperateurInsertion) {
            Rencontre r = o.getRencontre();
            valcDelta = parcoursJournees(championnat, r);
        }
        return valcDelta;
    }

    /**
     * Parcourt les journées de la contrainte pour incrémenter un compteur à chaque fois qu'une certaine rencontre a lieu sur la journée concernée
     * @param championnat la solution
     * @param r la rencontre concernée
     * @return le nombre entier du compteur
     */
    private int parcoursJournees(Solution championnat, Rencontre r) { //Factorisation du code
        int valcDelta=0;

        if (r == null) {
            for (Integer jID : this.journees) {
                for (String rencontre : this.rencontres) {
                    //si la journee courante contient la rencontre
                    if (jID != null && championnat.getJournees().get(jID).getRencontres().containsKey(rencontre)) { // inutile ?? -------> && championnat.getJournees().equals(jID)
                        valcDelta++;
                    }
                }
            }
        } else {
            for (Integer jID : this.journees) {
                //si la journee courante contient la rencontre
                if (jID != null && championnat.getJournees().get(jID).getRencontres().containsKey(r)) { // inutile ?? -------> && championnat.getJournees().equals(jID)
                    valcDelta++;
                }
            }
        }
        return valcDelta;
    }

    @Override
    public int evalDeltaCout(Solution championnat, Operateur o) {
        Integer valcDelta=evalDeltaCoef(championnat,o);
        return evalDeltaCout(championnat, o, valcDelta);
    }


    @Override
    public int evalDeltaCout(Solution championnat, Operateur o, Integer valcDelta) {
        //TODO tchequer contrainte inerante si oui renvoyer max integer
        if(o instanceof OperateurInsertion){

            if(championnat.getCoefContraintes().get(this)+valcDelta>max || championnat.getCoefContraintes().get(this)+valcDelta < min){
                if (estDure()) return Integer.MAX_VALUE;
                //au dela du max ou en-dessous du min le cout suit une relation lineaire le deltat cout est donc proportionel
                return this.penalite *(valcDelta);
            }else return 0;
        }
        //TODO d'autre operation implique d'autre cout
        return 0;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ContrainteRencontres{");
        sb.append("journees=").append(journees).append(", ");
        sb.append("rencontres=").append(rencontres).append(", ");
        sb.append("min=").append(min).append(", ");
        sb.append("max=").append(max).append(", ");
        sb.append("penalite=").append(penalite).append(", ");
        sb.append("dure=").append(estDure()).append("}");
        return sb.toString();
    }
}
