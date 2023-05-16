package instance.modele.contrainte;

import operateur.OperateurInsertion;
import operateur.Operateur;
import solution.Journee;
import solution.Solution;
import solution.Rencontre;

import java.util.TreeSet;

/** classe définissant ContraintePauseGlobale (hérite de Contrainte)
 * @author Engloo Benjamin
 * @author Morcq Alexandre
 * @author Sueur Jeanne
 * @author Lux Hugo
 * @version 1.1
 */
public class ContraintePauseGlobale extends Contrainte{
    private TreeSet<Integer> journees;
    private TreeSet<Integer>  equipes;
    private Integer max;

    public ContraintePauseGlobale(Integer max) {
        super();
        this.journees = new TreeSet<>();
        this.equipes = new TreeSet<>();
        this.max = max;
    }

    public ContraintePauseGlobale(Integer max, Integer penalite) {
        super(penalite);
        this.journees = new TreeSet<>();
        this.equipes = new TreeSet<>();
        this.max = max;
    }

    /**
     * Ajoute une équipe à la liste des équipes de la contrainte
     * @param id l'ID de l'équipe à ajouter
     * @return true si l'ajout a réussi, false sinon
     */
    public boolean addEquipe(Integer id){
        return equipes.add(id);
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
        return TypeContrainte.PAUSEGLOBALE;
    }

    //TODO implementer les fonction de calcule de cout en sinspirent de la contrainte de placement, réflechire si on ne peux pas factoriser du code sout des fonction comune aux contraintes
    @Override
    public int getCoutTotal(Solution championnat) {
        int valc = 0;

        /*for (Integer e : this.equipes) {
            for (Integer j : this.journees) {
                Rencontre rEquipe = null;
                // Pour chaque rencontre de la journée courante, on prend celles de l'équipe de la contrainte
                for (Rencontre r : championnat.getJourneeByID(j).getRencontres().values()) {
                    if(r.isConcerne(championnat.getEquipes().get(e), TypeMode.INDEFINI)) {
                        rEquipe = r;
                        break;
                    }
                }
                valc += this.traitementModes(championnat, j, rEquipe);
            }
        }

        if(valc > this.max) {
            if (estDure()) return Integer.MAX_VALUE;
            return this.penalite * (valc-this.max);
        }
        return 0;*/

        for (Integer j : this.journees) {
            for (Rencontre r : championnat.getJourneeByID(j).getRencontres().values()) {
                if(this.equipes.contains(r.getDomicile().getId())){
                    valc+=traitementModes(championnat,r,TypeMode.DOMICILE);
                }
                if(this.equipes.contains(r.getExterieur().getId())){
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
        System.out.println("debut");
        int valcDelta=0;
        Journee jprec=championnat.getJourneeByID(o.getJournee().getId()-1);
        Journee jnext=championnat.getJourneeByID(o.getJournee().getId()+1);

        if(jprec!=null&&this.journees.contains(o.getJournee().getId())){

            System.out.println(jprec.toString());
            for(Rencontre r:jprec.getRencontres().values()){
                if(r.isConcerne(o.getRencontre().getDomicile(),TypeMode.DOMICILE)){
                    System.out.printf("'''''''''''''''''''''''''''''''''''"+r.toString()+"\n");
                    valcDelta++;
                };
                if(r.isConcerne(o.getRencontre().getExterieur(),TypeMode.EXTERIEUR)){
                    System.out.printf(r.toString()+"\n");
                    valcDelta++;
                }
            }
        }
        System.out.println("Milieu");
        if(jnext!=null&&this.journees.contains(jnext.getId())){
            System.out.println(jnext.toString());
            for(Rencontre r : jnext.getRencontres().values()){
                if(r.isConcerne(o.getRencontre().getDomicile(), TypeMode.DOMICILE)){
                    System.out.printf(r.toString()+"\n");
                    valcDelta++;
                }
                if(r.isConcerne(o.getRencontre().getExterieur(), TypeMode.EXTERIEUR))
                {
                    System.out.printf(r.toString()+"\n");
                    valcDelta++;
                }
            }
        }
        System.out.println("Fin");
       return valcDelta;
    }

    /**
     * retoune 1 si la rencontre requipe passé en paramètre implique une pause dans le mode sinon 0
     * @param championnat le championnat dans lequelle rechercher
     * @param rEquipe la rencontre a utiliser
     * @return int
     */
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

    @Override
    public int evalDeltaCout(Solution championnat, Operateur o) {
        Integer valcDelta= (Integer) evalDeltaCoef(championnat,o);
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
        sb.append("ContraintePauseGlobale{");
        sb.append("journees=").append(journees).append(", ");
        sb.append("equipes=").append(equipes).append(", ");
        sb.append("max=").append(max).append(", ");
        sb.append("penalite=").append(penalite).append(", ");
        sb.append("dure=").append(estDure()).append("}");
        return sb.toString();
    }
}
