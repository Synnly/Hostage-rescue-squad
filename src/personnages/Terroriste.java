package personnages;

import actions.Deplacement;
import carte.Routine;

public class Terroriste extends Personnage{

    private Routine routine;

    /**
     * Constructeur d'un terroriste. Le nombre de PA n'a aucun effet.
     * @param x Sa coordonnée en largeur
     * @param y Sa coordonnée en hauteur
     * @param deplacement Son action de déplacement
     */
    public Terroriste(int x, int y, int ignored, Deplacement deplacement) {
        super(x, y, 0, deplacement);
    }

    /**
     * Définit la routine du terroriste
     * @param routine Sa routine
     */
    public void setRoutine(Routine routine) {
        this.routine = routine;
    }

    /**
     * Renvoie la routine du terroriste
     * @return La routine
     */
    public Routine getRoutine() {
        return routine;
    }
}
