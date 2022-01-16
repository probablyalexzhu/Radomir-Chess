package network;

import config.MessageTypes;
import config.Page;
import views.Window;

public class ConnectionHandler extends Thread {

    private Window window;
    private boolean isActive;

    public ConnectionHandler(Window window) {
        isActive = true;
        this.window = window;
    }

    @Override
    public void run() {
        try {
            while (isActive) {
                if (ServerConnection.hasMessage()) {
                    Message message = ServerConnection.getMessage();
                    System.out.println("Received message: " + message.getText());
                    evalMessage(message);
                }
            }

        } catch (Exception e) {
            System.out.println("Failed to receive message from server.");
            e.printStackTrace();
        }
    }

    public void evalMessage(Message message) {

        if (message == null) {
            return;

        } else if (message.getType().equals(MessageTypes.GAME_CREATED)) { 
            createGame(message);

        } else if (message.getType().equals(MessageTypes.JOINED_GAME)) {
            joinGame(message);

        } else if (message.getType().equals(MessageTypes.JOIN_ERROR)) {

        } else if (message.getType().equals(MessageTypes.GUEST_JOINED)) {
            guestJoined(message);

        } else if (message.getType().equals(MessageTypes.SENT_TEXT)) {
            addTextMessage(message);

        } else if (message.getType().equals(MessageTypes.DISPLAY_GAMES)) {
            displayGames(message);
        } else if (message.getType().equals(MessageTypes.EXIT_PROGRAM)) {
            isActive = false;
            ServerConnection.close();
        } else if (message.getType().equals(MessageTypes.LOGIN_ACCEPTED)) {
            login();

        } else if (message.getType().equals(MessageTypes.LOGIN_FAILED)) {
            window.loginPanel.displayError();
        
        } else if (message.getType().equals(MessageTypes.LOGOUT)){
            logout();
        }
    }   

    public void login(){
        window.setLoggedIn(true);;
        window.changePage(Page.PLAY);
    }

    public void logout() {
        window.setLoggedIn(false);
        window.changePage(Page.LOGIN);
    }

    public void createGame(Message message) {
        String code = message.getParam(0);
        System.out.println(code);
        
        window.gamePanel.setLobbyCode(code);
        window.gamePanel.setHost(true);
    }

    public void displayGames(Message message) {
        String games = message.getParam(0);
        window.browseGamesPanel.setLobbyList(games);
    }

    public void guestJoined(Message message) {
        int guest = Integer.parseInt(message.getParam(0));

        window.gamePanel.addOther(guest);
    }


    public void joinGame(Message message) {
        String code = message.getParam(0);
        int host = Integer.parseInt(message.getParam(1));

        window.changePage(Page.GAME);
        window.gamePanel.setLobbyCode(code);
        window.gamePanel.setHost(false);
        window.gamePanel.addOther(host);
    }

    public void addTextMessage(Message message) {
        String text = message.getParam(0);

        window.gamePanel.addTextMessageFromOther(text);
    }
}
