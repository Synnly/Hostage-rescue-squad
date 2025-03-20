package mdp;

import observable.Environnement;
import personnages.Operateur;
import personnages.Terroriste;

import java.util.List;

public record Etat(Operateur operateur, List<Terroriste> ennemis, int menace, boolean estEchec, boolean estTerminal){

    public Etat(Environnement env){
        this(env.getOperateurActif(), env.getEnnemis(), env.getMenace(), env.isEchec(), env.isMissionFinie());
    }
}

