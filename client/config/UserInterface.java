package config;

import java.awt.Color;
import java.awt.Font;
import java.awt.BasicStroke;

import javax.swing.BorderFactory;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import views.components.CustomButton;
import views.pages.AbstractGamePanel;
import views.pages.Settings;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class UserInterface {
    
    // JFrame related
    public static final String WINDOW_TITLE = "Radomir Chess";
    public static final int UPDATE_RATE = 10;

    // Subject to change, not sure if we want any full screening or anything
    public static final int WINDOW_WIDTH  = 1280;
    public static final int WINDOW_HEIGHT = 720;

    public static final int NAVBAR_WIDTH  = WINDOW_WIDTH/5;
    public static final int CONTENT_WIDTH = WINDOW_WIDTH - NAVBAR_WIDTH;
    public static final int NAVBAR_BUTTON_HEIGHT = 60;

    //Right Panel
    public static final Color FRAME_COLOUR = new Color(41, 43, 45);
    public static final Color TEXT_COLOUR = new Color(250, 250, 250);

    // Navbar
    public static final Color NAVBAR_COLOUR = new Color(26,26,27);
    public static final Color NAVBAR_BUTTON_HOVER_COLOUR = new Color(38,39,39);
    public static final Border NAVBAR_BUTTON_MARGIN = new EmptyBorder(0, 20, 0, 0);
    public static final Border NAVBAR_BUTTON_HOVER_MARGIN = new EmptyBorder(0, 30, 0, 0);

    // wtf is this lol
    public static final String GUEST = "Guest";
    public static final Font USERNAME_FONT = new Font("Arial", Font.PLAIN, 20);

    // Play page
    public static final int MENU_BUTTON_MARGIN = 40;
    public static final int MENU_BUTTON_Y_OFFSET = WINDOW_HEIGHT / 2 - 40;
    public static final int MENU_BUTTON_HEIGHT = WINDOW_HEIGHT / 4 - (int) (MENU_BUTTON_MARGIN * 1.5);
    public static final int MENU_BUTTON_WIDTH = CONTENT_WIDTH / 2 - (int) (MENU_BUTTON_MARGIN * 1.5);
    public static final Font PLAY_BUTTONS_FONT = new Font("Serif", Font.PLAIN, 40);

    // Join game page
    public static final Color JOIN_GAME_BUTTON_BACKGROUND = new Color(200, 200, 200);
    public static final Font JOIN_GAME_FONT_1 = new Font("Serif", Font.PLAIN, 40);
    public static final Font JOIN_GAME_FONT_2 = new Font("Serif", Font.PLAIN, 25);

    public static final Color MENU_BUTTON_COLOUR = new Color(47, 78, 111);
    public static final Color MENU_BUTTON_HIGHLIGHT = MENU_BUTTON_COLOUR.brighter();
    public static final int MENU_BUTTON_RADIUS = 20;

    // Settings
    public static final Font SETTINGS_FONT = new Font("Serif", Font.PLAIN, 15);
    public static final int NUM_SETTINGS = 5;

    // Game board
    public static int activeTheme = 0;
    public static final int WOOD_BOARD = 2;
    public static final Color[] LIGHTER_TILE_COLOURS = new Color[]{
        new Color(192, 192, 193),
        new Color(218, 226, 234),
        new Color(206, 158, 106),
        new Color(255, 135, 178),
        new Color(253, 164, 72),
        new Color(162, 144, 178),
        new Color(87, 164, 24),
        new Color(29, 209, 186),
        new Color(238, 238, 210),
        new Color(34, 203, 209),
        new Color(255, 243, 237)
    };
    public static final Color[] DARKER_TILE_COLOURS = new Color[]{
        new Color(64, 64, 65),
        new Color(121, 156, 178),
        new Color(124, 88, 57),
        new Color(161, 216, 224),
        new Color(252, 63, 134),
        new Color(128, 74, 145),
        new Color(25, 85, 21),
        new Color(236, 124, 158),
        new Color(118, 150, 86),
        new Color(55, 37, 135),
        new Color(95, 51, 31)
    };
    public static Color lighterTile = LIGHTER_TILE_COLOURS[activeTheme];
    public static Color darkerTile = DARKER_TILE_COLOURS[activeTheme];

    // Background
    public static int activeBackground = 0;
    public static Color[] BACKGROUNDS = new Color[]{
        FRAME_COLOUR,
        new Color(220, 220, 220),
        new Color(27, 27, 27),
        new Color(194, 205, 220),
        new Color(47, 63, 76)
    };

    // Chess Sets
    private static int activeSetNum = 0;
    public static String activePieceSet = PathsConsts.PIECE_SETS[0];
    
    // Piece/board highlights
    public static final Color[] POSSIBLE_MOVE_COLOURS = new Color[]{
        new Color(61, 89, 169, 192),
        new Color(23, 213, 77, 192),
        new Color(208, 14, 37, 192),
        new Color(255, 225, 60, 192),
        new Color(254, 92, 13, 192),
        new Color(173, 0, 234, 192),
        new Color(252, 252, 252, 192),
        new Color(2, 2, 2, 192),
        new Color(226, 185, 131, 192)
    };
    private static int activeHighlight = 0;
    public static Color activeHighlightColour = POSSIBLE_MOVE_COLOURS[0];
    public static BasicStroke HIGHLIGHT_LINE_THICKNESS = new BasicStroke(5);
    
    public static boolean highlightToggle = true;
    public static Color ON_COLOUR = new Color(7, 107, 29);
    public static Color OFF_COLOUR = new Color(131, 35, 29);


    // Borders
    public static final Border EMPTY_BORDER = new EmptyBorder(0, 0, 0, 0);

    // Game Page
    public static final int GAME_BOARD_LENGTH = 480;
    public static final int GAME_BOARD_X = 120;
    public static final int GAME_BOARD_Y = 120;
    public static final int GAME_SIDE_PANEL_WIDTH = 240;
    public static final int GAME_INFO_BORDER_RADIUS = 15;
    
    // These are all greys, maybe just make a bunch of grey constsants GREY_1, GREY_2
    public static final Color CHAT_MESSAGE_COLOUR = new Color(230, 230, 230);
    public static final Color GAME_SIDE_BORDER_COLOR = new Color(128, 128, 128);
    public static final Color GAME_SIDE_HIGHLIGHT_COLOR = new Color(77, 77, 77);
    public static final Color GAME_MOVES_HEADER_BACKGROUND = new Color(51, 51, 51);
    public static final Color GAME_CHAT_TEXTFIELD_COLOUR = new Color(179, 179, 179);

    public static final Border GAME_CHAT_MARGIN = new EmptyBorder(10, 10, 0, 0);
    public static final Border GAME_CHAT_BORDER = BorderFactory.createLineBorder(GAME_SIDE_BORDER_COLOR, 1);
    public static final Border GAME_TEXTFIELD_MARGIN  = new EmptyBorder(5, 10, 5, 10);

    public static final Font LOBBY_INFO = new Font("Serif", Font.PLAIN, 40);

    /**
     * Changes the colour/theme of the board
     * @param theme
     */
    public static void changeBoard(int theme) {
        activeTheme = theme;
        lighterTile = LIGHTER_TILE_COLOURS[theme];
        darkerTile = DARKER_TILE_COLOURS[theme];
    }

    /**
     * Changes the background colour variable and returns it
     * @param colour
     * @return Color change to
     */
    public static Color changeBackground(int colour) {
        activeBackground = colour;
        return BACKGROUNDS[colour];
    }

    /**
     * Changes the chess set
     * @param set
     */
    public static void changePieceSet(int set) {
        activeSetNum = set;
        activePieceSet = PathsConsts.PIECE_SETS[set];
    }

    /**
     * Changes highlight colour
     * @param colour
     */
    public static void changeHighlights(int colour) {
        activeHighlight = colour;
        activeHighlightColour = POSSIBLE_MOVE_COLOURS[colour];
    }

    /**
     * Turns highlight on or off
     * @param button
     */
    public static void toggleHighlight(CustomButton button) {
        highlightToggle ^= true;
        if (highlightToggle) {
            button.setText("Turn Off Highlights");
            button.setBackground(ON_COLOUR);
            button.setHoverColor(UserInterface.ON_COLOUR.brighter());
        } else {
            button.setText("Turn On Highlights");
            button.setBackground(OFF_COLOUR);
            button.setHoverColor(UserInterface.OFF_COLOUR.brighter());
        }
    }

    /**
     * returns all the current settings states
     * @return int array containing state of all settings
     */
    public static int[] getCurrentSettings() {
        int[] settings = new int[NUM_SETTINGS];
        settings[0] = activeTheme;
        settings[1] = activeBackground;
        settings[2] = activeSetNum;
        settings[3] = highlightToggle?1:0;
        settings[4] = activeHighlight;
        return settings;
    }
}