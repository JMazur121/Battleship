import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AppController implements Initializable {
    @FXML
    private Button servButton;
    @FXML
    private Button clientButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {}

    @FXML
    private void loadServer(ActionEvent event) throws IOException {
        Stage stage;
        stage = (Stage) this.servButton.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ServerView.fxml"));
        Parent root = (Parent) loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    @FXML
    private void loadClient(ActionEvent event) throws IOException {
        Stage stage;
        stage = (Stage) this.clientButton.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/LoginWindow.fxml"));
        Parent root = (Parent) loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
}
