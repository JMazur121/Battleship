package Client.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginWindowController implements Initializable{
    @FXML
    private TextField fieldAdres;
    @FXML
    private TextField fieldPort;
    @FXML
    private Button connectButton;

    private GameService service;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        service = new GameService();

    }
    @FXML
    private void connect(ActionEvent event) throws IOException {
        String address;
        int port;
        try {
            address = this.fieldAdres.getText();
            port = Integer.parseInt(this.fieldPort.getText());
        }
        catch (NumberFormatException e){
            e.printStackTrace();
            System.err.println(e);
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Format liczbowy");
            alert.setHeaderText(null);
            alert.setContentText("Wprowadzona wartość portu nie jest rozpoznawana jako format liczbowy.");
            alert.showAndWait();
            return;
        }
        boolean result = this.service.tryConnect(address,port);
        if(result){
            Stage stage;
            stage = (Stage) this.connectButton.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ClientView.fxml"));
            Parent root = (Parent) loader.load();
            ClientViewController clientController = loader.getController();
            clientController.setServiceAndStage(this.service,stage);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        }
        else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Błąd połączenia");
            alert.setHeaderText(null);
            alert.setContentText("Nie udało się nawiązać połącznenia z serwerem. Sprawdź poprawność danych i spróbuj ponownie później");
            alert.showAndWait();
        }
    }

}
