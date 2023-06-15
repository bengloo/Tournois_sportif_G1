package instance.modele.contrainte;

import ilog.concert.IloException;
import ilog.concert.IloLinearNumExpr;
import instance.Instance;
import operateur.OperateurEchange;
import operateur.OperateurInsertion;
import operateur.Operateur;
import solution.Solution;
import solution.Rencontre;
import solveur.SolveurCplex;

import java.util.Objects;
import java.util.TreeSet;

import static instance.modele.contrainte.TypeMode.*;

/** classe définissant ContrainteHBClassement (hérite de Contrainte)
 * @author Engloo Benjamin
 * @author Morcq Alexandre
 * @author Sueur Jeanne
 * @author Lux Hugo
 * @version 1.1
 */
public class ContrainteHBClassement extends Contrainte{
    private Integer equipe;
    private TreeSet<Integer> journees;
    private TreeSet<Integer> equipesAdverses;
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

    /**
     * Ajoute une journée à la liste des journées de la contrainte
     * @param id l'ID de la journée à ajouter
     * @return true si l'ajout a réussi, false sinon
     */
    public boolean addJournee(Integer id){
        return journees.add(id);
    }

    /**
     * Ajoute une équipe à la liste des équipes adverses de la contrainte
     * @param id l'ID de l'équipe à ajouter
     * @return true si l'ajout a réussi, false sinon
     */
    public boolean addEquipeAdverse(Integer id){
        return equipesAdverses.add(id);
    }

    @Override
    public TypeContrainte getTypeContrainte() {
        return TypeContrainte.HBCLASSEMENT;
    }

    //TODO implementer les fonction de calcule de cout en sinspirent de la contrainte de placement, réflechire si on ne peux pas factoriser du code sout des fonction comune aux contraintes
    @Override
    public int getCoutTotal(Solution championnat) {
        //le nombre de rencontres jouées par l’équipe de la contrainte selon un mode sur l’ensemble des journées
        int valc=0;
        //pour toute les rencontres
        for(Rencontre r:championnat.getRencontres().values()){
            // à chaque équipe adverse de la liste rencontrée
            if (this.equipesAdverses.contains(r.getDomicile().getId()) || this.equipesAdverses.contains(r.getExterieur().getId())) {
                valc += parcoursJournees(championnat, r);
            }
        }

        if(valc>this.max) {
            if (estDure()) return Integer.MAX_VALUE;
            return this.penalite *(valc-this.max);
        }
        return 0;
    }

    @Override
    public Object evalDeltaCoef(Solution championnat, Operateur o) {
        if(o instanceof OperateurInsertion) {
            int valcDelta=0;
            switch(this.mode) {
                case DOMICILE:
                    if(this.journees.contains(o.getJournee().getId()) && this.equipesAdverses.contains(o.getRencontre().getExterieur().getId()) && o.getRencontre().isConcerne(championnat.getEquipeByID(equipe),mode)) {
                        valcDelta = 1;
                    }
                    break;
                case EXTERIEUR:
                    if(this.journees.contains(o.getJournee().getId()) && this.equipesAdverses.contains(o.getRencontre().getDomicile().getId()) && o.getRencontre().isConcerne(championnat.getEquipeByID(equipe),mode)) {
                        valcDelta = 1;
                    }
                    break;
                case INDEFINI:
                    if(this.journees.contains(o.getJournee().getId()) && (this.equipesAdverses.contains(o.getRencontre().getDomicile().getId()) || this.equipesAdverses.contains(o.getRencontre().getExterieur().getId())) && o.getRencontre().isConcerne(championnat.getEquipeByID(equipe),mode)) {
                        valcDelta = 1;
                    }
                    break;
            }
            return valcDelta;
        }else if(o instanceof OperateurEchange){
            //la premierre rencontre fait partie des journee de la contrainte?
            boolean r1inJC=this.journees.contains(o.getJournee().getId());
            //la seconde rencontre fait partie des journee de la contrainte?
            boolean r2inJc=this.journees.contains(((OperateurEchange) o).getJournee2().getId());
            //les equipes de la premierre rencontre verififi la contraine en terme d'équipe
            boolean r1inEC=false;
            //les equipes de la seconde rencontre verififi la contraine en terme d'équipe
            boolean r2inEC=false;

            if(this.mode==DOMICILE||this.mode==INDEFINI){
                if(o.getRencontre().getDomicile().getId()==this.equipe && this.equipesAdverses.contains(o.getRencontre().getExterieur())){r1inEC=true;}
                if(((OperateurEchange) o).getRencontre2().getDomicile().getId()==this.equipe && this.equipesAdverses.contains(((OperateurEchange) o).getRencontre2().getExterieur())){r2inEC=true;}
            }
            if(this.mode==EXTERIEUR||this.mode==INDEFINI){
                if(o.getRencontre().getExterieur().getId()==this.equipe && this.equipesAdverses.contains(o.getRencontre().getDomicile())){r1inEC=true;}
                if(((OperateurEchange) o).getRencontre2().getExterieur().getId()==this.equipe && this.equipesAdverses.contains(((OperateurEchange) o).getRencontre2().getDomicile())){r2inEC=true;}
            }

            if((!r1inJC && r2inJc && r1inEC && !r2inEC)||(r1inJC && !r2inJc && !r1inEC && r2inEC)){
                //si l'echange de rencontre ajoute une rencontre a la contrainte :cf table de verité drive
                return 1;
            } else if((r1inJC && !r2inJc && r1inEC && !r2inEC)||(!r1inJC && r2inJc && !r1inEC && r2inEC)) {
                //si l'echange de rencontre sort une rencontre de la contrainte:cf table de verité drive
                return -1;
            }
            return 0;
        }
        return Integer.MAX_VALUE;

    }

    /**
     * Parcourt les journées de la contrainte pour incrémenter un compteur à chaque fois que son équipe fait partie de la rencontre
     * @param championnat la solution
     * @param r la rencontre concernée
     * @return le nombre entier du compteur
     */
    private int parcoursJournees(Solution championnat, Rencontre r) { //Factorisation du code
        int valcDelta=0;
        for (Integer jID : this.journees) {
            /*if(r.isConcerne(championnat.getEquipes().get(this.equipe), this.mode) && championnat.isRJPresent(jID,r));
            valcDelta++;*/
            switch(this.mode) {
                case DOMICILE:
                    if(r.isConcerne(championnat.getEquipes().get(this.equipe), this.mode) && this.equipesAdverses.contains(r.getExterieur().getId()) && championnat.isRJPresent(jID,r)) {
                        valcDelta++;
                    }
                    break;
                case EXTERIEUR:
                    if(r.isConcerne(championnat.getEquipes().get(this.equipe), this.mode) && this.equipesAdverses.contains(r.getDomicile().getId()) && championnat.isRJPresent(jID,r)) {
                        valcDelta++;
                    }
                    break;
                case INDEFINI:
                    if(r.isConcerne(championnat.getEquipes().get(this.equipe), this.mode) && (this.equipesAdverses.contains(r.getDomicile().getId()) || this.equipesAdverses.contains(r.getExterieur().getId())) && championnat.isRJPresent(jID,r)) {
                        valcDelta++;
                    }
                    break;
            }
        }
        return valcDelta;
    }

    @Override
    public int evalDeltaCout(Solution championnat, Operateur o) {
        Object valcDelta=evalDeltaCoef(championnat,o);
        return evalDeltaCout(championnat, o, valcDelta);
    }

    @Override
    public int evalDeltaCout(Solution championnat, Operateur o, Object valcDelta) {
        if(o instanceof OperateurInsertion){
            if((Integer)championnat.getCoefContraintes().get(this)+ (Integer) valcDelta>max){
                if (estDure()) return Integer.MAX_VALUE;
                //au dela du max le cout suit une relation lineaire le deltat cout est donc proportionel
                return this.penalite *((Integer)valcDelta);
            }else return 0;
        }
        //TODO d'autre operation implique d'autre cout
        return 0;
    }

    /**
     * @param sCplex
     */
    @Override
    public void initCplexEquationDure(SolveurCplex sCplex, Instance instance,boolean minimise) {
        if(this.mode==TypeMode.DOMICILE) {
            try {
                IloLinearNumExpr expr = sCplex.getCplex().linearNumExpr();
                for (int i:this.equipesAdverses) {
                    for (int j:this.journees) {
                        if(i!=this.equipe)expr.addTerm(sCplex.getX()[this.equipe][i][j], 1);
                    }
                }
                if(minimise)expr.addTerm(sCplex.getCDureMax(this),-1);
                sCplex.getCplex().addLe(expr, this.max);
            } catch (IloException e) {
                throw new RuntimeException(e);
            }
        } else if (this.mode==TypeMode.EXTERIEUR){
            try {
                IloLinearNumExpr expr = sCplex.getCplex().linearNumExpr();
                for (int i:this.equipesAdverses) {
                    for (int j:this.journees) {
                        if(i!=this.equipe)expr.addTerm(sCplex.getX()[i][this.equipe][j], 1);
                    }
                }
                if(minimise)expr.addTerm(sCplex.getCDureMax(this),-1);
                sCplex.getCplex().addLe(expr, this.max);
            } catch (IloException e) {
                throw new RuntimeException(e);
            }
        }else{
            try {
                IloLinearNumExpr expr = sCplex.getCplex().linearNumExpr();
                for (int i:this.equipesAdverses) {
                    for (int j:this.journees) {
                        if(i!=this.equipe){
                            expr.addTerm(sCplex.getX()[i][this.equipe][j], 1);
                            expr.addTerm(sCplex.getX()[this.equipe][i][j], 1);
                        }
                    }
                }
                if(minimise)expr.addTerm(sCplex.getCDureMax(this),-1);
                sCplex.getCplex().addLe(expr, this.max);
            } catch (IloException e) {
                throw new RuntimeException(e);
            }
        }
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
