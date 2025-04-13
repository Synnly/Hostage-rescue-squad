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
            effectuer(env, (Operateur) perso, arr);
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
            //System.out.println("Pas assez de PA pour effectuer l'action");
        }else{
            if(env.aEnnemisSurCase(arr) && arr.peutVoir()){
                List<Double> nombres = env.getNombresAleatoires(1);
                if(nombres.get(0) > probaSucces){
                    //System.out.println("L'action a échoué");
                }
                else {
                    env.tuerEnnemis(arr);
                    if (env.tousTerrsMorts()) {
                        env.resetMenace();
                    }
                    perso.removePointsAction(cout);
                }

            }
        }
    }

    @Override
    public List<Case> getCasesValides(Environnement env, Personnage perso) {
        return getCasesValides(env, env.getCase(perso.getX(), perso.getY()));
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
        boolean skip;

        if(caseDepart == AucuneCase.instance) return cases;

        if(caseX < env.getLargeur() - 1) { // Droite
            skip = false;
            if(!env.getCase(caseX + 1, caseY).peutVoir()) skip = true;
            for(Separation sep:env.getSeparations()){
                if(!sep.estVertical() || sep.getCase1().y != caseY) continue;
                if((sep.getCase1().x == caseX && sep.getCase2().x == caseX + 1) || (sep.getCase2().x == caseX && sep.getCase1().x == caseX + 1)) skip = true;
                if(skip) break;
            }
            if(!skip && env.aEnnemisSurCase(env.getCase(caseX + 1, caseY))) cases.add(env.getCase(caseX + 1, caseY));
        }
        if(caseX > 0) {    // Gauche
            skip = false;
            if(!env.getCase(caseX - 1, caseY).peutVoir()) skip = true;
            for(Separation sep:env.getSeparations()){
                if(!sep.estVertical() || sep.getCase1().y != caseY) continue;
                if((sep.getCase1().x == caseX && sep.getCase2().x == caseX - 1) || (sep.getCase2().x == caseX && sep.getCase1().x == caseX - 1)) skip = true;
                if(skip) break;
            }
            if(!skip && env.aEnnemisSurCase(env.getCase(caseX - 1, caseY))) cases.add(env.getCase(caseX - 1, caseY));
        }
        if(caseY < env.getHauteur() - 1) { // Bas
            skip = false;
            if(!env.getCase(caseX, caseY + 1).peutVoir()) skip = true;
            for(Separation sep:env.getSeparations()){
                if(sep.estVertical() || sep.getCase1().x != caseX) continue;
                if((sep.getCase1().y == caseY && sep.getCase2().y == caseY + 1) || (sep.getCase2().y == caseY && sep.getCase1().y == caseY + 1)) skip = true;
                if(skip) break;
            }
            if(!skip && env.aEnnemisSurCase(env.getCase(caseX, caseY + 1))) cases.add(env.getCase(caseX, caseY + 1));
        }
        if(caseY > 0) {    // Haut
            skip = false;
            if(!env.getCase(caseX, caseY - 1).peutVoir()) skip = true;
            for(Separation sep:env.getSeparations()){
                if(sep.estVertical() || sep.getCase1().x != caseX) continue;
                if((sep.getCase1().y == caseY && sep.getCase2().y == caseY - 1) || (sep.getCase2().y == caseY && sep.getCase1().y == caseY - 1)) skip = true;
                if(skip) break;
            }
            if(!skip && env.aEnnemisSurCase(env.getCase(caseX, caseY - 1))) cases.add(env.getCase(caseX, caseY - 1));
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
