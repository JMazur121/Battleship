package main.Server;/**
 * Created by Jakub on 2017-05-27.
 */

import javafx.application.Application;
import javafx.stage.Stage;

public class ServerApplication extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Server server = Server.getInstance();
        server.start();
    }
}
