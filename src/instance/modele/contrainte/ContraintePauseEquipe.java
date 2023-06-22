package instance.modele.contrainte;

import ilog.concert.IloException;
import ilog.concert.IloLinearNumExpr;
import instance.Instance;
import operateur.Operateur;
import operateur.OperateurInsertion;
import solution.Journee;
import solution.Solution;
import solution.Rencontre;
import solveur.SolveurCplex;

import java.util.TreeSet;

/** classe définissant ContraintePauseEquipe (hérite de Contrainte)
 * @author Engloo Benjamin
 * @author Morcq Alexandre
 * @author Sueur Jeanne
 * @author Lux Hugo
 * @version 1.1
 */
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
        return TypeContrainte.PAUSEEQUIPE;
    }

    @Override
    public int getCoutTotal(Solution championnat) {
        int valc=0;
        for (Integer j : this.journees) {
            for (Rencontre r : championnat.getJourneeByID(j).getRencontres().values()) {
                if(r.isConcerne(this.equipe,this.mode)){
                    valc+=traitementModes(championnat,r, this.mode);
                }
            }
        }
        if(valc > this.max) {
            if (estDure()) return Integer.MAX_VALUE;
            return this.penalite * (valc-this.max);
        }
        return 0;
    }

    @Override
    public Object evalDeltaCoef(Solution championnat, Operateur o) {
        //System.out.println("debut");
        int valcDelta=0;
        Journee jprec=championnat.getJourneeByID(o.getJournee().getId()-1);
        Journee jnext=championnat.getJourneeByID(o.getJournee().getId()+1);
        if(o.getRencontre().isConcerne(this.equipe,this.mode)){
            if (jprec != null && this.journees.contains(o.getJournee().getId())) {
                //System.out.println(jprec.toString());
                for (Rencontre r : jprec.getRencontres().values()) {
                    if (r.isConcerne(this.equipe, this.mode)) {
                        valcDelta++;
                    }
                }
            }
            if (jnext != null && this.journees.contains(jnext.getId())) {
                for (Rencontre r : jnext.getRencontres().values()) {
                    if (r.isConcerne(this.equipe, this.mode)) {
                        valcDelta++;
                    }
                }
            }
        }
        return valcDelta;
    }

    private int traitementModes(Solution championnat,Rencontre rEquipe,TypeMode mode) {
        Journee jprec=championnat.getJourneeByID(rEquipe.getJournee().getId()-1);
        if(jprec==null)return 0;
        for(Rencontre rjprec : jprec.getRencontres().values()){
            switch (mode){
                case DOMICILE:
                    if(rjprec.getDomicile().equals(rEquipe.getDomicile())){
                        return 1;
                    }
                case EXTERIEUR:
                    if(rjprec.getExterieur().equals(rEquipe.getExterieur())){
                        return 1;
                    }
            }
        }
        return 0;
    }


    private Journee nextJournee(Solution championnat, Journee journee){
        return championnat.getJourneeByID(journee.getId()+1);
    }

    private Journee precJournee(Solution championnat, Journee journee){
        return championnat.getJourneeByID(journee.getId()-1);
    }


    @Override
    public int evalDeltaCout(Solution championnat, Operateur o) {
        Object valcDelta= (Integer) evalDeltaCoef(championnat,o);
        return evalDeltaCout(championnat, o, valcDelta);
    }


    @Override
    public int evalDeltaCout(Solution championnat, Operateur o, Object valcDelta) {
        if(o instanceof OperateurInsertion){
            if((Integer)championnat.getCoefContraintes().get(this)+(Integer) valcDelta>max){
                if (estDure()) return Integer.MAX_VALUE;
                //au dela du max le cout suit une relation linéaire le delta cout est donc proportionel
                return this.penalite *((Integer)valcDelta);
            }else return 0;
        }
        return 0;
    }


    @Override
    public void initCplexEquation(SolveurCplex sCplex, Instance instance,boolean minimise,boolean minimiseSouple,
                                  boolean dure) {
        if(this.mode==TypeMode.DOMICILE) {

            IloLinearNumExpr expr = null;
            try {
                expr = sCplex.getCplex().linearNumExpr();
                for(int j:this.journees){
                    if(j!=0){
                        expr.addTerm(sCplex.getY()[j-1][this.equipe], 1);
                    }
                }
                if(dure) {
                    if (minimise) expr.addTerm(sCplex.getCDureMax(this), -1);
                    sCplex.getCplex().addLe(expr, this.max,"CDPE1_"+sCplex.getCplex().getNrows());
                }else if(minimiseSouple){
                    addEqSoupleMax(sCplex,max,expr);
                }
            } catch (IloException e) {
                throw new RuntimeException(e);
            }
        }else if(this.mode==TypeMode.EXTERIEUR){
            IloLinearNumExpr expr = null;
            try {
                expr = sCplex.getCplex().linearNumExpr();
                for(int j:this.journees){
                    if(j!=0){
                        expr.addTerm(sCplex.getZ()[j-1][this.equipe], 1);
                    }
                }
                if(dure) {
                    if (minimise) expr.addTerm(sCplex.getCDureMax(this), -1);
                    sCplex.getCplex().addLe(expr, this.max,"CDPE2_"+sCplex.getCplex().getNrows());
                }else if(minimiseSouple){
                    addEqSoupleMax(sCplex,max,expr);
                }
            } catch (IloException e) {
                throw new RuntimeException(e);
            }
        }else{
            IloLinearNumExpr expr = null;
            try {
                expr = sCplex.getCplex().linearNumExpr();
                for(int j:this.journees){
                    if(j!=0){
                        expr.addTerm(sCplex.getZ()[j-1][this.equipe], 1);
                        expr.addTerm(sCplex.getY()[j-1][this.equipe], 1);
                    }
                }
                if(dure) {
                    if (minimise) expr.addTerm(sCplex.getCDureMax(this), -1);
                    sCplex.getCplex().addLe(expr, this.max,"CDPE3_"+sCplex.getCplex().getNrows());
                }else if(minimiseSouple){
                    addEqSoupleMax(sCplex,max,expr);
                }
            } catch (IloException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public boolean isConserne(int e,int j,TypeMode m){
        if(this.equipe!=e)return false;
        if(!this.journees.contains(j))return false;
        switch (m){
            case DOMICILE:
                if(this.mode==TypeMode.EXTERIEUR)return false;
                break;
            case EXTERIEUR:
                if(this.mode==TypeMode.DOMICILE)return false;
                break;
        }
        return true;
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
