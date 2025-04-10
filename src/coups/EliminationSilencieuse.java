package coups;

import carte.Case;
import carte.Objectif;
import observable.Environnement;
import personnages.Operateur;
import personnages.Personnage;
import personnages.Terroriste;

import java.util.ArrayList;
import java.util.List;

public class EliminationSilencieuse extends Coup{

    public EliminationSilencieuse(int cout, double probaSucces) {
        super(cout, probaSucces);
    }

    public EliminationSilencieuse(Coup c) {
        super(c);
    }

    /**
     * Si le personnage est un opérateur, redirige vers la bonne fonction, sinon ne fait rien
     * @param env   L'environnement
     * @param perso Le personnage
     * @param arr   La case visée
     */
    @Override
    public void effectuer(Environnement env, Personnage perso, Case arr) {
        if(perso.estOperateur()){
            effectuer(env, perso, arr);
            return;
        }
    }

    /**
     * Elimine silencieusement l'ennemi sur la case sélectionnée si l'opérateur à assez de PA et qu'il y a un ennemi sur la case, rien sinon
     * @param env   L'environnement
     * @param perso L'opérateur
     * @param arr   La case visée
     */
    @Override
    public void effectuer(Environnement env, Operateur perso, Case arr) {
        if(perso.getPointsAction() < this.cout){
            System.out.println("Pas assez de PA pour effectuer l'action");
        }else{
            if(env.aEnnemisSurCase(arr)){
                List<Double> nombres = env.getNombresAleatoires(1);
                if(nombres.get(0) > probaSucces){
                    //System.out.println("L'action a échoué");
                }
                else {
                    env.tuerEnnemis(arr);
                    perso.removePointsAction(cout);
                }

            }
        }
    }

    @Override
    public List<Case> getCasesValides(Environnement env, Personnage perso) {
        ArrayList<Case> cases = new ArrayList<>();
        int persoX = perso.getX();
        int persoY = perso.getY();

        if(persoX < env.getLargeur() - 1 && env.aEnnemisSurCase(env.getCase(persoX + 1, persoY))) {
            cases.add(env.getCase(persoX + 1, persoY));
        }
        if(persoX > 0 && env.aEnnemisSurCase(env.getCase(persoX - 1, persoY))) {
            cases.add(env.getCase(persoX - 1, persoY));
        }
        if(persoY < env.getHauteur() - 1 && env.aEnnemisSurCase(env.getCase(persoX, persoY+1))) {
            cases.add(env.getCase(persoX, persoY + 1));
        }
        if(persoY > 0 && env.aEnnemisSurCase(env.getCase(persoX, persoY-1))) {
            cases.add(env.getCase(persoX, persoY - 1));
        }

        return cases;
    }

    /**
     * Renvoie la liste des toutes les cases valides pour qu'un opérateur puisse effectuer une élimination silencieuse
     * @param env L'environnement
     * @param perso L'opérateur voulant effectuer l'élimination
     * @return La liste des cases
     */
    @Override
    public List<Case> getCasesValides(Environnement env, Operateur perso) {
        return getCasesValides(env, env.getCase(perso.getX(), perso.getY()));
    }

    /**
     * Renvoie la liste des toutes les cases valides pour effectuer une élimination silencieuse à partir d'une case
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
                for(Terroriste ennemi : ennemis){   // Ennemi présent sur la case ?
                    if(ennemi.getX() == c.getX() && ennemi.getY() == c.getY()){
                        cases.add(c);
                    }
                }
            }
        }
        return cases;
    }

    @Override
    public String toString() {
        return "ElimSil";
    }

    @Override
    public EliminationSilencieuse copy(){
        return new EliminationSilencieuse(this);
    }

    @Override
    public boolean estElimSil(){
        return true;
    }
}
