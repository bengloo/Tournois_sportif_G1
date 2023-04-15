package instance.modele.contrainte;

import solution.Rencontre;
import operateur.Operateur;
import solution.Journee;
import solution.Championnat;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeSet;

public class ContrainteRencontres extends Contrainte{
    private TreeSet<Integer> journees;
    private TreeSet<String>  rencontres;
    private Integer min;
    private Integer max;

    public ContrainteRencontres(Integer min,Integer max) {
        super();
        this.journees = new TreeSet<>();
        this.rencontres = new TreeSet<>();
        this.min = max;
        this.max = max;
        this.penalite = Integer.MAX_VALUE;
    }

    public ContrainteRencontres(Integer min,Integer max, Integer penalite) {
        super(penalite);
        this.journees = new TreeSet<>();
        this.rencontres = new TreeSet<>();
        this.min = max;
        this.max = max;
    }

    public boolean addJournee(Integer id){
        return journees.add(id);
    }

    public boolean addRencontre(String id){
        return rencontres.add(id);
    }
    @Override
    public TypeContrainte getTypeContrainte() {
        return TypeContrainte.RENCONTRES;
    }

    @Override
    public int getCoutTotal(Championnat championnat) {
        return 0;
    }

    @Override
    public int evalDeltatCoef(Championnat championnat, Operateur o) {
        return 0;
    }

    @Override
    public int evalDeltatCout(Championnat championnat, Operateur o) {
        return 0;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ContrainteRencontres{");
        sb.append("journees=").append(journees).append(", ");
        sb.append("rencontres=").append(rencontres).append(", ");
        sb.append("min=").append(min).append(", ");
        sb.append("max=").append(max).append(", ");
        sb.append("penalite=").append(penalite).append(", ");
        sb.append("dure=").append(estDure()).append("}");
        return sb.toString();
    }
}
