package mdp;

import java.util.ArrayList;

public class PolitiqueDistanceHostageRescueSquad implements Politique{


    @Override
    public Action P(MDP mdp, Etat s) {
        ArrayList<Action> actions = (ArrayList<Action>) mdp.getActionsEtat(s);
        // À partir d'ici on va utiliser une politique pour sélectionner Le meilleur coup possible
        System.out.println("Affiche des actions possibles à partir de l'état "+s);
        for (Action action : actions) {
            System.out.println(action);
        }
        return null;
    }
}
