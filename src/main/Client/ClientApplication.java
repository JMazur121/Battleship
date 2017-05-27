package main.Client;
/**
 * Created by Jakub on 2017-05-24.
 */

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ClientApplication extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("LoginWindow.fxml"));
        primaryStage.setTitle("Zaloguj");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
}
