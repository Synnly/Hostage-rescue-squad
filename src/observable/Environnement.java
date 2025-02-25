package observable;

import actions.*;
import carte.*;
import personnages.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * L'environnement d'une mission.
 */
public class Environnement extends Observable{

    private final int largeur;
    private final int hauteur;
    private final List<Case> cases;
    private final Operateur operateur;
    private final List<Terroriste> ennemis;
    private int menace = 2;

    /**
     * Constructeur de l'environnement. Initialise le plateau de taille <code>largeur</code> x <code>hauteur</code>,
     * un opérateur et deux terroristes
     *
     * @param largeur Le nombre de cases en largeur du plateau. Doit être &gt;&nbsp;0
     * @param hauteur Le nombre de cases en hauteur du plateau. Doit être &gt;&nbsp;0
     * @param probaSuccesDeplacement La probabilité de succès des déplacements de l'opérateur. Doit être
     *                               0 &le;&nbsp;proba &le;&nbsp;1
     * @param probaSuccesTir La probabilité de succès des tirs de l'opérateur. Doit être
     *                       0 &le;&nbsp;proba &le;&nbsp;1
     */
    public Environnement(int largeur, int hauteur, double probaSuccesDeplacement, double probaSuccesTir){
        assert largeur > 0 : "La largeur doit être > 0 (largeur =" + largeur + ")";
        assert hauteur > 0 : "La hauteur doit être > 0 (hauteur =" + hauteur + ")";
        assert probaSuccesDeplacement <= 1 && probaSuccesDeplacement >= 0 : "La probabilité de succès des déplacements de l'opérateur doit être 0 <= proba <= 1";
        assert probaSuccesTir <= 1 && probaSuccesTir >= 0 : "La probabilité de succès des tirs de l'opérateur doit être 0 <= proba <= 1";

        this.largeur = largeur;
        this.hauteur = hauteur;

        cases = new ArrayList<>();
        initPlateau(largeur, hauteur);

        // Creation des opérateurs
        Deplacement deplacementOp = new Deplacement(1, probaSuccesDeplacement);
        Tir tirOp = new Tir(1, probaSuccesTir);
        operateur = new Operateur(this, largeur/2, hauteur-1, 2, deplacementOp, tirOp);
        operateur.setActionActive(deplacementOp);

        // Création de la routine
        Routine routine = creerRoutine();

        // Création des ennemis
        Deplacement deplacementTer = new Deplacement(0, 1);
        Tir tirTer = new Tir(0, 1);
        ennemis = new ArrayList<>(1);

        Terroriste ennemi = new Terroriste(this, largeur/2, 0, 0, deplacementTer, tirTer);
        Terroriste ennemi2 = new Terroriste(this, largeur/2, 4, 0, deplacementTer, tirTer);
        ennemi.setRoutine(routine);
        ennemi2.setRoutine(routine);
        ennemis.add(ennemi);
        ennemis.add(ennemi2);
    }

    /**
     * Fonction temporaire de creation de routine
     *
     * @return La routine
     */
    public Routine creerRoutine(){
        Case pred = getCase(largeur/2, 0);
        Case next;
        Routine routine = new Routine(pred);

        next = getCase(pred.x+1, pred.y); routine.ajouterCase(pred, next); pred = next;
        next = getCase(pred.x+1, pred.y); routine.ajouterCase(pred, next); pred = next;

        next = getCase(pred.x, pred.y+1); routine.ajouterCase(pred, next); pred = next;
        next = getCase(pred.x, pred.y+1); routine.ajouterCase(pred, next); pred = next;
        next = getCase(pred.x, pred.y+1); routine.ajouterCase(pred, next); pred = next;
        next = getCase(pred.x, pred.y+1); routine.ajouterCase(pred, next); pred = next;

        next = getCase(pred.x-1, pred.y); routine.ajouterCase(pred, next); pred = next;
        next = getCase(pred.x-1, pred.y); routine.ajouterCase(pred, next); pred = next;
        next = getCase(pred.x-1, pred.y); routine.ajouterCase(pred, next); pred = next;
        next = getCase(pred.x-1, pred.y); routine.ajouterCase(pred, next); pred = next;

        next = getCase(pred.x, pred.y-1); routine.ajouterCase(pred, next); pred = next;
        next = getCase(pred.x, pred.y-1); routine.ajouterCase(pred, next); pred = next;
        next = getCase(pred.x, pred.y-1); routine.ajouterCase(pred, next); pred = next;
        next = getCase(pred.x, pred.y-1); routine.ajouterCase(pred, next); pred = next;

        next = getCase(pred.x+1, pred.y); routine.ajouterCase(pred, next);


        return routine;
    }

    /**
     * Renvoie la largeur du plateau en nombre de cases
     *
     * @return La largeur
     */
    public int getLargeur() {return largeur;}

    /**
     * Renvoie la hauteur du plateau en nombre de cases
     *
     * @return La hauteur
     */
    public int getHauteur() {return hauteur;}

    /**
     * Renvoie la liste des cases constituant le plateau.&nbsp;Les cases sont ordonnés dans l'ordre de la largeur puis hauteur,
     * donc la case d'indice <code>i</code> est de coordonnées <code>(i % largeur, i // largeur)</code>
     *
     * @return Le plateau
     */
    public List<Case> getPlateau(){return cases;}

    /**
     * Retourne la case aux coordonnées (<code>x</code>, <code>y</code>)
     *
     * @param x La coordonnée en largeur de la case cible. Doit être 0 &le;&nbsp;<code>x</code>&lt;&nbsp;<code>largeur</code>
     * @param y La coordonnée en hauteur de la case cible. Doit être 0 &le;&nbsp;<code>y</code>&lt;&nbsp;<code>hauteur</code>
     * @return La case
     */
    public Case getCase(int x, int y){
        assert x >= 0 && x < largeur : "x doit être 0 <= x < largeur (x = "+ x + ")";
        assert y >= 0 && y < hauteur : "y doit être 0 <= y < hauteur (y = "+ y + ")";

        return cases.get(y * largeur + x);
    }

    /**
     * Renvoie l'opérateur actif, c'est-à-dire le dernier opérateur sélectionné pour
     * une action ou l'opérateur suivant dans la liste après que l'ancien opérateur
     * a fini son tour
     *
     * @return L'opérateur actif
     */
    public Operateur getOperateurActif() {
        return operateur;
    }

    /**
     * Renvoie la liste des ennemis en vie.
     *
     * @return La liste des ennemis
     */
    public List<Terroriste> getEnnemis(){
        return ennemis;
    }

    /**
     * Recrée le plateau de taille <code>largeur</code> x <code>hauteur</code>
     *
     * @param largeur Le nombre de cases en largeur du plateau. Doit être &gt;&nbsp;0
     * @param hauteur Le nombre de cases en hauteur du plateau. Doit être &gt;&nbsp;0
     */
    public void initPlateau(int largeur, int hauteur){
        assert largeur > 0 : "La largeur doit être > 0 (largeur =" + largeur + ")";
        assert hauteur > 0 : "La hauteur doit être > 0 (hauteur =" + hauteur + ")";

        cases.clear();
        for (int y = 0; y < hauteur; y++) {
            for (int x = 0; x < largeur; x++) {
                cases.add(new CaseNormale(this, x, y));
            }
        }
        int x = (largeur/2);
        int y = (hauteur/4);
        cases.set(y * largeur + x, new Objectif(this, x, y));
    }

    /**
     * Effectue le tour des ennemis.&nbsp;Remet l'action choisie par les opérateurs au déplacement et leur redonne tous leurs
     * points d'action
     */
    public void tourEnnemi(){
        List<Double> nombres = getNombresAleatoires(menace);
        for(int i = 0; i < menace; i++) {
            if(nombres.get(i) < 0.3){   // Tir
                for (Terroriste ennemi : ennemis) {
                    ennemi.getTir().effectuer(this, ennemi, getCase(operateur.getX(), operateur.getY()));
                }
            }
            else {
                for (Terroriste ennemi : ennemis) {     // Deplacement
                    Case posEnnemi = getCase(ennemi.getX(), ennemi.getY());
                    ennemi.getDeplacement().effectuer(this, ennemi, ennemi.getRoutine().prochaineCase(posEnnemi));
                }
            }
            notifyObservers();
        }

        operateur.setActionActive(operateur.getDeplacement());
        operateur.resetPointsAction();
    }

    /**
     * Définit la case ciblée par l'action active de l'opérateur actif
     *
     * @param x La coordonnée en largeur de la case cible. Doit être 0 &le;&nbsp;<code>x</code>&lt;&nbsp;<code>largeur</code>
     * @param y La coordonnée en hauteur de la case cible. Doit être 0 &le;&nbsp;<code>y</code>&lt;&nbsp;<code>hauteur</code>
     */
    public void choisirCase(int x, int y){
        assert x >= 0 && x < largeur : "x doit être 0 <= x < " + largeur + "(x = " + x + ")";
        assert y >= 0 && y < hauteur : "y doit être 0 <= y < " + hauteur + "(y = " + y + ")";

        Case c = getCase(x, y);

        // Effectuer l'action
        Operateur op = getOperateurActif();
        op.getActionActive().effectuer(this, op, c);

        // Tour ennemi
        if(op.getPointsAction() == 0){
            tourEnnemi();
        }
        notifyObservers();
    }

    /**
     * Fait récupérer l'objectif à l'opérateur.&nbsp;L'objectif devient une case normale après l'appel de cette fonction
     *
     * @param obj L'objectif récupéré.&nbsp;Ne peut être <code>null</code>.
     * @param op  L'opérateur récupérant l'objectif.&nbsp;Ne peut être <code>null</code>.&nbsp;Doit être sur la même case que l'objectif.
     */
    public void recupereObjectif(Objectif obj, Operateur op){
        assert obj != null : "L'objectif ne peut être null";
        assert op != null : "L'opérateur ne peut être null";

        assert obj.x == op.getX() && obj.y == op.getY() : "L'opérateur doit être sur l'objectif";

        op.setPossedeObjectif(true);
        int index = cases.indexOf(obj);
        cases.set(index, new CaseNormale(this, obj.x, obj.y));
    }

    /**
     * Définit l'action de déplacement comme action active pour l'opérateur actif
     */
    public void setDeplacementActionActive(){
        operateur.setActionActive(operateur.getDeplacement());
        notifyObservers();
    }

    /**
     * Définit l'action de tir comme action active pour l'opérateur actif
     */
    public void setTirActionActive(){
        operateur.setActionActive(operateur.getTir());
        notifyObservers();
    }

    /**
     * Définit l'action de fin de tour comme action active pour l'opérateur actif
     */
    public void setFinTourActionActive(){
        operateur.setActionActive(operateur.getDeplacement());
        tourEnnemi();
        notifyObservers();
    }

    /**
     * Tue tous les ennemis présents sur la case <code>arr</code>.&nbsp;Si aucun ennemi n'est dans la case, ne fait
     * rien.
     *
     * @param arr La case sur laquelle se situent les ennemis à tuer.&nbsp;Ne peut être <code>null</code>
     */
    public void tuerEnnemis(Case arr){
        assert arr != null : "La case ne peut être null";
        List<Terroriste> aTuer = new ArrayList<>();
        for(Terroriste ennemi : ennemis) {
            if (ennemi.getX() == arr.x && ennemi.getY() == arr.y) {
                aTuer.add(ennemi);
            }
        }
        ennemis.removeAll(aTuer);
    }

    /**
     * Indique si au moins un ennemi est présent sur la case <code>c</code>
     *
     * @param c La case.&nbsp;Ne peut être <code>null</code>
     * @return <code>true</code> si au moins un ennemi est présent dessus, <code>false</code> sinon
     */
    public boolean aEnnemisSurCase(Case c){
        assert c != null : "La case ne peut être null";
        for(Terroriste ennemi : ennemis){
            if(ennemi.getX() == c.x && ennemi.getY() == c.y){
                return true;
            }
        }
        return false;
    }

    /**
     * Renvoie une liste de réels aléatoires entre 1 et 0
     * @param nb Le nombre de réels à générer. Doit être &gt;&nbsp;0
     * @return La liste des nombres
     */
    public List<Double> getNombresAleatoires(int nb){
        assert nb > 0 : "Le nombre de réels à générer doit être strictement positif";
        Random random = new Random();
        List<Double> nombres = new ArrayList<>();
        for (int i = 0; i < nb; i++) {
            nombres.add(random.nextDouble());
        }
        return nombres;
    }
}
