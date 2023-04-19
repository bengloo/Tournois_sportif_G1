package Solveur;

import instance.Instance;
import solution.Solution;

public class SolveurRecursif implements Solveur{

    @Override
    public String getNom() {
        return "SolveurRecusif";
    }

    @Override
    public Solution solve(Instance instance) {
        //solution solutionglobal(instance)
        //solution bestsolution
        //soit r la premierre rencontre ayant journnee à null
        //pour toute les journee  j ayant rencontres.size<NbRencontreJournee
            //si insertion j r valide sur solution global
                //Solution newsolution(instance)
                //do insertion sur newsolution
                //newSolution = solvRec(newsolution);
                //si newsolution beter best solution
                    //bestsolution= newsolution
        //return best solution
        return null;
    }


    public Solution solveRec(Solution solprec) {

        //soit r la premierre rencontre ayant journnee à null
        //si r==null retune solprec
        //solution bestsolution
        //pour toute les journee j ayant rencontres.size<NbRencontreJournee
            //si insertion j r valide sur solprec
                //Solution newsolution(solution) //constructeur par copy , l'instance et equipe ont les même reference mais pas rencontres journees
                //do insertion sur newsolution
                    //newSolution = solvRec(newsolution);
                    //si newsolution beter best solution
                        //bestsolution= newsolution
        // return best solution
        return null;
    }
}
