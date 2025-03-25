package mdp;

public class EtatReussite extends Etat{


    public EtatReussite(int[] indCaseOperateurs, boolean[] aObjectif, int[] indCaseTerroristes, int menace) {
        super(indCaseOperateurs, aObjectif, indCaseTerroristes, menace);
    }

    @Override
    public boolean estTerminal() {
        return true;
    }

    @Override
    public boolean estReussite() {
        return true;
    }
}
