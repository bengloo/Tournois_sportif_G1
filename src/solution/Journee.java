package solution;

import java.util.HashMap;
import java.util.Objects;

/** classe définissant Journée
 * @author Engloo Benjamin
 * @author Morcq Alexandre
 * @author Sueur Jeanne
 * @author Lux Hugo
 * @version 1.0
 */
public class Journee {
    private final Integer id;
    private HashMap<String,Rencontre> rencontres;

    public Journee(Integer id) {
        this.id = id;
        this.rencontres = new HashMap<>();
    }

    /**
     * Indique l'ID de la journée courante
     * @return l'ID en question
     */
    public Integer getId() {
        return this.id;
    }

    /**
     * Indique le tableau des rencontres de la journée courante
     * @return le tableau en question
     */
    public HashMap<String, Rencontre> getRencontres() {
        return rencontres;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Journee journee)) return false;
        return Objects.equals(getId(), journee.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    /**
     * ??? TODO: compléter ?
     * @return booléen
     */
    public boolean checkIntegriteChampionat(){
        return false;
    }

    /**
     * Méthode permettant d'ajouter une rencontre qui se déroulera sur la journée courante
     * @param rencontre à ajouter
     * @return true si la rencontre a été ajoutée avec succès, false sinon
     */
    public boolean addRencontre(Rencontre rencontre){
        if(rencontre==null){
            System.err.println("rencontre est null");
            return false;
        }
        if(rencontres.containsKey(rencontre.getLabel())){
            System.err.println("contains");
            return false;
        }
        rencontres.put(rencontre.getLabel(), rencontre);
        try {
            if(rencontre.setJournee(this)){
                return true;
            }else{
                //System.err.println("On remove");
                rencontres.remove(rencontre.getLabel());
                return false;
            }
        } catch (Exception e) {
            System.err.println("La rencontre:"+rencontre.toString()+
                    "n'a pas été assignée à la journée:"+this.toString());
            System.exit(-1);
            return false;
        }
    }

    /**
     * Méthode permettant de retirer une rencontre de la journée courante
     * @param rencontre à retirer
     * @return true si la rencontre a été supprimée avec succès, false sinon
     */
    public boolean removeRencontre(Rencontre rencontre){
        if(rencontre==null){
            System.err.println("rencontre is null");
            System.exit(-1);
        }
        if(!rencontre.isInJournee(this)){
            System.err.println("isInJournee");
            System.exit(-1);
        }
        rencontres.remove(rencontre.getLabel());
        return true;
    }

    /**
     * Méthode permettant de savoir si la journée contient une rencontre spécifiée
     * @param r la rencontre ciblée
     * @return true si la rencontre est bien présente, false sinon
     */
    public boolean isPresent(Rencontre r){
        return getRencontres().containsKey(r.getLabel());
    }

    @Override
    public String toString() {
        return "Journee{" +
                "id=" + id +
                ", rencontres=" + rencontres.values() +
                '}';
    }
}
