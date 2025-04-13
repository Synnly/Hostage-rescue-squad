package mdp;

import carte.cases.Case;
import coups.Coup;
import mdp.etat.Etat;
import mdp.etat.EtatNormal;
import observable.Environnement;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class ExplorationProba {

    public static double probaEchec(Environnement env, MDP mdp, Etat etatDepart, Coup coup, Direction dir, int nbCoupsMax, int nbIters, Coup[] listeCoups){

        double nbEchecs = 0;
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

                nbCoups ++;
            }

            if(etat.estTerminal() && !etat.estReussite()) nbEchecs ++;
        }

        return nbEchecs / nbIters;
    }

    private static Etat tirerEtat(Map<Etat, Double> distribution){
        double nb = new Random().nextDouble();
        double acc = 0;
        Etat[] keys = distribution.keySet().toArray(new Etat[0]);
        
        for(Etat e : keys){
            if(distribution.get(e) + acc < nb){
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

}
