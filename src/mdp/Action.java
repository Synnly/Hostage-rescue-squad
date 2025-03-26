package mdp;

import coups.Coup;
import personnages.Personnage;

import java.util.List;


public record Action(Personnage personnage, List<Coup> coups, List<Integer> directions) {
    public static final int HAUT = 0;
    public static final int BAS = 1;
    public static final int GAUCHE = 2;
    public static final int DROITE = 3;
    public static final int AUCUN = -1;
}
