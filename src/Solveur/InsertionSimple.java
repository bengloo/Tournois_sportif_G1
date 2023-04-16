package Solveur;

import instance.Instance;
import solution.Championnat;

public class InsertionSimple implements Solveur{
    @Override
    public String getNom() {
        return "InsertionSimple";
    }

    @Override
    public Championnat solve(Instance instance) {
        //TODO insert sucessivement la premierre insertion realisable
        //Attention potentielement bloquant sur certaine instance.
        return null;
    }
}
