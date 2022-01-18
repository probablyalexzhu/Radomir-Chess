package views.pages;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JTextField;

import config.MessageTypes;
import config.UserInterface;
import network.Message;
import network.ServerConnection;
import views.components.ContentPanel;

public class JoinGame extends ContentPanel implements ActionListener {
    
    // Constants
    private final JLabel joinGameLabel= new JLabel();
    private final JTextField joinGameField = new JTextField();

    public JoinGame() {
        joinGameLabel.setFont(UserInterface.JOIN_GAME_FONT_1);
        joinGameLabel.setText("Room Code: ");
        joinGameLabel.setForeground(UserInterface.TEXT_COLOUR);
        joinGameLabel.setBounds(UserInterface.CONTENT_WIDTH / 2 - 100, UserInterface.WINDOW_HEIGHT / 2 - 70, 210, 30);
        this.add(joinGameLabel);

        joinGameField.setFont(UserInterface.JOIN_GAME_FONT_2);
        joinGameField.setBounds(UserInterface.CONTENT_WIDTH / 2 - 100, UserInterface.WINDOW_HEIGHT / 2 - 15, 200, 30);
        joinGameField.addActionListener(this);
        this.add(joinGameField);
    }

    //@Override
    public void actionPerformed(ActionEvent e) {
        String joinGameCode = joinGameField.getText();

        //Validates code
        if (joinGameCode.length() != 4) {
            return;
        }
        for (int i = 0; i < 4; i++) {
            if (!Character.isDigit(joinGameCode.charAt(i))) {
                return;
            }
        }

        try{
            Message m = new Message(MessageTypes.JOIN_GAME);
            m.addParam(joinGameCode);
            ServerConnection.sendMessage(m);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}