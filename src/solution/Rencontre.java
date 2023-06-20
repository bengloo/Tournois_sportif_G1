package solution;

import instance.modele.contrainte.TypeMode;

import java.util.Objects;

/** classe définissant Rencontre
 * @author Engloo Benjamin
 * @author Morcq Alexandre
 * @author Sueur Jeanne
 * @author Lux Hugo
 * @version 1.0
 */
public class Rencontre {
    private Equipe exterieur;
    private Equipe domicile;
    private Journee journee;

    public Rencontre(Equipe exterieur, Equipe domicile) {
        this.exterieur = exterieur;
        this.domicile = domicile;
        this.journee = null;
    }

    /**
     *
     * @param e équipe ciblée
     * @param mode mode ciblé
     * @return un bolean indiquant si l'équipe ciblée fait partie de la rencontre dans le mode précisé
     */
    public boolean isConcerne(Equipe e, TypeMode mode){
        switch (mode) {
            case DOMICILE:
                return this.domicile.equals(e);
            case EXTERIEUR:
                return this.exterieur.equals(e);
            case INDEFINI:
                return this.domicile.equals(e)||this.exterieur.equals(e);
            default:
                return false;
        }
    }


    public boolean isConcerne(int eid, TypeMode mode){
        switch (mode) {
            case DOMICILE:
                return this.domicile.getId()==eid;
            case EXTERIEUR:
                return this.exterieur.getId()==eid;
            case INDEFINI:
                return this.domicile.getId()==eid||this.exterieur.getId()==eid;
            default:
                return false;
        }
    }

    /**
     * Indique l'équipe jouant en extérieur de la rencontre courante
     * @return l'ID de l'équipe en question
     */
    public Equipe getExterieur() {
        return this.exterieur;
    }

    /**
     * Indique l'équipe jouant à domicile de la rencontre courante
     * @return l'ID de l'équipe en question
     */
    public Equipe getDomicile() {
        return this.domicile;
    }

    /**
     * Indique la journée sur laquelle a lieu la rencontre courante
     * @return la journée en question
     */
    public Journee getJournee() {
        return this.journee;
    }

    /**
     * Indique à la fois l'ID de l'équipe jouant à domicile suivi de celui de l'équipe jouant en extérieur
     * Par exemple, si l'équipe 0 joue à domicile contre l'équipe 1, on aura "0-1"
     * @return la chaîne de caractères correspondante
     */
    public String getLabel(){
        return this.domicile.getId().toString()+"-"+this.exterieur.getId().toString();
    }

    /**
     * Indique à la fois l'ID de l'équipe jouant en extérieur suivi de celui de l'équipe jouant à domicile
     * Par exemple, si l'équipe 0 joue à domicile contre l'équipe 1, on aura "1-0"
     * @return la chaîne de caractères correspondante
     */
    public String getLabelRetour(){
        return this.exterieur.getId().toString()+"-"+this.domicile.getId().toString();
    }

    /**
     * Mise à jour de la journée sur laquelle a lieu la rencontre courante
     * @param j la journée à appliquer
     * @return true si la mise à jour a été effectuée avec succès, false sinon
     */
    public boolean setJournee(Journee j){
        if(j==null){//on utilise null pour le remove
            this.journee=null;
            return true;
        }
        if(j.isPresent(this)){//s'il est présent
            Journee lastJournee= this.journee;
            this.journee = j;
            if(lastJournee!=null){
                lastJournee.removeRencontre(this);
            }
            return true;
        }
        System.err.println("échec d'ajout Rencontre/Journee");
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Rencontre rencontre)) return false;
        return Objects.equals(exterieur, rencontre.exterieur) && Objects.equals(domicile, rencontre.domicile);
    }

    @Override
    public int hashCode() {
        return Objects.hash(exterieur, domicile);
    }

    /**
     * Indique si la rencontre courante est incluse dans une certaine journée
     * @param j la journée concernée
     * @return true si la rencontre est incluse, false sinon
     */
    public boolean isInJournee(Journee j){
        if(j==null||this.journee==null){return false;}
        else{
            return this.journee.equals(j);
        }
    }

    /**
     * Renvoie une chaîne de caractères longue qui caractérise une rencontre
     * @return la chaîne de caractères avec les équipes jouant à domicile et en extérieur de la rencontre
     */
    public String toString() {
         return "Rencontre{" +
                 "domicile=" + domicile +
                ", exterieur=" + exterieur +
                 ", journee=" + (journee==null?"null":journee.getId().toString())+
                '}';
    }
}