package vues;

import carte.cases.Case;
import carte.separation.Separation;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import observable.Environnement;
import personnages.Operateur;
import personnages.Terroriste;

import java.util.List;

/**
 * La vue du plateau.&nbsp;Cette vue contient les cases du plateau, les opérateurs, terroristes et objectifs.
 */
public class VuePlateau extends Observer {
    /**
     * Le conteneur du plateau
     */
    @FXML
    public StackPane stackPane;

    public GridPane gridPaneBoutons, gridPaneOtages, gridPanePersonnages, gridPaneSurbrillance, gridPaneMurs, gridPaneSprites;
    /**
     * La liste des boutons représentant chacun une case du plateau
     */
    private Button[][] boutons;
    private Pane[][] sprites, surbrillance, personnages, murs, otages;
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
        stackPane.setMinWidth(env.getLargeur() * tailleCase + 8);
        stackPane.setMinHeight(env.getHauteur() * tailleCase + 8);
        stackPane.setPrefWidth(env.getLargeur() * tailleCase + 8);
        stackPane.setPrefHeight(env.getHauteur() * tailleCase + 8);
        stackPane.setMaxWidth(env.getLargeur() * tailleCase + 8);
        stackPane.setMaxHeight(env.getHauteur() * tailleCase + 8);
        stackPane.setStyle("-fx-background-color: #4f6228");
    }

    /**
     * Initialise le plateau en créant autant de boutons que de cases dans l'environnement
     *
     * @param largeur Le nombre de cases en largeur
     * @param hauteur Le nombre de cases en hauteur
     */
    public void initPlateau(int largeur, int hauteur){
        // Boutons
        boutons = new Button[largeur][hauteur];
        for(int x = 0; x < largeur; x++){
            for(int y = 0; y < hauteur; y++) {
                Button button = new Button();
                button.setId("case" + x + "-" + y);
                button.setMinWidth(gridPaneBoutons.getWidth()/largeur);
                button.setMinHeight((gridPaneBoutons.getHeight())/hauteur);
                button.setText(y * env.getLargeur() + x + "");
                button.setStyle("-fx-opacity: 0;");
                button.setOnAction(this);
                gridPaneBoutons.add(button, x, y);
                boutons[x][y] = button;
            }
        }

        // Sprites, surbrillance, personnages, murs
        sprites = new Pane[largeur][hauteur];
        surbrillance = new Pane[largeur][hauteur];
        personnages = new Pane[largeur][hauteur];
        otages = new Pane[largeur][hauteur];
        murs = new Pane[largeur][hauteur];
        for(int x = 0; x < largeur; x++) {
            for (int y = 0; y < hauteur; y++) {
                // Sprites
                Pane p = new Pane();
                p.setMinWidth(gridPaneBoutons.getWidth() / largeur);
                p.setMinHeight((gridPaneBoutons.getHeight()) / hauteur);

                if (!env.getCase(x, y).peutVoir){    // Couverture
                    p.setStyle("-fx-background-color: #9b9b9b; -fx-background-insets: 1px; -fx-background-radius: 4px");
                } else{                                     // Case normale
                    p.setStyle("-fx-background-color: #d7e4bd; -fx-background-insets: 1px; -fx-background-radius: 4px");
                }

                gridPaneSprites.add(p, x, y);
                sprites[x][y] = p;

                // Surbrillance
                p = new Pane();
                p.setMinWidth(gridPaneBoutons.getWidth() / largeur);
                p.setMinHeight((gridPaneBoutons.getHeight()) / hauteur);
                p.setStyle("-fx-opacity: 0");
                gridPaneSurbrillance.add(p, x, y);
                surbrillance[x][y] = p;

                // Personnages
                p = new Pane();
                p.setMinWidth(gridPaneBoutons.getWidth() / largeur);
                p.setMinHeight((gridPaneBoutons.getHeight()) / hauteur);
                p.setStyle("-fx-opacity: 0");
                gridPanePersonnages.add(p, x, y);
                personnages[x][y] = p;

                // Otages
                p = new Pane();
                p.setMinWidth(gridPaneBoutons.getWidth() / largeur);
                p.setMinHeight((gridPaneBoutons.getHeight()) / hauteur);
                p.setStyle("-fx-opacity: 0");
                gridPaneOtages.add(p, x, y);
                otages[x][y] = p;

                // Murs
                p = new Pane();
                p.setMinWidth(gridPaneBoutons.getWidth() / largeur);
                p.setMinHeight((gridPaneBoutons.getHeight()) / hauteur);

                String leftBdColor = "#ffffff00"; String rightBdColor = "#ffffff00"; String botBdColor = "#ffffff00"; String topBdColor = "#ffffff00";
                String leftBdWidth = "1px"; String rightBdWidth = "1px"; String botBdWidth = "1px"; String topBdWidth = "1px";
                // Separations
                for(Separation sep : env.getSeparations()){
                    if(sep.getCase1() == env.getCase(x, y)){
                        if(sep.getCase1().x == sep.getCase2().x){   // Separation horizontale
                            if(sep.getCase1().y > sep.getCase2().y) {
                                topBdColor = "#963C00";
                                topBdWidth = "5px";
                            }
                            else {
                                botBdColor = "#963C00";
                                botBdWidth = "5px";
                            }
                        }
                        else{   // Separation verticale
                            if(sep.getCase1().x > sep.getCase2().x) {
                                leftBdColor = "#963C00";
                                leftBdWidth = "5px";
                            }
                            else {
                                rightBdColor = "#963C00";
                                rightBdWidth = "5px";
                            }
                        }
                    } else if (sep.getCase2() == env.getCase(x, y)) {
                        if(sep.getCase1().x == sep.getCase2().x){   // Separation horizontale
                            if(sep.getCase1().y < sep.getCase2().y) {
                                topBdColor = "#963C00";
                                topBdWidth = "5px";
                            }
                            else {
                                botBdColor = "#963C00";
                                botBdWidth = "5px";
                            }
                        }
                        else{   // Separation verticale
                            if(sep.getCase1().x < sep.getCase2().x) {
                                leftBdColor = "#963C00";
                                leftBdWidth = "5px";
                            }
                            else {
                                rightBdColor = "#963C00";
                                rightBdWidth = "5px";
                            }
                        }
                    }
                }

                String style = String.format("-fx-background-color: %s; -fx-border-color: %s %s %s %s; -fx-border-width: %s %s %s %s; -fx-text-fill: %s", "#ffffff00", topBdColor, rightBdColor, botBdColor, leftBdColor, topBdWidth, rightBdWidth, botBdWidth, leftBdWidth, "black");
                p.setStyle(style);
                gridPaneMurs.add(p, x, y);
                murs[x][y] = p;
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
                surbrillance[x][y].setStyle("-fx-opacity: 0");
                personnages[x][y].setStyle("-fx-opacity: 0");
                otages[x][y].setStyle("-fx-opacity: 0");

                for(Terroriste ennemi : env.getEnnemis()){      // Terroristes
                    if (ennemi.getX() == x && ennemi.getY() == y) {
                        personnages[x][y].setStyle("-fx-opacity: 1; -fx-background-position: center; -fx-background-size: 75%; -fx-background-repeat: no-repeat; -fx-background-image: url('tero.png')");
                        break;
                    }
                }

                if(perso.getX() == x && perso.getY() == y){     // Operateur
                    personnages[x][y].setStyle("-fx-opacity: 1; -fx-background-position: center; -fx-background-size: 75%; -fx-background-repeat: no-repeat; -fx-background-image: url('op.png')");
                    if(perso.possedeObjectif()) {
                        otages[x][y].setStyle("-fx-opacity: 1; -fx-background-position: 40px 40px; -fx-background-size: 30px; -fx-background-repeat: no-repeat; -fx-background-image: url('otage.png')");
                    }
                }

                // Otages
                if(env.getCase(x, y).estObjectif() && !perso.possedeObjectif()){
                    otages[x][y].setStyle("-fx-opacity: 1; -fx-background-position: center; -fx-background-size: 75%; -fx-background-repeat: no-repeat; -fx-background-image: url('otage.png')");
                }

                // Cases valides
                if(casesValides.contains(env.getCase(x, y))){
                    surbrillance[x][y].setStyle("-fx-opacity: 0.5; -fx-background-color: rgba(255,255,0,0.5); -fx-border-color: #ffff00ff");
                }

            }
        }
    }
}
