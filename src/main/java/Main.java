import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;


public class Main extends Application {

    private final static String iconURL = "/yls.png";
    private final static String fxmlURL = "/sample.fxml";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource(
                fxmlURL));
        Parent root = (Parent) loader.load();
        primaryStage.setTitle("Youtube Live Streamer");
        primaryStage.setScene(new Scene(root, 371, 428));
        primaryStage.setResizable(false);

        /**This will end the autoupdate service when window is closed**/
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                Platform.exit();
                System.exit(0);
            }
        });

        primaryStage.getIcons().add(new Image(Main.class.getResourceAsStream(iconURL)));
        primaryStage.show();
        /**Pass controller to mgr instance so I can control the application from within the class**/
        Manager mgr = new Manager(loader.getController());


    }
}
