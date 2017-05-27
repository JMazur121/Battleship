package main.Client;

/**
 * Created by Jakub on 2017-05-20.
 */
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

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
    private ComboBox<String> gamesCombo;
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
    private TextField fieldAdres;
    @FXML
    private TextField fieldPort;
    @FXML
    private Button connectButton;
    @FXML
    private BorderPane borderPane;

    public Board myBoard;
    public Board enemyBoard;
    private int myGameID;
    private ClientGameThread gameThread;

    private boolean shipPlacement = false;
    private boolean shooting = false;
    private boolean myTurn = false;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        gameThread = new ClientGameThread(this);
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
        boolean result = this.gameThread.tryConnect(address,port);
        if(result){
            //initBoards();
            Stage stage;
            Parent root;
            stage = (Stage)this.connectButton.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("ClientView.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        }
        else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Błąd połączenia");
            alert.setHeaderText(null);
            alert.setContentText("Nie udało się nawiązać połącznenia z serwerem. Sprawdź poprawność danych i spróbuj ponownie później");
            alert.showAndWait();
            System.exit(1);
        }
    }

    public void putInfo(String info){
        infoArea.setText(info);
    }

    private void initBoards(){
        myBoard = new Board(event -> {
            if(!shipPlacement && !shooting)
                return;
            if(!myTurn)
                return;
            ClientCell cell = (ClientCell)event.getSource();
            if(cell.wasUsed()){
                //todo
                return;
            }
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
        hbox.setAlignment(Pos.CENTER);
        hbox.getChildren().add(my);
        hbox.getChildren().add(enemy);
        borderPane.setTop(hbox);
    }

    private void reset(){
        this.shooting = false;
        this.shipPlacement = false;
        this.myTurn = false;
    }

    public void changeTurn(){
        this.myTurn = !myTurn;
    }



}

