package views.components;

import java.awt.Color;

import javax.swing.border.EmptyBorder;

import config.UserInterface;

public class PanelButton extends CustomButton {
    public PanelButton (String text, int x, int y) {
        super(text);
        //change
        this.setBounds(x, y, 280, UserInterface.MENU_BUTTON_HEIGHT / 2);

        this.setForeground(Color.WHITE);
        this.setBackground(UserInterface.MENU_BUTTON_COLOUR);
        this.setHoverColor(UserInterface.MENU_BUTTON_HIGHLIGHT);
        this.setPressedColor(UserInterface.MENU_BUTTON_HIGHLIGHT.brighter());

        this.setRound(true);
        this.setBorderRadius(UserInterface.MENU_BUTTON_RADIUS);
        this.setBorder(new EmptyBorder(0,0,0,0));
    }
}