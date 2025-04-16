package personnages;

import coups.AppelRenfort;
import coups.Deplacement;
import coups.Tir;
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
    private AppelRenfort appelRenfort;

    /**
     * Constructeur d'un terroriste
     *
     * @param env          L'environnement
     * @param x            Sa coordonnée en largeur.&nbsp;Doit être 0 &le;&nbsp;<code>x</code> &lt;&nbsp;<code>env.largeur</code>
     * @param y            Sa coordonnée en hauteur.&nbsp;Doit être 0 &le;&nbsp;<code>y</code> &lt;&nbsp;<code>env.hauteur</code>
     * @param deplacement  Son action de déplacement.&nbsp;Ne change jamais.&nbsp;Ne peut être <code>null</code>.
     * @param tir          Son action de tir.&nbsp;Ne change jamais.&nbsp;Ne peut être <code>null</code>.
     */
    public Terroriste(Environnement env, int x, int y, int ignored, Deplacement deplacement, Tir tir, AppelRenfort appelRenfort) {
        super(env, x, y, 0, deplacement, tir);
        this.appelRenfort = appelRenfort;
    }

    /**
     * Constructeur de copie d'un terroriste
     * @param t Le terroriste à copier
     */
    public Terroriste(Terroriste t){
        super(t);
        this.routine = t.routine;
        this.appelRenfort = t.appelRenfort;
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

    public Terroriste copy(){
        return new Terroriste(this);
    }

    @Override
    public boolean estTerroriste(){
        return true;
    }

    public AppelRenfort getAppelRenfort() {return this.appelRenfort;}
}
