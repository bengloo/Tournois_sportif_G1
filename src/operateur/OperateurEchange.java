package operateur;

import instance.modele.contrainte.Contrainte;

import java.util.Map;

public class OperateurEchange extends OperateurLocal{
    @Override
    protected int evalDeltaCout() {
        return 0;
    }

    @Override
    protected Map<Contrainte, Object> evalDeltaCoefs() {
        return null;
    }

    @Override
    protected boolean isRealisableInital() {
        return false;
    }

    @Override
    protected boolean doMouvement() {
        return false;
    }

    @Override
    public boolean isTabou(OperateurLocal operateur) {
        return false;
    }
    //TODO finir insertion simple avant
    //TODO Echange des rencontre entre diferente journee
}
