package operateur;

import solution.Journee;
import solution.Rencontre;

public abstract class Operateur {
    Journee journee;

    Rencontre rencontre;
    Integer penalitee;

    public Operateur() {
        this.penalitee = Integer.MAX_VALUE;
    }

    public Operateur(Journee j,Rencontre r) {
        this.journee = j;
        this.rencontre=r;
        this.penalitee = evalDeltaPenalite();
    }

    public int getDeltaPenalite() {
        return this.penalitee;
    }

    public boolean isMouvementRealisable() {
        return this.penalitee < Integer.MAX_VALUE;
    }

    public boolean isMeilleur(Operateur op) {
        return this.penalitee < op.getDeltaPenalite();
    }

    protected abstract int evalDeltaPenalite();

    protected abstract boolean doMouvement();

    public boolean doMouvementIfRealisable() {
        if(this.isMouvementRealisable()) return doMouvement();
        return false;
    }
    public boolean isMouvementAmeliorant(){
        return this.penalitee<0;
    }

    public Journee getJournee() {
        return journee;
    }

    public Integer getPenalitee() {
        return penalitee;
    }

    @Override
    public String toString() {
        return "Operateur{" +
                "journee=" + journee +
                ", penalitee=" + penalitee +
                '}';
    }

}
