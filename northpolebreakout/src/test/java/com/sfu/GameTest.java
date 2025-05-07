package com.sfu;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.sfu.Entities.Player;
import com.sfu.Items.Reward;
import com.sfu.Tiles.EndTile;
import com.sfu.Tiles.Tile;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.*;
import java.awt.image.BufferedImage;

public class GameTest { 
    private Game game;
    Dimension screenSize;

    @BeforeEach
    public void setUp() {
        String difficulty = "Easy";
        screenSize = new Dimension(1920, 1080);
        game = Game.createGame(difficulty, screenSize);
        game.getSound().stopBackgroundMusic();
        game.startGameThread();
    }

    @AfterEach
    public void tearDown() {
    if (Game.instance != null) {
        Game.instance.stop();
        Game.instance = null;
    }
    }

    @Test
    public void testGameInitialization() {
        assertNotNull(game);
        assertNotNull(game.player);  // Ensure player is created
        assertNotNull(game.entities);  // Ensure entities are created
    }

    @Test
    public void testBackgroundLoading() {
        assertNotNull(game.background);  // Check that the background image is loaded correctly
    }

    @Test
    public void testTileSize() {
        assertEquals(96, game.getTileSize());  // Ensure tile size is set correctly based on scaling
    }

    @Test
    void testStopBackgroundMusic() {
        Sounds sound = game.getSound();
        assertDoesNotThrow(() -> sound.stopBackgroundMusic(), "Stopping background music should handle null cases gracefully.");
    }

    @Test
    void testStopBackgroundMusicWhenPlaying() {
        Sounds sound = game.getSound();
        sound.playBackgroundMusic();
        assertDoesNotThrow(() -> sound.stopBackgroundMusic(), "Stopping background music should handle null cases gracefully.");
    }

    @Test
    void testEnemyInitialization() {
        assertNotNull(game.entities);
        assertFalse(game.entities.isEmpty());
    }

    @Test
    public void testGetColumns() {
        int expectedColumns = game.getBoard().getColumns(); // Assuming the Board initializes columns
        assertEquals(expectedColumns, game.getColumns(), "Game columns should match Board columns.");
    }

    @Test
    public void testGetRows() {
        int expectedRows = game.getBoard().getRows(); // Assuming the Board initializes rows
        assertEquals(expectedRows, game.getRows(), "Game rows should match Board rows.");
    }

    @Test
    public void testGetPlayerScreenYPosition() {
        Player player = game.getPlayer();
        int expectedScreenYPosition = player.getScreenYPosition();
        int actualScreenYPosition = game.getPlayerScreenYPosition();

        assertEquals(expectedScreenYPosition, actualScreenYPosition, 
            "Player's screen Y position should match the game-calculated value.");
    }

    @Test
    public void testGetPlayerScreenXPosition() {
        Player player = game.getPlayer();
        int expectedScreenXPosition = player.getScreenXPosition();
        int actualScreenXPosition = game.getPlayerScreenXPosition();

        assertEquals(expectedScreenXPosition, actualScreenXPosition, 
            "Player's screen X position should match the game-calculated value.");
    }

    @Test
    public void testGetPlayerPosition() {
        Player player = game.getPlayer();
        Point expectedPosition = new Point(player.getXPosition(), player.getYPosition());
        Point actualPosition = game.getPlayerPosition();

        assertEquals(expectedPosition, actualPosition, 
            "Player's position should match the point returned by Game.getPlayerPosition().");
    }

    @Test
    public void testGetPlayer() {
        Player player = game.getPlayer();
        assertNotNull(player, "Player instance should not be null.");
        assertTrue(player instanceof Player, "The object returned should be of type Player.");
    }

    @Test
    public void testGetScale() {
        int expectedScale = 6;
        int actualScale = game.getScale();
        assertEquals(expectedScale, actualScale, "The scale value should match the expected value.");
    }

    @Test
    void testRenderComponents() {
        BufferedImage testImage = new BufferedImage(game.getScreenWidth(), game.getScreenHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = testImage.createGraphics();

        game.paintComponent(g2d);

        assertNotNull(testImage);
        g2d.dispose();
    }

    @Test
    void testGameEnd() {
        game.startGameThread();
        
        game.endGame(false);

        assertNull(Game.instance);

    }

    @Test
    void testDifferentDifficulties(){
        game = Game.createGame("Medium", screenSize);
        assertEquals(1.0, game.getDifficultySpeedAdjustment());
        assertEquals(8, game.enemies.size());
        game.getSound().stopBackgroundMusic();

        game = Game.createGame("Hard", screenSize);
        assertEquals(1.2, game.getDifficultySpeedAdjustment());
        assertEquals(10, game.enemies.size());
        game.getSound().stopBackgroundMusic();

        game = Game.createGame("notEasy", screenSize);
        assertEquals(0.8, game.getDifficultySpeedAdjustment());
        assertEquals(6, game.enemies.size());
        game.getSound().stopBackgroundMusic();
    }

    @Test
    void testShowExitMessage(){
        Reward reward = new Reward(5, new Rectangle(11*4,10*4,13*4,14*4), null);
        Tile[][] board =  game.getBoard().getTemplate();
        Tile playerTile = board[game.player.getYPosition() / game.getTileSize()][game.player.getXPosition() / game.getTileSize()];
        game.startGameThread();
        assertFalse(game.exitMessageShown);
        game.getBoard().setTotalPresents(1);
        playerTile.addItem(reward);
        game.update();

        BufferedImage testImage = new BufferedImage(game.getScreenWidth(), game.getScreenHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = testImage.createGraphics();
        game.showExitMessage(g2d);
        game.setTimer(1);
        assertTrue(game.exitMessageShown);
        game.setTimer(5);

    }

    @Test
    void testWin(){

            Reward reward = new Reward(5, new Rectangle(11 * 4, 10 * 4, 13 * 4, 14 * 4), null);
            Tile[][] board = game.getBoard().getTemplate();
            Tile playerTile = board[game.player.getYPosition() / game.getTileSize()][game.player.getXPosition() / game.getTileSize()];
            
        
            game.getBoard().setTotalPresents(1);
            playerTile.addItem(reward);
            

            game.update();
        
            assertTrue(game.exitMessageShown);
        
            playerTile.leave(game.player);
            Tile endTile = new EndTile(game.player.getXPosition(), game.player.getYPosition(), game.getTileSize());
            board[game.player.getYPosition() / game.getTileSize()][game.player.getXPosition() / game.getTileSize()] = endTile;
    endTile.occupy(game.player);
            endTile.occupy(game.player);
        
            game.update();
            Tile occupiedTile = board[game.player.getYPosition() / game.getTileSize()][game.player.getXPosition() / game.getTileSize()];
            assertTrue(occupiedTile instanceof EndTile, "Player should be on the EndTile.");

          
        }
        
    
}
