package Client.Model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientSocket {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

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

    public void sendMessage(String message) {
        out.println(message);
        System.out.println("Message sent to the client is "+message);
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
        String msg;
        msg = in.readLine();
        System.out.println("Message received from the server : " +msg);
        return msg;
    }
}
