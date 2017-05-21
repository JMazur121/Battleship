package main.Server;

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

    public Game(int ID, Player player, int boardSize) {
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

    public boolean isGameActive() {
        return this.isGameActive;
    }

    public Player getHost() {
        return this.host;
    }

    public int getGameID() {
        return this.gameID;
    }
}
