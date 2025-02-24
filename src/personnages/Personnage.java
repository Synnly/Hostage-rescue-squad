package personnages;

import actions.Action;
import actions.Deplacement;
import actions.FinTour;
import actions.Tir;
import observable.Environnement;


public abstract class Personnage {
    protected int x, y;
    protected final int maxPointsAction;
    protected int pointsAction;
    protected Action actionActive;
    protected final Deplacement deplacement;
    protected final Tir tir;
    protected final FinTour finTour;

    /**
     * Constructeur d'un personnage
     * @param env L'environnement
     * @param x Sa coordonnée en largeur
     * @param y Sa coordonnée en hauteur
     * @param pointsAction Son nombre de points d'actions
     * @param deplacement Son action de déplacement. Ne change jamais
     * @param tir Son action de tir. Ne change jamais
     * @throws IllegalArgumentException Si x (resp y) négatif ou supérieur à env.largeur (resp env.hauteur) ou si pointsAction négatif
     * @throws NullPointerException Si deplacement ou tir null
     */
    public Personnage(Environnement env, int x, int y, int pointsAction, Deplacement deplacement, Tir tir){
        if(x < 0 || x >= env.getLargeur()){throw new IllegalArgumentException("Coordonnée x invalide (0 <= x < " + env.getLargeur() + ", mais x = " + x + ")");}
        if(y < 0 || y >= env.getHauteur()){throw new IllegalArgumentException("Coordonnée x invalide (0 <= y < " + env.getHauteur() + ", mais y = " + y + ")");}
        if(pointsAction < 0){throw new IllegalArgumentException("Nombre de points d'action invalide (PA <= 0 mais PA = " + pointsAction + ")");}
        if(deplacement == null){throw new NullPointerException("Action déplacement null");}
        if(tir == null){throw new NullPointerException("Action tir null");}

        this.x = x;
        this.y = y;
        this.pointsAction = pointsAction;
        this.maxPointsAction = pointsAction;
        this.deplacement = deplacement;
        this.tir = tir;
        this.finTour = new FinTour(0, 0);
    }

    /**
     * Définit quelle est l'action active pour ce personnage. Cette action sera celle exécutée quand le joueur
     * choisira la case cible (valide)
     * @param a L'action
     */
    public void setActionActive(Action a){actionActive = a;}

    /**
     * Renvoie l'action active pour ce personnage
     * @return L'action active
     */
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

    /**
     * Retire le nombre de points d'actions au personnage. Si le personnage n'a pas assez de points d'actions,
     * met le nombre de points d'action à 0.
     * @param nb Le nombre de points d'actions à retirer
     */
    public void removePointsAction(int nb) {
        pointsAction = Math.max(0, pointsAction - nb);
    }

    /**
     * Redonne tous les points d'actions au personnage
     */
    public void resetPointsAction(){
        pointsAction = maxPointsAction;
    }

    /**
     * Renvoie l'action de déplacement de ce personnage
     * @return L'action de déplacement
     */
    public Deplacement getDeplacement() {
        return deplacement;
    }

    /**
     * Renvoie l'action de tir de ce personnage
     * @return L'action de tir
     */
    public Tir getTir() {
        return tir;
    }

    /**
     * Renvoie l'action de fin de tour de ce personnage
     * @return L'action de fin de tour
     */
    public FinTour getFinTour() {
        return finTour;
    }


}
