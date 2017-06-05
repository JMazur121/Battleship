package main.Server;

import javafx.application.Platform;
import main.Utils.Command;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by Jakub on 2017-05-20.
 */
public class Player extends Thread {
    private String userName = null;
    private PlayerSocket socket;
    private Game game = null;
    private boolean isConnected = false;

    public Player(Socket socket) {
        this.socket = new PlayerSocket(socket);
        System.out.println("Player connected...");
        isConnected = true;
    }

    public void run(){
        System.out.println("Player started...");
        try {
            this.socket.connect();
            while(true){
                String received = this.socket.receiveMessage();
                if (received != null) {
                    String tmp[] = received.split("#");
                    String command = tmp[0];
                    if (command.equals("LOGIN")) {
                        if (Server.getInstance().checkNameAvailability(tmp[1])) {
                            userName = tmp[1];
                            Server.getInstance().addName(tmp[1]);
                            sendToPlayer(Command.LOGIN_SUCCEED.toString());
                            isConnected = true;

                            //send available games
                            for (Game game : Server.getInstance().getGames()) {
                                String info = Command.AVAILABLE_GAME.toString();
                                info = info + "#" + game.getInfo();
                                sendToPlayer(info);
                            }
                        } else {
                            sendToPlayer(Command.NAME_NOT_AVAILABLE.toString());
                        }
                    }
                    else if (command.equals("CREATE_GAME")) {
                        resetPlayer();
                        game = new Game(Server.getInstance().getNextGameID(), this);
                        Server.getInstance().addGame(game);
                        sendToPlayer(Command.WAIT_FOR_OPPONENT.toString() + "#" + game.getGameID());
                    }

                    else if(command.equals("ABANDON_GAME")){
                        if(this == game.getHost()){
                            Player guest = this.game.getGuest();
                            if (guest != null) {
                                guest.sendToPlayer(Command.HOST_CHANGE.toString());
                                this.game.setHost(guest);
                                this.game.setGuest(null);
                                this.sendToPlayer(Command.ABANDON_OK.toString());
                            }
                            else {
                                this.sendToPlayer(Command.GAME_ABANDON_AND_DELETED.toString());
                                this.resetPlayer();
                                Server.getInstance().deleteGame(this.game);
                            }
                        }
                        else {
                            Player host = this.game.getHost();
                            host.sendToPlayer(Command.OPPONENT_EXIT.toString());
                            this.sendToPlayer(Command.ABANDON_OK.toString());
                            this.game.setGuest(null);
                        }
                    }

                    else if (command.equals("DELETE_GAME")) {
                        if (this == this.game.getHost()) {
                            Player guest = this.game.getGuest();
                            if (guest != null) {
                                guest.sendToPlayer(Command.HOST_DELETED_THIS_GAME.toString());
                                guest.resetPlayer();
                            }
                            Server.getInstance().deleteGame(this.game);
                            this.resetPlayer();
                            this.sendToPlayer(Command.GAME_DELETED.toString());
                        }
                    }
                    else if (command.equals("JOIN_TO_GAME")) {
                        resetPlayer();
                        int index = Integer.parseInt(tmp[1]);
                        Game toJoin = Server.getInstance().findGame(index);
                        if (toJoin != null) {
                            if (toJoin.hasTwoPlayers()) {
                                sendToPlayer(Command.GAME_HAS_ALREADY_2_PLAYERS.toString());
                            } else {
                                toJoin.addPlayer(this);
                                this.game = toJoin;
                            }
                        } else {
                            sendToPlayer(Command.JOIN_TO_GAME_FAILED.toString());
                        }
                    }

                    else if(command.equals("INVITATION")){
                        if(!this.game.isGameActive() && this.game.hasTwoPlayers()) {
                            Player opponent = this.game.getOpponent(this);
                            opponent.sendToPlayer(Command.INVITATION.toString());
                        }
                    }

                    else if(command.equals("OFFER_ACCEPT")){
                        this.game.startGame(this.game.getOpponent(this));
                    }

                    else if(command.equals("OFFER_REJECT")){
                        Player opponent = this.game.getOpponent(this);
                        opponent.sendToPlayer(Command.OFFER_REJECT.toString());
                    }

                    else if(command.equals("GIVE_UP")){
                        this.game.exitGame(this);
                    }

                    else if(command.equals("CHAT_MESSAGE")){
                        Player opponent = this.game.getOpponent(this);
                        opponent.sendToPlayer(Command.CHAT_MESSAGE.toString() +"#"+tmp[1]);
                    }

                    else if (command.equals("PLACE_A_SHIP")) {
                        try {
                            boolean vertical = Boolean.parseBoolean(tmp[1]);
                            int x = Integer.parseInt(tmp[2]);
                            int y = Integer.parseInt(tmp[3]);
                            int length = Integer.parseInt(tmp[4]);
                            this.game.placeShip(this, x, y, length, vertical);
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }

                    else if(command.equals("REMOVE_SHIP")){
                        try {
                            int x = Integer.parseInt(tmp[1]);
                            int y = Integer.parseInt(tmp[2]);
                            this.game.removeShip(this,x,y);
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }

                    else if(command.equals("READY")){
                        this.game.setReady(this);
                    }

                    else if (command.equals("SHOOT")) {
                        try {
                            int x = Integer.parseInt(tmp[1]);
                            int y = Integer.parseInt(tmp[2]);
                            Server.getInstance().printLog(x +","+y);
                            this.game.shoot(this, x, y);
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }
                }
                Thread.sleep(50);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.socket.close();
        }
    }

    public String getUserName() {
        return userName;
    }

    public void sendToPlayer(String msg) { this.socket.sendMessage(msg);}

    public void resetPlayer() {
        game = null;
    }

}
