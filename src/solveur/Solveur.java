package solveur;

import instance.Instance;
import solution.Solution;

/** interface définissant Solveur
 * @author Engloo Benjamin
 * @author Morcq Alexandre
 * @author Sueur Jeanne
 * @author Lux Hugo
 * @version 1.0
 */
public interface Solveur {
    /**
     * Méthode permettant de récupérer le nom de la méthode d'insertion de rencontres
     * @return la chaîne de caractères du nom
     */
    public String getNom();

    /**
     * Méthode permettant de résoudre la solution à l'aide d'une méthode particulière d'insertion de rencontres
     * @param instance à traiter sur laquelle appliquer la solution
     * @return la solution finale optimisée selon la méthode appliquée
     */
    public Solution solve(Instance instance);
}