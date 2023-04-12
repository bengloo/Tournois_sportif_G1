package instance.modele;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Journee {
    private final Integer id;




    public Journee(Integer id) {
        this.id=id;

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
        return false;
    }

    public boolean checkIntegriteeChampiona(){
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