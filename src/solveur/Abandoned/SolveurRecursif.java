package solveur.Abandoned;

import instance.Instance;
import solution.Solution;
import solveur.Solveur;

/** classe définissant SolveurRecursif (implémentant l'interface Solveur)
 * @author Engloo Benjamin
 * @author Morcq Alexandre
 * @author Sueur Jeanne
 * @author Lux Hugo
 * @version 1.0
 */
public class SolveurRecursif implements Solveur {

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

    /**
     * Méthode permettant de résoudre la solution récursivement, en tenant compte de la solution précédente
     * @param solprec la solution précédente
     * @return la meilleure solution finale
     */
    public Solution solveRec(Solution solprec) {

        //soit r la premierre rencontre ayant journnee à null
        //si r==null retune solprec
        //solution bestsolution
        //TODO exclure les solution evoluant vers un coups trops fort (le cout doit evolué exponentielement au nombre de rencontre dejas inseré)
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
