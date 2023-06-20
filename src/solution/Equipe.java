package solution;

import java.util.Objects;

/** classe définissant Equipe
 * @author Engloo Benjamin
 * @author Morcq Alexandre
 * @author Sueur Jeanne
 * @author Lux Hugo
 * @version 1.0
 */
public class Equipe {
    private Integer id;

    /**
     * constructeur d'equipe
     * @param id numero de l'equipe
     */
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Equipe equipe = (Equipe) o;
        return Objects.equals(id, equipe.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
