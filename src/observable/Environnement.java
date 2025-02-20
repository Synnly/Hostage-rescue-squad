package observable;

import actions.Deplacement;
import carte.Case;
import carte.CaseNormale;
import personnages.Operateur;

import java.util.ArrayList;
import java.util.List;

public class Environnement extends Observable{

    private final int largeur = 6;
    private final int hauteur = 10;
    private final List<Case> cases;
    private final Operateur operateur;

    /**
     * Constructeur de l'environnement
     */
    public Environnement(){
        cases = new ArrayList<>();

        // Creation des opérateurs
        operateur = new Operateur(0, 0, 2);
        Deplacement depl = new Deplacement(1, 1);
        operateur.ajouterAction(depl);
        operateur.setActionActive(depl);

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
        notifyObservers();
    }
}
