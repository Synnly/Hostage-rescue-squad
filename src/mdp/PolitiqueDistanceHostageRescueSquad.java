package mdp;

import observable.Environnement;

import java.util.ArrayList;

public class PolitiqueDistanceHostageRescueSquad implements Politique{
    private final HostageRescueSquad hrs;
    private  Environnement environnement;

    /*
     public final int[] indCaseOperateurs;
        public final boolean[] aObjectif;
        public final int[] indCaseTerroristes;
        public final int menace;
     */
    public PolitiqueDistanceHostageRescueSquad(HostageRescueSquad hrsMdp, Environnement env){
        this.environnement = env.copy();
        this.hrs = hrsMdp;
    }

    @Override
    public Action P(MDP mdp, Etat s) {
        ArrayList<Action> actions = (ArrayList<Action>) hrs.getActionsEtat(s);
        Action bestAction = null;
        Double bestScore = Double.NEGATIVE_INFINITY;
        System.out.println("Affiche des actions possibles à partir de l'état "+s);
        for (Action action : actions) {
            System.out.println(action);
            Etat sPrime = s.copy();
            try{
                Etat sArrivee = hrs.simulerAction(action,sPrime);
                Double score = score(sPrime,sArrivee);
                if (score > bestScore) {
                    bestScore = score;
                    bestAction = action;
                }
            }catch (AssertionError e){
                System.out.println("L'action n'est pas valide, on la psse");
            }
        }
        System.out.println("L'action prédite par la politique "+bestAction);
        return bestAction;
    }
    public Double score(Etat sDepart,Etat sArrive){
        Double score = 0.0;
        if(sArrive.estEchec()){
            return Double.NEGATIVE_INFINITY;
        }
        if(sArrive.estReussite()){
            score+=1000;
        }
        int nbNewHostages = 0;
        for(int i =0;i< sArrive.aObjectif.length;i++){
            if( !sDepart.aObjectif[i] &&  sArrive.aObjectif[i]){
                nbNewHostages++;
            }

        }
        score+= (nbNewHostages*500);

        return score;
    }
    public int distanceObjectif(Etat sDepart,Etat sArrive){
        return 5;
    }
}
