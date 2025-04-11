package mdp;

import coups.Coup;
import org.javatuples.Pair;

import java.util.Map;

public interface MDP {

    double valeurReussite = 900;
    double valeurObjectif = 100;
    double valeurEchec = -1000;
    double valeurDeltaMenace = 100; // > 0 quand niveau de menace augmente, < 0 sinon
    double valeurTuerEnnemi = 50;
    double valeurDeplacement = -40;

    /**
     * Calcule les actions valides pouvant être effectués pour chaque état
     * @return Le dictionnaire qui associe à chaque état la liste des actions valides
     */
    Map<Etat, Pair<Coup, Direction>[]> getCoups();

    /**
     * Calcule l'ensemble des états possibles
     * @return La liste des états
     */
    Etat[] getEtats();

    /**
     * Calcule la distribution de probabilité pour tous les états accessibles à partir de l'état de depart et de l'action
     * effectuée
     * @param etatDepart L'état de départ
     * @param coup Le coup à effectuer
     * @return Le dictionnaire qui associe à chaque état la probabilité d'y arriver
     */
    Map<Etat, Double> transition(Etat etatDepart, Coup coup, Direction direction);

    /**
     * Calcule la récompense de la transition T(s, a, s')
     * @param s L'état de départ
     * @param c Le coup à effectuer
     * @param sPrime L'état d'arrivée
     * @return La récompense
     */
    double recompense(Etat s, Coup c, Etat sPrime);
}
