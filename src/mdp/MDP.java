package mdp;

import carte.Case;
import coups.Coup;
import coups.Deplacement;
import coups.FinTour;
import coups.Tir;
import observable.Environnement;
import personnages.Operateur;
import personnages.Personnage;

import java.util.HashMap;
import java.util.Map;

public interface MDP {

    double valeurEchec = -10000;
    double valeurReussite = 10000;
    double valeurObjectif = 10000;
    double valeurDeltaMenace = 2; // > 0 quand niveau de menace augmente, < 0 sinon
    double valeurTuerEnnemi = 2;
    double valeurDeplacement = -1;

    Map<Etat, Action[]> getActions();

    Etat[] getEtats();

    Map<Etat, Double> transition(Etat s, Action a);

    double recompense(Etat s, Action a, Etat sPrime);
}
