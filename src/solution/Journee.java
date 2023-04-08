package solution;

import instance.modele.Rencontre;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Journee {
    private final Integer id;
    private Map<Integer, Rencontre> Rencontres;
    private Integer penaliteeTotale;



    public Journee(Integer id) {
        this.id=id;
        Rencontres= new HashMap<>();
        penaliteeTotale=0;
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
}
