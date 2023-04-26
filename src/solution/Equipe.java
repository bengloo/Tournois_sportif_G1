package solution;

/** classe définissant Equipe
 * @author Engloo Benjamin
 * @author Morcq Alexandre
 * @author Sueur Jeanne
 * @author Lux Hugo
 * @version 1.0
 */
public class Equipe {
    private Integer id;

    public Equipe(Integer id) {
        this.id = id;
    }

    /**
     * Indique l'ID de l'équipe courante
     * @return l'ID en question
     */
    public Integer getId() {
        return this.id;
    }

    /**
     * Renvoie une chaîne de caractères longue qui caractérise une équipe
     * @return la chaîne de caractères avec l'ID de l'équipe
     */
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
