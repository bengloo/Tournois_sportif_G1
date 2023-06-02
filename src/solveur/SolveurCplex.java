package solveur;

import instance.Instance;
import solution.Journee;
import solution.Solution;
import ilog.concert.*;
import ilog.cplex.IloCplex;



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
        return null;
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
                // Cplex n’a pas trouve de solution realisable ...
            }
        } catch (IloException e) {
            throw new RuntimeException(e);
        }


    }

    private void init(Instance instance) {
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

        //chaque rencontre n'est afecter que une seul foix à une journee ou 0 pour les rencontre contre sois-même
        int nbEquipe=instance.getNbEquipes();
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



}
