package personnages;

import coups.Coup;
import coups.Deplacement;
import coups.FinTour;
import coups.Tir;
import observable.Environnement;
import outils.FabriqueIdentifiant;


/**
 * Type abstrait représentant un personnage effectuant des actions sur le plateau.
 */
public abstract class Personnage {
    protected int id;
    /**
     * Sa coordonnée en largeur
     */
    protected int x;
    /**
     * Sa coordonnée en hauteur
     */
    protected int y;
    /**
     * La quantité maximale de points d'actions de ce personnage.
     */
    protected final int maxPointsAction;
    /**
     * La quantité actuelle de points d'actions de ce personnage.
     */
    protected int pointsAction;
    /**
     * L'action active de ce personnage.&nbsp;L'action déplacement est l'action par défaut si ce personnage effectue une
     * action sans avoir choisi d'action.
     *
     * @see Deplacement
     */
    protected Coup coupActive;
    /**
     * L'action de déplacement de ce personnage.&nbsp;Cette action définit comment et à quel cout ce personnage se
     * déplace sur le plateau.
     *
     * @see Deplacement
     */
    protected final Deplacement deplacement;
    /**
     * L'action de tir de ce personnage.&nbsp;Cette action définit comment et à quel cout ce personnage tire sur un
     * autre.
     *
     * @see Tir
     */
    protected final Tir tir;
    /**
     * L'action de fin de tour de ce personnage.&nbsp;Cette action est la même pour tous les personnages.
     *
     * @see FinTour
     */
    protected final FinTour finTour;

    /**
     * Constructeur d'un personnage
     *
     * @param env          L'environnement
     * @param x            Sa coordonnée en largeur.&nbsp;Doit être 0 &le;&nbsp;<code>x</code> &lt;&nbsp;<code>env.largeur</code>
     * @param y            Sa coordonnée en hauteur.&nbsp;Doit être 0 &le;&nbsp;<code>y</code> &lt;&nbsp;<code>env.hauteur</code>
     * @param pointsAction Son nombre de points d'actions.&nbsp;Doit être &ge;&nbsp;0.
     * @param deplacement  Son action de déplacement.&nbsp;Ne change jamais.&nbsp;Ne peut être <code>null</code>.
     * @param tir          Son action de tir.&nbsp;Ne change jamais.&nbsp;Ne peut être <code>null</code>.
     */
    public Personnage(Environnement env, int x, int y, int pointsAction, Deplacement deplacement, Tir tir){
        assert x >= 0 && x < env.getLargeur() : "x doit être 0 <= x < " + env.getLargeur() + "(x = " + x + ")";
        assert y >= 0 && y < env.getHauteur() : "y doit être 0 <= y < " + env.getHauteur() + "(y = " + y + ")";
        assert pointsAction >= 0 : "Nombre de points d'action doit être <= 0 (PA = " + pointsAction + ")";
        assert deplacement != null : "Action déplacement null";
        assert tir != null : "Action tir null";

        this.id = FabriqueIdentifiant.nextIdPersonnage();
        this.x = x;
        this.y = y;
        this.pointsAction = pointsAction;
        this.maxPointsAction = pointsAction;
        this.deplacement = deplacement;
        this.tir = tir;
        this.finTour = new FinTour(1, 0);
    }

    public Personnage(Personnage perso){
        this.id = perso.id;
        this.x = perso.x;
        this.y = perso.y;
        this.pointsAction = perso.pointsAction;
        this.maxPointsAction = perso.maxPointsAction;
        this.deplacement = perso.deplacement;
        this.tir = perso.tir;
        this.finTour = new FinTour(1, 0);
        this.coupActive = perso.coupActive;
    }

    /**
     * Définit quelle est l'action active pour ce personnage.&nbsp;Cette action sera celle exécutée quand le joueur
     * choisira la case cible (valide)
     *
     * @param a L'action
     */
    public void setActionActive(Coup a){
        coupActive = a;}

    /**
     * Renvoie l'action active pour ce personnage
     *
     * @return L 'action active
     */
    public Coup getActionActive() {
        return coupActive;
    }

    /**
     * Définit la coordonnée en hauteur.
     *
     * @param y La coordonnée en hauteur
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * Définit la coordonnée en largeur.
     *
     * @param x La coordonnée en largeur
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Renvoie la coordonnée en largeur.
     *
     * @return La coordonnée en largeur
     */
    public int getX() {
        return x;
    }

    /**
     * Renvoie la coordonnée en hauteur.
     *
     * @return La coordonnée en hauteur
     */
    public int getY() {
        return y;
    }

    /**
     * Renvoie le nombre de points d'actions actuels restant au personnage.
     *
     * @return le nombre de points d'actions
     */
    public int getPointsAction() {
        return pointsAction;
    }

    /**
     * Retire le nombre de points d'actions au personnage.&nbsp;Si le personnage n'a pas assez de points d'actions,
     * met le nombre de points d'action à 0.
     *
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

    public int getMaxPointsAction() {
        return maxPointsAction;
    }

    public int getId() {
        return id;
    }

    /**
     * Renvoie l'action de déplacement de ce personnage
     *
     * @return L 'action de déplacement
     */
    public Deplacement getDeplacement() {
        return deplacement;
    }

    /**
     * Renvoie l'action de tir de ce personnage
     *
     * @return L 'action de tir
     */
    public Tir getTir() {
        return tir;
    }

    /**
     * Renvoie l'action de fin de tour de ce personnage
     *
     * @return L 'action de fin de tour
     */
    public FinTour getFinTour() {
        return finTour;
    }

    /**
     * Renvoie une instance en copie profonde de cet objet. Tous les champs de cette instance sont aussi des copies
     * profondes
     * @return La copie
     */
    public abstract Personnage copy();

    @Override
    public String toString() {
        return "(" + x + ", " + y +") " + pointsAction + " PA";
    }

    /**
     * Indique si ce personnage est un opérateur
     */
    public boolean estOperateur(){
        return false;
    }

    /**
     * Indique si ce personnage est un terroriste
     */
    public boolean estTerroriste(){
        return false;
    }
}
