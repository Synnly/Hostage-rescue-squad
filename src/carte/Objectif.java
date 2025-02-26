package carte;

import observable.Environnement;

/**
 * Case contenant un objectif de la mission.
 */
public class Objectif extends Case{
    /**
     * Constructeur d'un objectif
     *
     * @param env L'environnement
     * @param x Sa coordonnée en largeur. Doit être 0 &le;&nbsp;<code>x</code> &lt;&nbsp;<code>env.largeur</code>
     * @param y Sa coordonnée en hauteur. Doit être 0 &le;&nbsp;<code>y</code> &lt;&nbsp;<code>env.hauteur</code>
     */
    public Objectif(Environnement env, int x, int y) {
        super(env, x, y, true);
        super.estObjectif = true;
        this.recompense = 10;
    }
}
