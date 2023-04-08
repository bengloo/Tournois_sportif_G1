package instance.modele;

import solution.Journee;

import java.util.Objects;

public class Rencontre {
    private Equipe exterieur;
    private Equipe domicile;
    private Journee journee;

    public Rencontre(Equipe exterieur, Equipe domicile) {
        this.exterieur = exterieur;
        this.domicile = domicile;
        this.journee=null;
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
}
