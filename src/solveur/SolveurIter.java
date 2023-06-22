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
        Solution sbest= new Solution(instance);
        while (niter<nbIterMax){
            Solution sol= this.solveurInitial.solve(instance);
            if((!sbest.check(false))||sol.check(false)&&sol.isMeilleure(sbest)){
                  sbest=sol;
                System.out.println(sol.getLog());
            }
            niter++;
        }
        return sbest;
    }
}
