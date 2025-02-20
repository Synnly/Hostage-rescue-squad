package actions;

import carte.Case;
import observable.Environnement;
import personnages.Operateur;
import personnages.Personnage;

import java.util.List;

public abstract class Action {
    public final int cout;
    public final double probaSucces;

    /**
     * Constructeur d'une action type
     * @param cout Le cout de cette action
     * @param probaSucces La probabilité de succès allant de 1 à 0
     */
    public Action(int cout, double probaSucces){
        this.cout = cout;
        this.probaSucces = probaSucces;
    }

    /**
     * Effectue l'action associée
     * @param env L'environnement
     * @param perso Le personnage effectuant l'action
     * @param arr La case visée par l'action
     */
    public abstract void effectuer(Environnement env, Personnage perso, Case arr);

    /**
     * Effectue l'action associée à un opérateur
     * @param env L'environnement
     * @param perso L'opérateur effectuant l'action
     * @param arr La case visée par l'action
     */
    public abstract void effectuer(Environnement env, Operateur perso, Case arr);

    /**
     * Renvoie la liste des toutes les cases valides pour effectuer l'action
     * @param env L'environnement
     * @param perso Le personnage effectuant l'action
     * @return La liste des cases
     */
    public abstract List<Case> getCasesValides(Environnement env, Personnage perso);


    /**
     * Renvoie la liste des toutes les cases valides pour effectuer l'action spécifiques aux opérateurs
     * @param env L'environnement
     * @param perso L'opérateur effectuant l'action
     * @return La liste des cases
     */
    public abstract List<Case> getCasesValides(Environnement env, Operateur perso);
}
