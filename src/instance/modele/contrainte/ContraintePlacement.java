package instance.modele.contrainte;

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
    public int getPenaliteCumulee(Championnat championnat) {
        //le nombre de rencontres jouées par l’équipe e selon le mode mode sur l’ensemble des journées
        int valc=0;

        for(Rencontre r:championnat.getRencontres().values()){
            for(Integer jID:journees) {
                switch (mode) {
                    case DOMICILE:
                        if (r.getDomicile().equals(equipe)&&championnat.getJournees().get(jID).getRencontres().containsKey(r)) {
                            valc++;
                        }
                        break;
                    case EXTERIEUR:
                        if (r.getExterieur().equals(equipe)&&championnat.getJournees().get(jID).getRencontres().containsKey(r)){
                            valc++;
                        }
                        break;
                    case INDEFINI:
                        if ((r.getDomicile().equals(equipe) || r.getDomicile().equals(equipe))&&championnat.getJournees().get(jID).getRencontres().containsKey(r)){
                            valc++;
                        }
                    default:
                        //TODO interup process error
                }
            }
        }
        if(valc>this.max) {
            if (estDure()) return Integer.MAX_VALUE;
            return this.penalite *(valc-this.max);
        }
        return 0;

    }

    @Override
    public int evalDeltatPenalite(Championnat championnat, Operateur o) {
        //TODO definir valc dans championat pour pouvoir s'y referer
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
