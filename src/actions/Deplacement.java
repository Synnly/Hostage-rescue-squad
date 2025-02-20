package actions;

import carte.Case;
import observable.Environnement;
import personnages.Personnage;

import java.util.ArrayList;
import java.util.List;

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
        int distance = Math.abs(perso.getX() - arr.x) + Math.abs(perso.getY() - arr.y);

        if (distance> perso.getPointsAction()){
            System.out.println("Pas assez de PA (" + distance + " =/= " + perso.getPointsAction() + ")");
        }
        else {
            perso.setX(arr.x);
            perso.setY(arr.y);
            perso.removePointsAction(distance);
        }

    }

    @Override
    public List<Case> getCasesValides(Environnement env, Personnage perso) {
        ArrayList<Case> cases = new ArrayList<>();
        int persoX = perso.getX();
        int persoY = perso.getY();
        int persoPA = perso.getPointsAction();

        for (Case c: env.getPlateau()) {
            if(Math.abs(persoX - c.x) + Math.abs(persoY - c.y) <= persoPA){
                cases.add(c);
            }
        }
        return cases;
    }


}
