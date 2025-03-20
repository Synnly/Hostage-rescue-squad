package mdp;

import carte.Case;
import coups.Coup;
import observable.Environnement;
import personnages.Operateur;
import personnages.Personnage;
import personnages.Terroriste;

import java.util.*;

public class IterationValeur implements MDP{
    private static double gamma = 0.5;
    private static double epsilon = 0.0001;
    private static int maxTour = 3;

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

    /**
     * Calcule tous les arrangements de coups possibles, compte tenu de la limite de points d'actions de l'opérateur et
     * de la fin de tour
     * @param listeCoups La liste de tous les coups possibles
     * @param maxNbCoups Le nombre maximal de coups en un tour
     * @param op L'opérateur effectuant les coups
     * @return L'ensemble des suites de coups possibles
     */
    private static Set<List<Coup>> getAllSuitesCoupsPossibleOperateur(Coup[] listeCoups, int maxNbCoups, Operateur op){
        Set<List<Coup>> actionsPossibles = new HashSet<>();
        for (int i = 0; i < Math.pow(listeCoups.length, maxNbCoups); i++) {
            List<Coup> actionComplete = new ArrayList<>();
            int idAction = i; // Identifiant de l'action, ie une sequence d'indice de coups

            int k = Math.max(1, (int) (Math.log(i) / Math.log(listeCoups.length)));
            int sumPA = 0;

            do {
                // Tour fini, donc pas d'actions en plus possible
                if(actionComplete.contains(op.getFinTour())){
                    sumPA = op.getPointsAction();
                }
                else {
                    int indiceCoup = (int) (idAction % Math.pow(listeCoups.length, k));
                    // si assez de PA pour faire l'action
                    if(sumPA + listeCoups[indiceCoup].cout <= op.getPointsAction()) {
                        actionComplete.add(listeCoups[indiceCoup]);
                        sumPA += listeCoups[indiceCoup].cout;
                    }
                    else{
                        actionComplete.add(op.getFinTour());
                        sumPA = op.getPointsAction();
                    }
                }
                idAction = idAction / listeCoups.length;
            } while (sumPA < op.getPointsAction());

            actionsPossibles.add(actionComplete);
        }
        return actionsPossibles;
    }

    /**
     * Calcule tous les arrangements de coups possibles pour les terroristes
     * @param listeCoups La liste de tous les coups possibles
     * @param maxNbCoups Le nombre maximal de coups en un tour
     * @return L'ensemble des suites de coups possibles
     */
    private static Set<List<Coup>> getAllSuitesCoupsPossibleTerroristes(Coup[] listeCoups, int maxNbCoups){
        Set<List<Coup>> actionsPossibles = new HashSet<>();
        
        for (int i = 0; i < Math.pow(listeCoups.length, maxNbCoups); i++) {
            List<Coup> actionComplete = new ArrayList<>();
            int idAction = i; // Identifiant de l'action, ie une sequence d'indice de coups

            int k = Math.max(1, (int) (Math.log(i) / Math.log(listeCoups.length)));
            
            for (int j = 0; j < maxNbCoups; j++) {
                int indiceCoup = (int) (idAction % Math.pow(listeCoups.length, k));
                actionComplete.add(listeCoups[indiceCoup]);
                idAction = idAction / listeCoups.length;
            }

            actionsPossibles.add(actionComplete);
        }
        return actionsPossibles;
    }

    /**
     * Calcule toutes les actions possibles pour le joueur en fonction des suites de coups possibles
     * @param env L'environnement du joueur
     * @return La liste des actions valides
     */
    public static Set<Action> getAllActionsPossibleOperateur(Environnement env){
        Operateur op = env.getOperateurActif();
        Coup[] listeCoups = {op.getDeplacement(), op.getTir(), op.getFinTour()};

        // Nombre maximum de coups par opérateur
        int maxNbCoups = 0;
        for (Coup c : listeCoups) {
            maxNbCoups = Math.max(maxNbCoups, op.getPointsAction() / c.cout);
        }

        Set<List<Coup>> suiteCoupsPossibles = getAllSuitesCoupsPossibleOperateur(listeCoups, maxNbCoups, op);
        Set<Action> actionPossibles = new HashSet<>();

        for(List<Coup> suiteCoup : suiteCoupsPossibles) {   // Parcours de toutes les suites de coups
            // Liste des cases des couches. La case i de la couche j correspond à l'etat i de la case j
            List<List<Case>> casesCouches = new ArrayList<>();
            casesCouches.add(new ArrayList<>());
            casesCouches.get(0).add(env.getCase(env.getOperateurActif().getX(), env.getOperateurActif().getY()));

            // Liste des etats des couches. L'etat i de la couche j correspond à la case i de la case j
            List<List<Etat>> etatsCouches = new ArrayList<>();
            etatsCouches.add(new ArrayList<>());
            etatsCouches.get(0).add(new Etat(env.copy()));

            while (!casesCouches.isEmpty()) {
                while (!casesCouches.isEmpty() && casesCouches.size() < suiteCoup.size() + 1){     // création des nouvelles couches
                    int indiceCouche = etatsCouches.size()-1;
                    Map<Case, Etat> coucheTemp = getCasesValidesEtEtatsOperateur(env, etatsCouches.get(indiceCouche).get(0), suiteCoup.get(indiceCouche));

                    // Transformation de la map en listes
                    List<Case> casesTemp = new ArrayList<>(coucheTemp.size());
                    List<Etat> etatsTemp = new ArrayList<>(coucheTemp.size());
                    for(Case c : new ArrayList<>(coucheTemp.keySet())){
                        casesTemp.add(c.copy());
                        etatsTemp.add(coucheTemp.get(c));
                    }
                    casesCouches.add(casesTemp);
                    etatsCouches.add(etatsTemp);

                    // Si la derniere couche est vide ici, alors le coup n'as pas de cases valides, donc il faut
                    // supprimer le noeud
                    while(!casesCouches.isEmpty() && casesCouches.get(casesCouches.size() - 1).isEmpty()){
                        casesCouches.remove(casesCouches.size() - 1);
                        etatsCouches.remove(etatsCouches.size() - 1);

                        if(!casesCouches.isEmpty()){
                            casesCouches.get(casesCouches.size() - 1).remove(0);
                            etatsCouches.get(etatsCouches.size() - 1).remove(0);
                        }
                    }
                }
                if(casesCouches.isEmpty()){
                    break;
                }

                List<Case> casesCible = new ArrayList<>();
                for(List<Case> couche : casesCouches){
                    casesCible.add(couche.get(0));
                }
                casesCible.remove(0);   // Retrait de la case de départ
                actionPossibles.add(new Action(env.getOperateurActif(), suiteCoup, casesCible));

                // Suppression de la premiere case de la derniere couche
                casesCouches.get(casesCouches.size() - 1).remove(0);
                etatsCouches.get(etatsCouches.size() - 1).remove(0);

                // Suppression des couches vides et des premiers noeuds précédent une couche vide
                while(!casesCouches.isEmpty() && casesCouches.get(casesCouches.size() - 1).isEmpty()){
                    casesCouches.remove(casesCouches.size() - 1);
                    etatsCouches.remove(etatsCouches.size() - 1);

                    if(!casesCouches.isEmpty()){
                        casesCouches.get(casesCouches.size() - 1).remove(0);
                        etatsCouches.get(etatsCouches.size() - 1).remove(0);
                    }
                }
            }
        }
        return actionPossibles;
    }

    /**
     * Calcule toutes les cases et états correspondants d'arrivée apres que l'opérateur ait effectué le coup
     * @param env L'environnement de l'opérateur
     * @param etat L'etat de départ
     * @param coup Le coup à effectuer
     * @return Le dictionnaire de toutes les cases et états correspondants d'arrivée
     */
    private static Map<Case, Etat> getCasesValidesEtEtatsOperateur(Environnement env, Etat etat, Coup coup){
        Map<Case, Etat> caseEtat = new HashMap<>();
        Coup coupCopy = coup.copy();
        coupCopy.probaSucces = 1;

        Environnement envCopy = env.copy();
        envCopy.setEtat(etat);
        Personnage persoCopy = envCopy.getOperateurActif();
        List<Case> casesValides = new ArrayList<>(coup.getCasesValides(envCopy, envCopy.getCase(persoCopy.getX(), persoCopy.getY())));

        for(Case c : casesValides) {
            envCopy = env.copy();
            coupCopy.effectuer(envCopy, envCopy.getOperateurActif(), c);
            caseEtat.put(c, new Etat(envCopy));
        }
        return caseEtat;
    }

    /**
     * Calcule l'utilité de l'état si le tour actuel est celui du joueur
     * @param env L'environnement
     * @param etatDepart L'état de départ
     * @param tour Le numéro du tour
     * @return La somme des utilités pondéré par leurs probabilités des états accessibles si <code>tour</code> < <code>IterationValeur.maxTour</code>, <code>MDP.valeurEchec</code> sinon
     */
    private static double utiliteEtatTourJoueur(Environnement env, Etat etatDepart, int tour){
        // Si mission pas réussie au bout de maxTour alors échec
        if(tour > maxTour){
            return 0;
        }

        Set<Action> actions = getAllActionsPossibleOperateur(env);
        double sumUtil = 0;

        for(Action action : actions){
            Map<Etat, Double> probaEtats = MDP.transition(env.copy(), etatDepart, action);

            // Ajout des utilités des états d'arrivée pondéré par proba d'y arriver
            for(Etat etatArrivee : probaEtats.keySet()){
                Environnement envCopy = env.copy();
                envCopy.setEtat(etatArrivee);
                envCopy = envCopy.copy();

                sumUtil += probaEtats.get(etatArrivee) * (MDP.recompense(etatDepart, action, etatArrivee) + gamma * utiliteEtatTourEnnemi(envCopy, etatArrivee, tour));
            }
        }
        return sumUtil/actions.size();
    }

    /**
     * Calcule l'utilité de l'état si le tour actuel est celui du terroriste
     * @param env L'environnement
     * @param etatDepart L'état de départ
     * @param tour Le numéro du tour
     * @return La somme des utilités pondéré par leurs probabilités des états accessibles si <code>tour</code> < <code>IterationValeur.maxTour</code>, <code>MDP.valeurEchec</code> sinon
     */
    private static double utiliteEtatTourEnnemi(Environnement env, Etat etatDepart, int tour){
        // Si mission pas réussie au bout de maxTour alors échec
        if(tour >= maxTour){
            return MDP.valeurEchec;
        }
        if(env.getEnnemis().isEmpty()){     // Aucun ennemi, donc pas de changement
            Environnement envCopy = env.copy();
            envCopy.setEtat(etatDepart);
            envCopy = envCopy.copy();
            return utiliteEtatTourJoueur(envCopy, etatDepart, tour + 1);
        }

        Terroriste terro = env.getEnnemis().get(0);
        Set<List<Coup>> suitesCoupsPossible = getAllSuitesCoupsPossibleTerroristes(new coups.Coup[]{terro.getDeplacement(), terro.getTir()}, env.getMenace());
        double sumUtil = 0;

        Map<Etat, Double> probaEtats = new HashMap<>();
        for(List<Coup> suiteCoups : suitesCoupsPossible) {
            Environnement envCopy = env.copy();
            envCopy.setEtat(etatDepart);
            envCopy = envCopy.copy();
            envCopy.effectuerCoupsTerroristes(suiteCoups);

            // Proba de la suite de coups ennemi
            double proba = 1;
            for(Coup c : suiteCoups){
                proba *= env.getProbaCoupEnnemi(c);
            }

            Etat etatTemp = new Etat(envCopy);
            etatTemp.operateur().resetPointsAction();   // On redonne les PA à l'opérateur
            probaEtats.put(etatTemp, proba);
        }

        // Ajout des utilités des états d'arrivée pondéré par proba d'y arriver
        for(Etat etatArrivee : probaEtats.keySet()){
            Environnement envCopy = env.copy();
            envCopy.setEtat(etatArrivee);
            envCopy = envCopy.copy();
            sumUtil += probaEtats.get(etatArrivee) * (gamma * utiliteEtatTourJoueur(envCopy, etatArrivee, tour + 1));
        }

        return sumUtil;
    }

    private static class IterationValeurThread extends Thread{
        public double value = 0;
        private Environnement env;
        private Action action;
        private Etat etatDepart;

        /**
         * Constructeur d'un fil d'execution calculant l'utilité de l'action en partant de l'état de départ
         * @param env L'enrionnement
         * @param action L'action effectuée
         * @param etatDepart L'état de départ
         */
        public IterationValeurThread(Environnement env, Action action, Etat etatDepart){
            this.env = env;
            this.action = action;
            this.etatDepart = etatDepart;
        }

        public void run(){
            Map<Etat, Double> probaEtats = MDP.transition(env.copy(), etatDepart, action);

            // Ajout des utilités des états d'arrivée pondéré par proba d'y arriver
            for(Etat etatArrivee : probaEtats.keySet()){
                value += probaEtats.get(etatArrivee) * (MDP.recompense(etatDepart, action, etatArrivee) + utiliteEtatTourEnnemi(env.copy(), etatArrivee, 1));
            }
        }
    }
}


