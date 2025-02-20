package personnages;

import actions.Action;

import java.util.ArrayList;

public abstract class Personnage {
    protected int x, y;
    protected int pointsAction;
    private ArrayList<Action> actions;
    private Action actionActive = null;

    public Personnage(int x, int y, int pointsAction){
        this.x = x;
        this.y = y;
        this.pointsAction = pointsAction;
        actions = new ArrayList<>();
    }

    public void ajouterAction(Action a){actions.add(a);}

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
}
