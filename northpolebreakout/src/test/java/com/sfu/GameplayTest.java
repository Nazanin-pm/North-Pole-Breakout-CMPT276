package com.sfu;

import com.sfu.Entities.Elf;
import com.sfu.Entities.Enemy;
import com.sfu.Tiles.EndTile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.*;

public class GameplayTest {
    private Game game;
    private int tileSize;

    @BeforeEach
    public void setUp() {
        String difficulty = "Test";
        Dimension screenSize = new Dimension(1920, 1080);
        game = Game.createGame(difficulty, screenSize);
        game.getSound().stopBackgroundMusic();

        tileSize = game.getTileSize();
    }

    @Test
    void testBoardDimensions() {
        assertEquals(4, game.getRows());
        assertEquals(8, game.getColumns());
    }

    @Test
    void testPlayerSpawnOnWall() {
        assertFalse(game.player.validateSpawn(0,0));
    }

    @Test
    void testPlayerSpawnOnGround() {
        assertTrue(game.player.validateSpawn(96,96));
    }

    @Test
    void testCollectItem() {
        int tileSize = game.getTileSize();
        assertTrue(game.player.validateSpawn(3 * tileSize, tileSize));
        game.update();
        assertEquals(5, game.player.getPoints());
    }

    @Test
    void testCollectBomb() {
        assertTrue(game.player.validateSpawn(3 * tileSize, tileSize));
        game.update();
        assertTrue(game.player.validateSpawn(4 * tileSize, 2 * tileSize));
        game.update();
        assertEquals(0, game.player.getPoints());
    }

    @Test
    void testNegativePointsEndGame() {
        assertTrue(game.player.validateSpawn(4 * tileSize, 2 * tileSize));
        game.update();
        assertNull(Game.instance);
    }

    @Test
    void testEnemySpawnOnWall() {
        Enemy enemy = new Elf(game, 0, 0);
        game.addEnemy(enemy);
        assertFalse(enemy.validateSpawn(7 * game.getTileSize(), 3 * game.getTileSize()));
    }

    @Test
    void testEnemySpawnOnGround() {
        Enemy enemy = new Elf(game, 0, 0);
        game.addEnemy(enemy);
        assertTrue(enemy.validateSpawn(3 * game.getTileSize(), 2 * game.getTileSize()));
    }

    @Test
    void testEnemySpawnOnEnemy() {
        Enemy enemy = new Elf(game, 0, 0);
        game.addEnemy(enemy);
        assertTrue(enemy.validateSpawn(3 * tileSize, 2 * tileSize));

        Enemy enemy2 = new Elf(game, 0, 0);
        game.addEnemy(enemy2);
        assertFalse(enemy2.validateSpawn(3 * tileSize, 2 * tileSize));

    }

    @Test
    void testPlayerSpawnOnEnemy() {
        Enemy enemy = new Elf(game, 0, 0);
        game.addEnemy(enemy);
        assertTrue(enemy.validateSpawn(3 * tileSize, 2 * tileSize));

        assertTrue(game.getPlayer().validateSpawn(3 * tileSize, 2 * tileSize));
        game.update(); // player enemy collision ends the game
        assertNull(Game.instance);
    }

    @Test
    void testCollectedAllPresents() {
        testCollectItem();

        game.getPlayer().winCondition(new EndTile(1,1, 1));

        assertNull(Game.instance);
    }

    @Test
    void testWinConditionFalse() {
        game.getPlayer().winCondition(new EndTile(1,1, 1));
        assertNotNull(Game.instance);
    }

    @Test
    void testPlayerGroundMovement() {
        assertTrue(game.getPlayer().validateSpawn(tileSize, tileSize));
        game.getPlayer().getSAction().actionPerformed(null);
        game.update();
        int yPos = game.getPlayer().getYPosition();
        assertTrue(yPos != 96);
    }

    @Test
    void testPlayerWallMovement() {
        assertTrue(game.getPlayer().validateSpawn(4 * tileSize, 2 * tileSize));
        game.getPlayer().getSAction().actionPerformed(null);
        game.update();
        assertEquals(2 * tileSize, game.getPlayer().getYPosition());
    }
}
