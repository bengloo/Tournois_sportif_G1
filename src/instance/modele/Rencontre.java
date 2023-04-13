package instance.modele;

import java.util.Objects;

public class Rencontre {
    private Equipe exterieur;
    private Equipe domicile;

    public Rencontre(Equipe exterieur, Equipe domicile) {
        this.exterieur = exterieur;
        this.domicile = domicile;
    }

    public String getLabel(){
        return this.domicile.getId().toString()+"-"+this.exterieur.getId().toString();
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
        return exterieur.getId()+","+domicile.getId();
    }
}
