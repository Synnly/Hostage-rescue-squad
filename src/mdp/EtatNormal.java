package mdp;

import observable.Environnement;

public class EtatNormal extends Etat{


    public EtatNormal(int[] indCaseOperateurs, boolean[] aObjectif, int[] indCaseTerroristes, int menace) {
        super(indCaseOperateurs, aObjectif, indCaseTerroristes, menace);
    }

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
}
