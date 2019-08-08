package Client;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception{
        System.out.println("Создание клиента");

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("sample.fxml"));
        Parent root = loader.load();

        //Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("My Chat");
        primaryStage.setScene(new Scene(root, 350, 370));
        primaryStage.show();

        Controller controller = loader.getController();
        primaryStage.setOnCloseRequest(controller.getCloseEventHandler());
    }
    public static void main(String[] args) {
        launch(args);

    }
}
