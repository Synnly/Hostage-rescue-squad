package mdp;

import coups.Coup;
import org.javatuples.Pair;

import java.util.*;

public class IterationValeur {
    private static double gamma = 0.99;
    private static double epsilon = 1e-50;
    private static Map<Etat, Pair<Coup, Direction>> bestCoup = new HashMap<>();
    private static Map<Etat, Double> utils;
    /**
     * Calcule la meilleure action à faire à partir de l'état de départ
     * @param mdp Le ldp sur lequel appliquer l'algo
     * @param s L'état de départ
     * @return La meilleure action à effectuer
     */
    public static Pair<Coup, Direction> predict(MDP mdp, Etat s) {
        if(bestCoup.isEmpty()) {
            iterationValeur(mdp);
        }
        System.out.println(utils.get(s));
        return bestCoup.get(s);
    }

    /**
     * Calcule la Q-valeur de la transition T(s, a)
     * @param mdp Le mdp sur lequel appliquer l'algo
     * @param s L'état de départ
     * @param c Le coup à effectuer
     * @param utils Le dictionnaire associant à chaque état son utilité
     * @return La Q-valeur
     */
    public static double qValeur(MDP mdp, Etat s, Coup c, Direction direction, Map<Etat, Double> utils) {
        double util = 0;
        if(!s.estTerminal()) {
            Map<Etat, Double> distribution = mdp.transition(s, c, direction);
            for (Etat sPrime : distribution.keySet()) {

                util += distribution.get(sPrime) * (mdp.recompense(s, c, sPrime) + gamma * utils.get(sPrime));
            }
        }
        return util;
    }

    /**
     * Applique l'algorithme itération valeur
     * @param mdp Le mdp sur lequel appliquer l'algo
     * @return Le dictionnaire associant à chaque état son utilité
     */
    public static Map<Etat, Pair<Coup, Direction>> iterationValeur(MDP mdp) {
        utils = new HashMap<>();
        Map<Etat, Pair<Coup, Direction>[]> coups = mdp.getCoups();
        Etat[] etats = mdp.getEtats();
        System.out.println("Itération valeur sur " + etats.length + " états");

        for (Etat e : etats) {
            utils.put(e, 0.);
            bestCoup.put(e, null);
        }

        int nbIter = 0;
        double delta;
        do {
            delta = 0;
            Map<Etat, Double> utilClone = new HashMap<>();

            for (Etat e : etats) {
                double max = Double.NEGATIVE_INFINITY;

                for (Pair<Coup, Direction> c : coups.get(e)) {
                    double qval = qValeur(mdp, e, c.getValue0(), c.getValue1(), utils);

                    if (qval > max) {
                        max = qval;
                        bestCoup.put(e, c);
                    }
                }
                utilClone.put(e, max);
                delta = Math.max(delta, Math.abs(utilClone.get(e) - utils.get(e)));
            }

            utils = utilClone;
            nbIter ++;
        }
        while (delta > epsilon * (1 - gamma) / gamma);
        System.out.println(nbIter + " itérations");
        return bestCoup;
    }
}

