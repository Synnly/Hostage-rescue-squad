package observable;

import jdk.jshell.spi.ExecutionControl;
import vues.Observer;

import java.util.ArrayList;

public abstract class Observable {

    private ArrayList<Observer> observers = new ArrayList<>();

    public void addObserver(Observer obs){
        observers.add(obs);
    }

    public void notifyObservers() throws ExecutionControl.NotImplementedException {
        for (Observer obs: observers) {
            obs.update();
        }
    };
}
