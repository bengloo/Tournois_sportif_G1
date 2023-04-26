package instance.modele.contrainte;

import operateur.Operateur;
import solution.Solution;

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
     * Indique si la contrainte courante est dure ou souple
     * @return true si la contrainte est souple, false sinon
     */
    public boolean estSouple(){
        return penalite!=Integer.MAX_VALUE;
    }

    /**
     * Indique le coût total engendré par la contrainte (à chaque fois que la contrainte est vérifiée, cumule les pénalités)
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
    public abstract int evalDeltaCoef(Solution championnat, Operateur o);

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
     * @param deltaCoef le coefficient deltay
     * @return le delta (ou Max.INTEGER si la contrainte est dure)
     **/
    public abstract int evalDeltaCout(Solution championnat, Operateur o, Integer deltaCoef);

    /**
     * Indique si la contrainte conserve la viabilité de la solution
     * @param championnat
     * @return true si la solution est faisable, false sinon
     */
    public boolean checkContrainte(Solution championnat){
        return estDure()&&getCoutTotal(championnat)!=Integer.MAX_VALUE;
    }
}
