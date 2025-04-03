package mdp;

import javafx.util.Pair;
import observable.Environnement;
import personnages.Operateur;

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
           // System.out.println(action);
            Etat sPrime = s.copy();
            try{
                Etat sArrivee = hrs.simulerAction(action,sPrime);
                Double score = score(sPrime,sArrivee);
                if (score > bestScore) {
                    bestScore = score;
                    bestAction = action;
                }
            }catch (AssertionError e){
                System.out.println("L'action n'est pas valide, on la passe");
            }
        }
        System.out.println("L'action prédite par la politique "+bestAction);
        return bestAction;
    }

    public Pair P(MDP mdp, Etat s,Etat sArrivee, Action action) {
        Action bestAction = null;
        Double bestScore = Double.NEGATIVE_INFINITY;
        //System.out.println("Affiche des actions possibles à partir de l'état "+s);
            //System.out.println(action);
            Etat sDepartCopy = s.copy();
            Etat sArriveeCopy = sArrivee.copy();
            try{
                Double score = score(sDepartCopy,sArriveeCopy);
                if (score > bestScore) {
                    bestScore = score;
                    bestAction = action;
                }
            }catch (AssertionError e){
                System.out.println("L'action n'est pas valide, on la passe");
            }
        return new Pair(bestAction, bestScore);
    }
    public Double score(Etat sDepart,Etat sArrive){
        Double score = -1000.;
       /*
        if(sArrive.estEchec()){
            return Double.NEGATIVE_INFINITY;
        }
        if(sArrive.estReussite()){
            score+=1000;
        }

        */
        int nbNewHostages = 0;
        for(int i =0;i< sArrive.aObjectif.length;i++){
            if( !sDepart.aObjectif[i] &&  sArrive.aObjectif[i]){
                nbNewHostages++;
            }

        }
        score+= (nbNewHostages*50000);

        int distanceObjectif = distanceObjectif(sDepart) - distanceObjectif(sArrive);
        if(distanceObjectif == 0){
            score-=1000;
        }


        score+= distanceObjectif*(10000);
        /*
        score+= Math.max(((sDepart.menace - sArrive.menace)*50),0);
        int diffNbTerro = nbEnnemis(sDepart) - nbEnnemis(sArrive);
        score+= ((diffNbTerro)*(100));
         */

        return score;
    }
    public int nbEnnemis(Etat etat){
        int nbEnnemis = 0;
        for(int i =0;i< etat.indCaseTerroristes.length;i++){
            if(etat.indCaseTerroristes[i] != -1){
                nbEnnemis++;
            }
        }
        return nbEnnemis;

    }
    public  int distanceObjectif(Etat sArrive) {
        /*Il faudra ajouter ici les modifs, lorsqu'il y aura plusieurs hottages
         */

        Environnement environnementCopy = this.environnement.copy();
        environnementCopy.setEtat(sArrive);
        Operateur operateur = environnementCopy.getOperateurActif();

        Boolean hottagesRecup = operateur.getX()==environnementCopy.getPositionObjectifs().get(0).getX() && operateur.getY() == environnementCopy.getPositionObjectifs().get(0).getY();
        if(hottagesRecup){
            return Math.abs(operateur.getY()-environnementCopy.getHauteur());
        }else{
            return manhattanDistance(operateur.getX(),operateur.getY(),environnementCopy.getPositionObjectifs().get(0).getX(),environnementCopy.getPositionObjectifs().get(0).getY());
        }

    }

    public  int manhattanDistance(int x1, int y1, int x2, int y2) {
        return Math.abs(x1 - x2) + Math.abs(y1 - y2);
    }
}
