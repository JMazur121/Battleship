package main.Client;

import javafx.application.Platform;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import main.Utils.Command;

/**
 * Created by Jakub on 2017-05-27.
 */
public class ClientGameThread extends Thread {
    public ClientViewController viewController;
    private ClientSocket clientSocket;
    private boolean isConnected = false;

    public ClientGameThread() {
        clientSocket = new ClientSocket();
    }

    public void setViewController(ClientViewController viewController) {
        this.viewController = viewController;
    }

    public boolean tryConnect(String address, int port) {
        isConnected = this.clientSocket.connect(address, port);
        return isConnected;
    }

    public ClientSocket getClientSocket() {
        return this.clientSocket;
    }

    @Override
    public void run() {
        while (true) {
            if (Thread.currentThread().isInterrupted())
                break;
            String received = clientSocket.receiveMessage();
            if (received != null) {
                String tmp[] = received.split("#");
                String command = tmp[0];

                if(command.equals(Command.LOGIN_SUCCEED.toString())){
                    Platform.runLater(() -> {
                        viewController.setInfoColor(Color.GREENYELLOW);
                        viewController.putInfo("Udało się poprawnie zalogować");
                        viewController.afterLoginButtons();
                    });
                }

                else if (command.equals(Command.NAME_NOT_AVAILABLE.toString())){
                    Platform.runLater(() ->{
                        viewController.setInfoColor(Color.INDIANRED);
                        viewController.putInfo("To imię jest już zajęte, spróbuj wybrać inne");
                    });
                }

                else if(command.equals(Command.WAIT_FOR_OPPONENT.toString())){
                    Platform.runLater(()->{
                        viewController.setInfoColor(Color.GREENYELLOW);
                        viewController.putInfo("Utworzono twoją grę. Teraz czekaj na przeciwnika");
                        viewController.setMyGame(Integer.parseInt(tmp[1]));
                        viewController.changeDeleteButtonStatus(false);
                        viewController.changeExitButtonStatus(false);
                        viewController.changeJoinButtonStatus(true);
                        viewController.changeCreateButtonStatus(true);
                    });
                }

                else if(command.equals(Command.HOST_DELETED_THIS_GAME.toString())){
                    Platform.runLater(()->{
                        viewController.setInfoColor(Color.INDIANRED);
                        viewController.putInfo("Gospodarz usunął tę grę. Spróbuj poszukać innej gry na liście.");
                        viewController.clearMyGame();
                    });
                }

                else if(command.equals(Command.GAME_DELETED.toString())){
                    Platform.runLater(()->{
                        viewController.setInfoColor(Color.GREENYELLOW);
                        viewController.putInfo("Twoja gra została usunięta z serwera");
                        viewController.clearMyGame();
                    });
                }

                else if(command.equals(Command.ABANDON_OK.toString())){
                    Platform.runLater(()->{
                        viewController.setInfoColor(Color.GREENYELLOW);
                        viewController.putInfo("Odłączyłeś się od gry");
                        viewController.clearMyGame();
                    });
                }

                else if(command.equals(Command.GAME_ABANDON_AND_DELETED.toString())){
                    Platform.runLater(()->{
                        viewController.setInfoColor(Color.GREENYELLOW);
                        viewController.putInfo("Opuściłeś grę. Usunięto ją, gdyż nie było innego gracza");
                        viewController.clearMyGame();
                    });
                }

                else if(command.equals(Command.HOST_CHANGE.toString())){
                    Platform.runLater(()->{
                        viewController.setInfoColor(Color.GREENYELLOW);
                        viewController.putInfo("Gospodarz opuścił grę, teraz Ty jestes jej nowym gospodarzem");
                    });
                }

                else if(command.equals(Command.OPPONENT_EXIT.toString())){
                    Platform.runLater(()->{
                        viewController.setInfoColor(Color.INDIANRED);
                        viewController.putInfo("Drugi gracz opuścił grę. Musisz zaczekać na innego przeciwnika");
                    });
                }

                else if(command.equals(Command.GAME_HAS_ALREADY_2_PLAYERS.toString())){
                    Platform.runLater(()->{
                        viewController.setInfoColor(Color.INDIANRED);
                        viewController.putInfo("Ta gra jest już zajęta, spróbuj dołączyć do innej gry");
                    });
                }

                else if(command.equals(Command.JOIN_TO_GAME_FAILED.toString())){
                    Platform.runLater(()->{
                        viewController.setInfoColor(Color.INDIANRED);
                        viewController.putInfo("Nie udało się dołączyć do tej gry, spróbuj wybrać inną grę");
                    });
                }

                else if(command.equals(Command.JOINED.toString())){
                    Platform.runLater(()->{
                        viewController.setInfoColor(Color.GREENYELLOW);
                        viewController.putInfo("Udało się dołączyć do gry");
                        viewController.setMyGame(Integer.parseInt(tmp[1]));
                    });
                }

                else if(command.equals(Command.AVAILABLE_GAME.toString())){
                    Platform.runLater(()->{
                        viewController.setInfoColor(Color.GREENYELLOW);
                        viewController.putInfo("Pojawiła się nowa gra");
                        viewController.addGameToList(tmp[1]+"#"+tmp[2]);
                    });
                }

                else if(command.equals(Command.OPPONENT_JOINED.toString())){
                    Platform.runLater(()->{
                        viewController.setInfoColor(Color.GREENYELLOW);
                        viewController.putInfo("Przeciwnik dołączył i można już rozpocząć grę. Zacznij ustawiać swoje statki");
                        viewController.changeShipPlacement(true);
                    });
                }

                else if(command.equals(Command.PLACE_YOUR_SHIPS.toString())){
                    Platform.runLater(()->{
                        viewController.setInfoColor(Color.GREENYELLOW);
                        viewController.putInfo("Można rozpocząć grę. Zacznij ustawiać swoje statki");
                        viewController.changeShipPlacement(true);
                    });
                }

                else if(command.equals(Command.PLACEMENT_FAILED.toString())){
                    Platform.runLater(()->{
                        viewController.setInfoColor(Color.INDIANRED);
                        viewController.putInfo("Nie można umieścić statku w tym miejscu");
                        viewController.setPlacementValidation(false);
                    });
                }

                else if(command.equals(Command.PLACEMENT_SUCCEED.toString())){
                    Platform.runLater(()->{
                        viewController.setInfoColor(Color.GREENYELLOW);
                        viewController.putInfo("Udało się umieścić statek");
                        viewController.setPlacementValidation(false);
                        viewController.myBoard.placeCurrentShip();
                    });
                }

                else if(command.equals(Command.ALL_SHIPS_PLACED.toString())){
                    Platform.runLater(()->{
                        viewController.setInfoColor(Color.GREENYELLOW);
                        viewController.putInfo("Wszystkie statki juz ustawione. Jeśli jesteś gotów, kliknij GOTOWY");
                        viewController.setPlacementValidation(false);
                        viewController.setShipPlacement(false);
                    });
                }
            }
        }
    }
}
