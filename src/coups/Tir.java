package coups;

import carte.cases.Case;
import carte.separation.Separation;
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

    /**
     * Constructeur de copie d'un coup tir
     * @param t Le coup à copier
     */
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
        if(perso.estOperateur()){
            effectuer(env, (Operateur) perso, arr);
        }
        else if(perso.estOperateur()){
            effectuer(env, (Terroriste) perso, arr);
        }
    }

    /**
     * Fait tirer le terroriste vers la case renseignée.&nbsp;Si le cout pour tirer dépasse le nombre de points d'actions du
     * personnage, ne fait rien.&nbsp;Si la cible n'est pas visible ou pas dans la ligne de mire (en croix), ne fait rien.
     * &nbsp;Si la cible est un opérateur, termine la partie.
     *
     * @param env L'environnement
     * @param terro Le terroriste effectuant le tir
     * @param arr La destination
     */
    public void effectuer(Environnement env, Terroriste terro, Case arr){;
        if (terro.getY() == arr.y){ // Ennemi et case sur la meme ligne
            int min = Math.min(terro.getX(), arr.x);
            int max = Math.max(terro.getX(), arr.x);
            for(int x = min; x <= max; x++){    // Verification de la visibilité
                if(!env.getCase(x, terro.getY()).peutVoir() && x != terro.getX()){
                    return;
                }
                for(Separation sep : env.getSeparations()){
                    if(!sep.estVertical()) continue;
                    // Separation uniquement ENTRE terro et op
                    if(sep.getCase1().y != terro.getY()) continue;
                    if(sep.getCase1().x > max || sep.getCase1().x < min) continue;
                    if(sep.getCase2().x > max || sep.getCase2().x < min) continue;
                    if(!sep.peutVoir()) return;
                }
            }
            env.terminerMission(false);

        }
        else if (terro.getX() == arr.x) { // Ennemi et case sur la meme colonne
            int min = Math.min(terro.getY(), arr.y);
            int max = Math.max(terro.getY(), arr.y);
            for(int y = min; y <= max; y++){    // Verification de la visibilité
                if(!env.getCase(terro.getX(), y).peutVoir() && y != terro.getY()){
                    return;
                }
            }
            for(Separation sep : env.getSeparations()){
                if(sep.estVertical()) continue;
                // Separation uniquement ENTRE terro et op
                if(sep.getCase1().x != terro.getX()) continue;
                if(sep.getCase1().y > max || sep.getCase1().y < min) continue;
                if(sep.getCase2().y > max || sep.getCase2().y < min) continue;
                if(!sep.peutVoir()) return; // Separation valide mais pas visible
            }
            env.terminerMission(false);
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
        if(arr.x == -1 && arr.y == -1){
            return;
        }
        if(perso.getX() > arr.x){ // Gauche
            effectuerTirHorizontal(env, perso, true);
        }
        else if(perso.getX() < arr.x) { // Droite
            effectuerTirHorizontal(env, perso, false);
        }
        else if(perso.getY() > arr.y){ // Haut
            effectuerTirVertical(env, perso, true);
        }
        else { // Bas
            effectuerTirVertical(env, perso, false);
        }
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

    @Override
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
                    for(Separation sep : env.getSeparations()){
                        if(!sep.estVertical()) continue;
                        // Separation uniquement ENTRE terro et op
                        if(sep.getCase1().y != caseDepart.y) continue;
                        if(sep.getCase1().x > max || sep.getCase1().x < min) continue;
                        if(sep.getCase2().x > max || sep.getCase2().x < min) continue;
                        if(!sep.peutVoir()) peutVoir = false;
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
                    for(Separation sep : env.getSeparations()){
                        if(sep.estVertical()) continue;
                        // Separation uniquement ENTRE terro et op
                        if(sep.getCase1().x != caseDepart.x) continue;
                        if(sep.getCase1().y > max || sep.getCase1().y < min) continue;
                        if(sep.getCase2().y > max || sep.getCase2().y < min) continue;
                        if(!sep.peutVoir()) peutVoir = false; // Separation valide mais pas visible
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
    public String toString() {
        return "Tir";
    }

    public Tir copy(){
        return new Tir(this);
    }

    @Override
    public boolean estTir(){
        return true;
    }

    /**
     * Tue les ennemis présents dans la case c. A une chance d'échouer en fonction de la probabilité de succès du coup.
     * @param env L'environnement
     * @param c La case cible
     */
    private void tuerEnnemis(Environnement env, Case c){
        List<Double> nombres = env.getNombresAleatoires(1);
        if (nombres.get(0) > probaSucces) {
//          System.out.println("L'action a échoué");
            return;
        }
        env.tuerEnnemis(c);
        if (env.tousTerrsMorts()) {
            env.resetMenace();
        } else {
            env.augmenterMenace();
        }
    }

    /**
     * Effectue le tir dans la colonne où se situe l'opérateur
     * @param env L'environnement
     * @param perso L'opérateur effectuant le tir
     * @param versHaut Vrai si le tir est vers le haut, faux sinon
     */
    private void effectuerTirVertical(Environnement env, Operateur perso, boolean versHaut){
        int stopVal = versHaut ? 0 : env.getHauteur()-1;
        int yCase = perso.getY();

        while(yCase != stopVal){
            yCase += versHaut ? -1: 1;

            Case c = env.getCase(perso.getX(), yCase);

            int min = Math.min(perso.getY(), yCase);
            int max = Math.max(perso.getY(), yCase);
            for (int y = min; y <= max; y++) {
                if (!env.getCase(perso.getX(), y).peutVoir() && y != perso.getY()) {
                    continue;
                }
                for(Separation sep : env.getSeparations()){
                    if(sep.estVertical()) continue;
                    // Separation uniquement ENTRE terro et op
                    if(sep.getCase1().x != perso.getX()) continue;
                    if(sep.getCase1().y > max || sep.getCase1().y < min) continue;
                    if(sep.getCase2().y > max || sep.getCase2().y < min) continue;
                    if(!sep.peutVoir()) return; // Separation valide mais pas visible
                }
            }

            if (env.aEnnemisSurCase(c)) {
                perso.removePointsAction(cout);
                tuerEnnemis(env, c);
            }

        }
    }

    /**
     * Effectue le tir dans la ligne où se situe l'opérateur
     * @param env L'environnement
     * @param perso L'opérateur effectuant le tir
     * @param versGauche Vrai si le tir est vers la gauche, faux sinon
     */
    private void effectuerTirHorizontal(Environnement env, Operateur perso, boolean versGauche){
        int stopVal = versGauche ? 0 : env.getLargeur()-1;
        int xCase = perso.getX();

        while(xCase != stopVal){
            xCase += versGauche ? -1: 1;
            Case c = env.getCase(xCase, perso.getY());

            int min = Math.min(perso.getX(), c.x);
            int max = Math.max(perso.getX(), c.x);
            for(int x = min; x <= max; x++){
                if(!env.getCase(x, perso.getY()).peutVoir() && x != perso.getX()){
                    continue;
                }
                for(Separation sep : env.getSeparations()){
                    if(!sep.estVertical()) continue;
                    // Separation uniquement ENTRE terro et op
                    if(sep.getCase1().y != perso.getY()) continue;
                    if(sep.getCase1().x > max || sep.getCase1().x < min) continue;
                    if(sep.getCase2().x > max || sep.getCase2().x < min) continue;
                    if(!sep.peutVoir()) return;
                }
            }

            if (env.aEnnemisSurCase(c)) {
                perso.removePointsAction(cout);
                tuerEnnemis(env, c);
            }
        }
    }
}
