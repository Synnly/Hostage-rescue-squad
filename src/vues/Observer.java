package vues;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import observable.*;

public abstract class Observer implements EventHandler<ActionEvent> {
    protected final Observable sujet;

    public Observer(Observable sujet){
        this.sujet = sujet;
    }

    /**
     * Met à jour les élements graphiques de la vue
     */
    public abstract void update();
}
