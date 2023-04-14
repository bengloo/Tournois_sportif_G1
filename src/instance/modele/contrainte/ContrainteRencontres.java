package instance.modele.contrainte;

import instance.modele.Rencontre;
import operateur.Operateur;
import solution.Journee;
import solution.Championnat;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class ContrainteRencontres extends Contrainte{
    private Map<Integer, Journee> journees;
    private LinkedList<Rencontre> rencontres;
    private Integer min;
    private Integer max;

    public ContrainteRencontres(Integer min,Integer max) {
        super();
        this.journees = new HashMap<>();
        this.rencontres = new LinkedList<>();
        this.min = max;
        this.max = max;
        this.penalite = Integer.MAX_VALUE;
    }

    public ContrainteRencontres(Integer min,Integer max, Integer penalite) {
        super(penalite);
        this.journees = new HashMap<>();
        this.rencontres = new LinkedList<>();
        this.min = max;
        this.max = max;
    }

    public boolean addJournee(Journee journee){
        if(journee==null) return false;
        this.journees.put(journee.getId(),journee);
        return true;
    }

    public boolean addRencontre(Rencontre rencontre){
        if(rencontre == null) return false;
        this.rencontres.add(rencontre);
        return true;
    }


    @Override
    public TypeContrainte getTypeContrainte() {
        return TypeContrainte.RENCONTRES;
    }

    @Override
    public int getPenaliteCumulee(Championnat championnat) {
        return 0;
    }

    @Override
    public int evalDeltatPenalite(Championnat championnat, Operateur o) {
        return 0;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ContrainteRencontres{");
        sb.append("journees=").append(journees.values()).append(", ");
        sb.append("rencontres=").append(rencontres).append(", ");
        sb.append("min=").append(min).append(", ");
        sb.append("max=").append(max).append(", ");
        sb.append("penalite=").append(penalite).append(", ");
        sb.append("dure=").append(estDure()).append("}");
        return sb.toString();
    }
}
