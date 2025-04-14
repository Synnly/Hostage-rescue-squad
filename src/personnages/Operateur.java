package personnages;

import coups.*;
import observable.Environnement;

/**
 * Personnage représentant les personnages controlés par le joueur
 */
public class Operateur extends Personnage{
    private final Calmer calmer;
    private boolean possedeObjectif = false;
    private EliminationSilencieuse eliminationSilencieuse;

    /**
     * Constructeur d'un opérateur
     *
     * @param env          L'environnement
     * @param x            Sa coordonnée en largeur.&nbsp;Doit être 0 &le;&nbsp;<code>x</code> &lt;&nbsp;<code>env.largeur</code>
     * @param y            Sa coordonnée en hauteur.&nbsp;Doit être 0 &le;&nbsp;<code>y</code> &lt;&nbsp;<code>env.hauteur</code>
     * @param pointsAction Son nombre de points d'actions.&nbsp;Doit être &ge;&nbsp;0.
     * @param deplacement  Son action de déplacement.&nbsp;Ne change jamais.&nbsp;Ne peut être <code>null</code>.
     * @param tir          Son action de tir.&nbsp;Ne change jamais.&nbsp;Ne peut être <code>null</code>.
     * @param elimSil      Son action d'élimination silencieuse.&nbsp;Ne change jamais.&nbsp;Ne peut être <code>null</code>.
     */
    public Operateur(Environnement env, int x, int y, int pointsAction, Deplacement deplacement, Tir tir,EliminationSilencieuse elimSil, Calmer calmer) {
        super(env, x, y, pointsAction, deplacement, tir);
        this.eliminationSilencieuse = elimSil;
        this.calmer = calmer;
    }

    /**
     * Constructeur de copie d'un opérateur
     * @param op L'opérateur à copier
     */
    public Operateur( Operateur op){
        super(op);
        this.calmer = op.calmer;
        this.possedeObjectif = op.possedeObjectif;
        this.eliminationSilencieuse = op.eliminationSilencieuse;
    }

    /**
     * Indique si cet opérateur a un objectif
     *
     * @return <code>true</code> s'il a l'objectif, <code>false</code> sinon
     */
    public boolean possedeObjectif() {
        return possedeObjectif;
    }

    /**
     * Définit si l'opérateur possède l'objectif
     *
     * @param possedeObjectif <code>true</code> s'il a l'objectif, <code>false</code> sinon
     */
    public void setPossedeObjectif(boolean possedeObjectif) {
        this.possedeObjectif = possedeObjectif;
    }

    public Operateur copy(){
        return new Operateur(this);
    }

    @Override
    public boolean estOperateur(){
        return true;
    }

    /**
     * @return L'action d'élimination silencieuse de l'opérateur
     */
    public EliminationSilencieuse getEliminationSilencieuse() {return eliminationSilencieuse;}

    public Calmer getCalmer() {
        return calmer;
    }
}
