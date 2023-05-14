package operateur;

import java.util.concurrent.LinkedBlockingQueue;

public class ListeTabou {

    private final static int capacity= 10;
    private  LinkedBlockingQueue<OperateurLocal> liste;

    private static ListeTabou instance;

    private  int delatAspiration;

    private ListeTabou() {
        instance=null;
        this.liste= new LinkedBlockingQueue<>();
        this.delatAspiration=0;
    }

    public int getDelatAspiration() {
        return delatAspiration;
    }

    public void setDelatAspiration(int delatAspiration) {
        this.delatAspiration = delatAspiration;
    }

    public int getCapacity() {
        return capacity;
    }


    public boolean isTabou(OperateurLocal operateur){
        if(operateur.getDeltaCout()<delatAspiration)return false;
        for(OperateurLocal op:this.liste){
            if(op.isTabou(operateur))return true;
        }
        return false;
        //return this.liste.contains(operateur);
    }

    public void vider(){
        this.liste.clear();
    }

    public static ListeTabou getInstance(){
        if(instance==null){
            instance= new ListeTabou();
        }
        return instance;
    }

    public boolean add(OperateurLocal o){

        if(o==null)return false;
        if(this.liste.size()==capacity){
            liste.poll();
        }
        return liste.offer(o);
    }

    @Override
    public String toString() {
        return "ListeTabou{" +
                "capacity=" + capacity +
                ", liste=" + liste +
                '}';
    }
}
