package com.sfu;

import com.sfu.Entities.Player;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.Dimension;

public class PlayerMovementTest {
    private Game game;
    private Player player;

    @BeforeEach
    public void setUp() {
        String difficulty = "Easy";
        Dimension screenSize = new Dimension(1920, 1080);
        game = Game.createGame(difficulty, screenSize);
        game.getSound().stopBackgroundMusic();
        player = Player.createPlayer(game);
    }

    @Test
    public void testPlayerMovesUp() {
        int initialY = player.getYPosition();
    
        player.getWAction().actionPerformed(null); // Use getWAction to simulate 'W' key press
        player.update(); // Update player position
    
        assertTrue(player.getYPosition() < initialY);
    }
    
    @Test
    public void testPlayerMovesLeft() {
        int initialX = player.getXPosition();
    
        player.getAAction().actionPerformed(null); // Use getAAction to simulate 'A' key press
        player.update(); // Update player position
    
        assertTrue(player.getXPosition() < initialX);
    }
    
    @Test
    public void testPlayerMovesDown() {
        int initialY = player.getYPosition();
    
        player.getSAction().actionPerformed(null); // Use getSAction to simulate 'S' key press
        player.update(); // Update player position
    
        assertTrue(player.getYPosition() > initialY);
    }
    
    @Test
    public void testPlayerMovesRight() {
        int initialX = player.getXPosition();
    
        player.getDAction().actionPerformed(null); // Use getDAction to simulate 'D' key press
        player.update(); // Update player position
    
        assertTrue(player.getXPosition() > initialX);
    }

    @Test
        public void testAWDiagonalMovement() {
        int initialX = player.getXPosition();
        int initialY = player.getYPosition();

        // Simulate pressing A (left) and W (up)
        player.getAAction().actionPerformed(null);
        player.getWAction().actionPerformed(null);
        player.update();

        // Expect X to decrease (left) and Y to decrease (up)
        assertTrue(player.getYPosition() < initialY);
        assertTrue(player.getXPosition() < initialX);
    }

    @Test
    public void testSADiagonalMovement() {
        int initialX = player.getXPosition();
        int initialY = player.getYPosition();

        // Simulate pressing S (down) and A (left)
        player.getSAction().actionPerformed(null);
        player.getAAction().actionPerformed(null);
        player.update();

        // Expect X to decrease (left) and Y to increase (down)
        assertTrue(player.getYPosition() > initialY);
        assertTrue(player.getXPosition() < initialX);
    }

    @Test
    public void testDSDiagonalMovement() {
        int initialX = player.getXPosition();
        int initialY = player.getYPosition();

        // Simulate pressing D (right) and S (down)
        player.getDAction().actionPerformed(null);
        player.getSAction().actionPerformed(null);
        player.update();

        // Expect X to increase (right) and Y to increase (down)
        assertTrue(player.getYPosition() > initialY);
        assertTrue(player.getXPosition() > initialX);
    }

    @Test
    public void testWDDiagonalMovement() {
        int initialX = player.getXPosition();
        int initialY = player.getYPosition();

        // Simulate pressing W (up) and D (right)
        player.getWAction().actionPerformed(null);
        player.getDAction().actionPerformed(null);
        player.update();

        // Expect X to increase (right) and Y to decrease (up)
        assertTrue(player.getYPosition() < initialY);
        assertTrue(player.getXPosition() > initialX);
    }

   
    
}