package main.Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by Jakub on 2017-05-27.
 */
public class ClientSocket {
    private static Socket socket;
    private static PrintWriter out;
    private static BufferedReader in;

    public boolean connect(String address,int port){
        try {
            socket = new Socket(InetAddress.getByName(address),port);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            return true;

        } catch (UnknownHostException e) {
            return false;
        } catch (IOException e) {
            return false;
        }
    }

    public static void sendMessage(String message) {
        out.println(message);
        System.out.println("Message sent to the client is "+message);
    }

    public static String receiveMessage() {
        String msg = null;
        try {
            msg = in.readLine();
            System.out.println("Message received from the server : " +msg);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return msg;
    }
}
