package mdp;

import carte.Case;
import coups.Coup;
import observable.Environnement;
import personnages.Operateur;
import personnages.Personnage;
import personnages.Terroriste;

import java.util.*;

public class IterationValeur{
    private static double gamma = 0.8;
    private static double epsilon = 0.0001;


    /**
     * Calcule et prédis la meilleure action à effectuer. Lance le calcul des utilités des état immédiatement accessibles,
     * ie les états accessibles en une action joueur, sur autant de fils d'exécutions que d'actions différentes possibles.
     * @param env L'environnement
     * @return La meilleure action prédite
     */
    public static Action predict(Environnement env) {
        Set<Action> actions = getAllActionsPossibleOperateur(env);

        System.out.println("Launching " + actions.size() + " threads");
        long start = System.currentTimeMillis();
        Map<IterationValeurThread, Action> threads = new HashMap<>(actions.size());
        double maxUtil = Double.NEGATIVE_INFINITY;
        Action bestAction = null;


        // Lancement du multi threading
        for(Action action : actions){
            IterationValeurThread thread = new IterationValeurThread(env, action, new Etat(env));
            threads.put(thread, action);
            thread.start();
        }

        // Rendez vous
        for(IterationValeurThread t : threads.keySet()){
            try {
                t.join(0);
                if(t.value > maxUtil){
                    maxUtil = t.value;
                    bestAction = threads.get(t);
                }
            } catch (InterruptedException e) {
                System.out.println("Thread (" + threads.get(t) + ") a rencontré une erreur : " + e.getMessage());
            }
        }
        long finish = System.currentTimeMillis();
        System.out.println("Finished in " + (finish - start)/1000. + " s");
        return bestAction;
    }

