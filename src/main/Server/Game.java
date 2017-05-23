package main.Server;

import main.Utils.Command;

/**
 * Created by Jakub on 2017-05-20.
 */
public class Game {
    private int gameID;
    private Player host = null;
    private Player guest = null;
    private Player currentPlayer = null;
    private boolean isGameActive = false;
    private ServerBoard hostBoard = null;
    private ServerBoard guestBoard = null;
    private int hostShipsToPlace = 10;
    private int guestShipsToPlace = 10;
    private boolean hostReady = false;
    private boolean guestReady = false;

    public Game(int ID, Player player) {
        this.gameID = ID;
        this.host = player;
        hostBoard = new ServerBoard();
        currentPlayer = host;
        Server.getInstance().addGame(this);
    }

    public void changePlayer() {
        if (currentPlayer.equals(host))
            currentPlayer = guest;
        else
            currentPlayer = host;
    }

    public int getShipsToPlace(Player player) {
        if(player == host)
            return hostShipsToPlace;
        else
            return guestShipsToPlace;
    }

    public boolean isGameActive() {
        return this.isGameActive;
    }

    public Player getHost() {
        return this.host;
    }

    public int getGameID() {
        return this.gameID;
    }

    public void sendToBothPlayers(String message){
        this.host.sendToPlayer(message);
        this.guest.sendToPlayer(message);
    }

    private void reset(){
        this.hostBoard.initializeBoard();
        this.guestBoard.initializeBoard();
        this.hostShipsToPlace = 10;
        this.guestShipsToPlace = 10;
        this.hostReady = false;
        this.guestReady = false;
    }

    public boolean hasTwoPlayers() {
        boolean twoPlayers = false;
        if (this.host != null && this.guest != null) {
            twoPlayers = true;
        }
        return twoPlayers;
    }

    private Player getOpponent(Player player) {
        if (player == host) return guest;
        else return host;
    }

    private ServerBoard getPlayerBoard(Player player){
        if(player == host)
            return this.hostBoard;
        return this.guestBoard;
    }

    public void addPlayer(Player player) {
        this.guest = player;
        this.guest.sendToPlayer(Command.JOINED.toString());
        this.host.sendToPlayer(Command.PLACE_YOUR_SHIPS.toString());
        this.guest.sendToPlayer(Command.PLACE_YOUR_SHIPS.toString());
        this.isGameActive = true;
    }

    public void exitGame(Player player) {
        this.getOpponent(player).sendToPlayer(Command.OPPONENT_EXIT.toString());
        this.getOpponent(player).sendToPlayer(Command.YOU_WIN.toString());
        player.sendToPlayer(Command.YOU_LOSE.toString());
        this.isGameActive = false;
    }

    synchronized public void placeShip(Player player, Ship toPlace,int x,int y){
        if(this.host == player) {
            if(this.hostBoard.placeShip(toPlace, x, y)) {//placement ok
                this.hostShipsToPlace--;
                player.sendToPlayer(Command.PLACEMENT_SUCCEED.toString());
            }
            else{
                player.sendToPlayer(Command.PLACEMENT_FAILED.toString());
            }
        }
        else {
            if(this.guestBoard.placeShip(toPlace, x, y)){
                this.guestShipsToPlace--;
                player.sendToPlayer(Command.PLACEMENT_SUCCEED.toString());
            }
            else{
                player.sendToPlayer(Command.PLACEMENT_FAILED.toString());
            }
        }
    }

    public void shoot(Player shooter,int x, int y){
        Player opponent = this.getOpponent(shooter);
        ServerBoard opponentBoard = this.getPlayerBoard(opponent);

        if(opponentBoard.checkIfMissed(x,y)){//missed
            shooter.sendToPlayer(Command.MISSED.toString()+"#"+x+"#"+y);
            shooter.sendToPlayer(Command.NOT_YOUR_TURN.toString());
            opponent.sendToPlayer(Command.OPPONENT_MISSED.toString()+"#"+x+"#"+y);
            opponent.sendToPlayer(Command.YOUR_TURN.toString());
            this.changePlayer();
        }
        else{//was hit
            if(opponentBoard.hit(x,y)){//hit and sink
                shooter.sendToPlayer(Command.HIT_AND_SINK.toString()+"#"+x+"#"+y);
                opponent.sendToPlayer(Command.OPPONENT_HIT_AND_SINK.toString()+"#"+x+"#"+y);
                if(opponentBoard.destroyShip()){// all ships gone
                    shooter.sendToPlayer(Command.YOU_WIN.toString());
                    opponent.sendToPlayer(Command.YOU_LOSE.toString());
                    this.isGameActive = false;
                    this.reset();
                }
                else {//player has > 0 ships
                    shooter.sendToPlayer(Command.SHOOT_AGAIN.toString());
                }

            }
            else {//hit, but ship stays alive
                shooter.sendToPlayer(Command.HIT.toString()+"#"+x+"#"+y);
                shooter.sendToPlayer(Command.SHOOT_AGAIN.toString());
                opponent.sendToPlayer(Command.OPPONENT_HIT.toString()+"#"+x+"#"+y);
            }
        }
    }
}

