package operateur;

public class OperateurInsertion extends Operateur{
    @Override
    protected int evalDeltaPenalite() {
        return 0;
    }

    @Override
    protected boolean doMouvement() {
        return false;
    }
}
