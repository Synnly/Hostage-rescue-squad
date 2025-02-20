package vues;

import observable.*;

public abstract class Observer {
    private Observable sujet;

    /**
     * Définit le sujet observé par cet observateur
     * @param sujet Le sujet
     */
    public void setSujet(Observable sujet){
        this.sujet = sujet;
    }

    /**
     * Met à jour les élements graphiques de la vue
     */
    public abstract void update();
}
