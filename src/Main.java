import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import observable.Environnement;
import vues.*;

public class Main extends Application {
    private final int hauteurFenetre = 790;
    private final int largeurFenetre = 450;


    private final Environnement env = new Environnement();

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("vues/VuePlateau.fxml"));

        // Chargement vues
        VuePlateau vuePlateau = new VuePlateau(env);

        loader.setControllerFactory(iC->{
            if (iC.equals(VuePlateau.class)) return vuePlateau;
            else return null;
        });

        Parent root = loader.load();
        primaryStage.setTitle("Hostage Rescue Squad");
        primaryStage.setScene(new Scene(root, largeurFenetre, hauteurFenetre));
        primaryStage.setMinHeight(hauteurFenetre);
        primaryStage.setMinWidth(largeurFenetre);
        primaryStage.setMaxHeight(hauteurFenetre);
        primaryStage.setMaxWidth(largeurFenetre);
        primaryStage.show();

        vuePlateau.gridPane.setMinWidth(largeurFenetre);
        vuePlateau.gridPane.setMinHeight(hauteurFenetre);


        vuePlateau.initPlateau(env.getLargeur(), env.getHauteur());
        //env.notifyObservers();

    }


    public static void main(String[] args) {
        launch(args);
    }
}
