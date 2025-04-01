package mdp;

import observable.Environnement;

public class EtatEchec extends Etat{

    /**
     * Constructeur d'un état échec, c'est à dire quand un des opérateurs meurt.
     * @param indCaseOperateurs La liste des positions des cases où se situent les opérateurs dans la liste des cases du
     *                          plateau
     * @param aObjectif La liste des booléens indiquant si le ie opérateur possède un objectif
     * @param indCaseTerroristes La liste des positions des cases où se situent les terroristes dans la liste des cases
     *                           de la routine
     * @param menace Le niveau de menace
     */
    public EtatEchec(int[] indCaseOperateurs, boolean[] aObjectif, int[] indCaseTerroristes, int menace) {
        super(indCaseOperateurs, aObjectif, indCaseTerroristes, menace);
    }

    /**
     * Constructeur d'un état échec à partir d'un environnement
     * @param env L'environnement
     */
    public EtatEchec(Environnement env){
        super(env);
    }

    @Override
    public boolean estTerminal() {
        return true;
    }

    @Override
    public boolean estReussite() {
        return false;
    }
}
