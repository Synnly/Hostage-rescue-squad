package personnages;

import actions.Deplacement;
import actions.Tir;
import carte.Routine;
import observable.Environnement;

/**
 * Personnage représentant les ennemis des missions.
 */
public class Terroriste extends Personnage{

    /**
     * Paterne de déplacement sur le plateau
     */
    private Routine routine;

    /**
     * Constructeur d'un terroriste
     *
     * @param env         L'environnement
     * @param x           Sa coordonnée en largeur
     * @param y           Sa coordonnée en hauteur
     * @param ignored     the ignored
     * @param deplacement Son action de déplacement. Ne change jamais
     * @param tir         Son action de tir. Ne change jamais
     */
    public Terroriste(Environnement env, int x, int y, int ignored, Deplacement deplacement, Tir tir) {
        super(env, x, y, 0, deplacement, tir);
    }

    /**
     * Définit la routine du terroriste
     *
     * @param routine Sa routine
     */
    public void setRoutine(Routine routine) {
        this.routine = routine;
    }

    /**
     * Renvoie la routine du terroriste
     *
     * @return La routine
     */
    public Routine getRoutine() {
        return routine;
    }
}
