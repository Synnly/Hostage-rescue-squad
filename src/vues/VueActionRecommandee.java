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
    private final Environnement environnement;
    @FXML
    public Button executerAction;
    @FXML
    public TextField conseilIA;

    public VueActionRecommandee(Environnement sujet) {
        super(sujet);
        this.environnement = sujet;
        environnement.ajouterObserver(this);
    }

    @Override
    public void update() {
        conseilIA.setText(environnement.coupPreditToString());
    }

    @Override
    public void handle(ActionEvent actionEvent) {
        environnement.executerActionRecommandee();
    }
}
