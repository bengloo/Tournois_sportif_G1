package solveur.Abandoné;

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
        //solution solutionglobale(instance)
        //solution bestsolution
        //soit r la premiere rencontre ayant journée à null
        //pour toutes les journées j ayant rencontres.size<NbRencontreJournee
            //si insertion j r valide sur solution globale
                //Solution newsolution(instance)
                //do insertion sur newSolution
                //newSolution = solvRec(newsolution);
                //si newsolution better best solution
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

        //soit r la premiere rencontre ayant journée à null
        //si r==null return solprec
        //solution bestsolution
        //TODO exclure les solutions évoluant vers un coût trop fort (le coût doit évoluer exponentiellement au nombre
        // de rencontre deja inséré)
        //pour toute les journee j ayant rencontres.size<NbRencontreJournee
            //si insertion j r valide sur solprec
                //Solution newsolution(solution) //constructeur par copy , l'instance et equipe ont les même reference
        // mais pas rencontres journees
                //do insertion sur newSolution
                    //newSolution = solvRec(newsolution);
                    //si newsolution better best solution
                        //bestsolution= newsolution
        // return best solution
        return null;
    }
}
