package instance.modele.contrainte;

import operateur.Operateur;
import solution.Championnat;

public abstract class Contrainte {

    protected Integer penalite;

    public Contrainte(Integer penalite) {
        this.penalite=penalite;
    }
    public Contrainte() {
        this.penalite=Integer.MAX_VALUE;
    }

    public abstract TypeContrainte getTypeContrainte();

    public boolean estDure(){
        return penalite==Integer.MAX_VALUE;
    }
    public boolean estSouple(){
        return penalite!=Integer.MAX_VALUE;
    }

    /**
     * @param championnat la solution
     * @return a chaque fois que la contrainte est verifier cumule les penalité cumulé si la contrainte verifier est dure retourné Max integer sin retourner O
     *
     **/
    public abstract int getPenaliteCumulee(Championnat championnat);

    /**
     * @param championnat la solution
     * @return le delat de penalité pour une operation faite sur le championat , retourne max integer si la contrainte est dure
     *
     **/
    public abstract int evalDeltatPenalite(Championnat championnat, Operateur o);

    /**
     *
     * @param championnat
     * @return si la contrainte conserve la viabilité de la solution
     */
    public boolean checkContrainte(Championnat championnat){
        return getPenaliteCumulee(championnat)!=Integer.MAX_VALUE;
    }
}
