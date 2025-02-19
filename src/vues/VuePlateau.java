package vues;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import observable.Observable;

public class VuePlateau extends Observer implements EventHandler<ActionEvent> {
    @FXML
    public GridPane gridPane;

    private final Observable sujet;

    public VuePlateau(Observable observable){
        observable.addObserver(this);
        this.sujet = observable;
    }

    @FXML
    public void initialize(){}

    public void initPlateau(int largeur, int hauteur){
        for(int x = 0; x < largeur; x++){
            for(int y = 0; y < hauteur; y++) {
                Button button = new Button();
                button.setId("case" + x + "-" + y);
                button.setMinWidth(gridPane.getWidth()/largeur);
                button.setMinHeight((gridPane.getHeight() - 40)/hauteur);
                button.setStyle("-fx-background-color: #ffffff; -fx-border-color: gray");
                button.setOnAction(this);
                gridPane.add(button, x, y);
            }
        }
    }

    @Override
    public void handle(ActionEvent ae) {
        System.out.println(ae);
    }
}
