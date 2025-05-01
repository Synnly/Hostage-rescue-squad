package vues;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;
import observable.Environnement;
import observable.Observable;

public class VuePA extends Observer{

    @FXML
    public Label labelMinPA, labelMaxPA;

    @FXML
    public ProgressBar progressbarPA;

    public VuePA(Environnement env) {
        super(env);
        env.ajouterObserver(this);
    }

    @FXML
    public void initialize(){
        Environnement env = (Environnement) sujet;
        labelMinPA.setText("0");
        labelMaxPA.setText(env.getOperateurActif().getMaxPointsAction() + "");
        progressbarPA.setProgress(1);
    }

    @Override
    public void update() {
        Environnement env = (Environnement) sujet;
        progressbarPA.setProgress((double) env.getOperateurActif().getPointsAction() / env.getOperateurActif().getMaxPointsAction());
    }

    @Override
    public void handle(ActionEvent actionEvent) {

    }
}
