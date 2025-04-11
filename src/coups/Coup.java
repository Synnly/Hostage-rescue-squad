package coups;

import carte.cases.Case;
import observable.Environnement;
import personnages.Operateur;
import personnages.Personnage;

import java.util.List;

/**
 * Type abstrait représentant une action pouvant être effectuée par le joueur comme un déplacement ou un tir.
 */
public abstract class Coup {
    /**
     * Le cout en points d'actions de cette action.&nbsp;Ce cout est déduit des points d'actions du personnage lorsqu'il
     * effectue cette action.
     */
    public final int cout;
    /**
     * La probabilité de succès de l'action.&nbsp;Une probabilité de 1 représente une action n'échouant jamais et 0 au
     * contraire une action échouant toujours
     */
    public double probaSucces;

    /**
     * Constructeur d'une action type
     *
     * @param cout        Le cout de cette action
     * @param probaSucces La probabilité de succès allant de 1 à 0
     */
    public Coup(int cout, double probaSucces){
        this.cout = cout;
        this.probaSucces = probaSucces;
    }

    /**
     * Constructeur de copie d'un coup
     * @param c Le coup à copier
     */
    public Coup(Coup c){
        this.cout = c.cout;
        this.probaSucces = c.probaSucces;
    }

    /**
     * Effectue cette action au personnage.
     *
     * @param env   L'environnement
     * @param perso Le personnage effectuant cette action
     * @param arr   La case visée par cette action
     */
    public abstract void effectuer(Environnement env, Personnage perso, Case arr);

    /**
     * Effectue cette action à l'opérateur
     *
     * @param env   L'environnement
     * @param perso L'opérateur effectuant cette action
     * @param arr   La case visée par cette action
     */
    public abstract void effectuer(Environnement env, Operateur perso, Case arr);

    /**
     * Renvoie la liste des toutes les cases valides pour effectuer l'action.&nbsp;Si aucune case n'est valide, renvoie une
     * liste vide.
     *
     * @param env   L'environnement
     * @param perso Le personnage effectuant cette action
     * @return La liste des cases valides
     */
    public abstract List<Case> getCasesValides(Environnement env, Personnage perso);


    /**
     * Renvoie la liste des toutes les cases valides pour effectuer l'action, spécifiquement en rapport aux opérateurs
     * &nbsp;Si aucune case n'est valide, renvoie une liste vide.
     *
     * @param env   L'environnement
     * @param perso L'opérateur effectuant cette action
     * @return La liste des cases valides
     */
    public abstract List<Case> getCasesValides(Environnement env, Operateur perso);

    /**
     * Renvoie la liste des toutes les cases valides pour effectuer l'action, spécifiquement&nbsp;Si aucune case n'est valide, renvoie une liste vide.
     *
     * @param env   L'environnement
     * @param caseDepart La case sur laquelle effectuer le coup
     * @return La liste des cases valides
     */
    public abstract List<Case> getCasesValides(Environnement env, Case caseDepart);

    public abstract Coup copy();

    /**
     * Indique si le coup est le déplacement
     */
    public boolean estDeplacement(){
        return false;
    }

    /**
     * Indique si le coup est le tir
     */
    public boolean estTir(){
        return false;
    }

    /**
     * Indique si le coup est le fin de tour
     */
    public boolean estFinTour(){
        return false;
    }

    /**
     * Indique si le coup est une élimination silencieuse
     */
    public boolean estElimSil(){return false;}
}
