package instance.modele;

import java.util.HashMap;
import java.util.Map;

public class Journee {

    private static Integer id;
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


}
