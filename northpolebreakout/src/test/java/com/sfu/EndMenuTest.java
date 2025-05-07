package com.sfu;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.sfu.Menus.EndMenu;
import com.sfu.Menus.Menu;

import java.awt.Component;
import static org.junit.jupiter.api.Assertions.*;
import javax.swing.*;

class EndMenuTest {

    private JFrame parentFrame;
    private EndMenu endMenu;

    @BeforeEach
    void setUp() {
        // Initialize the parent JFrame and create an instance of EndMenu
        parentFrame = new JFrame();
        parentFrame.setSize(800, 600); // Adjust as necessary
        parentFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    @Test
    void testEndMenuInitialization() {
        // Create an instance of EndMenu with arbitrary values
        endMenu = new EndMenu(parentFrame, "Easy", 100, 120.5, true);

        // Check if the dialog is not null
        assertNotNull(endMenu, "EndMenu should not be null");

        assertNotNull(findLabelInDialog(endMenu, "Game Over"), "Game Over label should be initialized");
        assertNotNull(findLabelInDialog(endMenu, "You Win!"), "Win message label should be initialized");
        assertNotNull(findLabelInDialog(endMenu, "Score: 100"), "Score label should be initialized");
        assertNotNull(findLabelInDialog(endMenu, "Time: 02:00"), "Time label should be initialized");

        // Check if buttons are present
        assertNotNull(endMenu.getPlayButton(), "Play Again button should be initialized");
        assertNotNull(endMenu.getQuitButton(), "Quit button should be initialized");
    }

    @Test
    void testWinOrLoseMessage() {
        // Test the win/loss message for a win
        endMenu = new EndMenu(parentFrame, "Easy", 100, 120.5, true);
        assertEquals("You Win!", endMenu.getResultMessage(), "Win message should be displayed");

        // Test the win/loss message for a loss
        endMenu = new EndMenu(parentFrame, "Easy", 50, 150.0, false);
        assertEquals("You Lost!", endMenu.getResultMessage(), "Loss message should be displayed");
    }

    @Test
    void testButtonActions() {
        // Simulate the Play Again button click
        endMenu = new EndMenu(parentFrame, "Easy", 100, 120.5, true);
        JButton playButton = endMenu.getPlayButton();
        playButton.doClick();
        // Check if a new game is started (e.g., checking if the content pane changes)
        assertTrue(parentFrame.getContentPane() instanceof Game, "New game should be started");
        ((Game) parentFrame.getContentPane()).getSound().stopBackgroundMusic();

        // Simulate the Quit button click
        JButton quitButton = endMenu.getQuitButton();
        quitButton.doClick();
        // Check if the menu is shown (e.g., checking if the content pane changes)
        assertTrue(parentFrame.getContentPane() instanceof Menu, "Main menu should be displayed");
    }

    private JLabel findLabelInDialog(JDialog dialog, String labelText) {
        // This method will search for a JLabel with the given text in the EndMenu dialog
        for (Component comp : dialog.getContentPane().getComponents()) {
            if (comp instanceof JLabel) {
                JLabel label = (JLabel) comp;
                if (label.getText().contains(labelText)) {
                    return label;
                }
            }
        }
        return null; // Return null if the label is not found
    }
}
