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
    private TextArea textAvertissementOpti, textAvertissementRandom;

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
        // Random
        Map<Pair<Coup, Direction>, Double> dangerRandom = env.predireDanger(true);
        textAvertissementRandom.setText("");

        StringBuilder sb = new StringBuilder("");
        for(Pair<Coup, Direction> coup : dangerRandom.keySet()){
            sb.append(coup.getValue0()).append(" vers ").append(coup.getValue1()).append(" : ").append(Math.round(100 * dangerRandom.get(coup))).append(" %\n");
        }

        textAvertissementRandom.setText(sb.toString());

        // Optimal
        Map<Pair<Coup, Direction>, Double> dangerOpti = env.predireDanger(false);
        textAvertissementOpti.setText("");

        sb = new StringBuilder("");
        for(Pair<Coup, Direction> coup : dangerOpti.keySet()){
            sb.append(coup.getValue0()).append(" vers ").append(coup.getValue1()).append(" : ").append(Math.round(100 * dangerOpti.get(coup))).append(" %\n");
        }

        textAvertissementOpti.setText(sb.toString());
    }

    @Override
    public void handle(ActionEvent actionEvent) {

    }
}
