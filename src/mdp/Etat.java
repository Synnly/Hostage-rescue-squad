package mdp;

import carte.Routine;
import observable.Environnement;

import java.util.Arrays;
import java.util.Objects;

public abstract class Etat{
    public final int[] indCaseOperateurs;
    public final boolean[] aObjectif;
    public final int[] indCaseTerroristes;
    public final int menace;

    public Etat(int[] indCaseOperateurs, boolean[] aObjectif, int[] indCaseTerroristes, int menace){
        this.indCaseOperateurs = indCaseOperateurs;
        this.aObjectif = aObjectif;
        this.indCaseTerroristes = indCaseTerroristes;
        this.menace = menace;
    }

    public Etat(Environnement env){
        this.indCaseOperateurs = new int[]{env.getPlateau().indexOf(env.getCase(env.getOperateurActif().getX(), env.getOperateurActif().getY()))};
        this.aObjectif = new boolean[]{env.getOperateurActif().possedeObjectif()};

        int[] indCasesTerr = new int[env.getEnnemis().size()];
        Routine r = env.getEnnemis().get(0).getRoutine();
        for (int indTerr = 0; indTerr < env.getEnnemis().size(); indTerr++) {
            indCasesTerr[indTerr] = r.indexOf(env.getCase(env.getEnnemis().get(indTerr).getX(), env.getEnnemis().get(indTerr).getY()));
        }
        this.indCaseTerroristes = indCasesTerr;
        this.menace = env.getMenace();
    }

    public abstract boolean estTerminal();

    public abstract boolean estReussite();

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Etat etat = (Etat) o;
        return menace == etat.menace && Objects.deepEquals(indCaseOperateurs, etat.indCaseOperateurs) && Objects.deepEquals(aObjectif, etat.aObjectif) && Objects.deepEquals(indCaseTerroristes, etat.indCaseTerroristes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Arrays.hashCode(indCaseOperateurs), Arrays.hashCode(aObjectif), Arrays.hashCode(indCaseTerroristes), menace);
    }

    @Override
    public String toString() {
        return "Etat{" +
                "ops=" + Arrays.toString(indCaseOperateurs) +
                ", obj=" + Arrays.toString(aObjectif) +
                ", terrs=" + Arrays.toString(indCaseTerroristes) +
                ", m=" + menace +
                '}';
    }
}

