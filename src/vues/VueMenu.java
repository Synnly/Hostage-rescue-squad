package vues;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import observable.Environnement;

public class VueMenu extends Observer{
    @FXML
    public MenuItem nouvellePartie, menuItemConseils;

    public boolean afficherConseils = false;

    public VueMenu(Environnement env) {
        super(env);
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
        Environnement env = (Environnement) sujet;
        env.finDePartie();
    }

    @Override
    public void update(){
        Environnement env = (Environnement) sujet;
        if(!env.conseilsSontAffiches()){
            menuItemConseils.setText("Afficher les conseils");
        }
        else{
            menuItemConseils.setText("Masquer les conseils");
        }
    }

    public void alternerAfficherConseils(){
        Environnement env = (Environnement) sujet;
        env.alternerAfficherConseils();
    }

    @Override
    public void handle(ActionEvent actionEvent) {

    }
}
