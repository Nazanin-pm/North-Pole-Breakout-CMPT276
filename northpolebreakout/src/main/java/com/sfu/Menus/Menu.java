package com.sfu.Menus;

import javax.imageio.ImageIO;
import javax.swing.*;

import com.sfu.Game;
import com.sfu.Instructions;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

/**
 * The Menu class represents the main menu of the "North Pole Breakout" game.
 * It provides buttons to start the game, change the difficulty, and quit the game.
 * The background image is displayed, and the game can be started with a selected difficulty.
 */
public class Menu extends JPanel implements ActionListener {
    private JButton playButton;
    private JButton difficultyButton;
    private JButton quitButton;
    private BufferedImage background;

    private static Menu instance;

    private String difficultyLevel; // Declare difficultyLevel here

    /**
     * Private constructor for the Menu class. Initializes the menu's components,
     * such as buttons, labels, and the background image.
     */
    private Menu() {
        // Set the layout to BoxLayout for vertical stacking
        setUpLayout();

        loadBackgroundImage();

        initializeComponents();
       // Revalidate and repaint to ensure layout updates
        revalidate();
        repaint();
    }


    /**
     * Creates and returns the singleton instance of the Menu class.
     *
     * @return The single instance of the Menu class.
     */
    public static Menu createMenu() {
        if (instance == null) {
            instance = new Menu();
        }
        return instance;
    }

      /**
     * Set up the panel layout
     */
    private void setUpLayout(){
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    }

    /**
     * Load background image
     */
    private void loadBackgroundImage(){
        try {
            background = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Background/menuBackground.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Initialize and add components in the pannel
     */
    private void initializeComponents(){

        JLabel title = new JLabel("North Pole Breakout", SwingConstants.CENTER);
        title.setFont(new Font("Serif", Font.BOLD, 72));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Initialize buttons
        Dimension buttonSize = new Dimension(350, 80);
        playButton = createButton("Play", buttonSize, new Font("Serif", Font.PLAIN, 32));
        difficultyButton = createButton("Difficulty", buttonSize, new Font("Serif", Font.PLAIN, 32));
        quitButton = createButton("Quit", buttonSize, new Font("Serif", Font.PLAIN, 32));

        // Add title and buttons to the panel
        add(Box.createVerticalStrut(60));
        add(title);
        add(Box.createVerticalStrut(40));
        add(Box.createVerticalGlue());
        add(playButton);
        add(Box.createVerticalStrut(20));
        add(difficultyButton);
        add(Box.createVerticalStrut(20));
        add(quitButton);
        add(Box.createVerticalGlue());
    }

    /**
     * Create button with given attributes
     * @param text text of button
     * @param size size of button
     * @param font font of button
     * @return initialized button
     */
    private JButton createButton(String text, Dimension size, Font font){
        JButton button = new JButton(text);
        button.setPreferredSize(size);
        button.setMaximumSize(size);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.addActionListener(this);
        return button;
    }



    /**
     * Returns the preferred size of the menu panel.
     *
     * @return The preferred size (1280x720) of the menu panel.
     */
    @Override
    public Dimension getPreferredSize() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        return screenSize;
    }

    /**
     * Paints the background image on the menu panel.
     *
     * @param g The Graphics object used for painting.
     */
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (g == null) return; // Handle null Graphics object
        if (background != null) {
            g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
        }
    }

    /**
     * Set difficulty of game
     */
    public void setDifficulty(String difficulty){
        if(difficulty.equalsIgnoreCase("Easy") || difficulty.equalsIgnoreCase("Medium") || difficulty.equalsIgnoreCase("Hard")){
            this.difficultyLevel = difficulty;
        } else {
            this.difficultyLevel = "Medium";
        }
    }

    public String getDifficulty(){
        if(this.difficultyLevel == null){
            return "Medium";
        } 
        return this.difficultyLevel;
    }

    /**
     * 
     * @return play button
     */
    public JButton getPlayButton(){
        return this.playButton;
    }


    /**
     * Handles the action events for the buttons on the menu panel.
     * - Starts the game with the selected difficulty.
     * - Opens the difficulty selection dialog.
     * - Quits the game.
     *
     * @param e The ActionEvent that was triggered.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == playButton) {
            handlePlayAction();
        } else if (e.getSource() == difficultyButton) {
            handleDifficultyAction();
        } else if (e.getSource() == quitButton) {
            handleQuitAction();
        }
    }

    private void handlePlayAction() {
        playButton.setEnabled(false);
        if (difficultyLevel != null && !difficultyLevel.isEmpty()) {
            openInstructions(difficultyLevel);
            closeMenu();
        } else {
            setDifficultyAndOpenInstructions("Medium");
        }
    }

    private void handleDifficultyAction() {
        String difficulty = selectDifficulty();
        if (difficulty != null) {
            difficultyLevel = difficulty;
        }
    }

    private void handleQuitAction() {
        System.exit(0);
    }

    private void closeMenu() {
        instance = null;
        SwingUtilities.getWindowAncestor(this).setVisible(false);
    }

    protected String selectDifficulty(){

        String[] options = {"Easy", "Medium", "Hard"};
            String difficulty = (String) JOptionPane.showInputDialog(
                    null,
                    "Select Difficulty Level:",
                    "Difficulty",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[0]
            );

            return difficulty;

    }

    private void setDifficultyAndOpenInstructions(String difficulty) {
        this.difficultyLevel = difficulty;
        openInstructions(difficultyLevel);
        closeMenu();
    }

    /**
     * Opens the instructions screen with the selected difficulty.
     * 
     * @param difficulty The difficulty level to pass to the instructions screen.
     */
    protected void openInstructions(String difficulty) {
        JFrame instructionsWindow = new JFrame("Instructions");

        instructionsWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        instructionsWindow.setExtendedState(JFrame.MAXIMIZED_BOTH);
        instructionsWindow.setVisible(true);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        instructionsWindow.setSize(screenSize);

        // Create the Instructions panel and add it to the window
        Instructions instructions = new Instructions(difficulty);
        instructionsWindow.add(instructions);
        instructionsWindow.pack();
        instructionsWindow.setResizable(false);
    }
    
    protected void startGame(String difficulty){
        JFrame gameWindow = new JFrame("North Pole Breakout");
                
        gameWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameWindow.setExtendedState(JFrame.MAXIMIZED_BOTH);
        gameWindow.setVisible(true);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        gameWindow.setSize(screenSize);

        Game game = Game.createGame(difficulty, gameWindow.getSize());

        // Pass the difficulty to the game creation
        gameWindow.add(game);
        gameWindow.pack();
        gameWindow.setResizable(false);
        game.startGameThread();
    }

    public JButton getDifficultyButton(){
        return this.difficultyButton;
    }
}
