package main.Server;

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
    Socket socket;
    private Game game = null;
    private BufferedReader in;
    private PrintWriter out;
    private boolean isConnected = false;

    public Player(Socket socket) {
        this.socket = socket;
        System.out.println("Player connected...");
    }

    public void run(){
        System.out.println("Player started...");
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(),true);
            while(true){

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getUserName() {
        return userName;
    }

    public void sendToPlayer(String msg) {
        out.println(msg);
    }

    public void resetPlayer() {
        game = null;
    }

}
