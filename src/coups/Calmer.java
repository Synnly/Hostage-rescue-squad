package coups;

import carte.cases.AucuneCase;
import carte.cases.Case;
import observable.Environnement;
import personnages.Operateur;
import personnages.Personnage;

import java.util.List;

public class Calmer extends Coup{
    public Calmer(Coup c) {
        super(c);
    }

    public Calmer(int cout, double probaSucces) {
        super(cout, probaSucces);
    }

    @Override
    public void effectuer(Environnement env, Personnage perso, Case arr) {
        if(perso.estOperateur()){
            effectuer(env, (Operateur) perso, arr);
        }
    }

    @Override
    public void effectuer(Environnement env, Operateur perso, Case arr) {
        List<Double> nombres = env.getNombresAleatoires(1);
        if(nombres.get(0) > probaSucces){
            //System.out.println("L'action a échoué");
        }else{
            env.diminuerMenace();
        }
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
        return new Calmer(this);
    }
    @Override
    public String toString() {
        return "Se Calmer";
    }

}
