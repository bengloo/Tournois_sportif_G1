package solveur;

import instance.Instance;
import instance.modele.contrainte.Contrainte;
import instance.modele.contrainte.TypeMode;
import operateur.OperateurInsertion;
import solution.Solution;
import ilog.concert.*;
import ilog.cplex.IloCplex;

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



    private int watchDog = 3000;
    private boolean minimiseDure =true;
    private boolean minimiseSouple =false;

    @Override
    public String getNom() {
        return "SolveurCplex";
    }

    @Override
    public Solution solve(Instance instance) {

        buildModel(instance);
        Solution s =formatSaveSolution(instance);
        return s;
    }

    private void buildModel(Instance instance) {
        try {
            this.cplex = new IloCplex();
        } catch (IloException e) {
            throw new RuntimeException(e);
        }
        init(instance);
        // imposer un temps limite de resolutuon, ici 60 secondes
        try {
            this.cplex.setParam(IloCplex.DoubleParam.TiLim, watchDog);
        } catch (IloException e) {
            throw new RuntimeException(e);
        }
        try {
            if(cplex.solve()) {
                // Cplex a trouve une solution realisable !

                //System.out.println(cplex.toString());

            } else {
                System.out.println("Cplex n’a pas trouve de solution realisable");
                
                //System.out.println(cplex.getInfeasibilities(cplex.ge));
                // Cplex n’a pas trouve de solution realisable ...
            }
        } catch (IloException e) {
            throw new RuntimeException(e);
        }


    }

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
        // exporter le modele dans un fichier texte au format .lp
        // ce format est comprehensible par cplex
        try {
            this.cplex.exportModel("modelsCplex/model_" + instance.getNom() + ".lp");
        } catch (IloException e) {
            throw new RuntimeException(e);
        }
    }

    private void initContrainteInerante(Instance instance){
        int nbEquipe=instance.getNbEquipes();

        //chaque equipe est presente une seul fois par jour
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

        //chaque rencontre n'est afecter que une seul foix sur l'ensemble des journee ou 0 pour les rencontre contre sois-même

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

    public void initContrainteDecision(Instance instance){
        int nbE=instance.getNbEquipes();
        int nbJ=instance.getNbJournees();
        if(instance.getNbContraintePause()>0) {
            for (int e = 0; e < nbE; e++) {
                for (int j = 1; j < nbJ; j++) {
                    IloLinearNumExpr expry1 = null;
                    IloLinearNumExpr expry2 = null;
                    IloLinearNumExpr exprz1 = null;
                    IloLinearNumExpr exprz2 = null;
                    try {
                        expry1 = cplex.linearNumExpr();//pause au au jour j-1 d'une equipe en domicile
                        expry2 = cplex.linearNumExpr();//pause au au jour j d'une equipe en domicile
                        exprz1 = cplex.linearNumExpr();//pause au au jour j-1 d'une equipe en exterieur
                        exprz2 = cplex.linearNumExpr();//pause au au jour j d'une equipe en exterieur
                        for (int i = 0; i < nbE; i++) {
                            if (i != e) {
                                expry1.addTerm(x[e][i][j - 1], 1);
                                expry2.addTerm(x[e][i][j], 1);
                                exprz1.addTerm(x[i][e][j - 1], 1);
                                exprz2.addTerm(x[i][e][j], 1);
                            }
                        }
                        if(instance.isPauseConcerne(e,j, TypeMode.DOMICILE)) {
                            cplex.addLe(cplex.sum(cplex.sum(expry1, expry2), -1), y[j - 1][e], "CNPD1j" + j + "e" + e);
                            cplex.addLe(y[j - 1][e], expry1, "CNPD2j" + j + "e" + e);
                            cplex.addLe(y[j - 1][e], expry2, "CNPD3j" + j + "e" + e);
                        }
                        if(instance.isPauseConcerne(e,j, TypeMode.EXTERIEUR)) {
                            cplex.addLe(cplex.sum(cplex.sum(exprz1, exprz2), -1), z[j - 1][e], "CNPE1j" + j + "e" + e);
                            cplex.addLe(z[j - 1][e], exprz2, "CNPE3j" + j + "e" + e);
                            cplex.addLe(z[j - 1][e], exprz1, "CNPE2j" + j + "e" + e);
                        }

                    } catch (IloException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        }
        //on cherche à minimiser le nombre de contrainte dure non respecté
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

    public IloCplex getCplex() {
        return cplex;
    }

    public IloIntVar[][][] getX() {
        return x;
    }

    public IloIntVar[][] getY() {
        return y;
    }

    public IloIntVar[][] getZ() {
        return z;
    }

    public IloIntVar getCDureMax(Contrainte c){
        return cDurMax.get(c);
    }

    public IloIntVar getCDureMin(Contrainte c){
        return cDurMin.get(c);
    }

    public IloIntVar getCoutC(Contrainte c){
        return coutC.get(c);
    }


    private void initContrainte(Instance instance){
        for(Contrainte c:instance.getContraintes()){
            if(c.estDure()||((!c.estDure()) && minimiseSouple)) {
                c.initCplexEquation(this, instance, minimiseDure, minimiseSouple, c.estDure());
            }
            //System.out.println("Contrainte set: "+c.toString());
        }
    }

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
                    if(instance.isPauseConcerne(e,j, TypeMode.DOMICILE)) {
                        y[j - 1][e] = this.cplex.intVar(0, 1, "y" + j + "_" + e);
                    }
                    if(instance.isPauseConcerne(e,j, TypeMode.EXTERIEUR)) {
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

    private Solution formatSaveSolution(Instance instance){
        Solution s=new Solution(instance);
        for(int d=0;d<instance.getNbEquipes();d++) {
            for (int e = 0; e < instance.getNbEquipes(); e++) {
                if(d!=e) {
                    for (int j = 0; j < instance.getNbJournees(); j++) {
                        try {
                            if(cplex.getValue(x[d][e][j])==1){
                                OperateurInsertion o=new OperateurInsertion(s,s.getJourneeByID(j),s.getRencontreByEquipes(d,e));
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
