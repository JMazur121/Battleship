package main.Client;

/**
 * Created by Jakub on 2017-05-20.
 */
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author Jakub
 */
public class ClientViewController implements Initializable {

    @FXML
    private VBox options;
    @FXML
    private TextField nickField;
    @FXML
    private Button loginButton;
    @FXML
    private TextField yourgameField;
    @FXML
    private ComboBox<?> gamesCombo;
    @FXML
    private Button joinButton;
    @FXML
    private Button createButton;
    @FXML
    private Button exitButton;
    @FXML
    private VBox chat;
    @FXML
    private TextArea infoArea;
    @FXML
    private TextArea chatArea;
    @FXML
    private TextField chatField;
    @FXML
    private Button sendButton;

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

}

