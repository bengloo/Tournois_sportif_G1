package instance.modele.contrainte;

import ilog.concert.IloException;
import ilog.concert.IloLinearNumExpr;
import instance.Instance;
import operateur.Operateur;
import solution.Solution;
import solveur.SolveurCplex;

/** classe définissant Contrainte (classe abstraite)
 * @author Engloo Benjamin
 * @author Morcq Alexandre
 * @author Sueur Jeanne
 * @author Lux Hugo
 * @version 1.0
 */
public abstract class Contrainte {

    protected Integer penalite;

    public Contrainte(Integer penalite) {
        this.penalite=penalite;
    }
    public Contrainte() {
        this.penalite=Integer.MAX_VALUE;
    }

    /**
     * Indique le type de la contrainte courante
     * @return le type ENUM correspondant au type de contrainte
     */
    public abstract TypeContrainte getTypeContrainte();

    /**
     * Indique si la contrainte courante est dure ou souple
     * @return true si la contrainte est dure, false sinon
     */
    public boolean estDure(){
        return penalite==Integer.MAX_VALUE;
    }


    /**
     * Indique le coût total engendré par la contrainte (à chaque fois que la contrainte est vérifiée,
     * cumule les pénalités)
     * @param championnat la solution
     * @return l'entier associé au coût (ou Max.INTEGER si la contrainte vérifiée est dure)
     **/
    public abstract int getCoutTotal(Solution championnat);


    /**
     * Indique le delta du coeff de la fonction objective de la contrainte
     * @param championnat la solution
     * @param o l'opérateur
     * @return le delta du coeff
     **/
    public abstract Object evalDeltaCoef(Solution championnat, Operateur o);

    /**
     * Indique le delta de penalité pour une opération faite sur le championnat
     * @param championnat la solution
     * @param o l'opérateur
     * @return le delta (ou Max.INTEGER si la contrainte est dure)
     **/
    public abstract int evalDeltaCout(Solution championnat, Operateur o);

    /**
     * Indique le delta de penalité pour une opération faite sur le championnat
     * @param championnat la solution
     * @param o l'opérateur
     * @param deltaCoef le coefficient delta
     * @return le delta (ou Max.INTEGER si la contrainte est dure)
     **/
    public abstract int evalDeltaCout(Solution championnat, Operateur o, Object deltaCoef);

    /**
     * Indique si la contrainte conserve la viabilité de la solution (coût non égal à l'infini)
     * @param championnat
     * @return true si la solution est faisable, false sinon
     */
    public boolean checkContrainte(Solution championnat){
        return getCoutTotal(championnat)!=Integer.MAX_VALUE;
    }

    /**
     *
     * @param sCplex    Solveur dans lequel est definit le model cplex et ces variables de decisions
     * @param instance  instance traité par le solveur
     * @param minimiseDure  definit si il faut rendre permisive les equation de contrainte dure en ajoutant delat
     *                      que l'on cherchera à minimiser
     * @param minimiseSouple    definit si il faut pauser les equations des contrainte souple afin de minimiser leurs
     *                          couts
     * @param dure
     */
    public abstract void initCplexEquation(SolveurCplex sCplex, Instance instance, boolean minimiseDure,
                                           boolean minimiseSouple, boolean dure);


    /**
     * definit l'equoition de contrainte souple pour une contrainte soumis à un maximum à partire de l'expr du coef/valc
     * @param sCplex    le solveur contenant le model cplex et les variable de decision
     * @param max la valeur max admise par le contrainte
     * @param expr expression cplex du coef de la contrainte 
     */
    public void addEqSoupleMax(SolveurCplex sCplex, int max, IloLinearNumExpr expr){
        //cout=max(valc-k;0)*w
        try {
            sCplex.getCplex().addEq(
                    sCplex.getCoutC(this),
                    sCplex.getCplex().prod(
                            sCplex.getCplex().max(
                                    sCplex.getCplex().sum(expr,-max),
                                    0
                            )
                            ,this.penalite
                    ),
                    "CSM_"+sCplex.getCplex().getNrows()

            );
        } catch (IloException e) {
            throw new RuntimeException(e);
        }
    }
    public void addEqSoupleMaxMin(SolveurCplex sCplex, int max,int min, IloLinearNumExpr expr){
        //expr est l'iloNumexpr representative de valc
        //cout=(max(expr-k;0)+max(L-expr;0))*w
        try {
            sCplex.getCplex().addEq(
                    sCplex.getCoutC(this),
                    sCplex.getCplex().prod(
                            sCplex.getCplex().sum(
                                sCplex.getCplex().max(
                                        sCplex.getCplex().sum(
                                                expr,
                                                -max
                                        ),
                                        0
                                ),
                                sCplex.getCplex().max(
                                        sCplex.getCplex().sum(
                                                sCplex.getCplex().prod(expr,-1),
                                                min
                                        ),
                                        0
                                )
                            )
                            ,this.penalite
                    ),
                    "CSMM_"+sCplex.getCplex().getNrows()

            );
        } catch (IloException e) {
            throw new RuntimeException(e);
        }
    }

}
