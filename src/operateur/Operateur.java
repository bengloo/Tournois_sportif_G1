package operateur;

import solution.Championnat;
import solution.Journee;
import solution.Rencontre;

public abstract class Operateur {
    Journee journee;
    Rencontre rencontre;

    Championnat championnat;
    Integer cout;

    public Operateur() {
        this.cout = Integer.MAX_VALUE;
    }

    public Operateur(Championnat c,Journee j,Rencontre r) {
        this.championnat=c;
        this.journee = j;
        this.rencontre=r;
        this.cout = evalDeltaCout();
    }

    public int getDeltaCout() {
        return this.cout;
    }

    public boolean isMouvementRealisable() {
        return this.cout < Integer.MAX_VALUE;
    }

    public boolean isMeilleur(Operateur op) {
        return this.cout < op.getDeltaCout();
    }

    protected abstract int evalDeltaCout();

    protected abstract boolean doMouvement();

    public boolean doMouvementIfRealisable() {
        if(this.isMouvementRealisable()) return doMouvement();
        return false;
    }
    public boolean isMouvementAmeliorant(){
        return this.cout<0;
    }

    public Journee getJournee() {
        return journee;
    }

    public Rencontre getRencontre() {
        return rencontre;
    }

    public void setRencontre(Rencontre rencontre) {
        this.rencontre = rencontre;
    }

    public Integer getCout() {
        return cout;
    }

    @Override
    public String toString() {
        return "Operateur{" +
                "journee=" + journee +
                ", cout=" + cout +
                '}';
    }

}
