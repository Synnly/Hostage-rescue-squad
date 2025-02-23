package observable;

import actions.Deplacement;
import carte.Case;
import carte.CaseNormale;
import carte.Objectif;
import carte.Routine;
import personnages.Operateur;
import personnages.Terroriste;

import java.util.ArrayList;
import java.util.List;

public class Environnement extends Observable{

    private  int largeur = 7;
    private  int hauteur = 10;
    private  List<Case> cases;
    private  Operateur operateur;
    private  Terroriste ennemi;

    /**
     * Constructeur de l'environnement
     */
    public Environnement(){
        this.nouvellePartie();
    }
    public void nouvellePartie(){
        cases = new ArrayList<>();
        initPlateau(largeur, hauteur);

        // Creation des opérateurs
        Deplacement deplacementOp = new Deplacement(1, 1);
        operateur = new Operateur(largeur/2, hauteur-1, 2, deplacementOp);
        operateur.setActionActive(deplacementOp);

        // Création de la routine
        Routine routine = creerRoutine();

        // Création des ennemis
        Deplacement deplacementTer = new Deplacement(0, 1);
        ennemi = new Terroriste(largeur/2, 0, 0, deplacementTer);
        ennemi.setRoutine(routine);
    }


    /**
     * Fonction temporaire de creation de routine
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
     * @return La largeur
     */
    public int getLargeur() {return largeur;}

    /**
     * Renvoie la hauteur du plateau en nombre de cases
     * @return La hauteur
     */
    public int getHauteur() {return hauteur;}

    /**
     * Renvoie la liste des cases constituant le plateau. Les cases sont ordonnés dans l'ordre de la largeur puis hauteur,
     * donc la case d'indice i est de coordonnées (i % largeur, i // largeur)
     * @return Le plateau
     */
    public List<Case> getPlateau(){return cases;}

    /**
     * Retourne la case aux coordonnées (x, y) en partant du coin haut gauche
     * @param x La coordonnée en largeur
     * @param y La coordonnée en hauteur
     * @return La case
     */
    public Case getCase(int x, int y){
        return cases.get(y * largeur + x);
    }

    /**
     * Renvoie l'opérateur actif, c'est-à-dire le dernier opérateur sélectionné pour
     * une action ou l'opérateur suivant dans la liste après que l'ancien opérateur
     * aie fini son tour
     * @return L'opérateur actif
     */
    public Operateur getOperateurActif() {
        return operateur;
    }

    public Terroriste getEnnemi(){
        return ennemi;
    }

    /**
     * Recrée la liste des cases
     * @param largeur Le nombre de cases en largeur
     * @param hauteur Le nombre de cases en hauteur
     */
    public void initPlateau(int largeur, int hauteur){
        cases.clear();
        for (int y = 0; y < hauteur; y++) {
            for (int x = 0; x < largeur; x++) {
                cases.add(new CaseNormale(x, y));
            }
        }
        int x = (largeur/2);
        int y = (hauteur/4);
        cases.set(y * largeur + x, new Objectif(x, y));
    }

    public void tourEnnemi(){
        Case posEnnemi = getCase(ennemi.getX(), ennemi.getY());
        ennemi.getDeplacement().effectuer(this, ennemi, ennemi.getRoutine().prochaineCase(posEnnemi));
        notifyObservers();
    }

    /**
     * Choisit la case ciblée par l'action active de l'opérateur actif
     * @param x La coordonnée en largeur de la case cible
     * @param y La coordonnée en hauteur de la case cible
     */
    public void choisirCase(int x, int y){
        Case c = getCase(x, y);

        // Effectuer l'action
        Operateur op = getOperateurActif();
        op.getActionActive().effectuer(this, op, c);

        // Tour ennemi
        if(op.getPointsAction() == 0){
            tourEnnemi();

            op.resetPointsAction();
        }
        notifyObservers();
    }

    /**
     * Fait récupérer l'objectif à l'opérateur. L'objectif devient une case normale après l'appel de cette fonction
     * @param obj L'objectif récupéré
     * @param op L'opérateur récupérant l'objectif
     */
    public void recupereObjectif(Objectif obj, Operateur op){
        op.setPossedeObjectif(true);
        int index = cases.indexOf(obj);
        cases.set(index, new CaseNormale(obj.x, obj.y));
    }
}
