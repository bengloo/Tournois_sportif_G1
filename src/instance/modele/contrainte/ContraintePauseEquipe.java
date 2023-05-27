package instance.modele.contrainte;

import operateur.Operateur;
import operateur.OperateurInsertion;
import solution.Journee;
import solution.Solution;
import solution.Rencontre;

import java.util.Objects;
import java.util.TreeSet;

/** classe définissant ContraintePauseEquipe (hérite de Contrainte)
 * @author Engloo Benjamin
 * @author Morcq Alexandre
 * @author Sueur Jeanne
 * @author Lux Hugo
 * @version 1.1
 */
public class ContraintePauseEquipe extends Contrainte{
    private Integer equipe;
    private TreeSet<Integer> journees;
    private TypeMode mode;
    private Integer max;

    public ContraintePauseEquipe(Integer equipe,TypeMode mode,Integer max) {
        super();
        this.equipe=equipe;
        this.journees = new TreeSet<>();
        this.mode = mode;
        this.max = max;
    }
    public ContraintePauseEquipe(Integer equipe,TypeMode mode,Integer max, Integer penalite) {
        super(penalite);
        this.equipe=equipe;
        this.journees = new TreeSet<>();
        this.mode = mode;
        this.max = max;
    }

    /**
     * Ajoute une journée à la liste des journées de la contrainte
     * @param id l'ID de la journée à ajouter
     * @return true si l'ajout a réussi, false sinon
     */
    public boolean addJournee(Integer id){
        return journees.add(id);
    }

    @Override
    public TypeContrainte getTypeContrainte() {
        return TypeContrainte.PAUSEEQUIPE;
    }

    //TODO implementer les fonction de calcule de cout en sinspirent de la contrainte de placement, réflechire si on ne peux pas factoriser du code sout des fonction comune aux contraintes
    @Override
    public int getCoutTotal(Solution championnat) {
        int valc=0;
        for (Integer j : this.journees) {
            for (Rencontre r : championnat.getJourneeByID(j).getRencontres().values()) {
                if(r.isConcerne(this.equipe,this.mode)){
                    valc+=traitementModes(championnat,r,TypeMode.DOMICILE);
                }
                if(r.isConcerne(this.equipe, this.mode)){
                    valc+=traitementModes(championnat,r,TypeMode.EXTERIEUR);
                }
            }
        }
        if(valc > this.max) {
            if (estDure()) return Integer.MAX_VALUE;
            return this.penalite * (valc-this.max);
        }
        return 0;
    }

    @Override
    public Object evalDeltaCoef(Solution championnat, Operateur o) {
        //System.out.println("debut");
        int valcDelta=0;
        Journee jprec=championnat.getJourneeByID(o.getJournee().getId()-1);
        Journee jnext=championnat.getJourneeByID(o.getJournee().getId()+1);
        if(o.getRencontre().isConcerne(this.equipe,this.mode)){
            if (jprec != null && this.journees.contains(o.getJournee().getId())) {
                //System.out.println(jprec.toString());
                for (Rencontre r : jprec.getRencontres().values()) {
                    if (r.isConcerne(this.equipe, this.mode)) {
                        valcDelta++;
                    }
                }
            }
            if (jnext != null && this.journees.contains(jnext.getId())) {
                for (Rencontre r : jnext.getRencontres().values()) {
                    if (r.isConcerne(this.equipe, this.mode)) {
                        valcDelta++;
                    }
                }
            }
        }
        return valcDelta;
    }

    private int traitementModes(Solution championnat,Rencontre rEquipe,TypeMode mode) {
        Journee jprec=championnat.getJourneeByID(rEquipe.getJournee().getId()-1);
        if(jprec==null)return 0;
        for(Rencontre rjprec : jprec.getRencontres().values()){
            switch (mode){
                case DOMICILE:
                    if(rjprec.getDomicile().equals(rEquipe.getDomicile())){
                        return 1;
                    }
                case EXTERIEUR:
                    if(rjprec.getExterieur().equals(rEquipe.getExterieur())){
                        return 1;
                    }
            }
        }
        return 0;
    }


    private Journee nextJournee(Solution championnat, Journee journee){
        return championnat.getJourneeByID(journee.getId()+1);
    }

    private Journee precJournee(Solution championnat, Journee journee){
        return championnat.getJourneeByID(journee.getId()-1);
    }


    @Override
    public int evalDeltaCout(Solution championnat, Operateur o) {
        Object valcDelta= (Integer) evalDeltaCoef(championnat,o);
        return evalDeltaCout(championnat, o, valcDelta);
    }


    @Override
    public int evalDeltaCout(Solution championnat, Operateur o, Object valcDelta) {
        if(o instanceof OperateurInsertion){
            if((Integer)championnat.getCoefContraintes().get(this)+(Integer) valcDelta>max){
                if (estDure()) return Integer.MAX_VALUE;
                //au dela du max le cout suit une relation lineaire le deltat cout est donc proportionel
                return this.penalite *((Integer)valcDelta);
            }else return 0;
        }
        //TODO d'autre operation implique d'autre cout
        return 0;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ContraintePauseEquipe{");
        sb.append("equipe=").append(equipe).append(", ");
        sb.append("journees=").append(journees).append(", ");
        sb.append("mode=").append(mode).append(", ");
        sb.append("max=").append(max).append(", ");
        sb.append("penalite=").append(penalite).append(", ");
        sb.append("dure=").append(estDure()).append("}");
        return sb.toString();
    }
}
