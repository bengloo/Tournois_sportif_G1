package instance.modele.contrainte;

import operateur.Operateur;
import operateur.OperateurInsertion;
import solution.Solution;
import solution.Rencontre;

import java.util.TreeSet;

/** classe définissant ContraintePauseEquipe (hérite de Contrainte)
 * @author Engloo Benjamin
 * @author Morcq Alexandre
 * @author Sueur Jeanne
 * @author Lux Hugo
 * @version 1.0
 */
public class ContraintePauseEquipe extends Contrainte{
    private Integer equipe;
    private TreeSet<Integer> journees;
    private TypeMode mode;
    private Integer max;

    public ContraintePauseEquipe(Integer equipe,TypeMode mode,Integer max) {
        super();
        this.equipe=equipe;
        this.journees = new TreeSet<>();
        this.mode = mode;
        this.max = max;
    }
    public ContraintePauseEquipe(Integer equipe,TypeMode mode,Integer max, Integer penalite) {
        super(penalite);
        this.equipe=equipe;
        this.journees = new TreeSet<>();
        this.mode = mode;
        this.max = max;
    }

    /**
     * Ajoute une journée à la liste des journées de la contrainte
     * @param id l'ID de la journée à ajouter
     * @return true si l'ajout a réussi, false sinon
     */
    public boolean addJournee(Integer id){
        return journees.add(id);
    }

    @Override
    public TypeContrainte getTypeContrainte() {
        return TypeContrainte.PAUSEEQUIPE;
    }

    //TODO implementer les fonction de calcule de cout en sinspirent de la contrainte de placement, réflechire si on ne peux pas factoriser du code sout des fonction comune aux contraintes
    @Override
    public int getCoutTotal(Solution championnat) {
        //TODO codé corectement en sinspirant de se pseudo code
        //coef=0 //(nombre de pause compté )
        //pour j dans l'interval l'interval de la contrainte
            //lastMode = mode du match de l'equipe de la contrainte au j-1 (null si n'existe pas)
            //curentMode = mode du match de l'equipe de la contrainte au jour j (un truc diferant de null si n'existe pas)
            //si lastMode == currentMode
                //coef++
        //apliqué la fonction objective

        // Nombre de pause comptées
        int valc = 0;
        TypeMode lastMode;

        for (Integer j : this.journees) {

        }



        //le nombre de rencontres jouées par l’équipe de la contrainte selon un mode sur l’ensemble des journées
        /*int valc=0;

        //pour toute les rencontres
        for(Rencontre r:championnat.getRencontres().values()){
            //pour toutes les journees concerné par la contraintes
            valc += parcoursJournees(championnat, r);
        }

        if(valc>this.max) {
            if (estDure()) return Integer.MAX_VALUE;
            return this.penalite *(valc-this.max);
        }*/
        return 0;
    }
    
    @Override
    public int evalDeltaCoef(Solution championnat, Operateur o) {

        int valcDelta=0;
        if(o instanceof OperateurInsertion) {
            //coef=0 //(nombre de pause compté )
                //pour j la journee d'insertion de la rencontre à j+1
                //lastMode = mode du match de l'equipe de la contrainte au j-1 (null si n'existe pas)
                //curentMode = mode du match de l'equipe de la contrainte au jour j (un truc diferant de null si n'existe pas)
                //si lastMode == currentMode
                    //coef++
            //apliqué la fonction objective
        }
        return valcDelta;
    }

    @Override
    public int evalDeltaCout(Solution championnat, Operateur o) {
        Integer valcDelta=evalDeltaCoef(championnat,o);
        return evalDeltaCout(championnat, o, valcDelta);
    }


    @Override
    public int evalDeltaCout(Solution championnat, Operateur o, Integer valcDelta) {
        //TODO tchequer contrainte inerante si oui renvoyer max integer
        if(o instanceof OperateurInsertion){

            if(championnat.getCoefContraintes().get(this)+valcDelta>max){
                if (estDure()) return Integer.MAX_VALUE;
                //au dela du max le cout suit une relation lineaire le deltat cout est donc proportionel
                return this.penalite *(valcDelta);
            }else return 0;
        }
        //TODO d'autre operation implique d'autre cout
        return 0;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ContraintePauseEquipe{");
        sb.append("equipe=").append(equipe).append(", ");
        sb.append("journees=").append(journees).append(", ");
        sb.append("mode=").append(mode).append(", ");
        sb.append("max=").append(max).append(", ");
        sb.append("penalite=").append(penalite).append(", ");
        sb.append("dure=").append(estDure()).append("}");
        return sb.toString();
    }
}
