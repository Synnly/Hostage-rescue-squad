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
        if(perso.getPointsAction() >= cout && env.getMenace() > env.getMinMenace()){
            List<Double> nombres = env.getNombresAleatoires(1);
            if(nombres.get(0) > probaSucces){
                //System.out.println("L'action a échoué");
            }else{
                env.diminuerMenace();
            }
            perso.removePointsAction(cout);
        }
    }

    @Override
    public List<Case> getCasesValides(Environnement env, Personnage perso) {
        if(perso.estOperateur()){
            return getCasesValides(env, (Operateur) perso);
        }
        return List.of();
    }

    @Override
    public List<Case> getCasesValides(Environnement env, Operateur perso) {
        if((perso.getPointsAction() >= cout) && (env.getMenace() > env.getMinMenace())){
            return List.of(AucuneCase.instance);
        }
        return List.of();


    }

    @Override
    public List<Case> getCasesValides(Environnement env, Case caseDepart) {
        return List.of();
    }

    @Override
    public Coup copy() {
        return new Calmer(this);
    }
    @Override
    public String toString() {
        return "Se Calmer";
    }
    public boolean estCalmer(){
        return true;
    }

}
