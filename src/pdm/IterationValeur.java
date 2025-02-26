package pdm;

import carte.Case;
import observable.Environnement;
import personnages.Operateur;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class IterationValeur {

    private static double devalMort = -2;
    private static double recompObjectif = 10;
    private static double recompFin = 10;
    private static double gamma = 0.75;
    private static double epsilon = 0.0001;


    public static ActionPredite[][] predict(Environnement env){
        int hauteur = env.getHauteur();
        int largeur = env.getLargeur();
        Operateur op = new Operateur(env.getOperateurActif());
        double[][] utilites = new double[largeur][hauteur];

        // Mise à 0 des utilités
        for(int x = 0; x < largeur; x++){
            for(int y = 0; y < largeur; y++){
                utilites[x][y] = 0;
            }
        }
        ActionPredite[][] actionPredites = new ActionPredite[largeur][hauteur];
        double delta;
        int nbIter = 0;

        do {
            // Clonage
            double[][] utilitesClone = new double[largeur][hauteur];
            for(int i = 0; i < largeur; i++){
                utilitesClone[i] = utilites[i].clone();
            }

            delta = 0;
            for (int x = 0; x < largeur; x++) {
                for (int y = 0; y < hauteur; y++) {
                    double maxQval = Double.MIN_VALUE;

                    // Deplacement
                    List<Case> casesCible = getCasesVoisines(env, x, y);
                    for (Case c : casesCible) {
                        double qval = op.getDeplacement().qvaleur(env, op, x, y, c, utilites, gamma);
                        if (qval > maxQval) {
                            maxQval = qval;
                            actionPredites[x][y] = new ActionPredite(op.getDeplacement(), c, op);
                        }
                    }

                    // Tir
                    Operateur opClone = new Operateur(op);
                    opClone.setX(x); opClone.setY(y);
                    casesCible = op.getTir().getCasesValides(env, opClone);

                    for (Case c : casesCible) {
                        double qval = op.getTir().qvaleur(env, opClone, x, y, c, utilites, gamma);
                        if (qval > maxQval) {
                            maxQval = qval;
                            actionPredites[x][y] = new ActionPredite(opClone.getTir(), c, opClone);
                        }
                    }

                    // Fin de tour
                    double qval = op.getFinTour().qvaleur(null, null, 0, 0, null, utilites, gamma);
                    if (qval > maxQval) {
                        maxQval = qval;
                        actionPredites[x][y] = new ActionPredite(op.getFinTour(), null, op);
                    }
                    utilitesClone[x][y] = maxQval;
                    delta = Math.max(delta, Math.abs(utilitesClone[x][y] - utilites[x][y]));
                }
            }
            utilites = utilitesClone;
            nbIter ++;
        }
        while(delta > epsilon * (1 - gamma) / gamma);
        System.out.println("Finished after " + nbIter + " iterations");
        return actionPredites;
    }

    private static List<Case> getCasesVoisines(Environnement env, int x, int y){
        ArrayList<Case> casesCible = new ArrayList<>(2);
        try{casesCible.add(env.getCase(x-1, y));} //Gauche
        catch (Exception ignored) {}

        try{casesCible.add(env.getCase(x, y-1));} //Haut
        catch (Exception ignored) {}

        try{casesCible.add(env.getCase(x+1, y));} //Droite
        catch (Exception ignored) {}

        try{casesCible.add(env.getCase(x, y+1));} //Bas
        catch (Exception ignored) {}

        return casesCible;
    }

    public static void printActions(ActionPredite[][] actions){
        for(int i = 0; i < actions[0].length; i++){
            StringBuilder sb = new StringBuilder();
            for (ActionPredite[] action : actions) {
                sb.append(action[i]).append(" | ");
            }
            System.out.println(sb);
        }
    }
}
