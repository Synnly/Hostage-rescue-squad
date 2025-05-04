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
    private static double gamma = 0.9935;;
    public static Pair<Coup, Direction> RTDP(MDP mdp, Etat s){
        Map<Etat, Double> J = new HashMap<>();
        int nbIterration = 0;
        while(nbIterration < 200){  //10 secondes
            ESSAI_RTDP(mdp,s,J);
            nbIterration++;
        }
        return getActionGloutonne(mdp,s, J);
    }


    public static Pair<Coup, Direction> ESSAI_RTDP(MDP mdp, Etat e, Map<Etat, Double> J){
        Pair<Coup, Direction> c = null;
        int cpt = 50;
        ArrayList<Pair<Etat, Pair<Coup,Direction>>> chemin = new ArrayList<>();
        while(! e.estTerminal() && cpt > 0){
             c = getActionGloutonne(mdp,e,J);
             if (!J.containsKey(e)) {
                 J.put(e,0.);
                 //J.put(e,MDP.valeurReussite*(1+gamma) + MDP.valeurObjectif + MDP.valeurTuerEnnemi*(1-gamma));
             }
             J.put(e, qValeur(mdp,e,c,J));
             e = choisirEtatSuivant(mdp,e,c);
             c = getActionGloutonne(mdp,e,J);
             chemin.add(new Pair(e,c));
             cpt--;
         }
        retropropagation(chemin, J,mdp);
         return c;
    }

    private static void retropropagation(ArrayList<Pair<Etat, Pair<Coup, Direction>>> chemin, Map<Etat, Double> J,MDP mdp) {
        int index = chemin.size()-1;
        Pair<Etat, Pair<Coup, Direction>> pairOne = chemin.get(index);
        System.out.println(pairOne.getValue0());

        while(!chemin.isEmpty()){
            Pair<Etat, Pair<Coup, Direction>> pair = chemin.remove(chemin.size()-1);
            J.put(pair.getValue0(), qValeur(mdp, pair.getValue0(), pair.getValue1(), J));
        }
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
        Pair<Coup, Direction> actionChoisie = null;
        double max = Double.NEGATIVE_INFINITY;
        double qvaleur;
        if(!s.estTerminal()) {
            for(Pair<Coup, Direction> a : actions){
                qvaleur = qValeur(mdp,s,a,J);
                if(qvaleur > max){
                    max = qvaleur;
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
                    J.put(sPrime, 0.); // Remplacer par une valeur optimiste
                    //J.put(sPrime,MDP.valeurReussite*(1+gamma) + MDP.valeurObjectif + MDP.valeurTuerEnnemi*(1-gamma));
                }
                util += distribution.get(sPrime) * (mdp.recompense(s, a.getValue0(), sPrime) +  gamma*J.get(sPrime));
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
