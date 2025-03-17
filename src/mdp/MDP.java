package mdp;

import carte.Case;
import coups.Coup;
import coups.Deplacement;
import coups.FinTour;
import coups.Tir;
import observable.Environnement;
import personnages.Operateur;
import personnages.Personnage;

import java.util.HashMap;
import java.util.Map;

public interface MDP {

    double valeurEchec = -9e20;
    double valeurReussite = 10;
    // ATTENTION /!\
    // LA VALEUR DE LA REUSSITE DE MISSION DOIT ETRE REDUIT DE LA VALEUR
    // DE RECUPERATION DE L'OBJECTIF
    double valeurObjectif = 5;
    double valeurDeltaMenace = 0; // > 0 quand niveau de menace augmente, < 0 sinon
    double valeurTuerEnnemi = 2;
    double valeurDeplacement = -0.4;

    /**
     * Calcule la récompense R(s, a, s')
     * @param etatDepart L'état de départ, ie s
     * @param action L'action, ie a
     * @param etatArrivee L'état d'arrivée, ie s'
     * @return La récompense R(s, a, s')
     */
    static double recompense(Etat etatDepart, Action action, Etat etatArrivee){
        double recomp = 0;
        // deplacement
        for(Coup coup : action.coups()){
            if (coup instanceof Deplacement) {
                recomp +=  valeurDeplacement;
            }
            else if (coup instanceof Tir) {
                recomp += Math.min(1, etatDepart.ennemis().size() - etatArrivee.ennemis().size()) * valeurTuerEnnemi;
            }
            else if (coup instanceof FinTour) {
                recomp += valeurDeplacement;
            }
        }

        // echec
        if(etatArrivee.estEchec()){
            recomp += valeurEchec;
        }

        // changement de niveau de menace
        recomp += (etatDepart.menace() - etatArrivee.menace()) * valeurDeltaMenace;

        // recuperation de l'objectif
        if(etatArrivee.operateur().possedeObjectif() && !etatDepart.operateur().possedeObjectif()){
            recomp += valeurObjectif;
        }

        // reussite de la mission
        if(etatArrivee.estTerminal() && !etatArrivee.estEchec()){
            recomp += valeurReussite - valeurObjectif;
        }

        return recomp;
    }

    /**
     * Calcule tous les états accessibles en fonction de si l'action échoue ou non et leur probabilité correspondante
     * @param env L'environnement
     * @param etatDepart L'etat de départ
     * @param action L'action effectuée
     * @return Le dictionnaire ayant pour clé l'état et en valeur la probabilité d'y arriver
     */
    static Map<Etat, Double> transition(Environnement env, Etat etatDepart, Action action){
        Map<Etat, Double> etats = new HashMap<>();
        etats.put(etatDepart, 1.);

        // parcours des coups composant l'action
        for(int i = 0; i < action.cibles().size(); i++){
            Map<Etat, Double> etatsTemp = new HashMap<>(2 * etats.size());

            // action des coups sur les etats
            for(Etat e : etats.keySet()){
                if(!e.estTerminal()) {
                    Etat etatSucces = simuler(env.copy(), e, action.coups().get(i).copy(), action.personnage().copy(), action.cibles().get(i).copy(), true);
                    Etat etatEchec = simuler(env.copy(), e, action.coups().get(i).copy(), action.personnage().copy(), action.cibles().get(i).copy(), false);

                    etatsTemp.put(etatSucces, etats.get(e) * action.coups().get(i).probaSucces);
                    etatsTemp.put(etatEchec, etats.get(e) * (1 - action.coups().get(i).probaSucces));
                }
                else{
                    etatsTemp.put(e, etats.get(e));
                }
            }
            etats = etatsTemp;
        }
        return etats;
    }

    /**
     * Calcule l'état intermédiaire d'arrivée
     * @param env L'environnement
     * @param etatDepart L'état de départ
     * @param coup Le coup à effectuer
     * @param perso Le personnage effectuant le coup
     * @param cible La case ciblée par le coup
     * @param succes true si le coup réussi, false sinon
     * @return L'état d'arrivée
     */
    static Etat simuler(Environnement env, Etat etatDepart, Coup coup, Personnage perso, Case cible, boolean succes){
        env.setEtat(etatDepart);
        coup.probaSucces = succes ? 1 : 0;

        coup.effectuer(env, perso, cible);
        return new Etat(env);
    }
}
