package instance.modele.contrainte;

import operateur.Operateur;
import operateur.OperateurInsertion;
import solution.Rencontre;
import solution.Solution;

import java.util.TreeSet;

public class ContrainteEquite extends Contrainte {

    private TreeSet<Integer> equipes;
    private TreeSet<Integer> journees;
    private Integer max;

    public ContrainteEquite(Integer max, Integer penalite) {
        super(penalite);
        this.equipes = new TreeSet<>();
        this.journees = new TreeSet<>();
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
        return TypeContrainte.EQUITE;
    }

    //TODO implementer les fonction de calcule de cout en sinspirent de la contrainte de placement, réflechire si on ne peux pas factoriser du code sout des fonction comune aux contraintes
    @Override
    public int getCoutTotal(Solution championnat) {
        //TODO tchequer contrainte inerante si oui renvoyer max integer

        //le nombre de rencontres jouées par l’équipe de la contrainte selon un mode sur l’ensemble des journées
        int valc=0;

        //pour toutes les rencontres
        for(Rencontre r:championnat.getRencontres().values()){
            //pour toutes les équpes et journees concernées par la contrainte
            valc += Math.max(0, parcoursJournees(championnat, r) - this.max);
        }

        return this.penalite * valc;
    }

    @Override
    public int evalDeltatCoef(Solution championnat, Operateur o) {
        int valcDelta=0;
        if(o instanceof OperateurInsertion) {
            Rencontre r = o.getRencontre();
            valcDelta = parcoursJournees(championnat, r);
        }
        return valcDelta;
    }


    private int parcoursJournees(Solution championnat, Rencontre r) { //Factorisation du code
        int maxDomicile=0;
        int minDomicile=Integer.MAX_VALUE;
        int flag;

        for (Integer eID : this.equipes) {
            flag=0;
            for (Integer jID : this.journees) {
                //si notre équipe courante joue à domicile sur cette rencontre et que la journee courante contient la rencontre
                if (r.getDomicile().equals(eID) && championnat.getJournees().get(jID).getRencontres().containsKey(r)) {
                    flag++;
                }
            }
            if (flag > maxDomicile) maxDomicile = flag;
            if (flag < minDomicile) minDomicile = flag;
        }
        return (maxDomicile-minDomicile);
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
        sb.append("ContrainteEquite{");
        sb.append("equipes=").append(equipes).append(", ");
        sb.append("journees=").append(journees).append(", ");
        sb.append("max=").append(max).append(", ");
        sb.append("penalite=").append(penalite).append(", ");
        sb.append("dure=").append(estDure()).append("}");
        return sb.toString();
    }
}
