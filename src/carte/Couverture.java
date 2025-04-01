package carte;

import observable.Environnement;

public class Couverture extends Case{
    /**
     * Constructeur d'une couverture
     *
     * @param env L'environnement
     * @param x   Sa coordonnée en largeur. Doit être 0 &le;&nbsp;<code>x</code> &lt;&nbsp;<code>env.largeur</code>
     * @param y   Sa coordonnée en hauteur. Doit être 0 &le;&nbsp;<code>y</code> &lt;&nbsp;<code>env.hauteur</code>
     */
    public Couverture(Environnement env, int x, int y) {
        super(env, x, y, false);
    }

    /**
     * Constructeur de copie d'une couverture
     * @param c La couverture à copier
     */
    public Couverture(Couverture c){
        super(c);
    }

    public Couverture copy(){
        return new Couverture(this);
    }
}
