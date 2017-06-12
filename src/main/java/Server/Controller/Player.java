package Server.Controller;

import Server.Model.Game;
import Server.Model.PlayerSocket;
import Utils.Command;

import java.net.Socket;

public class Player extends Thread {
    private String userName = null;
    private PlayerSocket socket;
    private Game game = null;
    private boolean isConnected = false;

    public Player(Socket socket) {
        this.socket = new PlayerSocket(socket);
        isConnected = true;
    }

    public void run(){
        try {
            this.socket.connect();
            loop : while(true){
                String received = this.socket.receiveMessage();
                if (received != null) {
                    String tmp[] = received.split("#");
                    String command = tmp[0];
                    if (command.equals(Command.LOGIN.toString())) {
                        if (Server.getInstance().checkNameAvailability(tmp[1])) {
                            userName = tmp[1];
                            Server.getInstance().addName(tmp[1]);
                            sendToPlayer(Command.LOGIN_SUCCEED.toString());

                            //send available games
                            for (Game game : Server.getInstance().getGames()) {
                                String info = Command.AVAILABLE_GAME.toString();
                                info = info + "#" + game.getGameName();
                                sendToPlayer(info);
                            }
                        } else {
                            sendToPlayer(Command.NAME_NOT_AVAILABLE.toString());
                        }
                    }
                    else if (command.equals(Command.CREATE_GAME.toString())) {
                        resetPlayer();
                        String name = tmp[1];
                        if(Server.getInstance().checkGameNameAvailability(name)) {
                            Game game = new Game(name,this);
                            Server.getInstance().addGame(game);
                            this.game = game;
                            sendToPlayer(Command.WAIT_FOR_OPPONENT.toString() + "#" + name);
                        }
                        else {
                            sendToPlayer(Command.GAME_NAME_NOT_AVAILABLE.toString());
                        }
                    }

                    else if(command.equals(Command.ABANDON_GAME.toString())){
                        if(this == game.getHost()){
                            Player guest = this.game.getGuest();
                            if (guest != null) {
                                guest.sendToPlayer(Command.HOST_CHANGE.toString());
                                this.game.setHost(guest);
                                this.game.setGuest(null);
                                this.game.reset();
                                this.resetPlayer();
                                this.sendToPlayer(Command.ABANDON_OK.toString());
                            }
                            else {
                                Server.getInstance().deleteGame(this.game);
                                this.sendToPlayer(Command.GAME_ABANDON_AND_DELETED.toString());
                                this.resetPlayer();
                            }
                        }
                        else {
                            Player host = this.game.getHost();
                            this.game.setGuest(null);
                            this.game.reset();
                            host.sendToPlayer(Command.OPPONENT_EXIT.toString());
                            this.sendToPlayer(Command.ABANDON_OK.toString());
                            this.resetPlayer();
                        }
                    }

                    else if (command.equals(Command.DELETE_GAME.toString())) {
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
                    else if (command.equals(Command.JOIN_TO_GAME.toString())) {
                        resetPlayer();
                        Game toJoin = Server.getInstance().findGame(tmp[1]);
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

                    else if(command.equals(Command.INVITATION.toString())){
                        if(!this.game.isGameActive()) {
                            this.game.getOpponent(this).sendToPlayer(Command.INVITATION.toString());
                        }
                    }

                    else if(command.equals(Command.OFFER_ACCEPT.toString())){
                        if(!this.game.isGameActive()) {
                            this.game.startGame(this.game.getOpponent(this));
                        }
                    }

                    else if(command.equals(Command.OFFER_REJECT.toString())){
                        if(!this.game.isGameActive()) {
                            this.game.getOpponent(this).sendToPlayer(Command.OFFER_REJECT.toString());
                        }
                    }

                    else if(command.equals(Command.GIVE_UP.toString())){
                        this.game.exitGame(this);
                    }

                    else if(command.equals(Command.CHAT_MESSAGE.toString())){
                        Player opponent = this.game.getOpponent(this);
                        opponent.sendToPlayer(Command.CHAT_MESSAGE.toString() +"#"+tmp[1]);
                    }

                    else if (command.equals(Command.PLACE_A_SHIP.toString())) {
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

                    else if(command.equals(Command.REMOVE_SHIP.toString())){
                        try {
                            int x = Integer.parseInt(tmp[1]);
                            int y = Integer.parseInt(tmp[2]);
                            this.game.removeShip(this,x,y);
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }

                    else if(command.equals(Command.READY.toString())){
                        this.game.setReady(this);
                    }

                    else if (command.equals(Command.SHOOT.toString())) {
                        try {
                            int x = Integer.parseInt(tmp[1]);
                            int y = Integer.parseInt(tmp[2]);
                            this.game.shoot(this, x, y);
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }

                    else if(command.equals(Command.CLIENT_CLOSE.toString())){
                        this.isConnected = false;
                        if(this.game != null) {
                            if (this == game.getHost()) {
                                Player guest = this.game.getGuest();
                                if (guest != null) {
                                    guest.sendToPlayer(Command.HOST_CHANGE.toString());
                                    this.game.setHost(guest);
                                    this.game.setGuest(null);
                                    this.game.reset();
                                } else {
                                    Server.getInstance().deleteGame(this.game);
                                }
                            } else {
                                Player host = this.game.getHost();
                                host.sendToPlayer(Command.OPPONENT_EXIT.toString());
                                this.game.setGuest(null);
                                this.game.reset();
                            }
                        }
                        break loop;
                    }
                    else if (command.equals(Command.SERVER_SHUTDOWN.toString())){
                        break loop;
                    }
                }
            }
        } catch (Exception e) {
            this.isConnected = false;
        } finally {
            this.socket.close();
            Server.getInstance().deleteName(this.userName);
            Server.getInstance().deletePlayer(this);
        }
    }

    public void sendToPlayer(String msg) { this.socket.sendMessage(msg);}

    public void resetPlayer() {
        game = null;
    }

    public boolean isConnected() { return this.isConnected;}

}
