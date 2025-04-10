package mdp;

import observable.Environnement;

public class EtatReussite extends Etat{

    /**
     * Constructeur d'un état réussite, c'est à dire quand tous les opérateurs sont sur la ligne du bas du plateau, tous
     * les objectifs ont été récupérés et tous les opérateurs sont en vie.
     * @param indCaseOperateurs La liste des positions des cases où se situent les opérateurs dans la liste des cases du
     *                          plateau
     * @param nbPAOperateurs La liste des quantités de points d'action de chaque opérateur
     * @param aObjectif La liste des booléens indiquant si le ie opérateur possède un objectif
     * @param indCaseTerroristes La liste des positions des cases où se situent les terroristes dans la liste des cases
     *                           de la routine
     * @param menace Le niveau de menace
     */
    public EtatReussite(int[] indCaseOperateurs, int[] nbPAOperateurs, boolean[] aObjectif, int[] indCaseTerroristes, int menace) {
        super(indCaseOperateurs, nbPAOperateurs, aObjectif, indCaseTerroristes, menace);
    }

    /**
     * Constructeur d'un état réussite à partir d'un état
     * @param env L'environnement
     */
    public EtatReussite(Environnement env){
        super(env);
    }

    @Override
    public boolean estTerminal() {
        return true;
    }

    @Override
    public boolean estReussite() {
        return true;
    }

    @Override
    public EtatReussite copy() {
        return new EtatReussite(this.indCaseOperateurs,this.aObjectif,this.indCaseTerroristes, this.menace);
    }

    @Override
    public Boolean estEchec() {
        return false;
    }
}
