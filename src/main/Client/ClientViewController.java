package main.Client;

/**
 * Created by Jakub on 2017-05-20.
 */
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
    private Button buttonDeleteGame;
    @FXML
    private Button buttonGiveUp;
    @FXML
    private Button playButton;
    @FXML
    private Button readyButton;
    @FXML
    private VBox chat;
    @FXML
    private VBox VBoxMy;
    @FXML
    private VBox VBoxEnemy;
    @FXML
    private VBox VBoxControl;
    @FXML
    private HBox Boards;
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
    private int myGameID = -1;
    private ClientGameThread gameThread;
    private ClientSocket clientSocket;
    private Stage stage;

    private boolean shipPlacement = false;
    private boolean placementValidation = false;
    private boolean shooting = false;
    private boolean myTurn = false;

    private ObservableList<String> gameList;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initBoards();
        this.buttonDeleteGame.setDisable(true);
        this.createButton.setDisable(true);
        this.exitButton.setDisable(true);
        this.joinButton.setDisable(true);
        this.sendButton.setDisable(true);
        this.gameList = FXCollections.observableArrayList();
        this.gamesCombo.setItems(this.gameList);
        this.gamesCombo.setDisable(true);
        this.buttonGiveUp.setDisable(true);
        this.chatField.setDisable(true);
    }

    @FXML
    private void logIn(ActionEvent event){
        Package pack = new Package(Command.LOGIN.toString(),this.nickField.getText());
        this.clientSocket.sendMessage(pack.toString());
    }

    @FXML
    private void giveUp(ActionEvent event){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Potwierdzenie poddania");
        alert.setHeaderText(null);
        alert.setContentText("Czy na pewno chcesz sie poddać?");
        Optional<ButtonType> action = alert.showAndWait();
        if(action.get() == ButtonType.OK){
            this.clientSocket.sendMessage(Command.GIVE_UP.toString());
        }
        else
            return;
    }

    @FXML
    private void joinGame(ActionEvent event){
        if(this.gamesCombo.getSelectionModel().isEmpty()){
            this.setInfoColor(Color.INDIANRED);
            this.putInfo("Nie wybrano żadnej gry z listy !!!");
        }

        else{
            String tmp[] = this.gamesCombo.getSelectionModel().getSelectedItem().split("#");
            Package pack = new Package(Command.JOIN_TO_GAME.toString(),tmp[0]);
            System.out.println(pack.toString());
            this.clientSocket.sendMessage(pack.toString());
        }
    }

    @FXML
    private void createGame(ActionEvent event){
        this.clientSocket.sendMessage(Command.CREATE_GAME.toString());
    }

    @FXML
    private void deleteGame(ActionEvent event){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Potwierdzenie usunięcia");
        alert.setHeaderText(null);
        alert.setContentText("Próbujesz usunąć grę. Czy na pewno chcesz to zrobić ?");
        Optional<ButtonType> action = alert.showAndWait();
        if(action.get() == ButtonType.OK){
            this.clientSocket.sendMessage(Command.DELETE_GAME.toString());
        }
        else
            return;
    }

    @FXML
    private void abandonGame(ActionEvent event){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Potwierdzenie opuszczenia");
            alert.setHeaderText(null);
            alert.setContentText("Próbujesz opuścić grę. Czy na pewno chcesz to zrobić ?");
            Optional<ButtonType> action = alert.showAndWait();
            if(action.get() == ButtonType.OK){
                this.clientSocket.sendMessage(Command.ABANDON_GAME.toString());
            }
            else
                return;
    }

    public void addGameToList(String gameInfo){
        this.gameList.add(gameInfo);
    }

    public void removeGameFromList(String gameInfo){
        this.gameList.remove(gameInfo);
    }

    public void changeShipPlacement(Boolean status){
        this.shipPlacement = status;
    }

    public void setPlacementValidation(boolean placementValidation) {
        this.placementValidation = placementValidation;
    }

    public void setShipPlacement(boolean shipPlacement) {
        this.shipPlacement = shipPlacement;
    }

    public void setShooting(boolean shooting) {
        this.shooting = shooting;
    }

    public void setMyTurn(boolean myTurn) {
        this.myTurn = myTurn;
    }

    public void setThreadAndStage(ClientGameThread gameThread, Stage stage) {
        this.gameThread = gameThread;
        this.gameThread.setViewController(this);
        this.stage = stage;
        this.clientSocket = this.gameThread.getClientSocket();
        System.out.println("Inicjalizacja gotowa");
        this.gameThread.start();
    }

    public void changeLoginButtonStatus(Boolean status){
        this.loginButton.setDisable(status);
    }
    public void changeJoinButtonStatus(Boolean status){
        this.joinButton.setDisable(status);
    }
    public void changeCreateButtonStatus(Boolean status){
        this.createButton.setDisable(status);
    }
    public void changeDeleteButtonStatus(Boolean status){
        this.buttonDeleteGame.setDisable(status);
    }
    public void changeGiveUpButtonStatus(Boolean status){
        this.buttonGiveUp.setDisable(status);
    }
    public void changeExitButtonStatus(Boolean status){
        this.exitButton.setDisable(status);
    }
    public void changeSendButtonStatus(Boolean status){
        this.sendButton.setDisable(status);
    }

    public void putInfo(String info){
        infoArea.setText(info);
    }

    public void setMyGame(int id){
        this.myGameID = id;
        this.yourgameField.setText("ID gry : " + Integer.toString(this.myGameID));
    }

    public void clearMyGame(){
        this.myGameID = -1;
        this.yourgameField.clear();
    }

    public void setInfoColor(Color paint){
        Region content = (Region) infoArea.lookup(".content");
        content.setStyle("-fx-background-color: " + toRgbString(paint) + ";");
    }

    private void initBoards(){
        myBoard = new Board(event -> {
            if(!shipPlacement)
                return;
            if(placementValidation){
                setInfoColor(Color.INDIANRED);
                putInfo("Poczekaj na odpowiedź serwera zanim postawisz kolejny statek");
                return;
            }
            if(myBoard.getShipsLeft() == 0){
                setInfoColor(Color.INDIANRED);
                putInfo("Postawiłeś już maksymalną ilość statków !!!");
                return;
            }
            ClientCell cell = (ClientCell)event.getSource();
            if(cell.wasUsed()){
                return;
            }
            myBoard.setCurrentCell(cell);
            boolean vertical = (event.getButton() == MouseButton.PRIMARY);
            myBoard.setCurrentVertical(vertical);
            Package pack = new Package(Command.PLACE_A_SHIP.toString(),Boolean.toString(vertical),cell.getXCoordinate(),cell.getYCoordinate(),myBoard.getCurrentPlacingSize());
            this.clientSocket.sendMessage(pack.toString());
            setPlacementValidation(true);
        });
        enemyBoard = new Board(event -> {
            if(!shooting)
                return;
            if(!myTurn){
                setInfoColor(Color.INDIANRED);
                putInfo("Czekaj na swoją kolej !!!");
                return;
            }
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

        this.VBoxMy.getChildren().add(myBoard);
        Label mylabel = new Label();
        mylabel.setText("Plansza gracza");
        mylabel.setAlignment(Pos.CENTER);
        Label enemyLabel = new Label();
        enemyLabel.setText("Plansza przeciwnika");
        enemyLabel.setAlignment(Pos.CENTER);
        this.VBoxMy.getChildren().add(mylabel);
        this.VBoxEnemy.getChildren().add(enemyBoard);
        this.VBoxEnemy.getChildren().add(enemyLabel);
    }

    private void reset(){
        this.shooting = false;
        this.shipPlacement = false;
        this.myTurn = false;
        this.placementValidation = false;
    }

    public void afterLoginButtons(){
        changeCreateButtonStatus(false);
        changeJoinButtonStatus(false);
        this.gamesCombo.setDisable(false);
        changeLoginButtonStatus(false);
        this.nickField.setDisable(true);
    }

    public void changeTurn(){
        this.myTurn = !myTurn;
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

