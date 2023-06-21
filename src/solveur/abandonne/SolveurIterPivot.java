package solveur.abandonne;

import instance.Instance;
import solution.Solution;
import solveur.Solveur;

public class SolveurIterPivot implements Solveur {

    private final Solveur solveurInitial;
    private  final int nbIterMax;

    public SolveurIterPivot(Solveur solveurInitial, int nbiter) {
        this.solveurInitial = solveurInitial;
        this.nbIterMax=nbiter;
    }

    @Override
    public String getNom() {
        return  "SolveurIterPivot("+solveurInitial.getNom()+")";
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
