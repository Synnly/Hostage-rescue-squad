package mdp;

import java.sql.Time;
import java.util.HashMap;
import java.util.Map;

/* Real-Time Dynamic Programming

 */
public class RTDP {
    public static void RTDP(MDP mdp, Etat s){
        Map<Etat, Double> J = new HashMap<>();
       long startTime =  System.currentTimeMillis();
       long currentTime =  System.currentTimeMillis();
        System.out.println(startTime);
       while(currentTime - startTime < 10000){  //10 secondes
           ESSAI_RTDP(mdp,s,J);
           currentTime =  System.currentTimeMillis();
       }
        System.out.println(currentTime - startTime);

    }


    public static void ESSAI_RTDP(MDP mdp, Etat s, Map<Etat, Double> J){
        while( ! s.estReussite()){
            Action a = mdp.getActionGloutonne(s);
            J.put(s, qValeur(mdp,s,a,J));
            s = choisirEtatSuivant(mdp,s,a);
        }
    }

    public static Etat choisirEtatSuivant(MDP mdp, Etat s, Action a) {
        return mdp.etatSuivant(s,a);
    }

    public static double qValeur(MDP mdp, Etat s, Action a, Map<Etat, Double> J) {
        double util = 0;
        if(!s.estTerminal()) {
            Map<Etat, Double> distribution = mdp.transition(s, a);
            for (Etat sPrime : distribution.keySet()) {
                util += distribution.get(sPrime) * (mdp.recompense(s, a, sPrime) +  J.get(sPrime));
            }
        }
        return util;
    }

    public static Action predict(MDP mdp, Etat s){

        return null;
    }


}
