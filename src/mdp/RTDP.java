package mdp;

import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/* Real-Time Dynamic Programming

 */
public class RTDP {
    public static void RTDP(MDP mdp, Etat s){
        Map<Etat, Double> J = new HashMap<>();
       /*long startTime =  System.currentTimeMillis();
       long currentTime =  System.currentTimeMillis();
        System.out.println(startTime);
       while(currentTime - startTime < 10000){  //10 secondes
           ESSAI_RTDP(mdp,s,J);
           currentTime =  System.currentTimeMillis();
       }
        System.out.println(currentTime - startTime);

        */
        ESSAI_RTDP(mdp,s,J);
    }


    public static void ESSAI_RTDP(MDP mdp, Etat s, Map<Etat, Double> J){
        Action a = null;
         while(! s.estReussite()){
             a = getActionGloutonne(mdp,s,J);
             if (!J.containsKey(s)) {
                 J.put(s, -1000.);
                 //System.out.println("L'état n'est pas présent dans la Map.");
             }
             J.put(s, qValeur(mdp,s,a,J));
            s = choisirEtatSuivant(mdp,s,a);
         }
        System.out.println("l'action choisi est "+a);


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

    public static Etat choisirEtatSuivant(MDP mdp, Etat s, Action a) {
        return mdp.etatSuivant(s,a);
    }
    public static Action getActionGloutonne(MDP mdp, Etat s, Map<Etat, Double> J){
        ArrayList<Action> actions = (ArrayList<Action>) mdp.getActionsEtat(s);
        double util = 0;
        double maxUtil = Double.NEGATIVE_INFINITY;
        Action actionChoisie = null;
        if(!s.estTerminal()) {
            for(Action a : actions){
                util = 0;
                Map<Etat, Double> distribution = mdp.transition(s, a);
                for (Etat sPrime : distribution.keySet()) {
                    if (!J.containsKey(sPrime)) {
                        J.put(sPrime, -1000.);
                    }
                   // util += distribution.get(sPrime) * (mdp.recompense(s, a, sPrime) +  (0.005)*J.get(sPrime));
                    double test = J.get(sPrime);
                    //System.out.println("test = "+test);
                    util += mdp.recompenseRTDP(s, a, sPrime) +test ;

                }
                if(util > maxUtil){
                    maxUtil = util;
                    actionChoisie = a;
                }
            }
        }

        return actionChoisie;
    }

    public static double qValeur(MDP mdp, Etat s, Action a, Map<Etat, Double> J) {
        double util = 0;
        if(!s.estTerminal()) {
            Map<Etat, Double> distribution = mdp.transition(s, a);
            for (Etat sPrime : distribution.keySet()) {
                if (!J.containsKey(sPrime)) {
                    J.put(sPrime, 1000.);
                }
                util += distribution.get(sPrime) * (mdp.recompense(s, a, sPrime) +  J.get(sPrime));
            }
        }
        return util;
    }

    public static Action predict(MDP mdp, Etat s){
        RTDP(mdp,s);
        //return mdp.getActionGloutonne(s);
        return null;
    }


}
