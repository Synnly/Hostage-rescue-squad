package vues;

import coups.Coup;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import mdp.Direction;
import observable.Environnement;
import observable.Observable;
import org.javatuples.Pair;
import vues.Observer;

import java.util.Map;

public class VueAvertissement extends Observer {

    @FXML
    private TextArea textAvertissement;

    /**
     * Constructeur d'un observateur type
     *
     * @param sujet Le sujet
     */
    public VueAvertissement(Environnement sujet) {
        super(sujet);
        sujet.ajouterObserver(this);
    }

    @Override
    public void update() {
        Environnement env = (Environnement) sujet;
        Map<Pair<Coup, Direction>, Double> danger = env.predireDanger();
        textAvertissement.setText("");

        StringBuilder sb = new StringBuilder("");
        for(Pair<Coup, Direction> coup : danger.keySet()){
            sb.append(coup.getValue0()).append(" vers ").append(coup.getValue1()).append(" : ").append(100 * danger.get(coup)).append(" %\n");
        }

        textAvertissement.setText(sb.toString());
    }

    @Override
    public void handle(ActionEvent actionEvent) {

    }
}
