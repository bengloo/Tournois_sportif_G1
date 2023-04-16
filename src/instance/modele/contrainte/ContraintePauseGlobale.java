package instance.modele.contrainte;

import solution.Equipe;
import operateur.Operateur;
import solution.Championnat;
import solution.Journee;

import java.util.HashMap;
import java.util.Map;
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
    public int evalDeltatCout(Championnat championnat, Operateur o, Integer deltaCoef) {
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
