package solution;

import java.util.Objects;
/** Class definissant .
 * @author Engloo Benjamin
 * @author Morcq Alexandre
 * @author Sueur Jeanne
 * @author Lux Hugo
 * @version 0.5
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

    public String getLabel(){
        return this.domicile.getId().toString()+"-"+this.exterieur.getId().toString();
    }

    public String getLabelRetour(){
        return this.exterieur.getId().toString()+"-"+this.domicile.getId().toString();
    }

    public Equipe getExterieur() {
        return exterieur;
    }

    public Equipe getDomicile() {
        return domicile;
    }

    public Journee getJournee() {
        return journee;
    }

    public boolean setJournee(Journee j){
        if(j==null){//on utilise null pour le remove
            this.journee=null;
            return true;
        }
        if(j.isPresent(this)){//si il est présent
            Journee lastJournee= this.journee;
            this.journee = j;
            if(lastJournee!=null){
                lastJournee.removeRencontre(this);
            }
            return true;
        }
        System.err.println("echec d'a jout Rencontre/Journee");
        return false;
    }

    public boolean isInJournee(Journee j){
        if(j==null||this.journee==null){return false;}
        else{
            return this.journee.equals(j);
        }
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


    public String toStringLong() {
        return "Rencontre{" +
                "exterieur=" + exterieur +
                ", domicile=" + domicile +
                '}';
    }
    @Override
   /* public String toString() {
        return exterieur.getId()+"-"+domicile.getId();
    }*/
    public String toString() {
     return "Rencontre{" +
             "exterieur=" + exterieur +
            ", domicile=" + domicile +
             ", journee=" + (journee==null?"null":journee.getId().toString())+
            '}';
}
}
