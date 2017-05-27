package main.Client;

import javafx.application.Platform;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import main.Utils.Command;

/**
 * Created by Jakub on 2017-05-27.
 */
public class ClientGameThread extends Thread {
    public ClientViewController viewController;
    private ClientSocket clientSocket;
    private boolean isConnected = false;

    public ClientGameThread() {
        clientSocket = new ClientSocket();
    }

    public void setViewController(ClientViewController viewController) {
        this.viewController = viewController;
    }

    public boolean tryConnect(String address, int port) {
        isConnected = this.clientSocket.connect(address, port);
        return isConnected;
    }

    public ClientSocket getClientSocket() {
        return this.clientSocket;
    }

    @Override
    public void run() {
        while (true) {
            if (Thread.currentThread().isInterrupted())
                break;
            String received = clientSocket.receiveMessage();
            if (received != null) {
                String tmp[] = received.split("#");
                String command = tmp[0];

                if(command.equals(Command.LOGIN_SUCCEED.toString())){
                    Platform.runLater(() -> {
                        viewController.setInfoColor(Color.GREENYELLOW);
                        viewController.putInfo("Udało się poprawnie zalogować");
                        viewController.loginDisable();
                    });
                }

                else if (command.equals(Command.NAME_NOT_AVAILABLE.toString())){
                    Platform.runLater(() ->{
                        viewController.setInfoColor(Color.INDIANRED);
                        viewController.putInfo("To imię jest już zajęte, spróbuj wybrać inne");
                    });
                }
            }
        }
    }
}
