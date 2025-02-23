import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import observable.Environnement;
import vues.*;

public class Main extends Application {

    private final Environnement env = new Environnement();

    @Override
    public void start(Stage primaryStage) throws Exception{

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("vues/VueGlobale.fxml"));

        // Chargement vues
        VuePlateau vuePlateau = new VuePlateau(env);
        VueActions vueActions = new VueActions(env);
        VueGlobale vueGlobale = new VueGlobale(env);

        loader.setControllerFactory(iC->{
            if (iC.equals(VueGlobale.class)) return vueGlobale;
            else if (iC.equals(VuePlateau.class)) return vuePlateau;
            else if (iC.equals(VueActions.class)) return vueActions;
            else return null;
        });

        Parent root = loader.load();
        primaryStage.setTitle("Hostage Rescue Squad");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

        vuePlateau.initPlateau(env.getLargeur(), env.getHauteur());
        env.notifyObservers();

    }


    public static void main(String[] args) {
        launch(args);
    }
}
