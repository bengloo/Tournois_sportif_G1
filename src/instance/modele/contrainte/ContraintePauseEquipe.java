package instance.modele.contrainte;

import operateur.Operateur;
import solution.Championnat;

import java.util.TreeSet;

public class ContraintePauseEquipe extends Contrainte{
    private Integer equipe;
    private TreeSet<Integer> journees;
    private TypeMode mode;
    private Integer max;

    public ContraintePauseEquipe(Integer equipe,TypeMode mode,Integer max) {
        super();
        this.equipe=equipe;
        this.journees = new TreeSet<>();
        this.mode = mode;
        this.max = max;
    }
    public ContraintePauseEquipe(Integer equipe,TypeMode mode,Integer max, Integer penalite) {
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
        return TypeContrainte.PAUSEEQUIPE;
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
        sb.append("ContraintePauseEquipe{");
        sb.append("equipe=").append(equipe).append(", ");
        sb.append("journees=").append(journees).append(", ");
        sb.append("mode=").append(mode).append(", ");
        sb.append("max=").append(max).append(", ");
        sb.append("penalite=").append(penalite).append(", ");
        sb.append("dure=").append(estDure()).append("}");
        return sb.toString();
    }
}
