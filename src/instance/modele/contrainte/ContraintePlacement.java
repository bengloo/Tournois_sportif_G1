package instance.modele.contrainte;

import ilog.concert.IloException;
import ilog.concert.IloLinearNumExpr;
import ilog.concert.IloNumExpr;
import ilog.concert.IloRange;
import ilog.cplex.IloCplex;
import instance.Instance;
import operateur.OperateurInsertion;
import operateur.Operateur;
import solution.Solution;
import solution.Rencontre;
import solveur.SolveurCplex;

import java.util.Objects;
import java.util.TreeSet;

/** classe définissant ContraintePlacement (hérite de Contrainte)
 * @author Engloo Benjamin
 * @author Morcq Alexandre
 * @author Sueur Jeanne
 * @author Lux Hugo
 * @version 1.0
 */
public class ContraintePlacement extends Contrainte{
    private Integer equipe;
    private TreeSet<Integer> journees;
    private TypeMode mode;
    private Integer max;

    public ContraintePlacement(Integer equipe,TypeMode mode,Integer max) {
        super();
        this.equipe=equipe;
        this.journees = new TreeSet<>();
        this.mode = mode;
        this.max = max;
    }

    public ContraintePlacement(Integer equipe,TypeMode mode,Integer max, Integer penalite) {
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
        return TypeContrainte.PLACEMENT;
    }


    @Override
    public int getCoutTotal(Solution championnat) {
        //pseudo code
            //coef=0
            //pour r dans toutes les rencontres de la solution
                //pour j dans toutes les journées de la contrainte
                    //Pour le mode de la contrainte
                        //si r verifie le mode concerné avec l'équipe de la contrainte
                            //coef++
        //appliqué fonction objective au coef


        //le nombre de rencontres jouées par l’équipe de la contrainte selon un mode sur l’ensemble des journées
        int valc=0;

        //pour toutes les rencontres
        for(Rencontre r:championnat.getRencontres().values()){
            //pour toutes les journees concerné par les contraintes
            valc += parcoursJournees(championnat, r);
            //System.out.println("coef contrainte placement get cout tt:"+valc);
        }

        if(valc>this.max) {
            if (estDure()) return Integer.MAX_VALUE;
            return this.penalite *(valc-this.max);
        }
        return 0;
    }

    @Override
    public Object evalDeltaCoef(Solution championnat, Operateur o) {
        Integer valcDelta=0;
        if(o instanceof OperateurInsertion) {
            if(this.journees.contains(o.getJournee().getId()) && o.getRencontre().isConcerne(championnat
                    .getEquipeByID(equipe),mode)) {
                valcDelta = 1;
            }
        }
        return valcDelta;
    }

    /**
     * Parcourt les journées de la contrainte pour incrémenter un compteur à chaque fois que son équipe fait partie
     * de la rencontre
     * @param championnat la solution
     * @param r la rencontre concernée
     * @return le nombre entier du compteur
     */
    private int parcoursJournees(Solution championnat, Rencontre r) { //Factorisation du code
        int valcDelta=0;
        for (Integer jID : this.journees) {
            if(r.isConcerne(championnat.getEquipes().get(this.equipe), this.mode) && championnat.isRJPresent(jID,r)) {
                //System.out.println("deltacoef++ evaldeltacoef coef:"+valcDelta);
                valcDelta++;
            }
        }
        return valcDelta;
    }

    @Override
    public int evalDeltaCout(Solution championnat, Operateur o) {
        Integer valcDelta= (Integer) evalDeltaCoef(championnat,o);
        return evalDeltaCout(championnat, o, valcDelta);
    }

    @Override
    public int evalDeltaCout(Solution championnat, Operateur o, Object valcDelta) {
        if((Integer)championnat.getCoefContraintes().get(this)+(Integer) valcDelta>max){
            if (estDure()){
                return Integer.MAX_VALUE;
            };
            //au dela du max le cout suit une relation lineaire le deltat cout est donc proportionel
            return this.penalite *((Integer)valcDelta);
        }else return 0;
    }

    /**
     * @param sCplex
     */
    @Override
    public void initCplexEquation(SolveurCplex sCplex, Instance instance,boolean minimise,boolean minimiseSouple,
                                  boolean dure) {
        if(this.mode==TypeMode.DOMICILE) {
            try {
                IloLinearNumExpr expr = sCplex.getCplex().linearNumExpr();
                for (int i = 0; i < instance.getNbEquipes(); i++) {
                    for (int j:this.journees) {
                        if(i!=this.equipe)expr.addTerm(sCplex.getX()[this.equipe][i][j], 1);
                    }
                }
                if(dure) {
                    if (minimise) expr.addTerm(sCplex.getCDureMax(this), -1);
                    sCplex.getCplex().addLe(expr, this.max,"CDP1_"+sCplex.getCplex().getNrows());
                }else if(minimiseSouple){
                    addEqSoupleMax(sCplex,max,expr);
                }
            } catch (IloException e) {
                throw new RuntimeException(e);
            }
        } else if (this.mode==TypeMode.EXTERIEUR){
            try {
                IloLinearNumExpr expr = sCplex.getCplex().linearNumExpr();
                for (int i = 0; i < instance.getNbEquipes(); i++) {
                    for (int j:this.journees) {
                        if(i!=this.equipe)expr.addTerm(sCplex.getX()[i][this.equipe][j], 1);
                    }
                }
                if(dure) {
                    if (minimise) expr.addTerm(sCplex.getCDureMax(this), -1);
                    sCplex.getCplex().addLe(expr, this.max,"CDP2_"+sCplex.getCplex().getNrows());
                }else if(minimiseSouple){
                    addEqSoupleMax(sCplex,max,expr);
                }
            } catch (IloException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public boolean useValC() {
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ContraintePlacement{");
        sb.append("equipe=").append(equipe).append(", ");
        sb.append("journees=").append(journees).append(", ");
        sb.append("mode=").append(mode).append(", ");
        sb.append("max=").append(max).append(", ");
        sb.append("penalite=").append(penalite).append(", ");
        sb.append("dure=").append(estDure()).append("}");
        return sb.toString();
    }
}
