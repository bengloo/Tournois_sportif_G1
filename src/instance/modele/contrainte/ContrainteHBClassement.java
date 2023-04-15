package instance.modele.contrainte;

import solution.Equipe;
import operateur.Operateur;
import solution.Championnat;
import solution.Journee;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

public class ContrainteHBClassement  extends Contrainte{
    private Integer equipe;
    private TreeSet<Integer> journees;
    private TreeSet<Integer>  equipesAdverses;
    private TypeMode mode;
    private Integer max;



    public ContrainteHBClassement(Integer equipe,TypeMode mode,Integer max) {
        super();
        this.equipe=equipe;
        this.journees = new TreeSet<>();
        this.equipesAdverses = new TreeSet<>();
        this.mode = mode;
        this.max = max;
    }
    public ContrainteHBClassement(Integer equipe,TypeMode mode,Integer max, Integer penalite) {
        super(penalite);
        this.equipe=equipe;
        this.journees = new TreeSet<>();
        this.equipesAdverses = new TreeSet<>();
        this.mode = mode;
        this.max = max;
    }

    public boolean addJournee(Integer id){
        return journees.add(id);
    }
    public boolean addEquipeAdverse(Integer id){
        return equipesAdverses.add(id);
    }
    @Override
    public TypeContrainte getTypeContrainte() {
        return TypeContrainte.HBCLASSEMENT;
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
        sb.append("ContrainteHBClassement{");
        sb.append("equipe=").append(equipe).append(", ");
        sb.append("journees=").append(journees).append(", ");
        sb.append("equipesAdverses=").append(equipesAdverses).append(", ");
        sb.append("mode=").append(mode).append(", ");
        sb.append("max=").append(max).append(", ");
        sb.append("penalite=").append(penalite).append(", ");
        sb.append("dure=").append(estDure()).append("}");
        return sb.toString();
    }
}
