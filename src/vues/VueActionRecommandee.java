package vues;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import observable.Environnement;

/**
 * Cette Vue permet d'afficher et exécuter les conseilles de l'IA
 */
public class VueActionRecommandee extends Observer{
    @FXML
    public Button executerAction;
    @FXML
    public TextField conseilIA;

    public VueActionRecommandee(Environnement sujet) {
        super(sujet);
        sujet.ajouterObserver(this);
    }

    @Override
    public void update() {
        Environnement environnement = (Environnement) sujet;
        if(environnement.conseilsSontAffiches()){
            conseilIA.setText(environnement.coupPreditToString());
            conseilIA.setDisable(false);
            executerAction.setDisable(false);
        }
        else {
            conseilIA.setText("");
            conseilIA.setDisable(true);
            executerAction.setDisable(true);
        }
    }

    @Override
    public void handle(ActionEvent actionEvent) {
        Environnement environnement = (Environnement) sujet;
        environnement.executerActionRecommandee();
    }
}
