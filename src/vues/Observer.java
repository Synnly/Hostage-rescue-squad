package vues;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import observable.*;

/**
 * Type abstrait représentant un observateur d'un sujet observable.
 */
public abstract class Observer implements EventHandler<ActionEvent> {
    /**
     * Le sujet observé
     */
    protected final Observable sujet;

    /**
     * Constructeur d'un observateur type
     *
     * @param sujet Le sujet
     */
    public Observer(Observable sujet){
        this.sujet = sujet;
    }

    /**
     * Met à jour les élements graphiques de la vue associée à cet observateur
     */
    public abstract void update();
}
