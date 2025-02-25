package carte;

/**
 * Case normale.&nbsp;Cette classe est une application directe de la classe abstraite <code>Case</code> et doit être
 * vue comme une case n'ayant aucune particularité par rapport à sa superclasse.
 */
public class CaseNormale extends Case{

    /**
     * Constructeur d'une case normale
     *
     * @param x Sa coordonnée en largeur
     * @param y Sa coordonnée en hauteur
     */
    public CaseNormale(int x, int y){
        super(x, y);
        this.recompense = -0.01;
    }
}
