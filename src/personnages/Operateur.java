package personnages;

import actions.Deplacement;
import actions.Tir;
import observable.Environnement;

/**
 * Personnage représentant les personnages controlés par le joueur
 */
public class Operateur extends Personnage{
    private boolean possedeObjectif = false;

    /**
     * Constructeur d'un opérateur
     *
     * @param env          L'environnement
     * @param x            Sa coordonnée en largeur
     * @param y            Sa coordonnée en hauteur
     * @param pointsAction Son nombre de points d'actions
     * @param deplacement  Son action de déplacement. Ne change jamais
     * @param tir          Son action de tir. Ne change jamais
     */
    public Operateur(Environnement env, int x, int y, int pointsAction, Deplacement deplacement, Tir tir) {
        super(env, x, y, pointsAction, deplacement, tir);
    }

    /**
     * Indique si cet opérateur a un objectif
     *
     * @return Vrai s'il a l'objectif, faux sinon
     */
    public boolean possedeObjectif() {
        return possedeObjectif;
    }

    /**
     * Définit si l'opérateur possède l'objectif
     *
     * @param possedeObjectif Vrai s'il a l'objectif, faux sinon
     */
    public void setPossedeObjectif(boolean possedeObjectif) {
        this.possedeObjectif = possedeObjectif;
    }
}
