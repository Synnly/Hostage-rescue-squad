package carte;

public abstract class Case {
    public final int x, y;
    public final boolean peutVoir;
    public double recompense;

    public Case(int x, int y){
        this.x = x;
        this.y = y;
        this.peutVoir = true;
        this.recompense = 0.;
    }
}
