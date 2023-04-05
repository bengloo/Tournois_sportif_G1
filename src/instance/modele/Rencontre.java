package instance.modele;

public class Rencontre {
    private Equipe exterieur;
    private Equipe domicile;
    private Journee journee;

    public Rencontre(Equipe exterieur, Equipe domicile) {
        this.exterieur = exterieur;
        this.domicile = domicile;
        this.journee = null;
    }
}
