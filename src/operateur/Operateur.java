package operateur;

import instance.modele.contrainte.Contrainte;
import solution.Championnat;
import solution.Journee;
import solution.Rencontre;

import java.util.HashMap;
import java.util.Map;

public abstract class Operateur {

    //TODO protected ou private pour ces atribut à réflechire
    private Journee journee;
    private Rencontre rencontre;

    private Championnat championnat;

    //delat cout totale de toutes les contraintes pour une operation donné on se baseras sur les coefDesContrainte pour ne par recalculer entierement les fonction objectives de l'entiereter de la solution dejas etablie
    private Integer cout;

    /*
    //TODO serait t'il judicieux de ne pas le garder en memoir et de le recalculer uniquement pour l'operation appliqué par do movement?
    //contient les deltatCoef des contrainte impacté par l'operation
    private Map<Contrainte,Integer> deltatCoefContraintes;
    */

    public Operateur() {
        this.cout = Integer.MAX_VALUE;
    }

    public Operateur(Championnat c,Journee j,Rencontre r) {
        this.championnat=c;
        this.journee = j;
        this.rencontre=r;
        this.cout = evalDeltaCout(evalDeltaCoefs());

    }

    public int getDeltaCout() {
        return this.cout;
    }

    public boolean isMouvementRealisable() {
        return this.cout < Integer.MAX_VALUE;
    }

    public boolean isMeilleur(Operateur op) {
        return this.cout < op.getDeltaCout();
    }

    protected abstract int evalDeltaCout(Map<Contrainte, Integer> deltaCoef);

    protected abstract Map<Contrainte,Integer> evalDeltaCoefs();

    protected abstract boolean doMouvement();

    public boolean doMouvementIfRealisable() {
        if(this.isMouvementRealisable()) return doMouvement();
        return false;
    }
    public boolean isMouvementAmeliorant(){
        return this.cout<0;
    }

    public Journee getJournee() {
        return journee;
    }

    public Rencontre getRencontre() {
        return rencontre;
    }

    public void setRencontre(Rencontre rencontre) {
        this.rencontre = rencontre;
    }

    public Integer getCout() {
        return cout;
    }

    @Override
    public String toString() {
        return "Operateur{" +
                "journee=" + journee +
                ", cout=" + cout +
                '}';
    }

    protected Championnat getChampionnat() {
        return championnat;
    }
}
