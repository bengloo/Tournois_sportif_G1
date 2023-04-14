package instance.modele.contrainte;

import instance.modele.Equipe;
import solution.Championnat;
import solution.Journee;

import java.util.HashMap;
import java.util.Map;

public class ContraintePauseEquipe extends Contrainte{
    private Equipe equipe;
    private Map<Integer, Journee> journees;
    private TypeMode mode;
    private Integer max;
    private Integer penalite;

    public ContraintePauseEquipe(Equipe equipe,TypeMode mode,Integer max) {
        super(true);
        this.equipe=equipe;
        this.journees = new HashMap<>();
        this.mode = mode;
        this.max = max;
        this.penalite = -1;
    }
    public ContraintePauseEquipe(Equipe equipe,TypeMode mode,Integer max, Integer penalite) {
        super(false);
        this.equipe=equipe;
        this.journees = new HashMap<>();
        this.mode = mode;
        this.max = max;
        this.penalite = penalite;
    }

    public boolean addJournee(Journee journee){
        if(journee==null) return false;
        this.journees.put(journee.getId(),journee);
        return true;
    }

    @Override
    public int getPenaliteCumulee(Championnat championnat) {
        return 0;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ContraintePauseEquipe{");
        sb.append("equipe=").append(equipe).append(", ");
        sb.append("journees=").append(journees.values()).append(", ");
        sb.append("mode=").append(mode).append(", ");
        sb.append("max=").append(max).append(", ");
        sb.append("penalite=").append(penalite).append(", ");
        sb.append("dure=").append(dure).append("}");
        return sb.toString();
    }
}
