package instance.modele.contrainte;

import ilog.concert.IloException;
import ilog.concert.IloLinearNumExpr;
import instance.Instance;
import operateur.Operateur;
import operateur.OperateurInsertion;
import solution.Solution;
import solveur.SolveurCplex;

import java.util.TreeSet;

/** classe définissant ContrainteRencontres (hérite de Contrainte)
 * @author Engloo Benjamin
 * @author Morcq Alexandre
 * @author Sueur Jeanne
 * @author Lux Hugo
 * @version 1.1
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
        this.min = min;
        this.max = max;
        this.penalite = Integer.MAX_VALUE;
    }

    public ContrainteRencontres(Integer min,Integer max, Integer penalite) {
        super(penalite);
        this.journees = new TreeSet<>();
        this.rencontres = new TreeSet<>();
        this.min = min;
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

    @Override
    public int getCoutTotal(Solution championnat) {
        //le nombre de rencontres jouées par l’équipe de la contrainte selon un mode sur l’ensemble des journées
        int valc=0;
        //pour toutes les journees concernées par la contrainte
        for (String rencontre : this.rencontres) {
            valc += parcoursJournees(championnat, rencontre);
        }

        if(valc>this.max || valc < this.min) {
            if (estDure()) return Integer.MAX_VALUE;
            return this.penalite *(Math.max(0, valc-this.max) + Math.max(0, this.min - valc)); //source :
            // https://docs.oracle.com/javase/8/docs/api/java/lang/Math.html
        }
        return 0;
    }

    @Override
    public Object evalDeltaCoef(Solution championnat, Operateur o) {
        int valcDelta=0;
        if(o instanceof OperateurInsertion) {
            if(this.journees.contains(o.getJournee().getId()) && this.rencontres.contains(o.getRencontre().getLabel())){
                valcDelta = 1;
            }
        }
        return valcDelta;
    }

    /**
     * Parcourt les journées de la contrainte pour incrémenter un compteur à chaque fois qu'une certaine rencontre
     * a lieu sur la journée concernée
     * @param championnat la solution
     * @param r la rencontre concernée
     * @return le nombre entier du compteur
     */
    private int parcoursJournees(Solution championnat, String r) {
        if(championnat.getRencontreByID(r).getJournee()==null)return 0;
        if(this.journees.contains(championnat.getRencontreByID(r).getJournee().getId())) return 1;
        return 0;
    }

    @Override
    public int evalDeltaCout(Solution championnat, Operateur o) {
        Integer valcDelta= (Integer) evalDeltaCoef(championnat,o);
        return evalDeltaCout(championnat, o, valcDelta);
    }


    @Override
    public int evalDeltaCout(Solution championnat, Operateur o, Object valcDelta) {
        if(o instanceof OperateurInsertion){

            if((Integer)championnat.getCoefContraintes().get(this)+(Integer) valcDelta>max || (Integer)championnat
                    .getCoefContraintes().get(this)+ (Integer) valcDelta < min){
                if (estDure()) return Integer.MAX_VALUE;
                //au dela du max ou en-dessous du min le cout suit une relation linéaire le delta cout est donc
                // proportionnel
                return this.penalite *((Integer)valcDelta);
            }else return 0;
        }
        return 0;
    }

    /**
     * @param sCplex
     */
    @Override
    public void initCplexEquation(SolveurCplex sCplex, Instance instance,boolean minimise,boolean minimiseSouple,
                                  boolean dure) {
        IloLinearNumExpr expr = null;
        IloLinearNumExpr expr2 = null;
        try {
            expr = sCplex.getCplex().linearNumExpr();
            expr2 = sCplex.getCplex().linearNumExpr();
            for(String rlabel:this.rencontres){
                String[] parties = rlabel.split("-");
                int d = Integer.parseInt(parties[0]);
                int e = Integer.parseInt(parties[1]);
                for(int j:this.journees){
                    expr.addTerm(sCplex.getX()[d][e][j], 1);
                    expr2.addTerm(sCplex.getX()[d][e][j], 1);
                }
            }
            if(dure) {
                if (minimise) expr.addTerm(sCplex.getCDureMax(this), -1);
                sCplex.getCplex().addLe(expr, this.max, "CDR1_"+sCplex.getCplex().getNrows());
                if (minimise) expr2.addTerm(sCplex.getCDureMin(this), -1);
                sCplex.getCplex().addGe(expr2, this.min,"CDR2_"+sCplex.getCplex().getNrows());
            }else if(minimiseSouple){
                addEqSoupleMaxMin(sCplex,this.max,this.min,expr);
            }

        } catch (IloException e) {
            throw new RuntimeException(e);
        }

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
