package com.sfu;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

/**
 * The Instructions class represents an instruction screen with a background image.
 * Users can click anywhere on the screen to start the game.
 */
public class Instructions extends JPanel {

    private BufferedImage background;
    private final String difficultyLevel;
    private final int screenWidth = 1920;
    private final int screenHeight = 1080;

    /**
     * Constructor to initialize the instructions screen.
     */
    public Instructions(String difficulty) {
        this.difficultyLevel = difficulty;

        try {
            background = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Background/InstructionsPage.png")));
        } catch (IOException e) {
            System.err.println("Error loading instruction background image: " + e.getMessage());
            background = null;
        }

        // Add mouse listener to detect clicks
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Start the game when the screen is clicked
                startGame(difficultyLevel);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setCursor(Cursor.getDefaultCursor());
            }
        });

        revalidate();
        repaint();
    }

 /**
     * Sets the preferred size to match the game screen size.
     *
     * @return The preferred size of the instructions screen.
     */
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(screenWidth, screenHeight);
    }

    /**
     * Paints the background image on the instructions panel.
     *
     * @param g The Graphics object used for painting.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Paint the background image to fit the screen size
        if (background != null) {
            g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
        }
    }

    /**
     * Starts the game with the specified difficulty.
     *
     * @param difficulty The difficulty level of the game.
     */
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
}
