package instance.modele.contrainte;

import operateur.OperateurInsertion;
import operateur.Operateur;
import solution.Solution;
import solution.Rencontre;

import java.util.TreeSet;

/** classe définissant ContraintePlacement (hérite de Contrainte)
 * @author Engloo Benjamin
 * @author Morcq Alexandre
 * @author Sueur Jeanne
 * @author Lux Hugo
 * @version 1.0
 */
public class ContraintePlacement extends Contrainte{
    private Integer equipe;
    private TreeSet<Integer> journees;
    private TypeMode mode;
    private Integer max;

    public ContraintePlacement(Integer equipe,TypeMode mode,Integer max) {
        super();
        this.equipe=equipe;
        this.journees = new TreeSet<>();
        this.mode = mode;
        this.max = max;
    }

    public ContraintePlacement(Integer equipe,TypeMode mode,Integer max, Integer penalite) {
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
        return TypeContrainte.PLACEMENT;
    }


    @Override
    public int getCoutTotal(Solution championnat) {
        //TODO tchequer contrainte inerante si oui renvoyer max integer

        //le nombre de rencontres jouées par l’équipe de la contrainte selon un mode sur l’ensemble des journées
        int valc=0;

        //pour toute les rencontres
        for(Rencontre r:championnat.getRencontres().values()){
            //pour toutes les journees concerné par la contraintes
            valc += parcoursJournees(championnat, r);
        }

        if(valc>this.max) {
            if (estDure()) return Integer.MAX_VALUE;
            return this.penalite *(valc-this.max);
        }
        return 0;
    }

    @Override
    public int evalDeltatCoef(Solution championnat, Operateur o) {
        int valcDelta=0;
        if(o instanceof OperateurInsertion) {
            Rencontre r = o.getRencontre();
            valcDelta = parcoursJournees(championnat, r);
        }
        return valcDelta;
    }

    /**
     * Parcourt les journées de la contrainte pour incrémenter un compteur à chaque fois que son équipe fait partie de la rencontre
     * @param championnat la solution
     * @param r la rencontre concernée
     * @return le nombre entier du compteur
     */
    private int parcoursJournees(Solution championnat, Rencontre r) { //Factorisation du code
        int valcDelta=0;
        for (Integer jID : this.journees) {
            switch (this.mode) {
                case DOMICILE:
                    //si l'équipe concernée par la contrainte est celle de la rencontre et la journée courante contient la rencontre
                    if (r.getDomicile().equals(this.equipe) && championnat.getJournees().get(jID).getRencontres().containsKey(r)) {
                        valcDelta++;
                    }
                    break;
                case EXTERIEUR:
                    if (r.getExterieur().equals(this.equipe) && championnat.getJournees().get(jID).getRencontres().containsKey(r)) {
                        valcDelta++;
                    }
                    break;
                case INDEFINI:
                    if ((r.getDomicile().equals(this.equipe) || r.getExterieur().equals(this.equipe)) && championnat.getJournees().get(jID).getRencontres().containsKey(r)) {
                        valcDelta++;
                    }
                default:
                    //TODO interup process error
            }
        }
        return valcDelta;
    }

    @Override
    public int evalDeltatCout(Solution championnat, Operateur o) {
        Integer valcDelta=evalDeltatCoef(championnat,o);
        return evalDeltatCout(championnat, o, valcDelta);
    }

    @Override
    public int evalDeltatCout(Solution championnat, Operateur o, Integer valcDelta) {
        //TODO tchequer contrainte inerante si oui renvoyer max integer
        if(o instanceof OperateurInsertion){

            if(championnat.getCoefContraintes().get(this)+valcDelta>max){
                if (estDure()) return Integer.MAX_VALUE;
                //au dela du max le cout suit une relation lineaire le deltat cout est donc proportionel
                return this.penalite *(valcDelta);
            }else return 0;
        }
        //TODO d'autre operation implique d'autre cout
        return 0;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ContraintePlacement{");
        sb.append("equipe=").append(equipe).append(", ");
        sb.append("journees=").append(journees).append(", ");
        sb.append("mode=").append(mode).append(", ");
        sb.append("max=").append(max).append(", ");
        sb.append("penalite=").append(penalite).append(", ");
        sb.append("dure=").append(estDure()).append("}");
        return sb.toString();
    }
}
