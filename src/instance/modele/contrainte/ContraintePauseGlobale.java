package instance.modele.contrainte;

import operateur.OperateurInsertion;
import operateur.Operateur;
import solution.Solution;
import solution.Rencontre;

import java.util.TreeSet;

public class ContraintePauseGlobale extends Contrainte{

    private TreeSet<Integer> journees;
    private TreeSet<Integer>  equipes;
    private Integer max;

    public ContraintePauseGlobale(Integer max) {
        super();
        this.journees = new TreeSet<>();
        this.equipes = new TreeSet<>();
        this.max = max;
    }

    public ContraintePauseGlobale(Integer max, Integer penalite) {
        super(penalite);
        this.journees = new TreeSet<>();
        this.equipes = new TreeSet<>();
        this.max = max;
    }

    public boolean addEquipe(Integer id){
        return equipes.add(id);
    }

    public boolean addJournee(Integer id){
        return journees.add(id);
    }

    @Override
    public TypeContrainte getTypeContrainte() {
        return TypeContrainte.PAUSEGLOBALE;
    }

    //TODO implementer les fonction de calcule de cout en sinspirent de la contrainte de placement, réflechire si on ne peux pas factoriser du code sout des fonction comune aux contraintes
    @Override
    public int getCoutTotal(Solution championnat) {
        //TODO tchequer contrainte inerante si oui renvoyer max integer

        //le nombre de rencontres jouées par l’équipe de la contrainte selon un mode sur l’ensemble des journées
        int valc=0;

        //pour toutes les journees concernées par la contrainte
        valc = parcoursJournees(championnat);

        if(valc>this.max) {
            if (estDure()) return Integer.MAX_VALUE;
            return this.penalite *(valc-this.max);
        }
        return 0;
    }

    @Override
    public int evalDeltatCoef(Solution championnat, Operateur o) {
        int valcDelta=0;
        if(o instanceof OperateurInsertion) {
            Rencontre r = o.getRencontre();
            valcDelta = parcoursJournees(championnat);
        }
        return valcDelta;
    }


    private int parcoursJournees(Solution championnat) { //Factorisation du code
        int valcDelta=0;
        for (Integer eID : this.equipes) {
            for (Integer jID : this.journees) {
                //si la journee courante contient la rencontre
                if (championnat.getEquipes().equals(eID) && championnat.getJournees().equals(jID)) {
                    valcDelta++;
                }
            }
        }
        return valcDelta;
    }

    @Override
    public int evalDeltatCout(Solution championnat, Operateur o) {
        Integer valcDelta=evalDeltatCoef(championnat,o);
        return evalDeltatCout(championnat, o, valcDelta);
    }

    @Override
    public int evalDeltatCout(Solution championnat, Operateur o, Integer valcDelta) {
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
        sb.append("ContraintePauseGlobale{");
        sb.append("journees=").append(journees).append(", ");
        sb.append("equipes=").append(equipes).append(", ");
        sb.append("max=").append(max).append(", ");
        sb.append("penalite=").append(penalite).append(", ");
        sb.append("dure=").append(estDure()).append("}");
        return sb.toString();
    }
}
