package instance.modele.contrainte;

import operateur.OperateurInsertion;
import operateur.Operateur;
import solution.Solution;
import solution.Rencontre;

import java.util.TreeSet;

/** classe définissant ContraintePauseGlobale (hérite de Contrainte)
 * @author Engloo Benjamin
 * @author Morcq Alexandre
 * @author Sueur Jeanne
 * @author Lux Hugo
 * @version 1.1
 */
public class ContraintePauseGlobale extends Contrainte{
    private TreeSet<Integer> journees;
    private TreeSet<Integer>  equipes;
    private Integer max;

    public ContraintePauseGlobale(Integer max) {
        super();
        this.journees = new TreeSet<>();
        this.equipes = new TreeSet<>();
        this.max = max;
    }

    public ContraintePauseGlobale(Integer max, Integer penalite) {
        super(penalite);
        this.journees = new TreeSet<>();
        this.equipes = new TreeSet<>();
        this.max = max;
    }

    /**
     * Ajoute une équipe à la liste des équipes de la contrainte
     * @param id l'ID de l'équipe à ajouter
     * @return true si l'ajout a réussi, false sinon
     */
    public boolean addEquipe(Integer id){
        return equipes.add(id);
    }

    /**
     * Ajoute une journée à la liste des journées de la contrainte
     * @param id l'ID de la journée à ajouter
     * @return true si l'ajout a réussi, false sinon
     */
    public boolean addJournee(Integer id){
        return journees.add(id);
    }

    @Override
    public TypeContrainte getTypeContrainte() {
        return TypeContrainte.PAUSEGLOBALE;
    }

    //TODO implementer les fonction de calcule de cout en sinspirent de la contrainte de placement, réflechire si on ne peux pas factoriser du code sout des fonction comune aux contraintes
    @Override
    public int getCoutTotal(Solution championnat) {
        int valc = 0;

        for (Integer e : this.equipes) {
            for (Integer j : this.journees) {
                Rencontre rEquipe = null;
                // Pour chaque rencontre de la journée courante, on prend celles de l'équipe de la contrainte
                for (Rencontre r : championnat.getJourneeByID(j).getRencontres().values()) {
                    if(r.isConcerne(championnat.getEquipes().get(e), TypeMode.INDEFINI)) {
                        rEquipe = r;
                        break;
                    }
                }
                valc = this.traitementModes(championnat, j, rEquipe);
            }
        }

        if(valc > this.max) {
            if (estDure()) return Integer.MAX_VALUE;
            return this.penalite * (valc-this.max);
        }
        return 0;
    }

    @Override
    public int evalDeltaCoef(Solution championnat, Operateur o) {
        int valcDelta=0;
        if(o instanceof OperateurInsertion) {
            for (Integer e : this.equipes) {
                for (Integer j : this.journees) {
                    Rencontre rEquipe = null;
                    Rencontre r = o.getRencontre();
                    if (r.isConcerne(championnat.getEquipes().get(e), TypeMode.INDEFINI)) {
                        rEquipe = r;
                    }
                    valcDelta = this.traitementModes(championnat, j, rEquipe);
                }
            }
        }
        return valcDelta;
    }

    /**
     * ???
     * @param ...
     * @return ...
     */
    private int traitementModes(Solution championnat, Integer journee, Rencontre rEquipe) {
        int valc = 0;
        boolean lastPresence = false, currentPresence = false;

        if (rEquipe != null) {
            if (championnat.isRJPresent(journee - 1, rEquipe)) {
                lastPresence = championnat.isRJPresent(journee - 1, rEquipe);
            }
            if (championnat.isRJPresent(journee, rEquipe)) {
                currentPresence = championnat.isRJPresent(journee, rEquipe);
            }
            if (lastPresence == currentPresence && (lastPresence != false || currentPresence != false)) {
                valc++;
            }
        }
        return valc;
    }

    @Override
    public int evalDeltaCout(Solution championnat, Operateur o) {
        Integer valcDelta=evalDeltaCoef(championnat,o);
        return evalDeltaCout(championnat, o, valcDelta);
    }

    @Override
    public int evalDeltaCout(Solution championnat, Operateur o, Integer valcDelta) {
        if(o instanceof OperateurInsertion){

            if(championnat.getCoefContraintes().get(this)+valcDelta>max){
                if (estDure()) return Integer.MAX_VALUE;
                //au dela du max le cout suit une relation lineaire le deltat cout est donc proportionel
                return this.penalite *(valcDelta);
            }else return 0;
        }
        //TODO d'autre operation implique d'autre cout
        return 0;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ContraintePauseGlobale{");
        sb.append("journees=").append(journees).append(", ");
        sb.append("equipes=").append(equipes).append(", ");
        sb.append("max=").append(max).append(", ");
        sb.append("penalite=").append(penalite).append(", ");
        sb.append("dure=").append(estDure()).append("}");
        return sb.toString();
    }
}
