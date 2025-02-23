package vues;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import observable.Observable;

public class VueGlobale extends Observer{

    @FXML
    public VBox vueGlobaleVbox;

    public VueGlobale(Observable sujet) {
        super(sujet);
    }

    @FXML
    public void initialize(){}

    @Override
    public void update() {

    }

    @Override
    public void handle(ActionEvent actionEvent) {

    }
}
