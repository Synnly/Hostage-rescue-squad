package carte.separation;

import carte.cases.Case;

import java.util.Objects;

public abstract class Separation {
    private final Case case1, case2;
    private final boolean peutVoir, peutPasser;
    private final boolean estVertical;

    public Separation(Case case1, Case case2, boolean peutVoir, boolean peutPasser) {
        assert case1 != case2 : "Les deux cases doivent être différentes";
        assert (Math.abs(case1.x - case2.x) == 1) != (Math.abs(case1.y - case2.y) == 1) : "Les deux cases doivent être cotes à cotes";
        this.case1 = case1;
        this.case2 = case2;
        this.peutVoir = peutVoir;
        this.peutPasser = peutPasser;
        this.estVertical = case1.y == case2.y;
    }

    public Separation(Separation sep){
        this.case1 = sep.case1.copy();
        this.case2 = sep.case2.copy();
        this.peutVoir = sep.peutVoir;
        this.peutPasser = sep.peutPasser;
        this.estVertical = sep.estVertical;
    }

    public Case getCase1() {
        return case1;
    }

    public Case getCase2() {
        return case2;
    }

    public boolean peutVoir() {
        return peutVoir;
    }

    public boolean peutPasser() {
        return peutPasser;
    }

    public boolean estVertical(){
        return  estVertical;
    }

    public abstract Separation copy();

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Separation that = (Separation) o;
        return peutVoir == that.peutVoir && peutPasser == that.peutPasser && Objects.equals(case1, that.case1) && Objects.equals(case2, that.case2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(case1, case2, peutVoir, peutPasser);
    }

    @Override
    public String toString() {
        return "Separation{" +
                "case1=" + case1 +
                ", case2=" + case2 +
                ", peutVoir=" + peutVoir +
                ", peutPasser=" + peutPasser +
                ", estVertical=" + estVertical +
                '}';
    }
}
