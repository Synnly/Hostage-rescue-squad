import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import observable.Environnement;
import vues.*;

/**
 * The type Main.
 */
public class Main extends Application {

    private final Environnement env = new Environnement(7, 10, 0.95, 0.85);

    @Override
    public void start(Stage primaryStage) throws Exception{

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("vues/VueGlobale.fxml"));

        // Chargement vues
        VuePlateau vuePlateau = new VuePlateau(env);
        VueActions vueActions = new VueActions(env);
        VueMenu vueMenu = new VueMenu(env);
        VueGlobale vueGlobale = new VueGlobale(env);


        loader.setControllerFactory(iC->{
            if (iC.equals(VueGlobale.class)) return vueGlobale;
            else if (iC.equals(VuePlateau.class)) return vuePlateau;
            else if (iC.equals(VueActions.class)) return vueActions;
            else if (iC.equals(VueMenu.class)) return vueMenu;
            else return null;
        });

        Parent root = loader.load();
        primaryStage.setTitle("Hostage Rescue Squad");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

        vuePlateau.initPlateau(env.getLargeur(), env.getHauteur());
        env.notifyObservers();

    }


    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
