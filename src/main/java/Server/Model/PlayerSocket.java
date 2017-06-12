package Server.Model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class PlayerSocket {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    public PlayerSocket(Socket socket) {
        this.socket = socket;
    }

    public void sendMessage(String message) {
        this.out.println(message);
    }

    public void close(){
        try {
            socket.close();
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        out.close();
    }

    public String receiveMessage() throws IOException {
        String msg = null;
        msg = in.readLine();
        return msg;
    }

    public void connect() throws IOException {
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
    }
}
