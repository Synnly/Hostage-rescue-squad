package carte.separation;

import carte.cases.Case;

public class Mur extends Separation{

    public Mur(Case case1, Case case2) {
        super(case1, case2, false, false);
    }

    public Mur(Separation sep){
        super(sep);
    }

    @Override
    public Mur copy() {
        return new Mur(this);
    }
}
