package observable;

import vues.Observer;

import java.util.ArrayList;

/**
 * Type abstrait d'un sujet observable.
 */
public abstract class Observable {

    /**
     * Liste des observateurs notifiés des changements de ce sujet
     */
    private final ArrayList<Observer> observers = new ArrayList<>();

    /**
     * Ajoute un observateur notifiable des changements de ce sujet
     *
     * @param obs L'observateur
     */
    public void ajouterObserver(Observer obs){
        observers.add(obs);
    }

    /**
     * Notifie les observateurs des changements de ce sujet
     */
    public void notifyObservers() {
        for (Observer obs: observers) {
            obs.update();
        }
    };
}
