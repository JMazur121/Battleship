package main.Server;

/**
 * Created by Jakub on 2017-05-21.
 */
public class Main {
    public static void main(String[] args) {
        Server server = Server.getInstance();
        server.start();
    }
}
