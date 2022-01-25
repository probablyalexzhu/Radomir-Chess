package views.pages;


import javax.swing.JLabel;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import config.UserInterface;
import config.MessageTypes;
import network.Message;
import network.ServerConnection;
import views.Window;
import views.components.ContentPanel;
import views.components.PanelButton;
import config.Page;
import config.Consts;

/**
 * [GameSetup.java]
 * 
 * @author
 * @version 1.0 Jan 24, 2022
 */
public class GameSetup extends ContentPanel implements ActionListener {

    private final int INSTRUCTION_LABEL_X = UserInterface.CONTENT_WIDTH / 2 - 140;
    private final int INSTRUCTION_LABEL_Y = UserInterface.WINDOW_HEIGHT / 2 - 110;
    private final int INSTRUCTION_LABEL_WIDTH = 280;
    private final int INSTRUCTION_LABEL_HEIGHT = 50;
    private final String INSTRUCTION_LABEL_TEXT = "Choose Lobby Type";
    private final int PUBLIC_BUTTON_Y = 340;
    private final int PRIVATE_BUTTON_Y = 430;
    private final int CREATE_ERROR_Y = INSTRUCTION_LABEL_Y + 30;
    private final String BACK_BUTTON_TEXT = "Back";

    private Window window;
    private JLabel instructionsLabel = new JLabel();
    private JLabel createErrorLabel = new JLabel("Failed to create lobby");
    private PanelButton createPublicLobbyBtn;
    private PanelButton createPrivateLobbyBtn;
    private PanelButton backButton;

    public GameSetup(Window window) {
        this.window = window;
        this.setLayout(null);

        instructionsLabel.setFont(UserInterface.orkney30);
        instructionsLabel.setText(INSTRUCTION_LABEL_TEXT);
        instructionsLabel.setForeground(UserInterface.TEXT_COLOUR);
        instructionsLabel.setBounds(INSTRUCTION_LABEL_X, INSTRUCTION_LABEL_Y, INSTRUCTION_LABEL_WIDTH, INSTRUCTION_LABEL_HEIGHT);
        this.add(instructionsLabel);

        createPublicLobbyBtn = new PanelButton(
            Consts.PUBLIC_LOBBY_STATUS,
            INSTRUCTION_LABEL_X,
            PUBLIC_BUTTON_Y
        );
        createPublicLobbyBtn.addActionListener(this);
        this.add(createPublicLobbyBtn);

        createPrivateLobbyBtn = new PanelButton(
            Consts.PRIVATE_LOBBY_STATUS,
            INSTRUCTION_LABEL_X,
            PRIVATE_BUTTON_Y
        );
        createPrivateLobbyBtn.addActionListener(this);
        this.add(createPrivateLobbyBtn);

        this.backButton = new PanelButton(
            BACK_BUTTON_TEXT,
            UserInterface.BACK_BUTTON_X,
            UserInterface.BACK_BUTTON_Y
        );
        this.backButton.addActionListener(this);
        this.add(backButton);

        // Error
        createErrorLabel.setFont(UserInterface.orkney18);
        createErrorLabel.setForeground(UserInterface.ERROR_COLOUR);
        createErrorLabel.setBounds(INSTRUCTION_LABEL_X, CREATE_ERROR_Y, INSTRUCTION_LABEL_WIDTH, INSTRUCTION_LABEL_HEIGHT);
    }

    public void displayError() {
        this.add(createErrorLabel);
        this.revalidate();
    }

    public void removeError() {
        this.remove(createErrorLabel);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == backButton) {
            removeError();
            window.changePage(Page.PLAY);

        } else if (e.getSource() == createPublicLobbyBtn) {
            Message createLobby = new Message(MessageTypes.CREATE_GAME);
            createLobby.addParam(Consts.PUBLIC_LOBBY_STATUS);
            ServerConnection.sendMessage(createLobby);

        } else if (e.getSource() == createPrivateLobbyBtn) {
            Message createLobby = new Message(MessageTypes.CREATE_GAME);
            createLobby.addParam(Consts.PRIVATE_LOBBY_STATUS);
            ServerConnection.sendMessage(createLobby);
        }
    }
}