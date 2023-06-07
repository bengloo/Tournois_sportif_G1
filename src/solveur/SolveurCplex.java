package solveur;

import instance.Instance;
import operateur.OperateurInsertion;
import solution.Journee;
import solution.Solution;
import ilog.concert.*;
import ilog.cplex.IloCplex;

import java.util.Random;


public class SolveurCplex implements Solveur{

    private IloCplex cplex;
    private IloIntVar[][][] x;
    //private IloNumVar[][] y;
    //private IloNumVar[][] z;

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
        initContrainte(instance);
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
                    cplex.addEq(expr,1);
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
                        cplex.addEq(expr,1);
                    }else{
                        cplex.addEq(expr,0);
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
                        cplex.addEq(expr, 1);
                    } catch (IloException ex) {
                        throw new RuntimeException(ex);
                    }
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

    private void initContrainte(Instance instance){

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
                                o.doMouvementIfRealisable();
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
