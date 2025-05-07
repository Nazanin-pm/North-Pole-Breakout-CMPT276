package com.sfu.Menus;

import javax.imageio.ImageIO;
import javax.swing.*;

import com.sfu.Game;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

/**
 * EndMenu class represents a dialog that appears when the game is over,
 * providing options to play again or exit to the main menu.
 */
public class EndMenu extends JDialog implements ActionListener {
    
    private JButton playButton;
    private JButton quitButton;
    private BufferedImage background;
    
    private String resultMessage; // Win/Loss message

    private GameResult result;


    /**
     * Constructs the EndMenu dialog with options to replay or quit.
     *
     * @param parent The parent JFrame of this dialog.
     * @param difficulty The difficulty level selected by the player.
     * @param score The final score of the player.
     * @param time The total time the player spent in the game.
     * @param playerWon Boolean flag indicating if the player won or lost.
     */
    public EndMenu(JFrame parent, String difficulty, int score, double time, boolean playerWon) {
        super(parent, "Game Over", true);
        this.result = new GameResult(difficulty, score, time);
        // Set the win/loss message based on playerWon flag
        winOrLose(playerWon);

        // Set the layout to BoxLayout for vertical stacking
        setPreferredSize(getPreferredSize());

        readBackground();
        setUpLayout();
        initializeComponents();
        // Revalidate and repaint to ensure layout updates
        pack();
        setLocationRelativeTo(parent);
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
     * Set up the panel layout
     */
    private void setUpLayout(){
        BackgroundPanel back = new BackgroundPanel();
        back.setLayout(new BoxLayout(back, BoxLayout.Y_AXIS));
        setContentPane(back);
        
    }

    private void initializeComponents(){

        JLabel gameOver = createLabel("Game Over", 60, Component.CENTER_ALIGNMENT, Color.BLACK);
        JLabel resultLabel = createLabel(resultMessage, 50, Component.CENTER_ALIGNMENT, Color.WHITE);
        JLabel scoreLabel = createLabel("Score: " + result.getScore(), 30, Component.CENTER_ALIGNMENT, Color.WHITE);
        

        // Convert time to minutes and seconds
        int minutes = (int) result.getTime() / 60;
        int seconds = (int) result.getTime() % 60;
        String timeFormatted = String.format("%02d:%02d", minutes, seconds); // Format time

        JLabel timeLabel = createLabel("Time: " + timeFormatted, 30, Component.CENTER_ALIGNMENT, Color.BLACK);


      // Initialize buttons
        Dimension buttonSize = new Dimension(250, 70);
        playButton = createButton("Play Agian", buttonSize, new Font("Serif", Font.PLAIN, 24));
        quitButton = createButton("Exit to Main Menu", buttonSize, new Font("Serif", Font.PLAIN, 24));
        

        addComponentsToPanel(gameOver, resultLabel, scoreLabel, timeLabel, playButton, quitButton);
    }

    private void addComponentsToPanel(JComponent... components){

        JPanel panel = (JPanel) getContentPane();
        for (JComponent comp : components) {
        panel.add(comp);
        panel.add(Box.createVerticalStrut(20)); // Adjust spacing as needed
    }

    }

    private JLabel createLabel(String text, int fontSize, float alignment, Color color){

        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(new Font("Serif", Font.BOLD, fontSize));
        label.setAlignmentX(alignment);
        label.setForeground(color);
        return label;
        
    }

    /**
     * Returns the preferred size of the EndMenu dialog.
     *
     * @return Dimension object representing the preferred size.
     */
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(1024, 600); // Desired menu size here
    }



    private void winOrLose(boolean outcome){
        if (outcome) {
            resultMessage = "You Win!";
        } else {
            resultMessage = "You Lost!";
        }
    }

    private void readBackground(){
        try {
            background = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Background/endBackground.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Handles button actions for replaying or exiting to the main menu.
     *
     * @param e The ActionEvent triggered by button press.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == playButton) {
            // Start the game with the selected difficulty
            JFrame mainFrame = (JFrame) getParent();
            Game newGame = Game.createGame(result.getDifficultyLevel(), mainFrame.getSize()); // Pass the difficulty level here
            mainFrame.setContentPane(newGame);
            mainFrame.revalidate(); // Refresh the main frame
            newGame.startGameThread(); // Start the game thread
            dispose();
        } else if (e.getSource() == quitButton) {
            // Exit to main menu
            JFrame mainframe = (JFrame) getParent();
            Menu menu = Menu.createMenu();
            mainframe.setContentPane(menu);
            mainframe.revalidate();
            dispose();
        }
    }

    /**
     * BackgroundPanel class is responsible for displaying the background image of the EndMenu.
     */
    private class BackgroundPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (background != null) {
                g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
            }
        }
    }

    // Getter for playButton
    public JButton getPlayButton() {
        return playButton;
    }

    // Getter for quitButton
    public JButton getQuitButton() {
        return quitButton;
    }
    // Getter for resultMessage
    public String getResultMessage() {
        return resultMessage;
    }

}
