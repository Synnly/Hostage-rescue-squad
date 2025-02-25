package actions;

import carte.Case;
import carte.Objectif;
import observable.Environnement;
import personnages.Operateur;
import personnages.Personnage;
import personnages.Terroriste;

import java.util.ArrayList;
import java.util.List;

/**
 * Action de déplacement représentant le déplacement du personnage vers une case
 */
public class Deplacement extends Action{

    /**
     * Constructeur d'une action déplacement
     *
     * @param cout        Le cout de cette action
     * @param probaSucces La probabilité de succès allant de 1 à 0
     */
    public Deplacement(int cout, double probaSucces) {
        super(cout, probaSucces);
    }

    /**
     * Déplace le personnage vers la case renseignée.&nbsp;Si le cout pour aller vers la case <code>arr</code> dépasse le
     * nombre de points d'actions du personnage, ne fait rien.
     *
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
     * Déplace l'opérateur vers la case renseignée.&nbsp;Si le cout pour aller vers la case <code>arr</code> dépasse le
     * nombre de points d'actions de l'opérateur, ne fait rien.
     *
     * @param env L'environnement
     * @param perso L'opérateur effectuant le déplacement
     * @param arr La destination
     */
    @Override
    public void effectuer(Environnement env, Operateur perso, Case arr) {
        int distance = Math.abs(perso.getX() - arr.x) + Math.abs(perso.getY() - arr.y);

        if (distance * cout > perso.getPointsAction()){
            System.out.println("Pas assez de PA (" + distance + " =/= " + perso.getPointsAction() + ")");
        }
        else {
            boolean ennemiPresent = false;
            for (Terroriste ennemi: env.getEnnemis()) {
                if(ennemi.getX() == arr.x && ennemi.getY() == arr.y){
                    ennemiPresent = true;
                    break;
                }
            }
            if (ennemiPresent) {
                System.out.println("Un ennemi est présent sur cette case");
            }
            else {

                perso.setX(arr.x);
                perso.setY(arr.y);
                perso.removePointsAction(distance * cout);
                if(arr.estObjectif){
                    env.recupereObjectif((Objectif) arr, perso);
                }
            }
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
     * Renvoie la liste des toutes les cases valides pour déplacer l'opérateur.&nbsp;Si aucune case n'est valide, renvoie une
     * liste vide.
     *
     * @param env L'environnement
     * @param perso L'opérateur se déplaçant
     * @return La liste des cases
     */
    @Override
    public List<Case> getCasesValides(Environnement env, Operateur perso) {
        ArrayList<Case> cases = new ArrayList<>();
        int persoX = perso.getX();
        int persoY = perso.getY();
        int persoPA = perso.getPointsAction();
        List<Terroriste> ennemis = env.getEnnemis();

        for (Case c: env.getPlateau()) {
            if(Math.abs(persoX - c.x) + Math.abs(persoY - c.y) <= persoPA){ // Distance suffisamment proche
                boolean aEnnemi = false;
                for(Terroriste ennemi : ennemis){   // Ennemi présent sur la case ?
                    if(ennemi.getX() == c.x && ennemi.getY() == c.y){
                        aEnnemi = true;
                        break;
                    }
                }
                if (!aEnnemi) {
                    cases.add(c);
                }
            }
        }
        return cases;
    }


}
