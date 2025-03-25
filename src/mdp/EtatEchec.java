package mdp;

public class EtatEchec extends Etat{


    public EtatEchec(int[] indCaseOperateurs, boolean[] aObjectif, int[] indCaseTerroristes, int menace) {
        super(indCaseOperateurs, aObjectif, indCaseTerroristes, menace);
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
