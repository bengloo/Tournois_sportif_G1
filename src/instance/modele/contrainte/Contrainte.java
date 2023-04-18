package instance.modele.contrainte;

import operateur.Operateur;
import solution.Solution;

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
    public abstract int getCoutTotal(Solution championnat);


    /**
     * @param championnat la solution
     * @return le delat du coef de la fonction objective de la contrainte
     *
     **/
    //TODO deltat sans t
    public abstract int evalDeltatCoef(Solution championnat, Operateur o);

    /**
     * @param championnat la solution
     * @return le delat de penalité pour une operation faite sur le championat , retourne max integer si la contrainte est dure
     *
     **/
    public abstract int evalDeltatCout(Solution championnat, Operateur o);

    public abstract int evalDeltatCout(Solution championnat, Operateur o, Integer deltaCoef);

    /**
     *
     * @param championnat
     * @return si la contrainte conserve la viabilité de la solution
     */
    public boolean checkContrainte(Solution championnat){
        return estDure()&&getCoutTotal(championnat)!=Integer.MAX_VALUE;
    }
}
