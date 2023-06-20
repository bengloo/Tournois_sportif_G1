package instance.modele.contrainte;

import instance.Instance;
import operateur.Operateur;
import operateur.OperateurInsertion;
import solution.Journee;
import solution.Solution;
import solveur.SolveurCplex;
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
        int valc =0;
        //pour tout les couple d'équipes sans doublont
        for (Integer equipe1 : equipes) {
            for (Integer equipe2 : equipes) {
                if (equipe1 < equipe2) {
                   Journee j1=championnat.getRencontreByEquipes(equipe1,equipe2).getJournee();
                    Journee j2=championnat.getRencontreByEquipes(equipe2,equipe1).getJournee();
                    if(j1!=null && j2!=null){
                        int separation= min - Math.abs(j1.getId()-j2.getId())-1;
                        if(separation>0)valc+=separation;
                    }
                }
            }
        }
        return this.penalite * valc;
    }

    @Override
    public Object evalDeltaCoef(Solution championnat, Operateur o) {
        if(this.equipes.contains(o.getRencontre().getDomicile().getId())&&this.equipes.contains(o.getRencontre()
                .getExterieur().getId())){
            Journee j1 = o.getJournee();
            Journee j2 = championnat.getMatchRetour(o.getRencontre()).getJournee();
            //System.out.println(j1);
            //System.out.println(j2);
            if (j2 != null) {
                int separation = min - (Math.abs(j1.getId() - j2.getId()) - 1);
                if (separation > 0) return separation;
            }
        }
        return 0;
    }

    @Override
    public int evalDeltaCout(Solution championnat, Operateur o) {
        Integer valcDelta=(Integer) evalDeltaCoef(championnat,o);
        return evalDeltaCout(championnat, o, valcDelta);
    }

    @Override
    public int evalDeltaCout(Solution championnat, Operateur o, Object valcDelta) {
        if(o instanceof OperateurInsertion){
            if (estDure()) return Integer.MAX_VALUE;
            return this.penalite *((Integer)valcDelta);
        }
        //TODO d'autre operation implique d'autre cout*/
        return 0;
    }


    @Override
    public void initCplexEquation(SolveurCplex sCplex, Instance instance,boolean minimise,boolean minimiseSouple,
                                  boolean dure) {
        if(!dure){
            //TODO cout
        }
    }

    @Override
    public boolean useValC() {
        return false;
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
