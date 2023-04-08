package solution;

import java.util.Map;

public class Championnat {
    private static Integer nbJournees;
    private Map<Integer, Journee> journees;

    public boolean getPhase(Journee journee){
        return journee.getId()>nbJournees/2;
    }
    public boolean checkIntegriteeChampiona(){
        return false;
    }
}

