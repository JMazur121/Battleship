package Server.Model;

import Server.Controller.Player;
import Utils.Command;
import Utils.Package;
import Utils.Point;

public class Game {
    private String gameName;
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

    public Game(String name, Player player) {
        this.gameName = name;
        this.host = player;
        hostBoard = new ServerBoard();
        guestBoard = new ServerBoard();
    }

    private void changePlayer() {
        if (currentPlayer.equals(host))
            currentPlayer = guest;
        else
            currentPlayer = host;
    }

    public synchronized boolean isGameActive() {
        return this.isGameActive;
    }

    public Player getHost() {
        return this.host;
    }

    public Player getGuest() {return this.guest;}

    public synchronized String getGameName() {return this.gameName;}

    public void setHost(Player host) {
        this.host = host;
    }

    public void setGuest(Player guest) {
        this.guest = guest;
    }

    public void reset(){
        this.hostBoard.initializeBoard();
        this.guestBoard.initializeBoard();
        this.hostShipsToPlace = 10;
        this.guestShipsToPlace = 10;
        this.hostReady = false;
        this.guestReady = false;
        this.isGameActive = false;
    }

    public boolean hasTwoPlayers() {
        boolean twoPlayers = false;
        if (this.host != null && this.guest != null) {
            twoPlayers = true;
        }
        return twoPlayers;
    }

    public Player getOpponent(Player player) {
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
        this.guest.sendToPlayer(Command.JOINED.toString() + "#" + this.getGameName());
        this.host.sendToPlayer(Command.OPPONENT_JOINED.toString());
    }

    public void startGame(Player player){
        this.isGameActive = true;
        this.guest.sendToPlayer(Command.PLACE_YOUR_SHIPS.toString());
        this.host.sendToPlayer(Command.PLACE_YOUR_SHIPS.toString());
        this.currentPlayer = player;
    }

    public void exitGame(Player player) {
        this.getOpponent(player).sendToPlayer(Command.OPPONENT_GIVE_UP.toString());
        player.sendToPlayer(Command.YOU_LOSE.toString());
        this.isGameActive = false;
        this.reset();
    }

    synchronized public void setReady(Player player){
        if(player == this.host) {
            this.hostReady = true;
            this.guest.sendToPlayer(Command.OPPONENT_IS_READY.toString());
        }
        else {
            this.guestReady = true;
            this.host.sendToPlayer(Command.OPPONENT_IS_READY.toString());
        }
        if(this.guestReady && this.hostReady){
            this.currentPlayer.sendToPlayer(Command.YOUR_TURN.toString());
            this.getOpponent(this.currentPlayer).sendToPlayer(Command.NOT_YOUR_TURN.toString());
        }
    }

    synchronized public void placeShip(Player player,int x,int y,int length,boolean orientation){
        Ship toPlace = new Ship(length,orientation);
        if(this.host == player) {
            if(this.hostBoard.placeShip(toPlace, x, y)) {//placement ok
                this.hostShipsToPlace--;
                player.sendToPlayer(Command.PLACEMENT_SUCCEED.toString());
                if(this.hostShipsToPlace == 0)
                    player.sendToPlayer(Command.ALL_SHIPS_PLACED.toString());
            }
            else{
                player.sendToPlayer(Command.PLACEMENT_FAILED.toString());
            }
        }
        else {
            if(this.guestBoard.placeShip(toPlace, x, y)){
                this.guestShipsToPlace--;
                player.sendToPlayer(Command.PLACEMENT_SUCCEED.toString());
                if(this.guestShipsToPlace == 0)
                    player.sendToPlayer(Command.ALL_SHIPS_PLACED.toString());
            }
            else{
                player.sendToPlayer(Command.PLACEMENT_FAILED.toString());
            }
        }
    }

    synchronized public void removeShip(Player p,int x, int y){
        ServerBoard board = this.getPlayerBoard(p);
        Ship removed = board.removeShip(x, y);
        if(p == this.host)
            this.hostShipsToPlace++;
        else
            this.guestShipsToPlace++;
        Package pack = new Package(Command.REMOVE_OK.toString(),Boolean.toString(removed.getOrientation()),removed.getFirstCell().getX(),removed.getFirstCell().getY(),removed.getLength());
        p.sendToPlayer(pack.toString());
    }

    public void shoot(Player shooter,int x, int y){
        Player opponent = this.getOpponent(shooter);
        ServerBoard opponentBoard = this.getPlayerBoard(opponent);
        //Server.getInstance().printLog(shooter.getUserName() + ","+opponent.getUserName());
        if(opponentBoard.checkIfMissed(x,y)){//missed
            shooter.sendToPlayer(Command.MISSED_NOT_YOUR_TURN.toString()+"#"+x+"#"+y);
            opponent.sendToPlayer(Command.OPPONENT_MISSED_YOUR_TURN.toString()+"#"+x+"#"+y);
            this.changePlayer();
        }
        else{//was hit
            if(opponentBoard.hit(x,y)){//hit and sink
                if(opponentBoard.destroyShip()){// all ships gone
                    shooter.sendToPlayer(Command.YOU_WIN.toString());
                    opponent.sendToPlayer(Command.YOU_LOSE.toString());
                    this.isGameActive = false;
                    this.reset();
                }
                else {//player has > 0 ships
                    shooter.sendToPlayer(Command.HIT_AND_SINK.toString()+"#"+x+"#"+y);
                    opponent.sendToPlayer(Command.OPPONENT_HIT_AND_SINK.toString()+"#"+x+"#"+y);
                    Ship wreck = opponentBoard.getCell(x,y).getShip();
                    Point firstCell = wreck.getFirstCell();
                    //shooter.sendToPlayer(Command.PLAYER_HINT.toString()+"#"+firstCell.getX()+"#"+firstCell.getY()+"#"+wreck.getLength()+"#"+wreck.getOrientation());
                    Package pack = new Package(Command.PLAYER_HINT.toString(),Boolean.toString(wreck.getOrientation()),firstCell.getX(),firstCell.getY(),wreck.getLength());
                    shooter.sendToPlayer(pack.toString());
                }

            }
            else {//hit, but ship stays alive
                shooter.sendToPlayer(Command.HIT_SHOOT_AGAIN.toString()+"#"+x+"#"+y);
                opponent.sendToPlayer(Command.OPPONENT_HIT.toString()+"#"+x+"#"+y);
            }
        }

    }
}

