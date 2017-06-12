package Server.Controller;

import javafx.application.Platform;
import Server.Model.Game;
import Utils.Command;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.HashSet;

public class Server extends Thread {
    private static Server instance = null;
    private int port;
    private ArrayList<Player> players;
    private ArrayList<Game> createdGames;
    private HashSet<String> userNames;
    private ServerViewController controller;
    private ServerSocket listener;

    public void run(){
        createdGames = new ArrayList<Game>();
        players = new ArrayList<Player>();
        userNames = new HashSet<String>();
        listener = null;
        try {
            listener = new ServerSocket(port);
            Platform.runLater(()->{
                this.controller.updatePlayers(players.size());
            });
            while (true){
                Player newPlayer = new Player(listener.accept());
                newPlayer.start();
                players.add(newPlayer);
                Platform.runLater(()->{
                    this.controller.updatePlayers(players.size());
                });
            }
        } catch (IOException e) {
        } finally {
            try {
                listener.close();
            } catch (IOException e) {
            }
        }
    }

    public void kill(){
        try {
            this.listener.close();
        } catch (IOException e) {
        }
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setController(ServerViewController controller) {
        this.controller = controller;
    }

    public synchronized void addGame(Game game) {
        instance.createdGames.add(game);
        String info = Command.AVAILABLE_GAME.toString();
        info = info + "#" + game.getGameName();
        instance.sendBroadcast(info);
    }

    public synchronized void addName(String name) {
        instance.userNames.add(name);
    }

    public synchronized void deleteName(String name) {
        instance.userNames.remove(name);
    }

    public synchronized void deleteGame(Game game) {
        instance.createdGames.remove(game);
        String info = Command.REMOVE_GAME.toString();
        info = info + "#" + game.getGameName();
        instance.sendBroadcast(info);
    }

    public synchronized void deletePlayer(Player player){
        instance.players.remove(player);
        Platform.runLater(()->{
            this.controller.updatePlayers(players.size());
        });
    }

    public synchronized Game findGame(String name) {
        for (Game game: instance.createdGames) {
            if (game.getGameName().equals(name))
                return game;
        }
        return null;
    }

    public static Server getInstance() {
        if (instance == null)
            synchronized (Server.class) {
                if (instance == null)
                    instance = new Server();
            }
        return instance;
    }

    public synchronized ArrayList<Game> getGames() {
        return this.createdGames;
    }

    public synchronized boolean checkNameAvailability(String name) {
        boolean available = true;
        if(instance.userNames.contains(name))
            available = false;
        return available;
    }

    public synchronized boolean checkGameNameAvailability(String name){
        boolean available = true;
        for (Game g: this.instance.createdGames) {
            if(g.getGameName().equals(name))
                available = false;
        }
        return available;
    }

    public void sendBroadcast(String message){
        for (Player p: instance.players) {
            if(p.isConnected())
                p.sendToPlayer(message);
        }
    }
}
