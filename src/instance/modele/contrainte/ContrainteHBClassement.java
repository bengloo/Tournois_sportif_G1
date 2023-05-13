package instance.modele.contrainte;

import operateur.OperateurInsertion;
import operateur.Operateur;
import solution.Solution;
import solution.Rencontre;

import java.util.Objects;
import java.util.TreeSet;

import static instance.modele.contrainte.TypeMode.*;

/** classe définissant ContrainteHBClassement (hérite de Contrainte)
 * @author Engloo Benjamin
 * @author Morcq Alexandre
 * @author Sueur Jeanne
 * @author Lux Hugo
 * @version 1.1
 */
public class ContrainteHBClassement extends Contrainte{
    private Integer equipe;
    private TreeSet<Integer> journees;
    private TreeSet<Integer> equipesAdverses;
    private TypeMode mode;
    private Integer max;

    public ContrainteHBClassement(Integer equipe,TypeMode mode,Integer max) {
        super();
        this.equipe=equipe;
        this.journees = new TreeSet<>();
        this.equipesAdverses = new TreeSet<>();
        this.mode = mode;
        this.max = max;
    }
    public ContrainteHBClassement(Integer equipe,TypeMode mode,Integer max, Integer penalite) {
        super(penalite);
        this.equipe=equipe;
        this.journees = new TreeSet<>();
        this.equipesAdverses = new TreeSet<>();
        this.mode = mode;
        this.max = max;
    }

    /**
     * Ajoute une journée à la liste des journées de la contrainte
     * @param id l'ID de la journée à ajouter
     * @return true si l'ajout a réussi, false sinon
     */
    public boolean addJournee(Integer id){
        return journees.add(id);
    }

    /**
     * Ajoute une équipe à la liste des équipes adverses de la contrainte
     * @param id l'ID de l'équipe à ajouter
     * @return true si l'ajout a réussi, false sinon
     */
    public boolean addEquipeAdverse(Integer id){
        return equipesAdverses.add(id);
    }

    @Override
    public TypeContrainte getTypeContrainte() {
        return TypeContrainte.HBCLASSEMENT;
    }

    //TODO implementer les fonction de calcule de cout en sinspirent de la contrainte de placement, réflechire si on ne peux pas factoriser du code sout des fonction comune aux contraintes
    @Override
    public int getCoutTotal(Solution championnat) {
        //le nombre de rencontres jouées par l’équipe de la contrainte selon un mode sur l’ensemble des journées
        int valc=0;
        //pour toute les rencontres
        for(Rencontre r:championnat.getRencontres().values()){
            // à chaque équipe adverse de la liste rencontrée
            if (this.equipesAdverses.contains(r.getDomicile().getId()) || this.equipesAdverses.contains(r.getExterieur().getId())) {
                valc += parcoursJournees(championnat, r);
            }
        }

        if(valc>this.max) {
            if (estDure()) return Integer.MAX_VALUE;
            return this.penalite *(valc-this.max);
        }
        return 0;
    }

    @Override
    public Object evalDeltaCoef(Solution championnat, Operateur o) {
        int valcDelta=0;
        if(o instanceof OperateurInsertion) {
            /*if (this.equipesAdverses.contains(r.getDomicile().getId()) || this.equipesAdverses.contains(r.getExterieur().getId())) {
                valcDelta = parcoursJournees(championnat, r);
            }*/
            switch(this.mode) {
                case DOMICILE:
                    if(this.journees.contains(o.getJournee().getId()) && this.equipesAdverses.contains(o.getRencontre().getExterieur().getId()) && o.getRencontre().isConcerne(championnat.getEquipeByID(equipe),mode)) {
                        valcDelta = 1;
                    }
                    break;
                case EXTERIEUR:
                    if(this.journees.contains(o.getJournee().getId()) && this.equipesAdverses.contains(o.getRencontre().getDomicile().getId()) && o.getRencontre().isConcerne(championnat.getEquipeByID(equipe),mode)) {
                        valcDelta = 1;
                    }
                    break;
                case INDEFINI:
                    if(this.journees.contains(o.getJournee().getId()) && (this.equipesAdverses.contains(o.getRencontre().getDomicile().getId()) || this.equipesAdverses.contains(o.getRencontre().getExterieur().getId())) && o.getRencontre().isConcerne(championnat.getEquipeByID(equipe),mode)) {
                        valcDelta = 1;
                    }
                    break;
            }
        }
        return valcDelta;
    }

    /**
     * Parcourt les journées de la contrainte pour incrémenter un compteur à chaque fois que son équipe fait partie de la rencontre
     * @param championnat la solution
     * @param r la rencontre concernée
     * @return le nombre entier du compteur
     */
    private int parcoursJournees(Solution championnat, Rencontre r) { //Factorisation du code
        int valcDelta=0;
        for (Integer jID : this.journees) {
            /*if(r.isConcerne(championnat.getEquipes().get(this.equipe), this.mode) && championnat.isRJPresent(jID,r));
            valcDelta++;*/
            switch(this.mode) {
                case DOMICILE:
                    if(r.isConcerne(championnat.getEquipes().get(this.equipe), this.mode) && this.equipesAdverses.contains(r.getExterieur().getId()) && championnat.isRJPresent(jID,r)) {
                        valcDelta++;
                    }
                    break;
                case EXTERIEUR:
                    if(r.isConcerne(championnat.getEquipes().get(this.equipe), this.mode) && this.equipesAdverses.contains(r.getDomicile().getId()) && championnat.isRJPresent(jID,r)) {
                        valcDelta++;
                    }
                    break;
                case INDEFINI:
                    if(r.isConcerne(championnat.getEquipes().get(this.equipe), this.mode) && (this.equipesAdverses.contains(r.getDomicile().getId()) || this.equipesAdverses.contains(r.getExterieur().getId())) && championnat.isRJPresent(jID,r)) {
                        valcDelta++;
                    }
                    break;
            }
        }
        return valcDelta;
    }

    @Override
    public int evalDeltaCout(Solution championnat, Operateur o) {
        Object valcDelta=evalDeltaCoef(championnat,o);
        return evalDeltaCout(championnat, o, valcDelta);
    }

    @Override
    public int evalDeltaCout(Solution championnat, Operateur o, Object valcDelta) {
        if(o instanceof OperateurInsertion){
            if((Integer)championnat.getCoefContraintes().get(this)+ (Integer) valcDelta>max){
                if (estDure()) return Integer.MAX_VALUE;
                //au dela du max le cout suit une relation lineaire le deltat cout est donc proportionel
                return this.penalite *((Integer)valcDelta);
            }else return 0;
        }
        //TODO d'autre operation implique d'autre cout
        return 0;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ContrainteHBClassement{");
        sb.append("equipe=").append(equipe).append(", ");
        sb.append("journees=").append(journees).append(", ");
        sb.append("equipesAdverses=").append(equipesAdverses).append(", ");
        sb.append("mode=").append(mode).append(", ");
        sb.append("max=").append(max).append(", ");
        sb.append("penalite=").append(penalite).append(", ");
        sb.append("dure=").append(estDure()).append("}");
        return sb.toString();
    }
}
