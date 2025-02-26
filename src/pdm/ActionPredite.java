package pdm;

import actions.Action;
import carte.Case;
import personnages.Operateur;

public record ActionPredite(Action action, Case cible, Operateur op) {

    @Override
    public String toString() {
        if (cible == null) {
            return action.toString() + "      ";
        }
        return action.toString() + " (" + cible.x + "," + cible.y + ")";
    }
}
