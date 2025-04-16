package mdp;

import coups.Coup;
import mdp.etat.Etat;
import org.javatuples.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/* Real-Time Dynamic Programming

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


    public static Pair<Coup, Direction> ESSAI_RTDP(MDP mdp, Etat s, Map<Etat, Double> J){
        Pair<Coup, Direction> c = null;
        Etat e = s;
         while(! e.estTerminal()){
             c = getActionGloutonne(mdp,e,J);
             if (!J.containsKey(e)) {
                 J.put(e, 0.);
                 //System.out.println("L'état n'est pas présent dans la Map.");
             }
             J.put(e, qValeur(mdp,e,c,J));
             e = choisirEtatSuivant(mdp,e,c);
             //System.out.println("nouveau etat = "+s);
         }
         //System.out.println("l'action choisi est "+c);
         return c;


       /*
        System.out.println("cECI EST BINE LE PRINT DE TST QUE NOUS VOULONS");
        Action a = mdp.getActionGloutonne(s);
        if (!J.containsKey(s)) {
            J.put(s, 0.0);
            //System.out.println("L'état n'est pas présent dans la Map.");
        }
        J.put(s, qValeur(mdp,s,a,J));
        //if (J.containsKey(s)) {System.out.println("L'état est présent dans la Map.");}
        s = choisirEtatSuivant(mdp,s,a);
        System.out.println("L'état suivant est = "+s);
        */
    }

    public static Etat choisirEtatSuivant(MDP mdp, Etat s, Pair<Coup, Direction> c) {
        return mdp.etatSuivant(s,c);
    }

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
                    double test = J.get(sPrime);
                    //System.out.println("test = "+util);
                    //util += mdp.recompense(s, a.getValue0(), sPrime) +test ;
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

    public static Pair<Coup, Direction> predict(MDP mdp, Etat s){
        Pair<Coup, Direction> coupPredit = RTDP(mdp,s);
        System.out.println("r  tdp terminé");
        //return mdp.getActionGloutonne(s);
        return coupPredit;
    }


}
