package main.Server;

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
                String received = in.readLine();
                String tmp[] = received.split("#");
                String command = tmp[0];

                if(command.equals("LOGIN")){
                    if(Server.getInstance().checkNameAvailability(tmp[1])){
                        userName = tmp[1];
                        out.println(Command.LOGIN_SUCCEED.toString());
                        isConnected = true;
                    }
                    else {
                        out.println(Command.NAME_NOT_AVAILABLE.toString());
                    }
                }

                else if(command.equals("CREATE_GAME")){
                    resetPlayer();
                    game = new Game(Server.getInstance().getNextGameID(),this);
                    out.println(Command.WAIT_FOR_OPPONENT.toString());
                }

                else if(command.equals("JOIN_TO_GAME")){
                    resetPlayer();
                    int index = Integer.parseInt(tmp[1]);
                    Game toJoin = Server.getInstance().findGame(index);
                    if(toJoin != null){
                        toJoin.addPlayer(this);
                        this.game = toJoin;
                    }
                    else {
                        out.println(Command.JOIN_TO_GAME_FAILED.toString());
                    }
                }

                else if(command.equals("PLACE_A_SHIP")){
                    boolean vertical = Boolean.parseBoolean(tmp[1]);
                    int x = Integer.parseInt(tmp[2]);
                    int y = Integer.parseInt(tmp[3]);
                    int length = Integer.parseInt(tmp[4]);
                    this.game.placeShip(this,new Ship(length,vertical),x,y);
                }

                else if(command.equals("SHOOT")){
                    int x = Integer.parseInt(tmp[1]);
                    int y = Integer.parseInt(tmp[2]);
                    this.game.shoot(this,x,y);
                }

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
