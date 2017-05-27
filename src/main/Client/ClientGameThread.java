package main.Client;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

/**
 * Created by Jakub on 2017-05-27.
 */
public class ClientGameThread extends Service<Void> {
    private ClientViewController viewController;
    private ClientSocket clientSocket;

    public ClientGameThread(ClientViewController controller){
        this.viewController = controller;
        clientSocket = new ClientSocket();
    }

    public boolean tryConnect(String address,int port){
        return this.clientSocket.connect(address,port);
    }

    @Override
    protected Task<Void> createTask() {
        return new Task<Void>() {
            @Override
            public Void call() throws Exception {
                while (true) {
                }
            }
        };
    }
}
