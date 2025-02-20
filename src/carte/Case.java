package carte;

public abstract class Case {
    public final int x, y;
    public final boolean peutVoir;
    public double recompense;

    /**
     * Constructeur d'une case type
     * @param x Sa coordonnée en largeur
     * @param y Sa coordonnée en hauteur
     */
    public Case(int x, int y){
        this.x = x;
        this.y = y;
        this.peutVoir = true;
        this.recompense = 0.;
    }
}
