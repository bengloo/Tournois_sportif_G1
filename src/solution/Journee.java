package solution;

import java.util.HashMap;
import java.util.Objects;

public class Journee {
    private final Integer id;
    private HashMap<String,Rencontre> rencontres;




    public Journee(Integer id) {
        this.id = id;
        this.rencontres = new HashMap<>();

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

   /* public boolean addRencontre(Rencontre rencontreToADD){
        if(rencontreToADD == null)return false;
        return  this.rencontres.put(rencontreToADD.getLabel(),rencontreToADD) != null;
        //TODO checker interupt
    }*/

    public boolean checkIntegriteeChampionatf(){
        return false;
    }

    /*
    ajoute une rencontre a une journee
    */
    public boolean addRencontre(Rencontre rencontre) throws Exception{
        if(rencontre==null){
            System.err.println("rencontre est null");
            return false;
        }
        if(rencontres.containsKey(rencontre.getLabel())){
            System.err.println("contains");
            return false;
        }

        rencontres.put(rencontre.getLabel(), rencontre);//add


        if(rencontre.setJournee(this)){

            return true;
        }else{
            System.err.println("On remove");
            rencontres.remove(rencontre.getLabel());//remove
            return false;
        }
    }


    public boolean removeRencontre(Rencontre rencontre) throws Exception{
        if(rencontre==null){
            throw new Exception("rencontre is null");
        }
        if(rencontre.isInJournee(this)){
            throw new Exception("isInJournee");
            //return false;
        }

        rencontres.remove(rencontre.getLabel());//remove
        return true;
    }


    public String toStringLong() {
        return "Journee{" +
                "id=" + id +
                '}';
    }

    /*
     *return true si la journee contient la rencontre spécifié
     */
    public boolean isPresent(Rencontre r){
        return getRencontres().containsKey(r);
    }

    public HashMap<String, Rencontre> getRencontres() {
        return rencontres;
    }

    @Override
    public String toString() {
        return "Journee{" +
                "id=" + id +
                ", rencontres=" + rencontres.values() +
                '}';
    }
}
