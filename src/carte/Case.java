package carte;

/**
 * Type abstrait représentant une case du plateau.
 */
public abstract class Case {
    /**
     * La coordonnée en largeur de la case
     */
    public final int x;
    /**
     * La coordonnée en hauteur de la case
     */
    public final int y;
    /**
     * Indique si les personnages peuvent voir dans et à travers cette case
     */
    public final boolean peutVoir;
    /**
     * La récompense immédiate de la case.&nbsp;N'est utile que pour les PDM.
     */
    public double recompense;
    /**
     * Indique si cette case est un objectif, ie si cette case est de type <code>Objectif</code>
     *
     * @see Objectif
     */
    public boolean estObjectif;

    /**
     * Constructeur d'une case type
     *
     * @param x Sa coordonnée en largeur
     * @param y Sa coordonnée en hauteur
     */
    public Case(int x, int y){
        this.x = x;
        this.y = y;
        this.peutVoir = true;
        this.recompense = 0.;
        this.estObjectif = false;
    }
}
