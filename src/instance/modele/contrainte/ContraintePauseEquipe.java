package instance.modele.contrainte;

import operateur.Operateur;
import operateur.OperateurInsertion;
import solution.Solution;
import solution.Rencontre;

import java.util.TreeSet;

/** classe définissant ContraintePauseEquipe (hérite de Contrainte)
 * @author Engloo Benjamin
 * @author Morcq Alexandre
 * @author Sueur Jeanne
 * @author Lux Hugo
 * @version 1.1
 */
public class ContraintePauseEquipe extends Contrainte{
    private Integer equipe;
    private TreeSet<Integer> journees;
    private TypeMode mode;
    private Integer max;

    public ContraintePauseEquipe(Integer equipe,TypeMode mode,Integer max) {
        super();
        this.equipe=equipe;
        this.journees = new TreeSet<>();
        this.mode = mode;
        this.max = max;
    }
    public ContraintePauseEquipe(Integer equipe,TypeMode mode,Integer max, Integer penalite) {
        super(penalite);
        this.equipe=equipe;
        this.journees = new TreeSet<>();
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

    @Override
    public TypeContrainte getTypeContrainte() {
        return TypeContrainte.PAUSEEQUIPE;
    }

    //TODO implementer les fonction de calcule de cout en sinspirent de la contrainte de placement, réflechire si on ne peux pas factoriser du code sout des fonction comune aux contraintes
    @Override
    public int getCoutTotal(Solution championnat) {
        // Nombre de pause comptées
        int valc = 0;

        for (Integer j : this.journees) {
            Rencontre rEquipe = null;
            // Pour chaque rencontre de la journée courante, on prend celles de l'équipe de la contrainte
            for (Rencontre r : championnat.getJourneeByID(j).getRencontres().values()) {
                if(r.isConcerne(championnat.getEquipes().get(this.equipe), TypeMode.INDEFINI)) {
                    rEquipe = r;
                    break;
                }
            }
            valc = this.traitementModes(championnat, j, rEquipe);
        }

        if(valc > this.max) {
            if (estDure()) return Integer.MAX_VALUE;
            return this.penalite * (valc-this.max);
        }
        return 0;
    }

    @Override
    public int evalDeltaCoef(Solution championnat, Operateur o) {
        int valcDelta = 0;
        if(o instanceof OperateurInsertion) {
            //coef=0 //(nombre de pause compté )
            //pour j la journee d'insertion de la rencontre à j+1
            //lastMode = mode du match de l'equipe de la contrainte au j-1 (null si n'existe pas)
            //curentMode = mode du match de l'equipe de la contrainte au jour j (un truc diferant de null si n'existe pas)
            //si lastMode == currentMode
            //coef++
            //apliqué la fonction objective

            for (Integer j : this.journees) {
                Rencontre rEquipe = null;
                Rencontre r = o.getRencontre();
                if(r.isConcerne(championnat.getEquipes().get(this.equipe), TypeMode.INDEFINI)) {
                    rEquipe = r;
                }
                valcDelta = this.traitementModes(championnat, j, rEquipe);
            }
        }
        return valcDelta;
    }

    private int traitementModes(Solution championnat, Integer journee, Rencontre rEquipe) {
        int valc = 0;
        TypeMode lastMode = null, currentMode = null;

        if (rEquipe != null) {
            if (rEquipe.isConcerne(championnat.getEquipes().get(this.equipe), this.mode) && championnat.isRJPresent(journee - 1, rEquipe)) {
                lastMode = this.mode;
            }
            if (rEquipe.isConcerne(championnat.getEquipes().get(this.equipe), this.mode) && championnat.isRJPresent(journee, rEquipe)) {
                currentMode = this.mode;
            }
            if (lastMode.equals(currentMode) && (lastMode != null || currentMode != null)) {
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
        //TODO tchequer contrainte inerante si oui renvoyer max integer
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
        sb.append("ContraintePauseEquipe{");
        sb.append("equipe=").append(equipe).append(", ");
        sb.append("journees=").append(journees).append(", ");
        sb.append("mode=").append(mode).append(", ");
        sb.append("max=").append(max).append(", ");
        sb.append("penalite=").append(penalite).append(", ");
        sb.append("dure=").append(estDure()).append("}");
        return sb.toString();
    }
}
