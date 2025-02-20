package actions;

import carte.Case;
import carte.Objectif;
import observable.Environnement;
import personnages.Operateur;
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

        if (distance * cout > perso.getPointsAction()){
            System.out.println("Pas assez de PA (" + distance + " =/= " + perso.getPointsAction() + ")");
        }
        else {
            perso.setX(arr.x);
            perso.setY(arr.y);
            perso.removePointsAction(distance * cout);
        }
    }

    /**
     * Déplace l'opérateur vers la case renseignée. Si le cout pour aller vers la case dépasse le nombre de points d'actions de l'opérateur, ne fait rien.
     * @param env L'environnement
     * @param perso Le personnage effectuant le déplacement
     * @param arr La destination
     */
    @Override
    public void effectuer(Environnement env, Operateur perso, Case arr) {
        int distance = Math.abs(perso.getX() - arr.x) + Math.abs(perso.getY() - arr.y);

        if (distance * cout > perso.getPointsAction()){
            System.out.println("Pas assez de PA (" + distance + " =/= " + perso.getPointsAction() + ")");
        }
        else if (env.getEnnemi().getX() == arr.x && env.getEnnemi().getY() == arr.y) {
            System.out.println("Un ennemi est présent sur cette case");
        }
        else {
            if(arr.estObjectif){
                env.recupereObjectif((Objectif) arr, perso);
            }
            perso.setX(arr.x);
            perso.setY(arr.y);
            perso.removePointsAction(distance * cout);
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

    /**
     * Renvoie la liste des toutes les cases valides pour déplacer l'opérateur
     * @param env L'environnement
     * @param perso L'opérateur se déplacant
     * @return La liste des cases
     */
    @Override
    public List<Case> getCasesValides(Environnement env, Operateur perso) {
        ArrayList<Case> cases = new ArrayList<>();
        int persoX = perso.getX();
        int persoY = perso.getY();
        int persoPA = perso.getPointsAction();

        for (Case c: env.getPlateau()) {
            if(Math.abs(persoX - c.x) + Math.abs(persoY - c.y) <= persoPA && !(env.getEnnemi().getX() == c.x && env.getEnnemi().getY() == c.y)){
                cases.add(c);
            }
        }
        return cases;
    }


}
