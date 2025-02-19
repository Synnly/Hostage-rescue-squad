package vues;

import jdk.jshell.spi.ExecutionControl;
import observable.*;

public abstract class Observer {
    private Observable sujet;

    public void setSujet(Observable sujet){
        this.sujet = sujet;
    }

    public void update() throws ExecutionControl.NotImplementedException {
        throw new ExecutionControl.NotImplementedException("");
    };
}
