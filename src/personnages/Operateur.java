package personnages;

import actions.Deplacement;

public class Operateur extends Personnage{
    private boolean possedeObjectif = false;

    public Operateur(int x, int y, int pointsAction, Deplacement deplacement) {
        super(x, y, pointsAction, deplacement);
    }

    public boolean possedeObjectif() {
        return possedeObjectif;
    }

    public void setPossedeObjectif(boolean possedeObjectif) {
        this.possedeObjectif = possedeObjectif;
    }
}
