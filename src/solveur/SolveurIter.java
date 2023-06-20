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
        int bestCout=Integer.MAX_VALUE;
        while (niter<nbIterMax){
            Solution sol= this.solveurInitial.solve(instance);
            if(sol.check(false)&&Integer.parseInt(sol.getLog(9))<bestCout){
                  sbest=sol;
                  bestCout= Integer.parseInt(sol.getLog(9));
                System.out.println(sol.getLog());
            }
            niter++;
        }
        return sbest;
    }
}
