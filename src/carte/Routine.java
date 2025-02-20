package carte;

import java.util.ArrayList;
import java.util.List;

public class Routine {
    private List<Case> cases;

    /**
     * Constructeur d'une routine, c'est-à-dire une liste de cases qui se suivent, dans le sens de parcours des dites cases
     * @param c La case initiale
     */
    public Routine(Case c){
        cases = new ArrayList<>();
        cases.add(c);
    }

    /**
     * Insère une case dans la liste de cases de la routine
     * @param pred La case précédant la nouvelle case
     * @param c La nouvelle case
     */
    public void ajouterCase(Case pred, Case c){
        cases.add(cases.indexOf(pred) + 1, c);
    }

    /**
     * Renvoie la case suivante dans la routine
     * @param c La case
     * @return La prochaine case
     */
    public Case prochaineCase(Case c){
        return cases.get((cases.indexOf(c) + 1) % cases.size());
    }


}
