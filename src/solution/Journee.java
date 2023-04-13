package solution;

import instance.modele.Rencontre;

import java.util.HashMap;
import java.util.Objects;

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

    public boolean ajouterRencontre(Rencontre rencontreToADD){
        if(rencontreToADD == null)return false;
        return  this.rencontres.put(rencontreToADD.getLabel(),rencontreToADD) != null;
    }

    public boolean checkIntegriteeChampionatf(){
        return false;
    }


    public String toStringLong() {
        return "Journee{" +
                "id=" + id +
                '}';
    }
    @Override
    public String toString() {
        return id.toString();
    }
}
