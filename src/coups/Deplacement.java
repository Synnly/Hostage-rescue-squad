package coups;

import carte.cases.AucuneCase;
import carte.cases.Case;
import carte.cases.Objectif;
import carte.separation.Separation;
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

    /**
     * Constructeur de copie d'un coup déplacement
     * @param d Le coup à copier
     */
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

    /**
     * Déplace le terroriste vers la case renseignée. Si le cout pour aller vers la case dépasse le nombre de points d'actions du personnage, ne fait rien.
     * @param env L'environnement
     * @param terroriste Le terroriste effectuant le déplacement
     * @param arr La destination
     */
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
            boolean peutPasser = true;
            for (Terroriste ennemi: env.getEnnemis()) {
                if(ennemi.getX() == arr.getX() && ennemi.getY() == arr.getY()){
                    peutPasser = false;
                    break;
                }
            }

            if(peutPasser) {
                for (Separation sep : env.getSeparations()) {
                    if ((sep.getCase1() == arr && sep.getCase2() == env.getCase(perso.getX(), perso.getY())) ||
                            (sep.getCase2() == arr && sep.getCase1() == env.getCase(perso.getX(), perso.getY()))) { // Si separation entre perso et case arr
                        if (!sep.peutPasser()) {
                            peutPasser = false;
                            break;
                        }
                    }
                }
            }
            if (!peutPasser) {
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

        if(persoX < env.getLargeur() - 1) { // Droite
            boolean skip = false;
            for(Separation sep:env.getSeparations()){
                if(!sep.estVertical()) continue;
                if((sep.getCase1().x == persoX && sep.getCase2().x == persoX + 1) || (sep.getCase2().x == persoX && sep.getCase1().x == persoX + 1)){
                    skip = true;
                }
                if(skip) break;
            }
            System.out.println("droite " + skip);
            if(!skip) cases.add(env.getCase(persoX + 1, persoY));
        }
        if(persoX > 0) {    // Gauche
            boolean skip = false;
            for(Separation sep:env.getSeparations()){
                if(!sep.estVertical()) continue;
                if((sep.getCase1().x == persoX && sep.getCase2().x == persoX - 1) || (sep.getCase2().x == persoX && sep.getCase1().x == persoX - 1)){
                    skip = true;
                }
                if(skip) break;
            }
            System.out.println("gauche " + skip);
            if(!skip) cases.add(env.getCase(persoX - 1, persoY));
        }
        if(persoY < env.getHauteur() - 1) { // Bas
            boolean skip = false;
            for(Separation sep:env.getSeparations()){
                if(sep.estVertical()) continue;
                if((sep.getCase1().y == persoY && sep.getCase2().y == persoY + 1) || (sep.getCase2().y == persoY && sep.getCase1().y == persoY + 1)){
                    skip = true;
                }
                if(skip) break;
            }
            System.out.println("bas " + skip);
            if(!skip) cases.add(env.getCase(persoX, persoY + 1));
        }
        if(persoY > 0) {    // Haut
            boolean skip = false;
            for(Separation sep:env.getSeparations()){
                if(sep.estVertical()) continue;
                if((sep.getCase1().y == persoY && sep.getCase2().y == persoY - 1) || (sep.getCase2().y == persoY && sep.getCase1().y == persoY - 1)){
                    skip = true;
                }
                if(skip) break;
            }
            System.out.println("haut " + skip);
            if(!skip) cases.add(env.getCase(persoX, persoY - 1));
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
        boolean skip;

        if(caseDepart == AucuneCase.instance) return cases;

        if(caseX < env.getLargeur() - 1) { // Droite
            skip = false;
            for(Separation sep:env.getSeparations()){
                if(!sep.estVertical() || sep.getCase1().y != caseY) continue;
                if((sep.getCase1().x == caseX && sep.getCase2().x == caseX + 1) || (sep.getCase2().x == caseX && sep.getCase1().x == caseX + 1)) skip = true;
                if(skip) break;
            }
            for(Terroriste t : ennemis){
                if (t.getX() == caseX + 1 && t.getY() == caseY) {
                    skip = true;
                    break;
                }
            }
            if(!skip) cases.add(env.getCase(caseX + 1, caseY));
        }
        if(caseX > 0) {    // Gauche
            skip = false;
            for(Separation sep:env.getSeparations()){
                if(!sep.estVertical() || sep.getCase1().y != caseY) continue;
                if((sep.getCase1().x == caseX && sep.getCase2().x == caseX - 1) || (sep.getCase2().x == caseX && sep.getCase1().x == caseX - 1)) skip = true;
                if(skip) break;
            }
            for(Terroriste t : ennemis){
                if (t.getX() == caseX - 1 && t.getY() == caseY) {
                    skip = true;
                    break;
                }
            }
            if(!skip) cases.add(env.getCase(caseX - 1, caseY));
        }
        if(caseY < env.getHauteur() - 1) { // Bas
            skip = false;
            for(Separation sep:env.getSeparations()){
                if(sep.estVertical() || sep.getCase1().x != caseX) continue;
                if((sep.getCase1().y == caseY && sep.getCase2().y == caseY + 1) || (sep.getCase2().y == caseY && sep.getCase1().y == caseY + 1)) skip = true;
                if(skip) break;
            }
            for(Terroriste t : ennemis){
                if (t.getX() == caseX && t.getY() == caseY + 1) {
                    skip = true;
                    break;
                }
            }
            if(!skip) cases.add(env.getCase(caseX, caseY + 1));
        }
        if(caseY > 0) {    // Haut
            skip = false;
            for(Separation sep:env.getSeparations()){
                if(sep.estVertical() || sep.getCase1().x != caseX) continue;
                if((sep.getCase1().y == caseY && sep.getCase2().y == caseY - 1) || (sep.getCase2().y == caseY && sep.getCase1().y == caseY - 1)) skip = true;
                if(skip) break;
            }
            for(Terroriste t : ennemis){
                if (t.getX() == caseX && t.getY() == caseY - 1) {
                    skip = true;
                    break;
                }
            }
            if(!skip) cases.add(env.getCase(caseX, caseY - 1));
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
