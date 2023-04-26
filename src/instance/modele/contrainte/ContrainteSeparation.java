package instance.modele.contrainte;

import operateur.Operateur;
import operateur.OperateurInsertion;
import solution.Rencontre;
import solution.Solution;

import java.util.LinkedList;
import java.util.TreeSet;

/** classe définissant ContrainteSeparation (hérite de Contrainte)
 * @author Engloo Benjamin
 * @author Morcq Alexandre
 * @author Sueur Jeanne
 * @author Lux Hugo
 * @version 1.1
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
        int valc = 0;
        Rencontre r2 = null;
        LinkedList<Rencontre> rencontresEquipe = null;

        for (Integer e1 : this.equipes) {
            rencontresEquipe = championnat.getRencontresByEquipe(e1);

            for (Rencontre r1 : rencontresEquipe) {
                r2 = championnat.getMatchRetour(r1);
                if (r1.getJournee() != null && r2.getJournee() != null && (r1.getJournee().getId() < r2.getJournee().getId() || (r1.getJournee().getId() == r2.getJournee().getId()) && r1.getLabel().compareTo(r2.getLabel()) > r2.getLabel().compareTo(r1.getLabel()))) {
                    valc += Math.max(0, this.min - (r2.getJournee().getId() - r1.getJournee().getId() - 1));
                }
            }
        }
        return this.penalite * valc;
    }

    @Override
    public int evalDeltaCoef(Solution championnat, Operateur o) {
        if(o instanceof OperateurInsertion) {
            Rencontre r2=championnat.getMatchRetour(o.getRencontre());
            if(r2.getJournee()!=null){
                return Math.max(0, this.min - (Math.abs(r2.getJournee().getId() - o.getJournee().getId()) - 1));
            }else{
                return 0;
            }
        }
        return 0;
    }

    @Override
    public int evalDeltaCout(Solution championnat, Operateur o) {
        Integer valcDelta=evalDeltaCoef(championnat,o);
        return evalDeltaCout(championnat, o, valcDelta);
    }

    @Override
    public int evalDeltaCout(Solution championnat, Operateur o, Integer valcDelta) {
        if(o instanceof OperateurInsertion){
            if (estDure()) return Integer.MAX_VALUE;
            return this.penalite *(valcDelta);
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
