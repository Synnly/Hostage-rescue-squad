package actions;

import carte.Case;
import observable.Environnement;
import personnages.Personnage;

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
}
