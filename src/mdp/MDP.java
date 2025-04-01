package mdp;

import java.util.Map;

public interface MDP {

    double valeurReussite = 500;
    double valeurObjectif = 500;
    double valeurEchec = -999999999;
    double valeurDeltaMenace = 2; // > 0 quand niveau de menace augmente, < 0 sinon
    double valeurTuerEnnemi = 2;
    double valeurDeplacement = -1;

    /**
     * Calcule les actions valides pouvant être effectués pour chaque état
     * @return Le dictionnaire qui associe à chaque état la liste des actions valides
     */
    Map<Etat, Action[]> getActions();

    /**
     * Calcule l'ensemble des états possibles
     * @return La liste des états
     */
    Etat[] getEtats();

    /**
     * Calcule la distribution de probabilité pour tous les états accessibles à partir de l'état de depart et de l'action
     * effectuée
     * @param s L'état de départ
     * @param a L'action à effectuer
     * @return Le dictionnaire qui associe à chaque état la probabilité d'y arriver
     */
    Map<Etat, Double> transition(Etat s, Action a);

    /**
     * Calcule la récompense de la transition T(s, a, s')
     * @param s L'état de départ
     * @param a L'action à effectuer
     * @param sPrime L'état d'arrivée
     * @return La récompense
     */
    double recompense(Etat s, Action a, Etat sPrime);
}
