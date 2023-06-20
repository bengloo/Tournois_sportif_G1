package solveur;

import instance.Instance;
import instance.modele.contrainte.Contrainte;
import instance.modele.contrainte.ContraintePauseGlobale;
import instance.modele.contrainte.TypeMode;
import operateur.OperateurInsertion;
import solution.Solution;
import ilog.concert.*;
import ilog.cplex.IloCplex;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Random;


public class SolveurCplex implements Solveur{

    private IloCplex cplex;
    private IloIntVar[][][] x;
    private IloIntVar[][] y;
    private IloIntVar[][] z;

    private HashMap<Contrainte,IloIntVar> cDurMax;
    private HashMap<Contrainte,IloIntVar> cDurMin;
    private HashMap<Contrainte,IloIntVar> coutC;



    private int watchDog = 1000;
    private boolean minimiseDure =true;
    private boolean minimiseSouple =false;
    private boolean avoidContraintePauseGlobale=true;

    public SolveurCplex() {
    }

    public SolveurCplex(int watchDog, boolean minimiseDure, boolean minimiseSouple,boolean avoidContraintePauseGlobale){
        this.watchDog = watchDog;
        this.minimiseDure = minimiseDure;
        this.minimiseSouple = minimiseSouple;
        this.avoidContraintePauseGlobale = avoidContraintePauseGlobale;
    }

    @Override
    public String getNom() {
        return "SolveurCplex";
    }

    @Override
    public Solution solve(Instance instance) {
        long start = System.currentTimeMillis();
        buildModel(instance);
        Solution s =formatSaveSolution(instance);
        long time = System.currentTimeMillis() - start;
        try {
            s.addLog("|"+ InetAddress.getLocalHost().getHostName()+"|"+System.getProperty("user.name"));
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
        s.addLog("|"+time);
        s.writeSolutionCheckerProf(this.getNom());
        s.logCheckProf();
        return s;
    }

    /**
     * Construit le model d'équation et le résout au sens de cplex
     * @param instance instance traitée
     */
    private void buildModel(Instance instance) {
        try {
            this.cplex = new IloCplex();
        } catch (IloException e) {
            throw new RuntimeException(e);
        }
        init(instance);
        // imposer un temps limite de résolution, ici 60 secondes
        try {
            this.cplex.setParam(IloCplex.DoubleParam.TiLim, watchDog);
        } catch (IloException e) {
            throw new RuntimeException(e);
        }
        try {
            if(cplex.solve()) {
                // Cplex a trouvé une solution realisable !

                //System.out.println(cplex.toString());

            } else {
                System.out.println("Cplex n’a pas trouve de solution realisable");
                
                //System.out.println(cplex.getInfeasibilities(cplex.ge));
                // Cplex n’a pas trouvé de solution realisable ...
            }
        } catch (IloException e) {
            throw new RuntimeException(e);
        }


    }

    /**
     * pose les equations du model cplex et les enregistre dans modelsCplex/model_{nom instance}.lp
     * @param instance instance traitée par le solveur
     */
    private void init(Instance instance) {
        try {
            cplex.setParam(IloCplex.Param.RandomSeed,new Random().nextInt(Integer.MAX_VALUE*3/4));
        } catch (IloException e) {
            throw new RuntimeException(e);
        }
        initVariableDecision(instance);
        initContrainteInerante(instance);
        initContrainteDecision(instance);
        initContrainte(instance);
        //System.out.println("all Contrainte et variable init done");
        // ne pas imprimer les informations sur la console
        // ne pas mettre cette option pendant les tests !
        this.cplex.setOut(null);
        // exporter le modèle dans un fichier texte au format .lp
        // ce format est comprehensible par cplex
        /*try {
            this.cplex.exportModel("modelsCplex/model_" + instance.getNom() + ".lp");
        } catch (IloException e) {
            throw new RuntimeException(e);
        }*/
    }

    /**
     * définit les equations relative à la viabilité de la solution.
     * @param instance instance traitée par le solveur
     */
    private void initContrainteInerante(Instance instance){
        int nbEquipe=instance.getNbEquipes();

        //chaque équipe est présente une seule fois par jour
        for (int j = 0; j < instance.getNbJournees(); j++) {
            for(int e=0;e<instance.getNbEquipes();e++) {
                IloLinearNumExpr expr = null;
                try {
                    expr = cplex.linearNumExpr();
                    for(int i=0;i<instance.getNbEquipes();i++) {
                       if(e!=i){
                           expr.addTerm(x[e][i][j], 1);
                           expr.addTerm(x[i][e][j], 1);
                       }
                    }
                    cplex.addEq(expr,1,"CI1e"+e+"j"+j);
                }catch (IloException ex){
                    throw new RuntimeException(ex);
                }
            }
        }

        //chaque rencontre n'est affectée qu'une seule fois sur l'ensemble des journées ou 0 pour les rencontres contre
        // sois-même

        for(int d=0;d<instance.getNbEquipes();d++){
            for(int e=0;e<instance.getNbEquipes();e++) {
                IloLinearNumExpr expr = null;
                try {
                    expr = cplex.linearNumExpr();
                    for (int j = 0; j < instance.getNbJournees(); j++) {
                        expr.addTerm(x[d][e][j],1);
                    }
                    if(d!=e){
                        cplex.addEq(expr,1,"CI2d"+d+"e"+e);
                    }else{
                        cplex.addEq(expr,0,"CI2d"+d+"e"+e);
                    }
                } catch (IloException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }

        // chaque rencontre à son match retour dans une phase différente
        for(int d=0;d<instance.getNbEquipes();d++) {
            for (int e = 0; e < instance.getNbEquipes(); e++) {
                IloLinearNumExpr expr = null;
                if(d!=e) {
                    try {
                        expr = cplex.linearNumExpr();
                        for (int j = 0; j < instance.getNbJournees() / 2; j++) {
                            expr.addTerm(x[d][e][j], 1);
                            expr.addTerm(x[e][d][j], 1);
                        }

                        cplex.addEq(expr, 1,"CI3d"+d+"e"+e);
                    } catch (IloException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        }
    }

    /**
     * définit les equations délimitant les variables de decisions ne définissant pas directement de la solution
     * (Pause,Minimiser)
     * @param instance instance traitée par le solveur
     */
    public void initContrainteDecision(Instance instance){
        int nbE=instance.getNbEquipes();
        int nbJ=instance.getNbJournees();
        if(instance.getNbContraintePause(minimiseSouple)>0) {
            for (int e = 0; e < nbE; e++) {
                for (int j = 1; j < nbJ; j++) {
                    IloLinearNumExpr expry1 = null;
                    IloLinearNumExpr expry2 = null;
                    IloLinearNumExpr exprz1 = null;
                    IloLinearNumExpr exprz2 = null;
                    try {
                        expry1 = cplex.linearNumExpr();//pause au jour j-1 d'une équipe en domicile
                        expry2 = cplex.linearNumExpr();//pause au jour j d'une équipe en domicile
                        exprz1 = cplex.linearNumExpr();//pause au jour j-1 d'une équipe en extérieur
                        exprz2 = cplex.linearNumExpr();//pause au jour j d'une équipe en extérieur
                        for (int i = 0; i < nbE; i++) {
                            if (i != e) {
                                expry1.addTerm(x[e][i][j - 1], 1);
                                expry2.addTerm(x[e][i][j], 1);
                                exprz1.addTerm(x[i][e][j - 1], 1);
                                exprz2.addTerm(x[i][e][j], 1);
                            }
                        }
                        if(instance.isPauseConcerne(e,j, TypeMode.DOMICILE,avoidContraintePauseGlobale)) {
                            cplex.addLe(cplex.sum(cplex.sum(expry1, expry2), -1), y[j - 1][e], "CNPD1j" + j +
                                    "e" + e);
                            cplex.addLe(y[j - 1][e], expry1, "CNPD2j" + j + "e" + e);
                            cplex.addLe(y[j - 1][e], expry2, "CNPD3j" + j + "e" + e);
                        }
                        if(instance.isPauseConcerne(e,j, TypeMode.EXTERIEUR,avoidContraintePauseGlobale)) {
                            cplex.addLe(cplex.sum(cplex.sum(exprz1, exprz2), -1), z[j - 1][e], "CNPE1j" + j +
                                    "e" + e);
                            cplex.addLe(z[j - 1][e], exprz2, "CNPE3j" + j + "e" + e);
                            cplex.addLe(z[j - 1][e], exprz1, "CNPE2j" + j + "e" + e);
                        }

                    } catch (IloException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        }
        //on cherche à minimiser le nombre de contraintes dures non respectées
        if(minimiseDure&&!minimiseSouple) {
            try {
                IloLinearNumExpr expr = cplex.linearNumExpr();
                for (IloIntVar c : cDurMax.values()) {
                    expr.addTerm(c, 1);
                }
                for (IloIntVar c : cDurMin.values()) {
                    expr.addTerm(c, 1);
                }
                this.cplex.addMinimize(expr,"minimiseDure");
            } catch (IloException e) {
                throw new RuntimeException(e);
            }
        }else if(minimiseSouple && !minimiseDure){
            try{
                IloLinearNumExpr expr = cplex.linearNumExpr();
                for (IloIntVar c : coutC.values()) {
                    expr.addTerm(c, 1);
                }
                this.cplex.addMinimize(expr,"minimiseCout");
            } catch (IloException e) {
                throw new RuntimeException(e);
            }
        } else if (minimiseDure&& minimiseSouple) {
            try {
                IloLinearNumExpr expr = cplex.linearNumExpr();
                for (IloIntVar c : cDurMax.values()) {
                    expr.addTerm(c, 1);
                }
                for (IloIntVar c : cDurMin.values()) {
                    expr.addTerm(c, 1);
                }
                IloLinearNumExpr expr2 = cplex.linearNumExpr();
                for (IloIntVar c : coutC.values()) {
                    expr2.addTerm(c, 1);
                }
                this.cplex.addMinimize(this.cplex.sum(this.cplex.prod(expr,10000),expr2),"miniseDureCout");
            } catch (IloException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * permet d'obtenir l'objet lié au model.
     * @return le model de type IloCplex
     */
    public IloCplex getCplex() {
        return cplex;
    }

    /**
     * permet d'accéder à la variable de decision définissant les affectations de couple d'équipes à une journée
     * (rencontres)
     * @return x un IloIntVar[][][] de taille nbEquipe ^2 et nb Journee
     */
    public IloIntVar[][][] getX() {
        return x;
    }

    /**
     * permet d'accéder à la variable de decision définissant les pauses par équipe par jour en domicile
     * @return y un IloIntVar[][] de taille nbEquipe et nb Journee
     */
    public IloIntVar[][] getY() {
        return y;
    }

    /**
     * permet d'accéder à la variable de decision définissant les pauses par équipe par jour en extérieur
     * @return z un IloIntVar[][] de taille nbEquipe et nb Journee
     */
    public IloIntVar[][] getZ() {
        return z;
    }

    /**
     * permet d'accéder à la variable de decision définissant les écarts de coeff sur les bornes max de la contrainte
     * spécifié
     * @param c contrainte ciblé
     * @return cDurMax un IloIntVar
     */
    public IloIntVar getCDureMax(Contrainte c){
        return cDurMax.get(c);
    }

    /**
     * permet d'accéder à la variable de decision définissant les écarts de coeff sur les bornes min de la contrainte
     * spécifié
     * @param c contrainte ciblé
     * @return cDurMin un IloIntVar
     */
    public IloIntVar getCDureMin(Contrainte c){
        return cDurMin.get(c);
    }

    /**
     * permet d'accéder à la variable de decision définissant le cout de la contrainte spécifié
     * @param c contrainte ciblé
     * @return coutC un IloIntVar
     */
    public IloIntVar getCoutC(Contrainte c){
        return coutC.get(c);
    }

    /**
     * définit les equations liées aux contraintes dures et souples
     * @param instance instance traitée par le solveur
     */
    private void initContrainte(Instance instance){
        for(Contrainte c:instance.getContraintes()){
            if(c.estDure()||((!c.estDure()) && minimiseSouple)) {
                if((c instanceof ContraintePauseGlobale)) {
                    if(!avoidContraintePauseGlobale) c.initCplexEquation(this, instance, minimiseDure,
                            minimiseSouple, c.estDure());
                }else{
                    c.initCplexEquation(this, instance, minimiseDure, minimiseSouple, c.estDure());
                }
            }
            //System.out.println("Contrainte set: "+c.toString());
        }
    }

    /**
     * initialise-les variables de decisions en les affectant au modèle et les nommant
     * @param instance instance traitée par le solveur
     */
    private void initVariableDecision(Instance instance){
        int nbE=instance.getNbEquipes();
        int nbJ=instance.getNbJournees();
        x = new IloIntVar[nbE][nbE][nbJ];
        for(int d=0;d<instance.getNbEquipes();d++) {
            for (int e = 0; e < instance.getNbEquipes(); e++) {
                for (int j = 0; j < instance.getNbJournees(); j++) {
                    try {
                        x[d][e][j]=this.cplex.intVar(0,1,"x"+d+"_"+e+"_"+j);
                    } catch (IloException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        }
        y= new IloIntVar[nbJ-1][nbE];
        z= new IloIntVar[nbJ-1][nbE];
        for(int j=1;j<nbJ;j++){
            for(int e=0;e<nbE;e++){
                try {
                    if(instance.isPauseConcerne(e,j, TypeMode.DOMICILE,avoidContraintePauseGlobale)) {
                        y[j - 1][e] = this.cplex.intVar(0, 1, "y" + j + "_" + e);
                    }
                    if(instance.isPauseConcerne(e,j, TypeMode.EXTERIEUR,avoidContraintePauseGlobale)) {
                        z[j - 1][e] = this.cplex.intVar(0, 1, "z" + j + "_" + e);
                    }
                } catch (IloException ex) {
                    throw new RuntimeException(ex);
                }

            }
        }
        if(minimiseDure) {
            cDurMax = new HashMap<Contrainte, IloIntVar>();
            cDurMin = new HashMap<Contrainte, IloIntVar>();
            int i = 0;
            for (Contrainte c : instance.getContraintes()) {
                if (c.estDure()) {
                    i++;
                    try {
                        cDurMax.put(c, this.cplex.intVar(0, Integer.MAX_VALUE, "cdmax" + i));
                        cDurMin.put(c, this.cplex.intVar(0, Integer.MAX_VALUE, "cdmax" + i));
                    } catch (IloException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        if(minimiseSouple){
            coutC= new HashMap<Contrainte, IloIntVar>();
            int i = 0;
            for (Contrainte c : instance.getContraintes()) {
                if (!c.estDure()) {
                    i++;
                    try {
                        coutC.put(c, this.cplex.intVar(0, Integer.MAX_VALUE, "coutC" + i));
                    } catch (IloException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

        }

    }

    /**
     * Convertit la variable de décision x du model cplex relative aux affectations équipes journées en Solution
     * update les logs de la solution
     * @param instance instance traitée par le solveur
     * @return
     */
    private Solution formatSaveSolution(Instance instance){
        Solution s=new Solution(instance);
        for(int d=0;d<instance.getNbEquipes();d++) {
            for (int e = 0; e < instance.getNbEquipes(); e++) {
                if(d!=e) {
                    for (int j = 0; j < instance.getNbJournees(); j++) {
                        try {
                            if(cplex.getValue(x[d][e][j])==1){
                                OperateurInsertion o=new OperateurInsertion(s,s.getJourneeByID(j),s
                                        .getRencontreByEquipes(d,e));
                                if(!o.doMouvementTrusted()){
                                    System.err.println(o.toString());
                                };
                            };
                        } catch (IloException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                }
            }
        }
        s.addLog(this.getNom()+"|"+instance.getNom());
        s.addLog("|"+s.check(false)+"|"+this.watchDog+"|"+ LocalDateTime.now()+"|"+s.getCoutTotal());
        return s;
    }



}
