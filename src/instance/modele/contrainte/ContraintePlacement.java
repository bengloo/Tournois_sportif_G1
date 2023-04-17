package instance.modele.contrainte;

import operateur.OperateurInsertion;
import solution.Equipe;
import operateur.Operateur;
import solution.Championnat;
import solution.Journee;
import solution.Rencontre;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

public class ContraintePlacement extends Contrainte{
    private Integer equipe;
    private TreeSet<Integer> journees;
    private TypeMode mode;
    private Integer max;

    public ContraintePlacement(Integer equipe,TypeMode mode,Integer max) {
        super();
        this.equipe=equipe;
        this.journees = new TreeSet<>();
        this.mode = mode;
        this.max = max;
    }

    public ContraintePlacement(Integer equipe,TypeMode mode,Integer max, Integer penalite) {
        super(penalite);
        this.equipe=equipe;
        this.journees = new TreeSet<>();
        this.mode = mode;
        this.max = max;
    }
    public boolean addJournee(Integer id){
        return journees.add(id);
    }

    @Override
    public TypeContrainte getTypeContrainte() {
        return TypeContrainte.PLACEMENT;
    }


    @Override
    public int getCoutTotal(Championnat championnat) {
        //TODO tchequer contrainte inerante si oui renvoyer max integer

        //le nombre de rencontres jouées par l’équipe e selon le mode mode sur l’ensemble des journées
        int valc=0;

        //pour toute les rencontres
        for(Rencontre r:championnat.getRencontres().values()){
            //pour toutes les journees concerné par la contraintes
            valc += parcoursJournees(championnat, r);
        }
        if(valc>this.max) {
            if (estDure()) return Integer.MAX_VALUE;
            return this.penalite *(valc-this.max);
        }
        return 0;

    }

    @Override
    public int evalDeltatCoef(Championnat championnat, Operateur o) {
        int valcDelta=0;
        if(o instanceof OperateurInsertion) {
            Rencontre r = o.getRencontre();
            for (Integer jID : journees) {
                switch (mode) {
                    case DOMICILE:
                        //si l'equipe concerné par la contrainte est celle de la rencontre et  la journee courante contient la rencontre
                        if (r.getDomicile().equals(equipe) && championnat.getJournees().get(jID).getRencontres().containsKey(r)) {
                            valcDelta++;
                        }
                        break;
                    case EXTERIEUR:
                        if (r.getExterieur().equals(equipe) && championnat.getJournees().get(jID).getRencontres().containsKey(r)) {
                            valcDelta++;
                        }
                        break;
                    case INDEFINI:
                        if ((r.getDomicile().equals(equipe) || r.getDomicile().equals(equipe)) && championnat.getJournees().get(jID).getRencontres().containsKey(r)) {
                            valcDelta++;
                        }
                    default:
                        //TODO interup process error
                }
            }
        }
        return valcDelta;
    }

    @Override
    public int evalDeltatCout(Championnat championnat, Operateur o) {
        Integer valcDelta=evalDeltatCoef(championnat,o);

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
    public int evalDeltatCout(Championnat championnat, Operateur o,Integer valcDelta) {
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
        sb.append("ContraintePlacement{");
        sb.append("equipe=").append(equipe).append(", ");
        sb.append("journees=").append(journees).append(", ");
        sb.append("mode=").append(mode).append(", ");
        sb.append("max=").append(max).append(", ");
        sb.append("penalite=").append(penalite).append(", ");
        sb.append("dure=").append(estDure()).append("}");
        return sb.toString();
    }
}
