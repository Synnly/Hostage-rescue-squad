package carte.cases;

import observable.Environnement;

public class CaseReapparitionEnnemis extends Case {
    public CaseReapparitionEnnemis(Case c) {
        super(c);
    }

    public CaseReapparitionEnnemis(Environnement env, int x, int y, boolean peutVoir) {
        super(env, x, y, peutVoir);
    }

    protected CaseReapparitionEnnemis(int x, int y, boolean peutVoir) {
        super(x, y, peutVoir);
    }

    @Override
    public Case copy() {
        return new CaseReapparitionEnnemis(this);
    }
}
