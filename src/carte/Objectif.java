package carte;

/**
 * Case contenant un objectif de la mission.
 */
public class Objectif extends Case{
    /**
     * Constructeur d'un objectif
     *
     * @param x Sa coordonnée en largeur
     * @param y Sa coordonnée en hauteur
     */
    public Objectif(int x, int y) {
        super(x, y);
        super.estObjectif = true;
        this.recompense = 10;
    }
}
