package main.Server;

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
    private HashSet<String> userNames;
    private ArrayList<Game> createdGames;
    private int gameID = 1;

    public void run(){
        createdGames = new ArrayList<Game>();
        userNames = new HashSet<String>();
        ServerSocket listener = null;
        try {
            System.out.println("Server is running...");
            listener = new ServerSocket(port);
            while (true){
                Player newPlayer = new Player(listener.accept());
                newPlayer.start();
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
    }

    public synchronized void addName(String name) {instance.userNames.add(name);}

    public synchronized void deleteGame(Game game) {
        instance.createdGames.remove(game);
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

    public synchronized int getNextGameID() {
        gameID++;
        return gameID - 1;
    }

    private void sendBroadcast(String message){
        for (Game game: instance.createdGames) {
            game.sendToBothPlayers(message);
        }
    }
}
