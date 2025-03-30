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
     * @param c La case initiale.&nbsp;Ne peut être <code>null</code>.
     */
    public Routine(Case c){
        assert c != null : "La case ne peut être null";
        cases = new ArrayList<>();
        cases.add(c);
    }

    public Routine(Routine r){
        this.cases = new ArrayList<>();
        for(Case c: r.cases){
            cases.add(c.copy());
        }
    }

    /**
     * Insère une case dans la liste de cases de cette routine
     *
     * @param pred La case précédant la nouvelle case.&nbsp;Ne peut être <code>null</code>.&nbsp;Doit être déjà présent dans la
     *             routine.
     * @param c    La nouvelle case.&nbsp;Ne peut être <code>null</code>.&nbsp;Ne peut déjà exister dans la routine.
     */
    public void ajouterCase(Case pred, Case c){
        assert pred != null : "La case précédente ne peut être null";
        assert c != null : "La case à ajouter ne peut être null";
        assert !cases.contains(c) : "La case c existe déjà dans la routine";
        assert cases.contains(pred) : "La case pred n'existe pas dans la routine";
        cases.add(cases.indexOf(pred) + 1, c);
    }

    /**
     * Renvoie la case suivante dans cette routine
     *
     * @param c La case. Ne peut être <code>null</code>.&nbsp;Doit déjà exister dans la routine
     * @return La prochaine case
     */
    public Case prochaineCase(Case c){
        if(c instanceof AucuneCase){
            return c;
        }

        assert c != null : "La case précédente ne peut être null";
        for(int i = 0; i< cases.size(); i++){
            if(cases.get(i).getId() == c.getId()){
                return cases.get((i + 1) % cases.size());
            }
        }

        assert false: "La case c n'existe pas dans la routine";
        return null; // Inutile vu que si c pas dans la liste, assert se déclenche
    }

    public int taille(){
        return cases.size();
    }

    public int nextIndex(int i){
        if(i == -1){
            return i;
        }

        return (i+1) % cases.size();
    }

    public Case getCase(int i){
        return i == -1 ? AucuneCase.instance : cases.get(i);
    }

    public int indexOf(Case c){
        if(c instanceof AucuneCase){
            return -1;
        }
        for(int i = 0; i < cases.size(); i++){
            if (c.id == cases.get(i).id){
                return i;
            }
        }
        return -1;
    }
}
