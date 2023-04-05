package instance.modele;

import java.util.Map;

public class Journee {

    private static Integer id;
    private Integer nbMatch;
    private Map<Integer, Rencontre> Rencontres;
    private Integer penaliteeTotale;

    public Integer getId() {
        return this.id;
    }
}
