import java.io.IOException;

import network.ConnectionHandler;
import network.ServerConnection;
import network.ShutdownHook;
import views.Window;

public class Application {

    public static Window window;
    public static ConnectionHandler connectionHandler;
    public static void main(String[] args) throws IOException, InterruptedException {

        // Socket connecting to server
        ServerConnection.createInstance();

        // Hook that runs when server shuts down
        Runtime.getRuntime().addShutdownHook(new ShutdownHook());

        // Initialize window and server connection thread
        window = new Window();
        connectionHandler = new ConnectionHandler(window);

        // This thread listens to messages from server
        connectionHandler.start();

        while(true) {
            window.repaint();
            Thread.sleep(25);
        }
    }
}