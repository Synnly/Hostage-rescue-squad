package carte;

import observable.Environnement;

/**
 * Case normale.&nbsp;Cette classe est une application directe de la classe abstraite <code>Case</code> et doit être
 * vue comme une case n'ayant aucune particularité par rapport à sa superclasse.
 */
public class CaseNormale extends Case{

    /**
     * Constructeur d'une case normale
     *
     * @param env L'environnement
     * @param x Sa coordonnée en largeur. Doit être 0 &le;&nbsp;<code>x</code> &lt;&nbsp;<code>env.largeur</code>
     * @param y Sa coordonnée en hauteur. Doit être 0 &le;&nbsp;<code>y</code> &lt;&nbsp;<code>env.hauteur</code>
     */
    public CaseNormale(Environnement env, int x, int y){
        super(env, x, y, true);
    }

    public CaseNormale(CaseNormale c){
        super(c);
    }

    public CaseNormale copy(){
        return new CaseNormale(this);
    }
}
