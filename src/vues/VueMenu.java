package vues;

import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import observable.Environnement;

public class VueMenu {
    private final Environnement env;
    @FXML
    public MenuItem nouvellePartie;

    public VueMenu(Environnement env) {
        this.env = env;
    }

    /**
     * Initialise les controleurs du menu
     */
    @FXML
    public void initialize(){
        nouvellePartie.setOnAction(event-> nouvellePartie());
    }
    /**
     *  Réinitialise la partie
     */
    private void nouvellePartie(){
        this.env.finDePartie();
    }
}
