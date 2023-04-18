package solution;

import javax.sound.midi.Soundbank;
import java.util.HashMap;
import java.util.Objects;
/** Class definissant Journee.
 * @author Engloo Benjamin
 * @author Morcq Alexandre
 * @author Sueur Jeanne
 * @author Lux Hugo
 * @version 0.5
 */
public class Journee {
    private final Integer id;
    private HashMap<String,Rencontre> rencontres;




    public Journee(Integer id) {
        this.id = id;
        this.rencontres = new HashMap<>();

    }
    public Integer getId() {
        return this.id;
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

    public boolean checkIntegriteeChampionatf(){
        return false;
    }

    /**
     * Fonction permettant d'ajouter une rencontre'
     * @Param rencontre la rencontre a ajouter
     * @return  un boolean pour la confirmation de l'ajout.
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
                    "n'a pas été assigné à la journée:"+this.toString());
            System.exit(-1);
            return false;
        }
    }

    /**
     * Fonction permettant de retirer une rencontre'
     * @Param rencontre la rencontre a retirer
     * @return  un boolean pour la confirmation du retrait.
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
     * Fonction permettant de savoir si la journée contient  une rencontre spécifié'
     * @Param rencontre la rencontre ciblée
     * @return  un boolean pour la confirmation de la présence ou non.
     */
    public boolean isPresent(Rencontre r){
        return getRencontres().containsKey(r.getLabel());
    }

    public HashMap<String, Rencontre> getRencontres() {
        return rencontres;
    }

    @Override
    public String toString() {
        return "Journee{" +
                "id=" + id +
                ", rencontres=" + rencontres.values() +
                '}';
    }
}
