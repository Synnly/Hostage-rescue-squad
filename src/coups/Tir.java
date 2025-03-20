package coups;

import carte.Case;
import observable.Environnement;
import personnages.Operateur;
import personnages.Personnage;
import personnages.Terroriste;

import java.util.ArrayList;
import java.util.List;

/**
 * Action de tir représentant un personnage tirant sur un autre.
 */
public class Tir extends Coup {
    /**
     * Constructeur d'une action de tir
     *
     * @param cout        Le cout de cette action
     * @param probaSucces La probabilité de succès allant de 1 à 0
     */
    public Tir(int cout, double probaSucces) {
        super(cout, probaSucces);
    }

    public Tir(Tir t){
        super(t);
    }

    /**
     * Fait tirer le personnage vers la case renseignée.&nbsp;Si le cout pour tirer dépasse le nombre de points d'actions du
     * personnage, ne fait rien.&nbsp;Si la cible n'est pas visible ou pas dans la ligne de mire (en croix), ne fait rien.
     * &nbsp;Si la cible est un opérateur, termine la partie.
     *
     * @param env L'environnement
     * @param perso Le personnage effectuant le tir
     * @param arr La destination
     */
    @Override
    public void effectuer(Environnement env, Personnage perso, Case arr){
        if(perso instanceof Operateur){
            effectuer(env, (Operateur) perso, arr);
        }
        else if(perso instanceof Terroriste){
            effectuer(env, (Terroriste) perso, arr);
        }
    }

    public void effectuer(Environnement env, Terroriste terro, Case arr){
        if (terro.getY() == arr.y){ // Ennemi et case sur la meme ligne
            int min = Math.min(terro.getX(), arr.x);
            int max = Math.max(terro.getX(), arr.x);
            for(int x = min; x <= max; x++){    // Verification de la visibilité
                if(!env.getCase(x, terro.getY()).peutVoir && x != terro.getX()){
                    return;
                }
            }
//            System.out.println("Vous etes mort");

        }
        else if (terro.getX() == arr.x) { // Ennemi et case sur la meme colonne
            int min = Math.min(terro.getY(), arr.y);
            int max = Math.max(terro.getY(), arr.y);
            for(int y = min; y <= max; y++){    // Verification de la visibilité
                if(!env.getCase(terro.getX(), y).peutVoir && y != terro.getY()){
                    return;
                }
            }
//            System.out.println("Vous etes mort");
        }
    }

    /**
     * Fait tirer l'opérateur vers la case renseignée.&nbsp;Si le cout pour tirer dépasse le nombre de points d'actions de
     * l'opérateur, ne fait rien.&nbsp;Si la cible n'est pas visible ou pas dans la ligne de mire (en croix), ne fait rien.
     *
     * @param env L'environnement
     * @param perso L'opérateur effectuant le tir
     * @param arr La destination
     */
    @Override
    public void effectuer(Environnement env, Operateur perso, Case arr) {
        List<Double> nombres = env.getNombresAleatoires(1);

        if (perso.getY() == arr.y){ // Perso et case sur la meme ligne
            int min = Math.min(perso.getX(), arr.x);
            int max = Math.max(perso.getX(), arr.x);
            for(int x = min; x <= max; x++){
                if(!env.getCase(x, perso.getY()).peutVoir && x != perso.getX()){
                    return;
                }
            }
            if(env.aEnnemisSurCase(arr)){
                perso.removePointsAction(cout);
                if(nombres.get(0) > probaSucces){
//                    System.out.println("L'action a échoué");
                    return;
                }
                env.tuerEnnemis(arr);
                return;
            }
        }
        else if (perso.getX() == arr.x) { // Perso et case sur la meme colonne
            int min = Math.min(perso.getY(), arr.y);
            int max = Math.max(perso.getY(), arr.y);
            for(int y = min; y <= max; y++){
                if(!env.getCase(perso.getX(), y).peutVoir && y != perso.getY()){
                    return;
                }
            }
            if(env.aEnnemisSurCase(arr)){
                perso.removePointsAction(cout);
                if(nombres.get(0) > probaSucces){
//                    System.out.println("L'action a échoué");
                    return;
                }
                env.tuerEnnemis(arr);
                return;
            }
        }
//        System.out.println("Aucun ennemi présent sur cette case");
    }

    /**
     * Renvoie la liste des cibles valides pour tirer depuis le personnage.&nbsp;Les cibles valides sont celles dans la croix
     * autour du personnage (jusqu'aux bords du plateau) qui ne sont pas camouflés par du terrain.
     *
     * @param env L'environnement
     * @param perso Le personnage effectuant le tir
     * @return La liste des cibles valides
     */
    @Override
    public List<Case> getCasesValides(Environnement env, Personnage perso) {
        return List.of();
    }

    /**
     * Renvoie la liste des cibles valides pour tirer depuis l'opérateur.&nbsp;Les cibles valides sont celles dans la croix
     * autour de l'opérateur (jusqu'aux bords du plateau) qui ne sont pas camouflés par du terrain.
     *
     * @param env L'environnement
     * @param perso L'opérateur effectuant le tir
     * @return La liste des cibles valides
     */
    @Override
    public List<Case> getCasesValides(Environnement env, Operateur perso) {
        return getCasesValides(env, env.getCase(perso.getX(), perso.getY()));
    }

    public List<Case> getCasesValides(Environnement env, Case caseDepart){
        List<Case> casesValides = new ArrayList<>();
        Case c;
        for(Terroriste ennemi : env.getEnnemis()) {
            boolean peutVoir = false;
            c = env.getCase(ennemi.getX(), ennemi.getY());

            if (caseDepart.y == c.y) { // Perso et case sur la meme ligne
                int min = Math.min(caseDepart.x, c.x);
                int max = Math.max(caseDepart.x, c.x);
                peutVoir = true;
                for (int x = min; x <= max; x++) {
                    if (!env.getCase(x, caseDepart.y).peutVoir) {
                        peutVoir = false;
                        break;
                    }
                }
            }
            else if (caseDepart.x == c.x) { // Perso et case sur la meme colonne
                int min = Math.min(caseDepart.y, c.y);
                int max = Math.max(caseDepart.y, c.y);
                peutVoir = true;
                for (int y = min; y <= max; y++) {
                    if (!env.getCase(caseDepart.x, y).peutVoir) {
                        peutVoir = false;
                        break;
                    }
                }
            }
            if (peutVoir){
                casesValides.add(c);
            }
        }
        return casesValides;
    }

    @Override
    public double qvaleur(Environnement env, Operateur op, int xDepart, int yDepart, Case arr, double[][] utilites, double gamma) {
        Operateur opClone = new Operateur(op);
        opClone.setX(xDepart); opClone.setY(yDepart);

        List<Case> casesValides = getCasesValides(env, opClone);

        // Qval initialisé à cas ou le tir echoue
        double qval = (1-probaSucces) * (env.getCase(xDepart, yDepart).recompense + gamma * utilites[xDepart][yDepart]);

        if(casesValides.contains(arr) && opClone.getPointsAction() >= cout){
            qval += probaSucces * (1 + gamma * utilites[arr.x][arr.y]);
        }
        return qval;
    }

    @Override
    public String toString() {
        return "Tir";
    }

    public Tir copy(){
        return new Tir(this);
    }
}
