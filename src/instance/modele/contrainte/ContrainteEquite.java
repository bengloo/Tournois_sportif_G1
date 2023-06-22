package instance.modele.contrainte;

import instance.Instance;
import operateur.Operateur;
import operateur.OperateurEchange;
import operateur.OperateurInsertion;
import solution.Rencontre;
import solution.Solution;
import solveur.SolveurCplex;

import java.util.*;

/** classe définissant ContrainteEquite (hérite de Contrainte)
 * @author Engloo Benjamin
 * @author Morcq Alexandre
 * @author Sueur Jeanne
 * @author Lux Hugo
 * @version 1.0
 */
public class ContrainteEquite extends Contrainte {
    private TreeSet<Integer> equipes;
    private TreeSet<Integer> journees;
    private Integer max;

    public ContrainteEquite(Integer max, Integer penalite) {
        super(penalite);
        this.equipes = new TreeSet<>();
        this.journees = new TreeSet<>();
        this.max = max;
    }

    /**
     * Ajoute une équipe à la liste des équipes de la contrainte
     * @param id l'ID de l'équipe à ajouter
     * @return true si l'ajout a réussi, false sinon
     */
    public boolean addEquipe(Integer id){
        return equipes.add(id);
    }

    /**
     * Ajoute une journée à la liste des journées de la contrainte
     * @param id l'ID de la journée à ajouter
     * @return true si l'ajout a réussi, false sinon
     */
    public boolean addJournee(Integer id){
        return journees.add(id);
    }


    @Override
    public TypeContrainte getTypeContrainte() {
        return TypeContrainte.EQUITE;
    }

    @Override
    public int getCoutTotal(Solution championnat) {
        int nbViolation = 0;

        for(int e1:this.equipes) {
            for(int e2:this.equipes) {
                if(e2>e1) {
                    int nb = this.computeMaxDiffNbRencontresDomiciles(championnat, e1, e2);
                    if (nb > this.max) {
                        nbViolation += nb - this.max;
                    }
                }
            }
        }
        return nbViolation * this.penalite;
    }

    @Override
    public Object evalDeltaCoef(Solution championnat, Operateur o) {
        HashMap<Integer,Integer> valcDelta =new HashMap<>();

        if(o instanceof OperateurInsertion) {
            if(this.journees.contains(o.getJournee().getId())&&this.equipes.contains(o.getRencontre().getDomicile()
                    .getId())){
                valcDelta.put(o.getRencontre().getDomicile().getId(),1);
            }
        }else if(o instanceof OperateurEchange) {
            if(this.journees.contains(o.getJournee())&&!this.journees.contains(((OperateurEchange) o).getJournee2())){
                if(this.equipes.contains(o.getRencontre().getDomicile().getId())){
                    valcDelta.put(o.getRencontre().getDomicile().getId(),-1);
                }
                if(this.equipes.contains(((OperateurEchange) o).getRencontre2().getDomicile().getId())) {
                    valcDelta.put(((OperateurEchange) o).getRencontre2().getDomicile().getId(), 1);
                }
            }
            if(!this.journees.contains(o.getJournee())&&this.journees.contains(((OperateurEchange) o).getJournee2())){
                if(this.equipes.contains(o.getRencontre().getDomicile().getId())){
                    valcDelta.put(o.getRencontre().getDomicile().getId(),1);
                }
                if(this.equipes.contains(((OperateurEchange) o).getRencontre2().getDomicile().getId())) {
                    valcDelta.put(((OperateurEchange) o).getRencontre2().getDomicile().getId(), -1);
                }
            }

        }
        return  valcDelta;
    }

    private int computeMaxDiffNbRencontresDomiciles(Solution sol, int e1, int e2) {
        int max = 0;
        int nbD1 = 0;
        int nbD2 = 0;
        int j1 = 0;
        List<Integer> jours = new ArrayList();

        int j2;
        for(Iterator var9 = this.journees.iterator(); var9.hasNext(); j1 = j2 + 1) {
            j2 = (Integer)var9.next();
            for(int j = j1; j <= j2; ++j) {
                jours.add(j);
            }
            nbD1=0;
            nbD2=0;
            for(int jc:jours){
                for(Rencontre r:sol.getJourneeByID(jc).getRencontres().values()){
                    if(r.getDomicile().getId()==e1)nbD1+=1;
                    if(r.getDomicile().getId()==e2)nbD2+=1;
                }
            }
            if (Math.abs(nbD1 - nbD2) > max) {
                max = Math.abs(nbD1 - nbD2);
            }
        }

        return max;
    }

    public TreeSet<Integer> getEquipes() {
        return equipes;
    }

    @Override
    public int evalDeltaCout(Solution championnat, Operateur o) {
        Object valcDelta=evalDeltaCoef(championnat,o);
        return evalDeltaCout(championnat, o, valcDelta);
    }

    @Override
    public int evalDeltaCout(Solution championnat, Operateur o, Object valcDelta) {
        int deltaCout=0;
        for(Integer e1:((HashMap<Integer,Integer>)valcDelta).keySet()){
            for(Integer e2:this.equipes){
                if(e1!=e2){
                    deltaCout += deltatDomicile(championnat.getCoefEquite(this, e1)+this.getDelatCoef(valcDelta,e1),
                            championnat.getCoefEquite(this, e2)+this.getDelatCoef(valcDelta,e2));
                    deltaCout -= deltatDomicile(championnat.getCoefEquite(this, e1), championnat.getCoefEquite(
                            this, e2));
                }
            }
        }
        return deltaCout;
    }

    @Override
    public void initCplexEquation(SolveurCplex sCplex, Instance instance,boolean minimiseDure,boolean minimiseSouple,
                                  boolean dure) {
        if(!dure){
            //TODO cout
        }

    }


    private int deltatDomicile(int coefe1,int coefe2){
        return Math.max(0,Math.abs(coefe1-coefe2)-this.max);
    }



    private int getDelatCoef(Object valcDelta,Integer equipe){
        Integer val= ((HashMap<Integer,Integer>)valcDelta).get(equipe);
        if(val==null)return 0;
        return val;
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ContrainteEquite{");
        sb.append("equipes=").append(equipes).append(", ");
        sb.append("journees=").append(journees).append(", ");
        sb.append("max=").append(max).append(", ");
        sb.append("penalite=").append(penalite).append(", ");
        sb.append("dure=").append(estDure()).append("}");
        return sb.toString();
    }
}
