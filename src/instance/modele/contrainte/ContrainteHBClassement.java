package instance.modele.contrainte;

import instance.modele.Equipe;
import operateur.Operateur;
import solution.Championnat;
import solution.Journee;

import java.util.HashMap;
import java.util.Map;

public class ContrainteHBClassement  extends Contrainte{
    private Equipe equipe;
    private Map<Integer, Journee> journees;
    private Map<Integer, Equipe> equipesAdverses;
    private TypeMode mode;
    private Integer max;



    public ContrainteHBClassement(Equipe equipe,TypeMode mode,Integer max) {
        super();
        this.equipe=equipe;
        this.journees = new HashMap<>();
        this.equipesAdverses = new HashMap<>();
        this.mode = mode;
        this.max = max;
    }
    public ContrainteHBClassement(Equipe equipe,TypeMode mode,Integer max, Integer penalite) {
        super(penalite);
        this.equipe=equipe;
        this.journees = new HashMap<>();
        this.equipesAdverses = new HashMap<>();
        this.mode = mode;
        this.max = max;
    }

    public boolean addEquipeAdverse(Equipe equipe){
        if(equipe==null) return false;
        this.equipesAdverses.put(equipe.getId(),equipe);
        return true;
    }
    public boolean addJournee(Journee journee){
        if(journee==null) return false;
        this.journees.put(journee.getId(),journee);
        return true;
    }

    @Override
    public TypeContrainte getTypeContrainte() {
        return TypeContrainte.HBCLASSEMENT;
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
        sb.append("ContrainteHBClassement{");
        sb.append("equipe=").append(equipe).append(", ");
        sb.append("journees=").append(journees.values()).append(", ");
        sb.append("equipesAdverses=").append(equipesAdverses.values()).append(", ");
        sb.append("mode=").append(mode).append(", ");
        sb.append("max=").append(max).append(", ");
        sb.append("penalite=").append(penalite).append(", ");
        sb.append("dure=").append(estDure()).append("}");
        return sb.toString();
    }
}
