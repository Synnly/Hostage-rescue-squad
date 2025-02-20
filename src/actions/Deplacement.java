package actions;

import carte.Case;
import observable.Environnement;
import personnages.Personnage;

public class Deplacement extends Action{

    /**
     * Constructeur d'une action déplacement
     * @param cout Le cout de cette action
     * @param probaSucces La probabilité de succès allant de 1 à 0
     */
    public Deplacement(int cout, double probaSucces) {
        super(cout, probaSucces);
    }

    /**
     * Déplace le personnage vers la case renseignée. Si le cout pour aller vers la case dépasse le nombre de points d'actions du personnage, ne fait rien.
     * @param env L'environnement
     * @param perso Le personnage effectuant le déplacement
     * @param arr La destination
     */
    @Override
    public void effectuer(Environnement env, Personnage perso, Case arr) {
        perso.setX(arr.x);
        perso.setY(arr.y);
    }
}
