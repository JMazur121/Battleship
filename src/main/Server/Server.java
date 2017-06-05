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
    private int gameID = 1;

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
        info = info + "#" + game.getInfo();
        instance.sendBroadcast(info);
    }

    public synchronized void addName(String name) {instance.userNames.add(name);}

    public synchronized void deleteGame(Game game) {
        instance.createdGames.remove(game);
        String info = Command.REMOVE_GAME.toString();
        info = info + "#" + game.getInfo();
        instance.sendBroadcast(info);
    }

    public synchronized Game findGame(int gameID) {
        int index = -1;
        for (Game game: instance.createdGames) {
            if (game.getGameID() == gameID)
                index = instance.createdGames.indexOf(game);
        }
        if(index == -1)
            return null;
        return instance.createdGames.get(index);
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

    synchronized public void printLog(String log){System.out.println(log);}

    public synchronized int getNextGameID() {
        gameID++;
        return gameID - 1;
    }

    private void sendBroadcast(String message){
        for (Player p: instance.players) {
            if(p.getUserName() != null)
                p.sendToPlayer(message);
        }
    }
}
