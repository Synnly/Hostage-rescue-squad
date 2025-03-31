package vues;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;
import observable.Environnement;
import observable.Observable;

public class VuePA extends Observer{

    private Environnement env;

    @FXML
    public Label labelMinPA, labelMaxPA;

    @FXML
    public ProgressBar progressbarPA;

    public VuePA(Environnement env) {
        super(env);
        env.ajouterObserver(this);
        this.env = env;
    }

    @FXML
    public void initialize(){
        labelMinPA.setText("0");
        labelMaxPA.setText(env.getOperateurActif().getMaxPointsAction() + "");
        progressbarPA.setProgress(1);
    }

    @Override
    public void update() {
        progressbarPA.setProgress((double) env.getOperateurActif().getPointsAction() / env.getOperateurActif().getMaxPointsAction());
    }

    @Override
    public void handle(ActionEvent actionEvent) {

    }
}
