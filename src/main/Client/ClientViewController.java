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
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Modality;
import javafx.stage.Stage;
import main.Utils.Command;
import main.Utils.Package;
import main.Utils.Point;

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
    private BorderPane borderPane;

    public Board myBoard;
    public Board enemyBoard;
    private int myGameID;
    private ClientGameThread gameThread;
    private ClientSocket clientSocket;
    private Stage stage;

    private boolean shipPlacement = false;
    private boolean shooting = false;
    private boolean myTurn = false;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initBoards();
    }

    @FXML
    private void logIn(ActionEvent event){
        Package pack = new Package(Command.LOGIN.toString(),this.nickField.getText());
        this.clientSocket.sendMessage(pack.toString());
    }

    public void setThreadAndStage(ClientGameThread gameThread, Stage stage) {
        this.gameThread = gameThread;
        this.gameThread.setViewController(this);
        this.stage = stage;
        this.clientSocket = this.gameThread.getClientSocket();
        System.out.println("Inicjalizacja gotowa");
        this.gameThread.start();
    }

    public void putInfo(String info){
        infoArea.setText(info);
    }

    public void setInfoColor(Color paint){
        Region content = (Region) infoArea.lookup(".content");
        content.setStyle("-fx-background-color: " + toRgbString(paint) + ";");
    }

    private void initBoards(){
        myBoard = new Board(event -> {
            if(!shipPlacement)
                return;
            ClientCell cell = (ClientCell)event.getSource();
            if(cell.wasUsed()){
                return;
            }
            myBoard.setCurrentCell(cell);
            boolean vertical = (event.getButton() == MouseButton.PRIMARY);
            myBoard.setCurrentVertical(vertical);
            Package pack = new Package(Command.PLACE_A_SHIP.toString(),Boolean.toString(vertical),cell.getXCoordinate(),cell.getYCoordinate(),myBoard.getCurrentPlacingSize());
            this.clientSocket.sendMessage(pack.toString());
        });
        enemyBoard = new Board(event -> {
            if(!shooting)
                return;
            if(!myTurn)
                return;
            ClientCell cell = (ClientCell)event.getSource();
            if(cell.wasUsed()){
                return;
            }
            enemyBoard.setCurrentCell(cell);
            boolean vertical = (event.getButton() == MouseButton.PRIMARY);
            enemyBoard.setCurrentVertical(vertical);
            Package pack = new Package(Command.SHOOT.toString(),cell.getXCoordinate(),cell.getYCoordinate());
            this.clientSocket.sendMessage(pack.toString());
        });
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

    public void loginDisable(){
        this.loginButton.setDisable(true);
        this.nickField.setDisable(true);
    }

    private String toRgbString(Color c) {
        return "rgb("
                + to255Int(c.getRed())
                + "," + to255Int(c.getGreen())
                + "," + to255Int(c.getBlue())
                + ")";
    }

    private int to255Int(double d) {
        return (int) (d * 255);
    }
}

