package views.components;

import java.awt.Color;

import javax.swing.border.EmptyBorder;

import config.UserInterface;

public class PlayMenuButton extends CustomButton {

    private final EmptyBorder BUTTON_MARGIN = new EmptyBorder(15, 0, 0, 0);

    public PlayMenuButton (String text, int x, int y) {
        super(text);
        this.setBounds(x, y, UserInterface.MENU_BUTTON_WIDTH, UserInterface.MENU_BUTTON_HEIGHT);

        this.setForeground(Color.WHITE);
        this.setBackground(UserInterface.MENU_BUTTON_COLOUR);
        this.setHoverColor(UserInterface.MENU_BUTTON_HIGHLIGHT);
        this.setPressedColor(UserInterface.MENU_BUTTON_HIGHLIGHT.brighter());

        this.setRound(true);
        this.setBorderRadius(UserInterface.MENU_BUTTON_RADIUS);
        this.setBorder(BUTTON_MARGIN);

        this.setFont(UserInterface.orkney36);
    }
}