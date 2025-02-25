package carte;

import java.util.ArrayList;
import java.util.List;

/**
 * Collection de cases représentant le chemin parcouru par un/des terroristes(s).&nbsp;Les terroristes parcourent cette
 * routine dans l'ordre de parcours des cases.&nbsp;Dans son implémentation actuelle, les croisements dans les routines
 * ne sont pas possibles.
 */
public class Routine {
    /**
     * La liste des cases de cette routine
     */
    private List<Case> cases;

    /**
     * Constructeur d'une routine
     *
     * @param c La case initiale
     */
    public Routine(Case c){
        cases = new ArrayList<>();
        cases.add(c);
    }

    /**
     * Insère une case dans la liste de cases de cette routine
     *
     * @param pred La case précédant la nouvelle case
     * @param c    La nouvelle case
     */
    public void ajouterCase(Case pred, Case c){
        cases.add(cases.indexOf(pred) + 1, c);
    }

    /**
     * Renvoie la case suivante dans cette routine
     *
     * @param c La case
     * @return La prochaine case
     */
    public Case prochaineCase(Case c){
        return cases.get((cases.indexOf(c) + 1) % cases.size());
    }


}
