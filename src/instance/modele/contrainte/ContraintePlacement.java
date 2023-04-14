package instance.modele.contrainte;

import instance.modele.Equipe;
import instance.modele.Rencontre;
import operateur.Operateur;
import solution.Championnat;
import solution.Journee;

import java.util.HashMap;
import java.util.Map;

public class ContraintePlacement extends Contrainte{
    private Equipe equipe;
    private Map<Integer, Journee> journees;
    private TypeMode mode;
    private Integer max;

    public ContraintePlacement(Equipe equipe,TypeMode mode,Integer max) {
        super();
        this.equipe=equipe;
        this.journees = new HashMap<>();
        this.mode = mode;
        this.max = max;
    }

    public ContraintePlacement(Equipe equipe,TypeMode mode,Integer max, Integer penalite) {
        super(penalite);
        this.equipe=equipe;
        this.journees = new HashMap<>();
        this.mode = mode;
        this.max = max;
    }

    public boolean addJournee(Journee journee){
        if(journee==null) return false;
        this.journees.put(journee.getId(),journee);
        return true;
    }


    @Override
    public TypeContrainte getTypeContrainte() {
        return TypeContrainte.PLACEMENT;
    }

    @Override
    public int getPenaliteCumulee(Championnat championnat) {
        //le nombre de rencontres jouées par l’équipe e selon le mode mode sur l’ensemble des journées
        int valc=0;
        //TODO
        if(valc>this.max) {
            if (estDure()) return Integer.MAX_VALUE;
            return this.penalite *(valc-this.max);
        }
        return 0;

    }

    @Override
    public int evalDeltatPenalite(Championnat championnat, Operateur o) {
        return 0;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ContraintePlacement{");
        sb.append("equipe=").append(equipe).append(", ");
        sb.append("journees=").append(journees.values()).append(", ");
        sb.append("mode=").append(mode).append(", ");
        sb.append("max=").append(max).append(", ");
        sb.append("penalite=").append(penalite).append(", ");
        sb.append("dure=").append(estDure()).append("}");
        return sb.toString();
    }
}
