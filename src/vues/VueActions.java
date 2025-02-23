package vues;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import observable.Environnement;
import personnages.Operateur;

public class VueActions extends Observer{
    private final Environnement env;
    @FXML
    public Button boutonDepl, boutonTir, boutonFinTour;

    public VueActions(Environnement sujet) {
        super(sujet);
        this.env = sujet;
    }

    @FXML
    public void initialize(){}

    @Override
    public void update() {

    }

    @Override
    public void handle(ActionEvent ae) {
        Button button = (Button) ae.getSource();
        Operateur opActif = env.getOperateurActif();

        if (button.equals(boutonDepl)){         // Déplacement
            env.setDeplacementActionActive();
        }
        else if (button.equals(boutonFinTour)){ // Fin de tour
            opActif.setActionActive(opActif.getFinTour());
            env.tourEnnemi();
        }
        else if (button.equals(boutonTir)){ // Tir
            env.setTirActionActive();
        }
    }
}
