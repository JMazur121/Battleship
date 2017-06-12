package Client.Controller;

import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.paint.Color;
import Client.Model.ClientSocket;
import Utils.Command;

public class GameService extends Service<Void> {
    public ClientViewController viewController;
    private ClientSocket clientSocket;
    private boolean isConnected = false;

    public GameService() {
        super();
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
    protected Task<Void> createTask() {
        return new Task<Void>() {
            @Override
            protected Void call() {
                try {
                    loop:
                    while (true) {
                        String received = clientSocket.receiveMessage();
                        if (received != null) {
                            String tmp[] = received.split("#");
                            String command = tmp[0];

                            if (command.equals(Command.LOGIN_SUCCEED.toString())) {
                                Platform.runLater(() -> {
                                    viewController.setInfoColor(Color.GREENYELLOW);
                                    viewController.putInfo("Udało się zalogować");
                                    viewController.afterLoginButtons();
                                });
                            } else if (command.equals(Command.NAME_NOT_AVAILABLE.toString())) {
                                Platform.runLater(() -> {
                                    viewController.setInfoColor(Color.INDIANRED);
                                    viewController.putInfo("Inny gracz wykorzystuje ten nick. Podaj inny");
                                });
                            } else if (command.equals(Command.GAME_NAME_NOT_AVAILABLE.toString())) {
                                Platform.runLater(() -> {
                                    viewController.setInfoColor(Color.INDIANRED);
                                    viewController.putInfo("Ta nazwa gry jest już zajęta. Spróbuj wymyślić inną nazwę");
                                });
                            } else if (command.equals(Command.WAIT_FOR_OPPONENT.toString())) {
                                Platform.runLater(() -> {
                                    viewController.setInfoColor(Color.GREENYELLOW);
                                    viewController.putInfo("Utworzono twoją grę. Teraz czekaj na przeciwnika");
                                    viewController.setMyGame(tmp[1]);
                                    viewController.changeDeleteButtonStatus(false);
                                    viewController.changeExitButtonStatus(false);
                                    viewController.changeJoinButtonStatus(true);
                                    viewController.changeCreateButtonStatus(true);
                                    viewController.changeGamesComboStatus(true);
                                });
                            } else if (command.equals(Command.HOST_DELETED_THIS_GAME.toString())) {
                                Platform.runLater(() -> {
                                    viewController.setInfoColor(Color.INDIANRED);
                                    viewController.putInfo("Gospodarz usunął tę grę. Spróbuj poszukać innej gry na liście.");
                                    viewController.clearMyGame();
                                    viewController.disableChat();
                                    viewController.afterLoginButtons();
                                    viewController.reset();
                                    viewController.clearChatArea();
                                });
                            } else if (command.equals(Command.GAME_DELETED.toString())) {
                                Platform.runLater(() -> {
                                    viewController.setInfoColor(Color.GREENYELLOW);
                                    viewController.putInfo("Twoja gra została usunięta z serwera");
                                    viewController.clearMyGame();
                                    viewController.disableChat();
                                    viewController.clearChatArea();
                                    viewController.afterLoginButtons();
                                    viewController.reset();
                                });
                            } else if (command.equals(Command.ABANDON_OK.toString())) {
                                Platform.runLater(() -> {
                                    viewController.setInfoColor(Color.GREENYELLOW);
                                    viewController.putInfo("Odłączyłeś się od gry");
                                    viewController.clearMyGame();
                                    viewController.reset();
                                    viewController.afterLoginButtons();
                                    viewController.disableChat();
                                    viewController.clearChatArea();
                                });
                            } else if (command.equals(Command.GAME_ABANDON_AND_DELETED.toString())) {
                                Platform.runLater(() -> {
                                    viewController.setInfoColor(Color.GREENYELLOW);
                                    viewController.putInfo("Opuściłeś grę. Usunięto ją, gdyż nie było innego gracza");
                                    viewController.clearMyGame();
                                    viewController.afterLoginButtons();
                                    viewController.reset();
                                    viewController.disableChat();
                                    viewController.clearChatArea();
                                });
                            } else if (command.equals(Command.HOST_CHANGE.toString())) {
                                Platform.runLater(() -> {
                                    viewController.setInfoColor(Color.GREENYELLOW);
                                    viewController.putInfo("Gospodarz opuścił grę, teraz Ty jestes jej nowym gospodarzem");
                                    viewController.disableChat();
                                    viewController.clearChatArea();
                                    viewController.reset();
                                    viewController.changeDeleteButtonStatus(false);
                                    viewController.changeOfferButtonStatus(true);
                                });
                            } else if (command.equals(Command.OPPONENT_EXIT.toString())) {
                                Platform.runLater(() -> {
                                    viewController.setInfoColor(Color.INDIANRED);
                                    viewController.putInfo("Drugi gracz opuścił grę. Musisz zaczekać na innego przeciwnika");
                                    viewController.disableChat();
                                    viewController.clearChatArea();
                                    viewController.reset();
                                    viewController.changeOfferButtonStatus(true);
                                });
                            } else if (command.equals(Command.GAME_HAS_ALREADY_2_PLAYERS.toString())) {
                                Platform.runLater(() -> {
                                    viewController.setInfoColor(Color.INDIANRED);
                                    viewController.putInfo("Ta gra jest już zajęta, spróbuj dołączyć do innej gry");
                                });
                            } else if (command.equals(Command.JOIN_TO_GAME_FAILED.toString())) {
                                Platform.runLater(() -> {
                                    viewController.setInfoColor(Color.INDIANRED);
                                    viewController.putInfo("Nie udało się dołączyć do tej gry, spróbuj wybrać inną grę");
                                });
                            } else if (command.equals(Command.JOINED.toString())) {
                                Platform.runLater(() -> {
                                    viewController.setInfoColor(Color.GREENYELLOW);
                                    viewController.putInfo("Udało się dołączyć do gry");
                                    viewController.setMyGame(tmp[1]);
                                    viewController.enableChat();
                                    viewController.changeGamesComboStatus(true);
                                    viewController.changeOfferButtonStatus(false);
                                    viewController.changeExitButtonStatus(false);
                                    viewController.changeCreateButtonStatus(true);
                                    viewController.changeJoinButtonStatus(true);
                                });
                            } else if (command.equals(Command.AVAILABLE_GAME.toString())) {
                                Platform.runLater(() -> {
                                    viewController.setInfoColor(Color.GREENYELLOW);
                                    viewController.putInfo("Pojawiła się nowa gra");
                                    viewController.addGameToList(tmp[1]);
                                });
                            } else if (command.equals(Command.REMOVE_GAME.toString())) {
                                Platform.runLater(() -> {
                                    viewController.setInfoColor(Color.GREENYELLOW);
                                    viewController.removeGameFromList(tmp[1]);
                                });
                            } else if (command.equals(Command.OPPONENT_JOINED.toString())) {
                                Platform.runLater(() -> {
                                    viewController.setInfoColor(Color.GREENYELLOW);
                                    viewController.putInfo("Przeciwnik dołączył do gry.");
                                    viewController.enableChat();
                                    viewController.changeOfferButtonStatus(false);
                                });
                            } else if (command.equals(Command.PLACE_YOUR_SHIPS.toString())) {
                                Platform.runLater(() -> {
                                    viewController.setInfoColor(Color.GREENYELLOW);
                                    viewController.putInfo("Można rozpocząć grę. Zacznij ustawiać swoje statki");
                                    viewController.changeShipPlacement(true);
                                    viewController.changeSizeBoxStatus(false);
                                    viewController.radioSizeActivate();
                                    viewController.changeGiveUpButtonStatus(false);
                                    viewController.changeRemoveButtonStatus(false);
                                    viewController.changeOfferButtonStatus(true);
                                });
                            } else if (command.equals(Command.PLACEMENT_FAILED.toString())) {
                                Platform.runLater(() -> {
                                    viewController.setInfoColor(Color.INDIANRED);
                                    viewController.putInfo("Nie można umieścić statku w tym miejscu");
                                    viewController.setPlacementValidation(false);
                                    viewController.changeSizeBoxStatus(false);
                                });
                            } else if (command.equals(Command.PLACEMENT_SUCCEED.toString())) {
                                Platform.runLater(() -> {
                                    viewController.setInfoColor(Color.GREENYELLOW);
                                    viewController.putInfo("Udało się umieścić statek");
                                    viewController.setPlacementValidation(false);
                                    viewController.changeSizeBoxStatus(false);
                                    viewController.myBoard.placeCurrentShip();
                                });
                            } else if (command.equals(Command.ALL_SHIPS_PLACED.toString())) {
                                Platform.runLater(() -> {
                                    viewController.setInfoColor(Color.GREENYELLOW);
                                    viewController.putInfo("Wszystkie statki juz ustawione. Jeśli jesteś gotów, kliknij GOTOWY");
                                    viewController.setPlacementValidation(false);
                                });
                            } else if (command.equals(Command.REMOVE_OK.toString())) {
                                Platform.runLater(() -> {
                                    viewController.setInfoColor(Color.GREENYELLOW);
                                    viewController.putInfo("Statek został usunięty");
                                    viewController.setPlacementValidation(false);
                                    viewController.myBoard.removeShip(Boolean.parseBoolean(tmp[1]), Integer.parseInt(tmp[2]), Integer.parseInt(tmp[3]), Integer.parseInt(tmp[4]));
                                });
                            } else if (command.equals(Command.OPPONENT_IS_READY.toString())) {
                                Platform.runLater(() -> {
                                    viewController.setInfoColor(Color.GREENYELLOW);
                                    viewController.putInfo("Twój przeciwnik jest juz gotowy");
                                });
                            } else if (command.equals(Command.OPPONENT_GIVE_UP.toString())) {
                                Platform.runLater(() -> {
                                    viewController.setInfoColor(Color.GREENYELLOW);
                                    viewController.putInfo("Twój przeciwnik poddał się - wygrywasz !!!");
                                    viewController.reset();
                                    viewController.changeOfferButtonStatus(false);
                                });
                            } else if (command.equals(Command.INVITATION.toString())) {
                                Platform.runLater(() -> {
                                    viewController.offerReceived();
                                });
                            } else if (command.equals(Command.OFFER_REJECT.toString())) {
                                Platform.runLater(() -> {
                                    viewController.setInfoColor(Color.INDIANRED);
                                    viewController.putInfo("Przeciwnik odrzucił twoja propozycję");
                                });
                            } else if (command.equals(Command.YOUR_TURN.toString())) {
                                Platform.runLater(() -> {
                                    viewController.setInfoColor(Color.GREENYELLOW);
                                    viewController.putInfo("Twój ruch. Kliknij w komórkę na planszy przeciwnika aby strzelić !!!");
                                    viewController.setPlacementValidation(false);
                                    viewController.setShipPlacement(false);
                                    viewController.setShooting(true);
                                    viewController.setMyTurn(true);
                                    viewController.radioDeactivate();
                                });
                            } else if (command.equals(Command.NOT_YOUR_TURN.toString())) {
                                Platform.runLater(() -> {
                                    viewController.setInfoColor(Color.LIGHTGREY);
                                    viewController.putInfo("Grę rozpocznie twój przeciwnik. Poczekaj na swoją kolej");
                                    viewController.setShooting(true);
                                    viewController.setMyTurn(false);
                                    viewController.radioDeactivate();
                                });
                            } else if (command.equals(Command.MISSED_NOT_YOUR_TURN.toString())) {
                                Platform.runLater(() -> {
                                    viewController.setInfoColor(Color.INDIANRED);
                                    viewController.putInfo("Pudło ! . Musisz zaczekać na swoją kolej");
                                    viewController.setMyTurn(false);
                                    viewController.enemyBoard.repaintOnMissed(Integer.parseInt(tmp[1]), Integer.parseInt(tmp[2]));
                                });
                            } else if (command.equals(Command.OPPONENT_MISSED_YOUR_TURN.toString())) {
                                Platform.runLater(() -> {
                                    viewController.setInfoColor(Color.GREENYELLOW);
                                    viewController.putInfo("Przeciwnik spudłował. Teraz twoja kolej");
                                    viewController.setMyTurn(true);
                                    viewController.myBoard.repaintOnMissed(Integer.parseInt(tmp[1]), Integer.parseInt(tmp[2]));
                                });
                            } else if (command.equals(Command.HIT_SHOOT_AGAIN.toString())) {
                                Platform.runLater(() -> {
                                    viewController.setInfoColor(Color.GREENYELLOW);
                                    viewController.putInfo("Trafiłeś, ale statek wciąż pozostaje na powierzchni. Strzelaj jeszcze raz !!!");
                                    viewController.setMyTurn(true);
                                    viewController.enemyBoard.repaintOnHit(Integer.parseInt(tmp[1]), Integer.parseInt(tmp[2]));
                                });
                            } else if (command.equals(Command.OPPONENT_HIT.toString())) {
                                Platform.runLater(() -> {
                                    viewController.setInfoColor(Color.INDIANRED);
                                    viewController.putInfo("Przeciwnik trafia !!! Musisz zaczekać na swoją kolej");
                                    viewController.setMyTurn(false);
                                    viewController.myBoard.repaintOnHit(Integer.parseInt(tmp[1]), Integer.parseInt(tmp[2]));
                                });
                            } else if (command.equals(Command.HIT_AND_SINK.toString())) {
                                Platform.runLater(() -> {
                                    viewController.setInfoColor(Color.GREENYELLOW);
                                    viewController.putInfo("Trafiony - zatopiony !!! Przeciwnik wciąż ma statki do zatopienia - strzelaj dalej !!! ");
                                    viewController.setMyTurn(true);
                                    viewController.enemyBoard.repaintOnHit(Integer.parseInt(tmp[1]), Integer.parseInt(tmp[2]));
                                });
                            } else if (command.equals(Command.OPPONENT_HIT_AND_SINK.toString())) {
                                Platform.runLater(() -> {
                                    viewController.setInfoColor(Color.INDIANRED);
                                    viewController.putInfo("Przeciwnik zatapia twój statek !!! Czekaj na swoją kolej");
                                    viewController.setMyTurn(false);
                                    viewController.myBoard.repaintOnHit(Integer.parseInt(tmp[1]), Integer.parseInt(tmp[2]));
                                });
                            } else if (command.equals(Command.PLAYER_HINT.toString())) {
                                Platform.runLater(() -> {
                                    viewController.enemyBoard.paintHintAfterKill(Integer.parseInt(tmp[2]), Integer.parseInt(tmp[3]), Integer.parseInt(tmp[4]), Boolean.parseBoolean(tmp[1]));
                                });
                            } else if (command.equals(Command.YOU_WIN.toString())) {
                                Platform.runLater(() -> {
                                    viewController.setInfoColor(Color.GREENYELLOW);
                                    viewController.putInfo("WYGRYWASZ !!!");
                                    viewController.reset();
                                    viewController.changeOfferButtonStatus(false);
                                });
                            } else if (command.equals(Command.YOU_LOSE.toString())) {
                                Platform.runLater(() -> {
                                    viewController.setInfoColor(Color.INDIANRED);
                                    viewController.putInfo("PRZEGRYWASZ !!!");
                                    viewController.reset();
                                    viewController.changeOfferButtonStatus(false);
                                });
                            } else if (command.equals(Command.CHAT_MESSAGE.toString())) {
                                Platform.runLater(() -> {
                                    viewController.setInfoColor(Color.GREENYELLOW);
                                    viewController.putInfo("Nadeszła wiadomośc");
                                    viewController.chatReceived(tmp[1]);
                                });
                            } else if (command.equals(Command.SERVER_SHUTDOWN.toString())) {
                                Platform.runLater(() -> {
                                    viewController.serverShutdown();
                                });
                                break loop;
                            }
                        }
                    }
                } catch (Exception e) {
                } finally {
                    return null;
                }
            }
        };
    }
}
