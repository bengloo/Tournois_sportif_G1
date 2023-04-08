package instance.modele.contrainte;

import solution.Championnat;

public abstract class Contrainte {

    protected boolean dure;
    public Contrainte(boolean dure) {
        this.dure=dure;
    }

    public TypeContrainte getTypeContrainte(){
        if(this instanceof ContrainteEquite)return TypeContrainte.EQUITE;
        if(this instanceof ContrainteHBClassement)return TypeContrainte.HBCLASSEMENT;
        if(this instanceof ContraintePauseEquipe)return TypeContrainte.PAUSEEQUIPE;
        if(this instanceof ContraintePlacement)return TypeContrainte.PLACEMENT;
        if(this instanceof ContrainteRencontres)return TypeContrainte.RENCONTRES;
        if(this instanceof ContrainteSeparation)return TypeContrainte.SEPARATION;
        return null;
    }

    public boolean estDure(){
        return dure;
    }
    public boolean estSouple(){
        return !dure;
    }

    /**
     * @param championnat la solution
     * @return a chaque fois que la contrainte est verifier cumule les penalité cumulé si la contrainte verifier est dure retourné Max integer sin retourner O
     *
     **/
    public abstract int getPenaliteCumulee(Championnat championnat);

    /**
     *
     * @param championnat
     * @return si la contrainte conserve la viabilité de la solution
     */
    public boolean checkContrainte(Championnat championnat){
        return getPenaliteCumulee(championnat)!=Integer.MAX_VALUE;
    }
}
