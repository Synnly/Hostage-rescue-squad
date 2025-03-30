package mdp;

import java.util.*;

public class IterationValeur {
    private static double gamma = 0.8;
    private static double epsilon = 0.0001;
    private static Map<Etat, Action> bestActions = new HashMap<>();

    /**
     * Calcule la meilleure action à faire à partir de l'état de départ
     * @param mdp Le ldp sur lequel appliquer l'algo
     * @param s L'état de départ
     * @return La meilleure action à effectuer
     */
    public static Action predict(MDP mdp, Etat s) {
        if(bestActions.isEmpty()) {
            iterationValeur(mdp);
        }

        return bestActions.get(s);
    }

    /**
     * Calcule la Q-valeur de la transition T(s, a)
     * @param mdp Le mdp sur lequel appliquer l'algo
     * @param s L'état de départ
     * @param a L'action à effectuer
     * @param utils Le dictionnaire associant à chaque état son utilité
     * @return La Q-valeur
     */
    public static double qValeur(MDP mdp, Etat s, Action a, Map<Etat, Double> utils) {
        double util = 0;
        Map<Etat, Double> distribution = mdp.transition(s, a);
        for (Etat sPrime : distribution.keySet()) {
            if(distribution.get(sPrime)==null){
                System.out.println();
            }
            if(utils.get(sPrime) == null){
                System.out.println();
            }
            util += distribution.get(sPrime) * (mdp.recompense(s, a, sPrime) + gamma * utils.get(sPrime));
        }

        return util;
    }

    /**
     * Applique l'algorithme itération valeur
     * @param mdp Le mdp sur lequel appliquer l'algo
     * @return Le dictionnaire associant à chaque état son utilité
     */
    public static Map<Etat, Action> iterationValeur(MDP mdp) {
        Map<Etat, Double> util = new HashMap<>();
        Map<Etat, Action[]> actions = mdp.getActions();
        Etat[] etats = mdp.getEtats();
        System.out.println("Itération valeur sur " + etats.length + " états");

        for (Etat e : etats) {
            util.put(e, 0.);
            bestActions.put(e, null);
        }

        for(Etat e : util.keySet()){
//            System.out.println(e + " " + e.estTerminal() + " " + e.estReussite());
        }

        double delta;
        do {
            delta = 0;
            Map<Etat, Double> utilClone = new HashMap<>();

            for (Etat e : util.keySet()) {
                double max = Double.NEGATIVE_INFINITY;

                for (Action a : actions.get(e)) {
                    double qval = qValeur(mdp, e, a, util);

                    if (qval > max) {
                        max = qval;
                        bestActions.put(e, a);
                    }
                }

                utilClone.put(e, max);

                delta = Math.max(delta, Math.abs(utilClone.get(e) - util.get(e)));
            }

            util = utilClone;
        }
        while (delta > epsilon * (1 - gamma) / gamma);
        return bestActions;
    }
}

