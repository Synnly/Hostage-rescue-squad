package vues;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import observable.Observable;

/**
 * La vue regroupant toutes les autres vues de l'application.
 */
public class VueGlobale extends Observer{

    /*
    TODO :
        Couches :
            - Otages
            - Personnages
            - Murs
            - Surbrillance (coups alliés/ennemis)
            - Plateau
     */

    /**
     * Le conteneur des autres vues
     */
    @FXML
    public VBox vueGlobaleVbox;

    /**
     * Constructeur de la vue globale
     *
     * @param sujet Le sujet
     */
    public VueGlobale(Observable sujet) {
        super(sujet);
    }

    /**
     * Initialise les eléments graphiques
     */
    @FXML
    public void initialize(){}

    @Override
    public void update() {}

    @Override
    public void handle(ActionEvent actionEvent) {

    }
}
