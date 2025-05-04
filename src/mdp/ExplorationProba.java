package mdp;

import carte.cases.Case;
import coups.Coup;
import mdp.etat.Etat;
import mdp.etat.EtatNormal;
import observable.Environnement;
import org.javatuples.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class ExplorationProba {

    private static int nbThreads = 3;

    public static double probaEchec(Environnement env, MDP mdp, Etat etatDepart, Coup coup, Direction dir, int nbCoupsMax, int nbIters, Coup[] listeCoups, boolean random){
        ExplorationThread[] threads = new ExplorationThread[nbThreads];
        double sommeEchecs = 0;
        double sommeIters = 0;

        for (int i = 0; i < nbThreads; i++) {
            threads[i] = new ExplorationThread(env, mdp, etatDepart, coup, dir, nbCoupsMax, nbIters, listeCoups, random);
            threads[i].run();
        }

        for (int i = 0; i < nbThreads; i++) {
            try {
                threads[i].join();
                sommeIters += nbIters;
                sommeEchecs += threads[i].getNbEchecs();
            } catch (InterruptedException ignored){}
        }
        return sommeEchecs / sommeIters;
    }

    private static Etat tirerEtat(Map<Etat, Double> distribution){
        double nb = new Random().nextDouble();
        double acc = 0;
        Etat[] keys = distribution.keySet().toArray(new Etat[0]);
        
        for(Etat e : keys){
            if(distribution.get(e) > nb - acc){
                return e;
            }
            acc += distribution.get(e);
        }
        return keys[keys.length - 1];
    }

    private static Coup tirerCoup(List<Coup> listeCoup){
        return listeCoup.get(new Random().nextInt(listeCoup.size()));
    }

    private static Direction tirerDirection(Environnement env, Etat e, Coup c){
        Etat restoreState = new EtatNormal(env);

        env.setEtat(e);
        List<Case> casesValides = c.getCasesValides(env, env.getOperateurActif());
        Case caseTiree = casesValides.get(new Random().nextInt(casesValides.size()));

        Case casePerso = env.getCase(env.getOperateurActif().getX(), env.getOperateurActif().getY());
        env.setEtat(restoreState);

        if(caseTiree.x == -1 || caseTiree.y == -1) return Direction.AUCUN;
        if(caseTiree.x > casePerso.x) return Direction.DROITE;
        if(caseTiree.x < casePerso.x) return Direction.GAUCHE;
        if(caseTiree.y > casePerso.y) return Direction.BAS;
        return Direction.HAUT;
    }

    private static class ExplorationThread extends Thread{
        private int nbEchecs;
        private final int nbIters;
        private final Environnement env;
        private final MDP mdp;
        private final Etat etatDepart;
        private final Direction dir;
        private final int nbCoupsMax;
        private final Coup[] listeCoups;
        private final Coup coup;
        private final boolean random;

        public ExplorationThread(Environnement env, MDP mdp, Etat etatDepart, Coup coup, Direction dir, int nbCoupsMax, int nbIters, Coup[] listeCoups, boolean random){
            this.nbIters = nbIters;
            this.env = new Environnement(env);
            this.mdp = mdp;
            this.etatDepart = etatDepart;
            this.dir = dir;
            this.nbCoupsMax = nbCoupsMax;
            this.listeCoups = listeCoups;
            this.coup = coup;
            this.nbEchecs = 0;
            this.random = random;
        }

        public void run(){
            this.nbEchecs = 0;
            Direction direction;
            Environnement envCopy = new Environnement(env);

            for (int i = 0; i < nbIters; i++) {

                Coup premierCoup = coup.copy();
                premierCoup.probaSucces = 1;
                Map<Etat, Double> distributionDepart = mdp.transition(etatDepart, premierCoup, dir);
                Etat etat = tirerEtat(distributionDepart);
                int nbCoups = 1;

                while(nbCoups < nbCoupsMax && !(etat.estTerminal() && !etat.estReussite())){

                    envCopy.setEtat(etat);

                    if(random){
                        Pair<Coup, Direction> coupPredit = IterationValeur.predict(mdp, etat);
                        etat = tirerEtat(mdp.transition(etat, coupPredit.getValue0(), coupPredit.getValue1()));
                    }
                    else{
                        ArrayList<Coup> coupsValides = new ArrayList<>(List.of(listeCoups));

                        // Tirage d'un coup ayant des cases valides
                        Coup coupAFaire = tirerCoup(coupsValides);
                        while(coupAFaire.getCasesValides(envCopy, envCopy.getOperateurActif()).isEmpty()){
                            coupsValides.remove(coupAFaire);
                            coupAFaire = tirerCoup(coupsValides);
                        }

                        // Tirage de la direction
                        direction = tirerDirection(env, etat, coupAFaire);

                        // Tirage de l'etat
                        etat = tirerEtat(mdp.transition(etat, coupAFaire, direction));
                    }

                    nbCoups ++;
                }

                if(etat.estTerminal() && !etat.estReussite()) nbEchecs ++;
            }
        }

        public int getNbEchecs(){
            return nbEchecs;
        }
    }

}
