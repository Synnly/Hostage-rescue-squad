package carte;

import observable.Environnement;
import outils.FabriqueIdentifiant;

/**
 * Type abstrait représentant une case du plateau.
 */
public abstract class Case {
    public final int id;
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
     * @param env L'environnement
     * @param x Sa coordonnée en largeur. Doit être 0 &le;&nbsp;<code>x</code> &lt;&nbsp;<code>env.largeur</code>
     * @param y Sa coordonnée en hauteur. Doit être 0 &le;&nbsp;<code>y</code> &lt;&nbsp;<code>env.hauteur</code>
     * @param peutVoir Indique si les personnages peuvent voir dans et à travers cette case
     */
    public Case(Environnement env, int x, int y, boolean peutVoir){
        assert x >= 0 && x < env.getLargeur() : "x doit être 0 <= x < " + env.getLargeur() + "(x = " + x + ")";
        assert y >= 0 && y < env.getHauteur() : "y doit être 0 <= y < " + env.getHauteur() + "(y = " + y + ")";
        this.id = FabriqueIdentifiant.nextIdCase();
        this.x = x;
        this.y = y;
        this.peutVoir = peutVoir;
        this.recompense = -0.1;
        this.estObjectif = false;
    }

    public Case(Case c){
        this.id = c.id;
        this.x = c.x;
        this.y = c.y;
        this.peutVoir = c.peutVoir;
        this.recompense = -0.1;
        this.estObjectif = false;
    }

    protected Case(int x, int y, boolean peutVoir){
        this.id = FabriqueIdentifiant.nextIdCase();
        this.x = x;
        this.y = y;
        this.peutVoir = peutVoir;
        this.recompense = 0;
        this.estObjectif = false;
    }

    public int getX(){
        return x;
    }
    public int getY(){
        return y;
    }
    public boolean peutVoir(){
        return this.peutVoir;
    }

    public boolean estObjectif() {
        return estObjectif;
    }

    /**
     * Renvoie une instance en copie profonde de cet objet. Tous les champs de cette instance sont aussi des copies
     * profondes
     * @return La copie
     */
    public abstract Case copy();

    @Override
    public String toString() {
        return "(" + x + ", " + y + ')';
    }

    public int getId() {
        return id;
    }
}
