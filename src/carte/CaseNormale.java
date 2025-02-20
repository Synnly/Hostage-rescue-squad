package carte;

public class CaseNormale extends Case{

    /**
     * Constructeur d'une case normale
     * @param x Sa coordonnée en largeur
     * @param y Sa coordonnée en hauteur
     */
    public CaseNormale(int x, int y){
        super(x, y);
        this.recompense = -0.01;
    }
}
