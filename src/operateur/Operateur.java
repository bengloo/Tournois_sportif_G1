package operateur;

import solution.Journee;

public abstract class Operateur {
    Journee journee;
    Integer penalitee;

    public Operateur() {
        this.penalitee = Integer.MAX_VALUE;
    }

    public Operateur(Journee j) {
        this.journee = j;
        this.penalitee = Integer.MAX_VALUE;
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

    @Override
    public String toString() {
        return "Operateur{" +
                "journee=" + journee +
                ", penalitee=" + penalitee +
                '}';
    }
}
