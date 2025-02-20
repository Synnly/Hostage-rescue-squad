package vues;

import carte.Case;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import observable.Environnement;
import personnages.Operateur;

import java.util.List;

public class VuePlateau extends Observer implements EventHandler<ActionEvent> {
    @FXML
    public GridPane gridPane;

    private final Environnement env;
    private Button[][] boutons;

    /**
     * Constructeur de la vue du plateau
     * @param env L'environnement
     */
    public VuePlateau(Environnement env){
        env.ajouterObserver(this);
        this.env = env;
    }

    @FXML
    public void initialize(){}

    /**
     * Initialise le plateau en créant autant de boutons que de cases dans l'environnement
     * @param largeur Le nombre de cases en largeur
     * @param hauteur Le nombre de cases en hauteur
     */
    public void initPlateau(int largeur, int hauteur){
        boutons = new Button[largeur][hauteur];
        for(int x = 0; x < largeur; x++){
            for(int y = 0; y < hauteur; y++) {
                Button button = new Button();
                button.setId("case" + x + "-" + y);
                button.setMinWidth(gridPane.getWidth()/largeur);
                button.setMinHeight((gridPane.getHeight() - 40)/hauteur);
                button.setStyle("-fx-background-color: #ffffff; -fx-border-color: gray");
                button.setOnAction(this);
                gridPane.add(button, x, y);
                boutons[x][y] = button;
            }
        }
    }

    @Override
    public void handle(ActionEvent ae) {
        // Recuperation de la source
        Button button = (Button) ae.getSource();
        String[] coords = button.getId().split("case");
        coords = coords[1].split("-");

        // Recuperation de la case
        int x = Integer.parseInt(coords[0]);
        int y = Integer.parseInt(coords[1]);

        env.choisirCase(x, y);
    }

    @Override
    public void update() {
        Operateur perso = env.getOperateurActif();
        List<Case> casesValides = perso.getActionActive().getCasesValides(env, perso);

        for (int x = 0; x < env.getLargeur(); x++) {
            for (int y = 0; y < env.getHauteur(); y++) {
                boutons[x][y].setStyle("-fx-background-color: white; -fx-border-color: gray");

                if(casesValides.contains(env.getCase(x, y))){   // Cases valides
                    boutons[x][y].setStyle("-fx-background-color: #ffff0090; -fx-border-color: gray");
                }

                if(env.getEnnemi().getX() == x && env.getEnnemi().getY() == y){ // Terroriste
                    boutons[x][y].setStyle("-fx-background-color: red; -fx-border-color: gray");
                }

                if(perso.getX() == x && perso.getY() == y){     // Operateur
                    boutons[x][y].setStyle("-fx-background-color: cyan; -fx-border-color: gray");
                }
            }
        }
    }
}
