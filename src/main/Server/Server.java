package main.Server;

import main.Utils.Command;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by Jakub on 2017-05-21.
 */
public class Server extends Thread {
    private static Server instance = null;
    private final int port = 8765;
    private ArrayList<Player> players;
    private ArrayList<Game> createdGames;
    private HashSet<String> userNames;

    public void run(){
        createdGames = new ArrayList<Game>();
        players = new ArrayList<Player>();
        userNames = new HashSet<String>();
        ServerSocket listener = null;
        try {
            System.out.println("Server is running...");
            listener = new ServerSocket(port);
            while (true){
                Player newPlayer = new Player(listener.accept());
                newPlayer.start();
                players.add(newPlayer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                listener.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized void addGame(Game game) {
        instance.createdGames.add(game);
        String info = Command.AVAILABLE_GAME.toString();
        info = info + "#" + game.getGameName();
        instance.sendBroadcast(info);
    }

    public synchronized void addName(String name) {instance.userNames.add(name);}

    public synchronized void deleteName(String name) {instance.userNames.remove(name);}

    public synchronized void deleteGame(Game game) {
        instance.createdGames.remove(game);
        String info = Command.REMOVE_GAME.toString();
        info = info + "#" + game.getGameName();
        instance.sendBroadcast(info);
    }

    public synchronized void deletePlayer(Player player){
        instance.players.remove(player);
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

    synchronized public void printLog(String log){System.out.println(log);}

    private void sendBroadcast(String message){
        for (Player p: instance.players) {
            if(p.getUserName() != null && p.isConnected())
                p.sendToPlayer(message);
        }
    }
}
