package vues;

import carte.Case;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.GridPane;
import observable.Environnement;
import personnages.Operateur;
import personnages.Terroriste;

import java.util.ArrayList;
import java.util.List;

/**
 * La vue du plateau.&nbsp;Cette vue contient les cases du plateau, les opérateurs, terroristes et objectifs.
 */
public class VuePlateau extends Observer {
    /**
     * Le conteneur du plateau
     */
    @FXML
    public GridPane gridPane;
    /**
     * La liste des boutons représentant chacun une case du plateau
     */
    private Button[][] boutons;
    /**
     * L'environnement observé
     */
    private final Environnement env;

    /**
     * La taille en largeur et hauteur de chaque case
     */
    private final int tailleCase = 75;

    /**
     * Constructeur de la vue du plateau
     *
     * @param env L'environnement
     */
    public VuePlateau(Environnement env){
        super(env);
        env.ajouterObserver(this);
        this.env = env;
    }

    /**
     * Initialise les eléments graphiques
     */
    @FXML
    public void initialize(){
        gridPane.setMinWidth(env.getLargeur() * tailleCase);
        gridPane.setMinHeight(env.getHauteur() * tailleCase);
        gridPane.setPrefWidth(env.getLargeur() * tailleCase);
        gridPane.setPrefHeight(env.getHauteur() * tailleCase);
    }

    /**
     * Initialise le plateau en créant autant de boutons que de cases dans l'environnement
     *
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
                button.setText(y * env.getLargeur() + x + "");
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

        if(env.isMissionFinie()){
            if(env.isEchec()){
                System.out.println("Vous êtes mort");
            }
            else {
                System.out.println("Mission réussie");
            }
            env.finDePartie();
        }
    }

    @Override
    public void update() {
        Operateur perso = env.getOperateurActif();
        List<Case> casesValides = perso.getActionActive().getCasesValides(env, perso);

        for (int x = 0; x < env.getLargeur(); x++) {
            for (int y = 0; y < env.getHauteur(); y++) {
                String bgColor = "white";
                String bdColor = "gray";
                String textColor = "black";

                if(!env.getCase(x, y).peutVoir){    // Couverture
                    bgColor = "lightgray";
                }

                for(Terroriste ennemi : env.getEnnemis()){      // Terroristes
                    if (ennemi.getX() == x && ennemi.getY() == y) {
                        bgColor = "red";
                        break;
                    }
                }

                if(env.getCase(x, y).estObjectif){      // Objectif
                    bgColor = "green";
                }

                if(perso.getX() == x && perso.getY() == y){     // Operateur
                    if(perso.possedeObjectif()){
                        bgColor = "cyan";
                    }
                    else{
                        bgColor = "blue";
                        textColor = "white";
                    }
                }

                // TOUJOURS LAISSER CETTE LIGNE EN DERNIER
                if(casesValides.contains(env.getCase(x, y))){   // Cases valides
                    bdColor = "#ffff00ff";
                }

                boutons[x][y].setStyle("-fx-background-color:" + bgColor + "; -fx-border-color:" + bdColor + "; -fx-text-fill: " + textColor);
                boutons[x][y].setText(y * env.getLargeur() + x + "");
            }
        }
    }
}
