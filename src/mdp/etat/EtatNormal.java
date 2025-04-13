package mdp.etat;

import observable.Environnement;

import java.util.Arrays;

public class EtatNormal extends Etat {

    /**
     * Constructeur d'un état normal, c'est à dire quand la partie est encore en cours.
     * @param indCaseOperateurs La liste des positions des cases où se situent les opérateurs dans la liste des cases du
     *                          plateau
     * @param nbPAOperateurs La liste des quantités de points d'action de chaque opérateur
     * @param aObjectif La liste des booléens indiquant si le ie opérateur possède un objectif
     * @param indCaseTerroristes La liste des positions des cases où se situent les terroristes dans la liste des cases
     *                           de la routine
     * @param menace Le niveau de menace
     */
    public EtatNormal(int[] indCaseOperateurs, int[] nbPAOperateurs, boolean[] aObjectif, int[] indCaseTerroristes, int menace) {
        super(indCaseOperateurs, nbPAOperateurs, aObjectif, indCaseTerroristes, menace);
    }

    /**
     * Constructeur d'un état normal à partir d'un environnement
     * @param env L'environnement
     */
    public EtatNormal(Environnement env){
        super(env);
    }

    @Override
    public boolean estTerminal() {
        return false;
    }

    @Override
    public boolean estReussite() {
        return false;
    }


    boolean equals(EtatNormal en){
        return Arrays.equals(indCaseOperateurs, en.indCaseOperateurs) && Arrays.equals(indCaseTerroristes, en.indCaseTerroristes) && menace == en.menace && Arrays.equals(nbPAOperateurs, en.nbPAOperateurs) && Arrays.equals(aObjectif, en.aObjectif);
    }
    @Override
    public boolean estNormal(){return true;}
}
