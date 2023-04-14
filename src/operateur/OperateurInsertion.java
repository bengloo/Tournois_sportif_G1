package operateur;

import instance.modele.contrainte.Contrainte;
import instance.modele.contrainte.TypeContrainte;

public class OperateurInsertion extends Operateur{
    @Override
    protected int evalDeltaCout() {
        int delta=0;
        for(TypeContrainte type:TypeContrainte.values()){
            for(Contrainte c:championnat.getContraintes(type)){
                delta+=c.evalDeltatPenalite(championnat,this);
            }
        }
        return delta;
    }

    @Override
    protected boolean doMouvement() {


        return false;
    }
}
