package personnages;

import actions.Deplacement;
import actions.Tir;

public class Operateur extends Personnage{
    private boolean possedeObjectif = false;

    /**
     * Constructeur d'un opérateur
     * @param x Sa coordonnée en largeur
     * @param y Sa coordonnée en hauteur
     * @param pointsAction Son nombre de points d'actions
     * @param deplacement Son action de déplacement. Ne change jamais
     * @param tir Son action de tir. Ne change jamais
     */
    public Operateur(int x, int y, int pointsAction, Deplacement deplacement, Tir tir) {
        super(x, y, pointsAction, deplacement, tir);
    }

    /**
     * Indique si cet opérateur a un objectif
     * @return vrai s'il a l'objectif, faux sinon
     */
    public boolean possedeObjectif() {
        return possedeObjectif;
    }

    /**
     * Définit si l'opérateur possède l'objectif
     * @param possedeObjectif vrai s'il a l'objectif, faux sinon
     */
    public void setPossedeObjectif(boolean possedeObjectif) {
        this.possedeObjectif = possedeObjectif;
    }
}
