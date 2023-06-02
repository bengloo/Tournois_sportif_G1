package operateur;

import instance.modele.contrainte.Contrainte;
import solution.Solution;
import solution.Journee;
import solution.Rencontre;

import java.util.Map;

/** classe définissant Operateur
 * @author Engloo Benjamin
 * @author Morcq Alexandre
 * @author Sueur Jeanne
 * @author Lux Hugo
 * @version 1.0
 */
public abstract class Operateur {
    //TODO protected ou private pour ces atribut à réflechire
    private Journee journee;
    private Rencontre rencontre;

    public Rencontre rencontre2;
    public Journee journee2;
    private Solution championnat;
    //delat cout totale de toutes les contraintes pour une operation donné on se baseras sur les coefDesContrainte pour ne par recalculer entierement les fonction objectives de l'entiereter de la solution dejas etablie
    private Integer cout;

    /*
    //TODO serait t'il judicieux de ne pas le garder en memoir et de le recalculer uniquement pour l'operation appliqué par do movement?
    //contient les deltatCoef des contrainte impacté par l'operation
    private Map<Contrainte,Integer> deltatCoefContraintes;
    */

    public Operateur() {
        this.cout = Integer.MAX_VALUE;
    }

    public Operateur(Journee journee, Rencontre rencontre, Rencontre rencontre2, Journee journee2, Solution championnat) {
        this.journee = journee;
        this.rencontre = rencontre;
        this.rencontre2 = rencontre2;
        this.journee2 = journee2;
        this.championnat = championnat;
        this.cout = evalDeltaCout();
    }

    /**
     * Indique si le mouvement lié à l'opérateur est faisable
     * @return true si le mouvement est réalisable (coût inférieur à l'infini), false sinon
     */
    public boolean isMouvementRealisable() {
        return this.cout < Integer.MAX_VALUE;
    }

    /**
     * Indique si l'opérateur courant est meilleur qu'un autre
     * @param op l'opérateur qui sera comparé avec celui courant
     * @return true si le coût de l'opérateur courant est inférieur à celui passé en paramètre, false sinon
     */
    public boolean isMeilleur(Operateur op) {
        return this.cout < op.getDeltaCout();
    }

    /**
     * Indique la somme des deltas de pénalité pour l'opération courante, sur toutes les contraintes du championnat
     * @param
     * @return Max.INTEGER si deltaCoef est nul, la somme des deltas de pénalité sinon
     */
    protected abstract Integer evalDeltaCout();

    /**
     * Ajoute dans un tableau tous les deltas du coeff de la fonction objective de toutes les contraintes du championnat
     * @return un tableau de deltaCoefs
     */
    protected abstract Map<Contrainte,Object> evalDeltaCoefs();

    /**
     * ??? TODO: compléter (pas compris)
     * @return true si ??? est réalisable, false sinon
     */
    protected abstract boolean isRealisableInital();

    /**
     * Applique l'opérateur courant en mettant à jour le coût des contraintes sur le championnat, et en affectant la rencontre à la journée
     * @return true l'opérateur a pu être appliqué, false sinon
     */
    protected abstract boolean doMouvement();

    /**
     * Applique l'opérateur courant si le mouvement lié à celui-ci est réalisable
     * @return true si le mouvement a été réalisé, false sinon
     */
    public boolean doMouvementIfRealisable() {
        if(this.isMouvementRealisable()) return doMouvement();
        return false;
    }

    /**
     * Indique si le mouvement lié à l'opérateur courant est améliorant
     * @return true si le mouvement améliore la situation actuelle (coût plus faible), false sinon
     */
    public boolean isMouvementAmeliorant(){
        return this.cout<0;
    }

    /**
     * Indique le coût engendré par l'opérateur
     * @return l'entier associé au coût
     */
    public int getDeltaCout() {
        return this.cout;
    }

    /**
     * Indique la journée concernée par l'application de l'opérateur
     * @return la journée en question
     */
    public Journee getJournee() {
        return journee;
    }

    /**
     * Indique la rencontre concernée par l'application de l'opérateur
     * @return la rencontre en question
     */
    public Rencontre getRencontre() {
        return rencontre;
    }

    /**
     * Indique le championnat concerné par l'application de l'opérateur
     * @return le championnat en question (solution)
     */
    protected Solution getChampionnat() {
        return championnat;
    }

    /**
     * Indique le coût engendré par l'opérateur
     * @return le coût en question
     */
    public Integer getCout() {
        return cout;
    }

    /**
     * Mise à jour de la rencontre de l'opérateur courant
     * @param rencontre à appliquer
     */
    public void setRencontre(Rencontre rencontre) {
        this.rencontre = rencontre;
    }

    @Override
    public String toString() {
        return "Operateur{" +
                "journee=" + journee +
                ", rencontre=" + rencontre +
                ", cout=" + cout +
                '}';
    }
}
