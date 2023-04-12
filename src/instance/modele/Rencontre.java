package instance.modele;

import java.util.Objects;

public class Rencontre {
    private Equipe exterieur;
    private Equipe domicile;

    public Rencontre(Equipe exterieur, Equipe domicile) {
        this.domicile = domicile;
        this.exterieur = exterieur;

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

    public String getLabel(){
        return this.domicile.getId().toString()+"-"+this.exterieur.getId().toString();
    }

    public String toStringLong() {
        return "Rencontre{" +
                "domicile=" + domicile +
                ", exterieur=" + exterieur +
                '}';
    }
    @Override
    public String toString() {
        return exterieur.getId()+","+domicile.getId();
    }

    public Equipe getExterieur() {
        return exterieur;
    }

    public Equipe getDomicile() {
        return domicile;
    }
}
