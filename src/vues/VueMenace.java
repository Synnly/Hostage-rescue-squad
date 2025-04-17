package vues;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import observable.Environnement;

public class VueMenace extends Observer{

    private Environnement env;

    @FXML
    public Label labelMinMenace, labelMaxMenace;

    @FXML
    public ProgressBar progressbarMenace;

    public VueMenace(Environnement env) {
        super(env);
        env.ajouterObserver(this);
        this.env = env;
    }

    @FXML
    public void initialize(){
        labelMinMenace.setText(env.getMinMenace()+"");
        labelMaxMenace.setText(env.getMaxMenace()+"");
        progressbarMenace.setProgress((double) (env.getMenace()-env.getMinMenace()) / (env.getMaxMenace()-env.getMinMenace()));
    }

    @Override
    public void update() {
        progressbarMenace.setProgress((double) (env.getMenace()-env.getMinMenace()) / (env.getMaxMenace()-env.getMinMenace()));
    }

    @Override
    public void handle(ActionEvent actionEvent) {

    }
}
