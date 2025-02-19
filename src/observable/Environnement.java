package observable;

public class Environnement extends Observable{

    private final int largeur = 6;
    private final int hauteur = 10;

    public int getLargeur() {
        return largeur;
    }

    public int getHauteur() {
        return hauteur;
    }

    public Environnement(){}
}
