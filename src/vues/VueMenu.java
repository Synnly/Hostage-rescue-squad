package vues;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import observable.Environnement;

public class VueMenu extends Observer{
    private final Environnement env;
    @FXML
    public MenuItem nouvellePartie, menuItemConseils;

    public boolean afficherConseils = false;

    public VueMenu(Environnement env) {
        super(env);
        this.env = env;
        env.ajouterObserver(this);
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

    @Override
    public void update(){
        if(!env.conseilsSontAffiches()){
            menuItemConseils.setText("Afficher les conseils");
        }
        else{
            menuItemConseils.setText("Masquer les conseils");
        }
    }

    public void alternerAfficherConseils(){
        env.alternerAfficherConseils();
    }

    @Override
    public void handle(ActionEvent actionEvent) {

    }
}
