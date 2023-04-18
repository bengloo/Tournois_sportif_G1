package instance.modele.contrainte;

import operateur.Operateur;
import solution.Solution;

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

    //TODO implementer les fonction de calcule de cout en sinspirent de la contrainte de placement, réflechire si on ne peux pas factoriser du code sout des fonction comune aux contraintes
    @Override
    public int getCoutTotal(Solution championnat) {
        return 0;
    }

    @Override
    public int evalDeltatCoef(Solution championnat, Operateur o) {
        return 0;
    }

    @Override
    public int evalDeltatCout(Solution championnat, Operateur o) {
        return 0;
    }

    @Override
    public int evalDeltatCout(Solution championnat, Operateur o, Integer deltaCoef) {
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
