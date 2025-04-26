package vues;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import observable.Environnement;
import personnages.Operateur;

/**
 * La vue des actions.&nbsp;Cette vue contient les boutons de changement d'action active.
 */
public class VueActions extends Observer{
    /**
     * Le bouton de changement d'action active au déplacement
     */
    @FXML
    public Button boutonDepl,
    /**
     * Le bouton de changement d'action active au tir
     */
    boutonTir,
    /**
     * Le bouton de changement d'action active au fin de tour
     */
    boutonFinTour,
    /**
     * Le bouton de changement d'action active à l'élimination silencieuse
     */
    boutonElimSil,

    /**
     * Le bouton de changement d'action active à se calmer
     */
    boutonCalm;
    ;

    @FXML
    public Label probaTir, probaDepl, probaElimSil, probaCalm, coutTir, coutDepl, coutElimSil, coutCalm;

    /**
     * Constructeur de la vue des actions
     *
     * @param sujet the sujet
     */
    public VueActions(Environnement sujet) {
        super(sujet);
    }

    /**
     * Initialise les eléments graphiques
     */
    @FXML
    public void initialize(){
        Environnement env = (Environnement) sujet;
        probaTir.setText((int) (env.getOperateurActif().getTir().probaSucces * 100) + " %");
        probaDepl.setText((int) (env.getOperateurActif().getDeplacement().probaSucces * 100) + " %");
        probaElimSil.setText((int) (env.getOperateurActif().getEliminationSilencieuse().probaSucces * 100) + " %");
        probaCalm.setText((int) (env.getOperateurActif().getCalmer().probaSucces * 100) + " %");

        coutTir.setText(env.getOperateurActif().getTir().cout + " PA");
        coutDepl.setText(env.getOperateurActif().getDeplacement().cout + " PA");
        coutElimSil.setText(env.getOperateurActif().getEliminationSilencieuse().cout + " PA");
        coutCalm.setText(env.getOperateurActif().getCalmer().cout + " PA");

    }

    @Override
    public void update() {

    }

    @Override
    public void handle(ActionEvent ae) {
        Environnement env = (Environnement) sujet;
        Button button = (Button) ae.getSource();
        Operateur opActif = env.getOperateurActif();

        if (button.equals(boutonDepl)){         // Déplacement
            env.setDeplacementActionActive();
        }
        else if (button.equals(boutonFinTour)){ // Fin de tour
            env.setFinTourActionActive();

            if(env.isMissionFinie()){
                if(env.isEchec()){
                    System.out.println("Vous êtes mort");
                }
                else {
                    System.out.println("Mission réussie");
                }
                env.finDePartie();
            }
        }
        else if (button.equals(boutonTir)){ // Tir
            env.setTirActionActive();

        }
        else if (button.equals(boutonElimSil)){
            env.setElimSilActionActive();
        }
        else if (button.equals(boutonCalm)){
            env.setCalmerActionActive();
        }
    }
}
