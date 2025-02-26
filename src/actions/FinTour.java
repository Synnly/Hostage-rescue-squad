package actions;

import carte.Case;
import observable.Environnement;
import personnages.Operateur;
import personnages.Personnage;

import java.util.List;
import java.util.Map;

/**
 * Action de fin de tour représentant un personnage finissant son tour et donnant la main au prochain
 */
public class FinTour extends Action{
    /**
     * Constructeur d'une action de fin de tour
     *
     * @param ignoredInt    Ignoré
     * @param ignoredDouble Ignoré
     */
    public FinTour(int ignoredInt, double ignoredDouble) {
        super(ignoredInt, ignoredDouble);
    }

    /**
     * Consomme tous les points d'action du personnage et passe au prochain
     */
    @Override
    public void effectuer(Environnement env, Personnage perso, Case arr) {
        perso.removePointsAction(perso.getPointsAction());
    }

    /**
     * Consomme tous les points d'action de l'opérateur et passe au prochain
     */
    @Override
    public void effectuer(Environnement env, Operateur perso, Case arr) {
        perso.removePointsAction(perso.getPointsAction());
    }

    @Override
    public List<Case> getCasesValides(Environnement env, Personnage perso) {
        return List.of();
    }

    @Override
    public List<Case> getCasesValides(Environnement env, Operateur perso) {
        return List.of();
    }

    @Override
    public double qvaleur(Environnement ignoredEnv, Operateur ignoredOp, int xDepart, int yDepart, Case ignoredArr, double[][] utilites, double gamma) {
        return -0.01 + gamma * utilites[xDepart][yDepart];
    }

    @Override
    public String toString() {
        return "Fin";
    }
}
