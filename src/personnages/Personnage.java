package personnages;

import actions.Action;
import actions.Deplacement;


public abstract class Personnage {
    protected int x, y;
    protected final int maxPointsAction;
    protected int pointsAction;
    protected Action actionActive;
    protected final Deplacement deplacement;

    public Personnage(int x, int y, int pointsAction, Deplacement deplacement){
        this.x = x;
        this.y = y;
        this.pointsAction = pointsAction;
        this.maxPointsAction = pointsAction;
        this.deplacement = deplacement;
    }

    /**
     * Définit quelle est l'action active pour ce personnage. Cette action sera celle exécutée quand le joueur
     * choisira la case cible (valide)
     * @param a L'action
     */
    public void setActionActive(Action a){actionActive = a;}

    public Action getActionActive() {
        return actionActive;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getPointsAction() {
        return pointsAction;
    }

    public void removePointsAction(int nb) {
        pointsAction -= nb;
    }

    public void resetPointsAction(){
        pointsAction = maxPointsAction;
    }

    public Deplacement getDeplacement() {
        return deplacement;
    }
}
