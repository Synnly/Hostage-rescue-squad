package coups;

import carte.Case;
import carte.Objectif;
import observable.Environnement;
import personnages.Operateur;
import personnages.Personnage;
import personnages.Terroriste;

import java.util.ArrayList;
import java.util.List;

public class Deplacement extends Coup {

    /**
     * Constructeur d'une action déplacement
     * @param cout Le cout de cette action
     * @param probaSucces La probabilité de succès allant de 1 à 0
     */
    public Deplacement(int cout, double probaSucces) {
        super(cout, probaSucces);
    }

    public Deplacement(Deplacement d){
        super(d);
    }
    /**
     * Déplace le personnage vers la case renseignée. Si le cout pour aller vers la case dépasse le nombre de points d'actions du personnage, ne fait rien.
     * @param env L'environnement
     * @param perso L'opérateur effectuant le déplacement
     * @param arr La destination
     */
    @Override
    public void effectuer(Environnement env, Personnage perso, Case arr) {
        if(perso.estOperateur()){ // Si type de perso est operateur mais connu qu'au runtime
            effectuer(env, (Operateur) perso, arr);
            return;
        }
        else if(perso.estTerroriste()){
            effectuer(env, (Terroriste) perso, arr);
            return;
        }

        int distance = Math.abs(perso.getX() - arr.x) + Math.abs(perso.getY() - arr.y);

        if (distance * cout > perso.getPointsAction()){
//            System.out.println("Pas assez de PA (" + distance + " =/= " + perso.getPointsAction() + ")");
        }
        else {
            perso.setX(arr.getX());
            perso.setY(arr.getY());
            perso.removePointsAction(distance * cout);
        }
    }

    public void effectuer(Environnement env, Terroriste terroriste, Case arr){
        terroriste.setX(arr.x);
        terroriste.setY(arr.y);

        if(env.getOperateurActif().getX() == arr.getX() && env.getOperateurActif().getY() == arr.getY()){
            env.terminerMission(false);
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
        int distance = Math.abs(perso.getX() - arr.getX()) + Math.abs(perso.getY() - arr.getY());

        if (distance * cout > perso.getPointsAction()){
//            System.out.println("Pas assez de PA (" + distance + " =/= " + perso.getPointsAction() + ")");
        }
        else {
            boolean ennemiPresent = false;
            for (Terroriste ennemi: env.getEnnemis()) {
                if(ennemi.getX() == arr.getX() && ennemi.getY() == arr.getY()){
                    ennemiPresent = true;
                    break;
                }
            }
            if (ennemiPresent) {
//                System.out.println("Un ennemi est présent sur cette case");
            }
            else {
                List<Double> nombres = env.getNombresAleatoires(1);
                if(nombres.get(0) > probaSucces){
//                    System.out.println("L'action a échoué");
                }
                else {
                    perso.setX(arr.getX());
                    perso.setY(arr.getY());
                    if(arr.estObjectif() && !perso.possedeObjectif()){
                        env.recupereObjectif((Objectif) arr, perso);
                    }

                    if(perso.getY() == env.getHauteur() - 1 && perso.possedeObjectif()){
                        env.terminerMission(true);
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

        if(persoX < env.getLargeur() - 1) {
            cases.add(env.getCase(persoX + 1, persoY));
        }
        if(persoX > 0) {
            cases.add(env.getCase(persoX - 1, persoY));
        }
        if(persoY < env.getHauteur() - 1) {
            cases.add(env.getCase(persoX, persoY + 1));
        }
        if(persoY > 0) {
            cases.add(env.getCase(persoX, persoY - 1));
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
        return getCasesValides(env, env.getCase(perso.getX(), perso.getY()));
    }

    /**
     * Renvoie la liste des toutes les cases valides pour déplacer l'opérateur
     * @param env L'environnement
     * @param caseDepart La case de départ
     * @return La liste des cases
     */
    @Override
    public List<Case> getCasesValides(Environnement env, Case caseDepart) {
        ArrayList<Case> cases = new ArrayList<>();
        int caseX = caseDepart.x;
        int caseY = caseDepart.y;
        List<Terroriste> ennemis = env.getEnnemis();

        for (Case c: env.getPlateau()) {
            if(Math.abs(caseX - c.getX()) + Math.abs(caseY - c.getY()) <= 1 && (caseDepart.x != c.x || caseDepart.y != c.y)){ // Distance suffisamment proche
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
    public String toString() {
        return "Dep";
    }

    public Deplacement copy(){
        return new Deplacement(this);
    }

    @Override
    public boolean estDeplacement(){
        return true;
    }
}
