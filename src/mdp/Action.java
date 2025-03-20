package mdp;

import carte.Case;
import coups.Coup;
import personnages.Personnage;

import java.util.List;

public record Action(Personnage personnage, List<Coup> coups, List<Case> cibles) {
}
