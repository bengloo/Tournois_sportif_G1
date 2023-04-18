package solution;
/** Class definissant Equipe.
 * @author Engloo Benjamin
 * @author Morcq Alexandre
 * @author Sueur Jeanne
 * @author Lux Hugo
 * @version 0.5
 */
public class Equipe {
    private Integer id;

    public Equipe(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return this.id;
    }

    public String toStringLong() {
        return "Equipe{" +
                "id=" + id +
                '}';
    }
    @Override
    public String toString(){
        return id.toString();
    }
}
