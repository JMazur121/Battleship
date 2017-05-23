package main.Client;

/**
 * Created by Jakub on 2017-05-20.
 */
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

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
    @FXML
    private BorderPane borderPane;

    private Board myBoard;
    private Board enemyBoard;

    private boolean shipPlacement = false;
    private boolean shooting = false;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        myBoard = new Board(event -> {
            if(!shipPlacement && !shooting)
                return;
            ClientCell cell = (ClientCell)event.getSource();
            cell.setFill(Color.GOLD);//for test
        });
        enemyBoard = new Board(event -> {});
        HBox hbox = new HBox();
        VBox my = new VBox();
        VBox enemy = new VBox();
        my.getChildren().add(myBoard);
        Label mylabel = new Label();
        mylabel.setText("Plansza gracza");
        mylabel.setAlignment(Pos.CENTER);
        Label enemyLabel = new Label();
        enemyLabel.setText("Plansza przeciwnika");
        enemyLabel.setAlignment(Pos.CENTER);
        my.getChildren().add(mylabel);
        enemy.getChildren().add(enemyBoard);
        enemy.getChildren().add(enemyLabel);
        hbox.setSpacing(10);
        hbox.getChildren().add(my);
        hbox.getChildren().add(enemy);
        borderPane.setTop(hbox);
    }

}

