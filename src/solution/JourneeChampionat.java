package solution;

import instance.modele.Journee;
import instance.modele.Rencontre;

import java.util.HashMap;
import java.util.Map;

public class JourneeChampionat extends Journee {

    private Map<String, Rencontre> rencontres;
    private int penaliteeJournee;
    public JourneeChampionat(Integer id) {
        super(id);
        rencontres=new HashMap<>();
        penaliteeJournee=0;
    }
}
