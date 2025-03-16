package carte;

public class AucuneCase extends Case{
    public static final AucuneCase instance = new AucuneCase(-1, -1, true);

    private AucuneCase(int x, int y, boolean peutVoir) {
        super(x, y, peutVoir);
    }

    @Override
    public Case copy() {
        return instance;
    }
}
