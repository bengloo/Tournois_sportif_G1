package solution;

import java.util.Objects;

public class Rencontre {
    private Equipe exterieur;
    private Equipe domicile;

    //TODO renseigné reciproquement la journee à la rencontre pour facilité les accés voir le code de l'anné dernierre sur les afectation reciproque de menierre securisé et retrocative en cas d'echec.

    public Rencontre(Equipe exterieur, Equipe domicile) {
        this.exterieur = exterieur;
        this.domicile = domicile;
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
    public String toString() {
        return exterieur.getId()+"-"+domicile.getId();
    }
}
