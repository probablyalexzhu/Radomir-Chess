package server;

import config.MessageTypes;

public class Lobby {
    
    private ClientHandler host, guest;
    private int code;
    private int hostColour;
    private String lobbyVisibility;

    private boolean joinable;


    public Lobby(ClientHandler host, int code) {
        this.code = code;
        this.host = host;
        this.hostColour = (int)(Math.random() * 2);
        this.lobbyVisibility = "public";
        this.joinable = true;
    }

    public int getCode() {
        return this.code;
    }

    public boolean isPublic() {
        if (this.lobbyVisibility.equals("Public")) {
            return true;
        }
        return false;
    }

    public String getLobbyVisibility() {
        return this.lobbyVisibility;
    }
    
    public void setPublicStatus(String lobbyVisibility) {
        this.lobbyVisibility = lobbyVisibility;
    }
    
    public String getHostName() {
        if (this.host == null) return null;
        return this.host.getClientName();
    }

    public String getGuestName() {
        if (this.guest == null) return null;
        return this.guest.getClientName();
    }

    public void setHost(ClientHandler host) {
        this.host = host;
    }

    public void setHostColour(int colour) {
        this.hostColour = colour;
    }

    public boolean setGuest(ClientHandler guest) {

        if (this.guest != null || !joinable) {
            return false;
        }

        if (host == null) {
            setHost(guest);

        } else {
            this.guest = guest;

            // Alert host that a guest has joined
            Message message = new Message(MessageTypes.GUEST_JOINED);
            message.addParam(getGuestName());
            host.sendMessage(message);
        }
        return true;
    }
    

    public ClientHandler getHost() {
        return this.host;
    }

    public ClientHandler getGuest() {
        return this.guest;
    }

    public boolean isJoinable() {
        return this.joinable;
    }

    public void setJoinable(boolean joinable) {
        this.joinable = joinable;
    }

    public int getHostColour() {
        return this.hostColour;
    }

    public int getGuestColour() {
        return (this.hostColour + 1) % 2; 
    }

    public void leaveLobby(ClientHandler client) {
        if (client != this.guest) {
            this.host = this.guest;
            this.hostColour = (hostColour + 1) % 2;
        }

        this.guest = null;

        // Send host message
        if (this.host != null) {
            Message messageToPlayerRemaining = new Message(MessageTypes.OPPONENT_LEFT);
            this.host.sendMessage(messageToPlayerRemaining);
        }
        Message messageToLeaver = new Message(MessageTypes.LEFT_SUCCESFULLY);
        client.sendMessage(messageToLeaver);
    } 

    public void sendMessage(ClientHandler from, Message message) {
        
        ClientHandler receiver;

        if (from == host) {
            receiver = guest;
        } else {
            receiver = host;
        }

        if (receiver == null) {
            return;
        }

        receiver.sendMessage(message);
    }
}