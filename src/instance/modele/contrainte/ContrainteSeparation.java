package instance.modele.contrainte;

import operateur.Operateur;
import solution.Solution;

import java.util.TreeSet;

public class ContrainteSeparation extends Contrainte{

    private TreeSet<Integer> equipes;

    private Integer min;

    public ContrainteSeparation(Integer min, Integer penalite) {

        super(penalite);
        this.equipes = new TreeSet<>();
        this.min = min;

    }

    public boolean addEquipe(Integer id){
        return equipes.add(id);
    }


    @Override
    public TypeContrainte getTypeContrainte() {
        return TypeContrainte.SEPARATION;
    }

    @Override
    public int getCoutTotal(Solution championnat) {
        return 0;
    }

    @Override
    public int evalDeltatCoef(Solution championnat, Operateur o) {
        return 0;
    }

    //TODO implementer les fonction de calcule de cout en sinspirent de la contrainte de placement, réflechire si on ne peux pas factoriser du code sout des fonction comune aux contraintes
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
        sb.append("ContrainteSeparation{");
        sb.append("equipes=").append(equipes).append(", ");
        sb.append("min=").append(min).append(", ");
        sb.append("penalite=").append(penalite).append(", ");
        sb.append("dure=").append(estDure()).append("}");
        return sb.toString();
    }
}
