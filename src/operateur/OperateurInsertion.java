package operateur;

import instance.modele.Rencontre;
import solution.Journee;

public class OperateurInsertion extends Operateur{

    public OperateurInsertion(Journee j, Rencontre r) {
        super(j, r);
        this.penalitee=this.evalDeltaPenalite();
    }

    @Override
    protected int evalDeltaPenalite() {
        return 0;
    }

    @Override
    protected boolean doMouvement() {
        return false;
    }
}
