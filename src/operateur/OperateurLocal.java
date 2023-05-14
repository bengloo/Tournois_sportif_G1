package operateur;
import solution.Journee;
import solution.Rencontre;
import solution.Solution;


public abstract class OperateurLocal extends Operateur{
    protected Rencontre rencontre2;
    protected Journee journee2;

    public OperateurLocal() {
        rencontre2=null;
        journee2=null;
    }
    
    public OperateurLocal(Solution c, Journee j, Rencontre r, Rencontre rencontre2, Journee journee2) {
        super(c, j, r);
        this.rencontre2 = rencontre2;
        this.journee2 = journee2;
    }

    public Rencontre getRencontre2() {
        return rencontre2;
    }

    public Journee getJournee2() {
        return journee2;
    }

    public static OperateurLocal getOperateurLocal(TypeOperateurLocal type){
        switch (type){
            case ECHANGE:return new OperateurEchange();
            default:return null;
        }
    }

    public abstract boolean isTabou(OperateurLocal operateur);


}
