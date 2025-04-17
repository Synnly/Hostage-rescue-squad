package mdp;

import coups.Coup;
import mdp.etat.Etat;
import org.javatuples.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * RTDP : Real-Time Dynamic Programming
 */
public class RTDP {
    public static Pair<Coup, Direction> RTDP(MDP mdp, Etat s){
        Map<Etat, Double> J = new HashMap<>();
        int nbIterration = 0;
        while(nbIterration < 1000){  //10 secondes
            ESSAI_RTDP(mdp,s,J);
            nbIterration++;
        }

        return getActionGloutonne(mdp,s, J);
    }


    public static Pair<Coup, Direction> ESSAI_RTDP(MDP mdp, Etat e, Map<Etat, Double> J){
        Pair<Coup, Direction> c = null;
        int cpt = 1000;
         while(! e.estTerminal() && cpt > 0){
             c = getActionGloutonne(mdp,e,J);
             if (!J.containsKey(e)) {
                 J.put(e, 0.);
             }
             J.put(e, qValeur(mdp,e,c,J));
             e = choisirEtatSuivant(mdp,e,c);
             cpt--;
         }
         return c;
    }

    /**
     * Effectue un Coup vers une direction depuis un Etat de départ
     * @param mdp Hostage Rescue Squad
     * @param s Etat de départ
     * @param c Paire (Coup, Direction)
     * @return Etat d'arrivé
     */
    public static Etat choisirEtatSuivant(MDP mdp, Etat s, Pair<Coup, Direction> c) {
        return mdp.etatSuivant(s,c);
    }

    /**
     * Choisi la meilleure Paire (Coup, Direction) depuis un Etat de départ
     * @param mdp Hostage Rescue Squad
     * @param s Etat de départ
     * @param J Coût  estimé depuis l'état de départ s jusqu’au but
     * @return Paire (Coup, Direction) par choix Glouton (dont l'utilitéé est max)
     */
    public static Pair<Coup, Direction> getActionGloutonne(MDP mdp, Etat s, Map<Etat, Double> J){
        ArrayList<Pair<Coup, Direction>> actions = (ArrayList<Pair<Coup, Direction>>) mdp.getCoupsEtat(s);
        double util = 0;
        double maxUtil = Double.NEGATIVE_INFINITY;
        Pair<Coup, Direction> actionChoisie = null;
        if(!s.estTerminal()) {
            for(Pair<Coup, Direction> a : actions){
                util = 0;
                Map<Etat, Double> distribution = mdp.transition(s, a.getValue0(),a.getValue1());
                for (Etat sPrime : distribution.keySet()) {
                    if (!J.containsKey(sPrime)) {
                        J.put(sPrime, 0.);
                    }
                    util += distribution.get(sPrime) * (mdp.recompense(s, a.getValue0(), sPrime) +  (0.005)*J.get(sPrime));
                }
                if(util > maxUtil){
                    maxUtil = util;
                    actionChoisie = a;
                }
            }
        }

        return actionChoisie;
    }

    public static double qValeur(MDP mdp, Etat s, Pair<Coup, Direction> a, Map<Etat, Double> J) {
        double util = 0;
        if(!s.estTerminal()) {
            Map<Etat, Double> distribution = mdp.transition(s, a.getValue0(),a.getValue1());
            for (Etat sPrime : distribution.keySet()) {
                if (!J.containsKey(sPrime)) {
                    J.put(sPrime, 0.);
                }
                util += distribution.get(sPrime) * (mdp.recompense(s, a.getValue0(), sPrime) +  J.get(sPrime));
            }
        }
        return util;
    }

    /**
     * Lance l'algo RTDP
     * @param mdp Hostage Rescue Squad
     * @param s Etat de départ
     * @return
     */
    public static Pair<Coup, Direction> predict(MDP mdp, Etat s){
        Pair<Coup, Direction> coupPredit = RTDP(mdp,s);
        return coupPredit;
    }


}
