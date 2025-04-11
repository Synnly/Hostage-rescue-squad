package mdp;

import carte.Routine;
import observable.Environnement;

import java.util.Arrays;
import java.util.Objects;

public abstract class Etat{
    public final int[] indCaseOperateurs;
    public final int[] nbPAOperateurs;
    public final boolean[] aObjectif;
    public final int[] indCaseTerroristes;
    public final int menace;

    /**
     * Constructeur d'un état type.
     * @param indCaseOperateurs La liste des positions des cases où se situent les opérateurs dans la liste des cases du
     *                          plateau
     * @param nbPAOperateurs La liste des quantités de points d'action de chaque opérateur
     * @param aObjectif La liste des booléens indiquant si le ie opérateur possède un objectif
     * @param indCaseTerroristes La liste des positions des cases où se situent les terroristes dans la liste des cases
     *                           de la routine
     * @param menace Le niveau de menace
     */
    public Etat(int[] indCaseOperateurs, int[] nbPAOperateurs, boolean[] aObjectif, int[] indCaseTerroristes, int menace){
        this.indCaseOperateurs = indCaseOperateurs;
        this.nbPAOperateurs = nbPAOperateurs;
        this.aObjectif = aObjectif;
        this.indCaseTerroristes = indCaseTerroristes;
        this.menace = menace;
    }

    /**
     * Constructeur d'un état à partir d'un environnement. Les champs copiés sont en copie profonde.
     * @param env L'environnement
     */
    public Etat(Environnement env){
        this.indCaseOperateurs = new int[]{env.getPlateau().indexOf(env.getCase(env.getOperateurActif().getX(), env.getOperateurActif().getY()))};
        this.aObjectif = new boolean[]{env.getOperateurActif().possedeObjectif()};
        int[] indCasesTerr = new int[env.getEnnemis().size()];
        this.nbPAOperateurs = new int[]{env.getOperateurActif().getPointsAction()};

        Routine r = env.getEnnemis().get(0).getRoutine();
        for (int indTerr = 0; indTerr < env.getEnnemis().size(); indTerr++) {
            indCasesTerr[indTerr] = r.indexOf(env.getCase(env.getEnnemis().get(indTerr).getX(), env.getEnnemis().get(indTerr).getY()));
        }
        this.indCaseTerroristes = indCasesTerr;
        this.menace = env.getMenace();
    }

    /**
     * Indique si cet état est terminal
     */
    public abstract boolean estTerminal();

    /**
     * Indique si cet état est une réussite de mission ou non. N'a d'utilité si estTerminal() = true.
     */
    public abstract boolean estReussite();

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Etat etat = (Etat) o;
        return menace == etat.menace && Objects.deepEquals(indCaseOperateurs, etat.indCaseOperateurs) && Objects.deepEquals(aObjectif, etat.aObjectif) && Objects.deepEquals(indCaseTerroristes, etat.indCaseTerroristes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Arrays.hashCode(indCaseOperateurs), Arrays.hashCode(nbPAOperateurs), Arrays.hashCode(aObjectif), Arrays.hashCode(indCaseTerroristes), menace);
    }

    @Override
    public String toString() {
        return "Etat{" +
                "indCaseOperateurs=" + Arrays.toString(indCaseOperateurs) +
                ", nbPAOperateurs=" + Arrays.toString(nbPAOperateurs) +
                ", aObjectif=" + Arrays.toString(aObjectif) +
                ", indCaseTerroristes=" + Arrays.toString(indCaseTerroristes) +
                ", menace=" + menace +
                '}';
    }

    public boolean estNormal(){
        return false;
    }
}

