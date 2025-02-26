package actions;

import carte.Case;
import carte.Objectif;
import observable.Environnement;
import personnages.Operateur;
import personnages.Personnage;
import personnages.Terroriste;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
     * @param perso L'opérateur effectuant le déplacement
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
                List<Double> nombres = env.getNombresAleatoires(1);
                if(nombres.get(0) > probaSucces){
                    System.out.println("L'action a échoué");
                }
                else {
                    perso.setX(arr.x);
                    perso.setY(arr.y);
                    if(arr.estObjectif()){
                        env.recupereObjectif((Objectif) arr, perso);
                    }
                }
                perso.removePointsAction(distance * cout);

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
            if(Math.abs(persoX - c.getX()) + Math.abs(persoY - c.getY()) <= persoPA){
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
        List<Terroriste> ennemis = env.getEnnemis();

        for (Case c: env.getPlateau()) {
            if(Math.abs(persoX - c.getX()) + Math.abs(persoY - c.getY()) <= persoPA){ // Distance suffisamment proche
                boolean aEnnemi = false;
                for(Terroriste ennemi : ennemis){   // Ennemi présent sur la case ?
                    if(ennemi.getX() == c.getX() && ennemi.getY() == c.getY()){
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


    @Override
    public double qvaleur(Environnement env, Operateur op, int xDepart, int yDepart, Case arr, double[][] utilites, double gamma) {

        Operateur opClone = new Operateur(op);
        opClone.setX(xDepart); opClone.setY(yDepart);

        List<Case> casesValides = getCasesValides(env, opClone);

        // Qval initialisé à cas ou le deplacement echoue
        double qval = (1-probaSucces) * (env.getCase(xDepart, yDepart).recompense + gamma * utilites[xDepart][yDepart]);

        if(casesValides.contains(arr) && opClone.getPointsAction() >= cout){
            qval += probaSucces * (arr.recompense + gamma * utilites[arr.x][arr.y]);
        }
        return qval;
    }

    @Override
    public String toString() {
        return "Dep";
    }
}
