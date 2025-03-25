package mdp;

import observable.Environnement;
import personnages.Operateur;
import personnages.Terroriste;

import java.util.List;

public abstract class Etat{
    int[] indCaseOperateurs;
    boolean[] aObjectif;
    int[] indCaseTerroristes;
    int menace;

    public Etat(int[] indCaseOperateurs, boolean[] aObjectif, int[] indCaseTerroristes, int menace){
        this.indCaseOperateurs = indCaseOperateurs;
        this.aObjectif = aObjectif;
        this.indCaseTerroristes = indCaseTerroristes;
        this.menace = menace;
    }

    public abstract boolean estTerminal();

    public abstract boolean estReussite();
}

