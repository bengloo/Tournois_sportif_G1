package solveur;

import instance.Instance;
import solution.Solution;

public class SolveurIter implements Solveur{

    private final Solveur solveurInitial;
    private  final int nbIterMax;

    public SolveurIter(Solveur solveurInitial,int nbiter) {
        this.solveurInitial = solveurInitial;
        this.nbIterMax=nbiter;
    }

    @Override
    public String getNom() {
        return  "SolveurIter("+solveurInitial.getNom()+")";
    }

    @Override
    public Solution solve(Instance instance) {
        int niter=0;
        while (niter<nbIterMax){
            Solution sol=this.solveurInitial.solve(instance);
            if(sol.check()){
                System.out.println(this.getNom()+" | "+instance.getNom()+" | niter:"+niter);
                return sol;
            }else{
                System.out.println(sol.toString());
                System.out.println(sol.getRencontreSansJournee());
                System.out.println(sol.nbMargineString());
            }
            niter++;
        }
        System.out.print(this.getNom()+" | "+instance.getNom()+" | niter:"+niter);
        return this.solveurInitial.solve(instance);
    }
}
