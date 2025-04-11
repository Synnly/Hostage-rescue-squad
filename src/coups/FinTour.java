package coups;

import carte.cases.AucuneCase;
import carte.cases.Case;
import observable.Environnement;
import personnages.Operateur;
import personnages.Personnage;

import java.util.List;

/**
 * Action de fin de tour représentant un personnage finissant son tour et donnant la main au prochain
 */
public class FinTour extends Coup {
    /**
     * Constructeur d'une action de fin de tour
     *
     * @param ignoredInt    Ignoré
     * @param ignoredDouble Ignoré
     */
    public FinTour(int ignoredInt, double ignoredDouble) {
        super(ignoredInt, 1);
    }

    /**
     * Constructeur de copie d'un coup fin de tour
     * @param f Le coup à copier
     */
    public FinTour(FinTour f){
        super(f);
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
        return List.of(AucuneCase.instance);
    }

    @Override
    public List<Case> getCasesValides(Environnement env, Operateur perso) {
        return List.of(AucuneCase.instance);
    }

    @Override
    public List<Case> getCasesValides(Environnement env, Case caseDepart){
        return List.of(AucuneCase.instance);
    }

    @Override
    public String toString() {
        return "Fin";
    }

    public FinTour copy(){
        return new FinTour(this);
    }

    @Override
    public boolean estFinTour(){
        return true;
    }
}
