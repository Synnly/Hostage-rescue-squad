package observable;

import vues.Observer;

import java.util.ArrayList;

public abstract class Observable {

    private final ArrayList<Observer> observers = new ArrayList<>();

    /**
     * Ajoute un observateur notifiable des changements au modèle
     * @param obs L'observateur
     */
    public void ajouterObserver(Observer obs){
        observers.add(obs);
    }

    /**
     * Notifie les observateurs des changements au modèle
     */
    public void notifyObservers() {
        for (Observer obs: observers) {
            obs.update();
        }
    };
}
