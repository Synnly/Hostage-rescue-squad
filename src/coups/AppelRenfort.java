package coups;

import carte.cases.AucuneCase;
import carte.cases.Case;
import observable.Environnement;
import personnages.Operateur;
import personnages.Personnage;

import java.util.List;

public class AppelRenfort extends Coup {

    public AppelRenfort(int ignoredCout, double ignoredProbaSucces) {
        super(ignoredCout, ignoredProbaSucces);
    }

    public AppelRenfort(Coup c) {
        super(c);
    }

    @Override
    public void effectuer(Environnement env, Personnage perso, Case arr) {
        if(perso.estTerroriste()){
            if(env.getEnnemisEnVie() == env.getEnnemis().size()){
                env.augmenterMenace();
            }else{
                env.reapparitionEnnemi();
            }
        }
    }

    @Override
    public void effectuer(Environnement env, Operateur perso, Case arr) {
        // Impossible
    }

    @Override
    public List<Case> getCasesValides(Environnement env, Personnage perso) {
        return List.of(AucuneCase.instance);
    }

    @Override
    public List<Case> getCasesValides(Environnement env, Operateur perso) {
        return List.of(AucuneCase.instance);
    }

    @Override
    public List<Case> getCasesValides(Environnement env, Case caseDepart) {
        return List.of(AucuneCase.instance);
    }

    @Override
    public Coup copy() {
        return new AppelRenfort(this);
    }

    @Override
    public boolean estRenfort(){return true;}
}
