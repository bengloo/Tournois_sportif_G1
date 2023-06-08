package solveur;

import instance.Instance;
import instance.modele.contrainte.Contrainte;
import operateur.OperateurInsertion;
import solution.Solution;
import ilog.concert.*;
import ilog.cplex.IloCplex;

import java.util.Random;


public class SolveurCplex implements Solveur{

    private IloCplex cplex;
    private IloIntVar[][][] x;
    private IloNumVar[][] y;
    private IloNumVar[][] z;

    @Override
    public String getNom() {
        return "SolveurCplex";
    }

    @Override
    public Solution solve(Instance instance) {
        buildModel(instance);
        return fomatSaveSolution(instance);
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
            this.cplex.setParam(IloCplex.DoubleParam.TiLim, 60);
        } catch (IloException e) {
            throw new RuntimeException(e);
        }
        try {
            if(cplex.solve()) {
                // Cplex a trouve une solution realisable !
                System.out.println(cplex.toString());

            } else {
                System.out.println("Cplex n’a pas trouve de solution realisable");
                // Cplex n’a pas trouve de solution realisable ...
            }
        } catch (IloException e) {
            throw new RuntimeException(e);
        }


    }

    private void init(Instance instance) {
        try {
            cplex.setParam(IloCplex.Param.RandomSeed, new Random().nextInt(Integer.MAX_VALUE));
        } catch (IloException e) {
            throw new RuntimeException(e);
        }
        initVariableDecision(instance);
        initContrainteInerante(instance);
        initContrainteDecision(instance);
        initContrainte(instance);
        System.out.println("all Contrainte et variable init done");
        // ne pas imprimer les informations sur la console
        // ne pas mettre cette option pendant les tests !
        this.cplex.setOut(null);
        // exporter le modele dans un fichier texte au format .lp
        // ce format est comprehensible par cplex
        try {
            this.cplex.exportModel("model_" + instance.getNom() + ".lp");
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
        for(int e=0;e<nbE;e++){
            for(int j=1;j<nbJ;j++) {
                IloLinearNumExpr expry1 = null;
                IloLinearNumExpr expry2 = null;
                IloLinearNumExpr exprz1 = null;
                IloLinearNumExpr exprz2 = null;
                try {
                    expry1 = cplex.linearNumExpr();//pause au au jour j-1 d'une equipe en domicile
                    expry2 = cplex.linearNumExpr();//pause au au jour j d'une equipe en domicile
                    exprz1 = cplex.linearNumExpr();//pause au au jour j-1 d'une equipe en exterieur
                    exprz2 = cplex.linearNumExpr();//pause au au jour j d'une equipe en exterieur
                    for(int i=0;i<nbE;i++){
                        if(i!=e) {
                            expry1.addTerm(x[e][i][j - 1], 1);
                            expry2.addTerm(x[e][i][j], 1);
                            exprz1.addTerm(x[i][e][j - 1], 1);
                            exprz2.addTerm(x[i][e][j], 1);
                        }
                    }
                    cplex.addLe(cplex.sum(cplex.sum(expry1,expry2),-1),y[j-1][e],"CNPD1j"+j+"e"+e);
                    cplex.addLe(cplex.sum(cplex.sum(exprz1,exprz2),-1),z[j-1][e],"CNPE1j"+j+"e"+e);
                    cplex.addLe(y[j-1][e],expry1,"CNPD2j"+j+"e"+e);
                    cplex.addLe(y[j-1][e],expry2,"CNPD3j"+j+"e"+e);
                    cplex.addLe(z[j-1][e],exprz1,"CNPE2j"+j+"e"+e);
                    cplex.addLe(z[j-1][e],exprz2,"CNPE3j"+j+"e"+e);

                } catch (IloException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
    }
    public IloCplex getCplex() {
        return cplex;
    }

    public IloIntVar[][][] getX() {
        return x;
    }

    public IloNumVar[][] getY() {
        return y;
    }

    public IloNumVar[][] getZ() {
        return z;
    }

    private void initContrainte(Instance instance){
        for(Contrainte c:instance.getContraintes()){
            c.initCplexEquation(this,instance);
            System.out.println("Contrainte set: "+c.toString());
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
                    y[j-1][e]=this.cplex.intVar(0,1,"y"+j+"_"+e);
                    z[j-1][e]=this.cplex.intVar(0,1,"z"+j+"_"+e);
                } catch (IloException ex) {
                    throw new RuntimeException(ex);
                }

            }
        }

    }

    private Solution fomatSaveSolution(Instance instance){
        Solution s=new Solution(instance);
        for(int d=0;d<instance.getNbEquipes();d++) {
            for (int e = 0; e < instance.getNbEquipes(); e++) {
                if(d!=e) {
                    for (int j = 0; j < instance.getNbJournees(); j++) {
                        try {
                            if(cplex.getValue(x[d][e][j])==1){
                                OperateurInsertion o=new OperateurInsertion(s,s.getJourneeByID(j),s.getRencontreByEquipes(d,e));
                                if(!o.doMouvementIfRealisable()){
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
        return s;
    }



}
