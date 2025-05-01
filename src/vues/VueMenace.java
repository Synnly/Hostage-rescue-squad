package vues;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import observable.Environnement;

public class VueMenace extends Observer{

    @FXML
    public Label labelMinMenace, labelMaxMenace;

    @FXML
    public ProgressBar progressbarMenace;

    public VueMenace(Environnement env) {
        super(env);
        env.ajouterObserver(this);
    }

    @FXML
    public void initialize(){
        Environnement env = (Environnement) sujet;
        labelMinMenace.setText(env.getMinMenace()+"");
        labelMaxMenace.setText(env.getMaxMenace()+"");
        progressbarMenace.setProgress((double) (env.getMenace()-env.getMinMenace()) / env.getMaxMenace());
    }

    @Override
    public void update() {
        Environnement env = (Environnement) sujet;
        progressbarMenace.setProgress((double) (env.getMenace()-env.getMinMenace()) / env.getMaxMenace());
    }

    @Override
    public void handle(ActionEvent actionEvent) {

    }
}
