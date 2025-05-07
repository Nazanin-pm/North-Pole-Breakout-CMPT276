package com.sfu;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.Dimension;
import java.awt.Rectangle;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.sfu.Entities.Player;
import com.sfu.Entities.Elf;
import com.sfu.Entities.Enemy;
import com.sfu.Items.*;
import com.sfu.Tiles.Tile;

public class PlayerCollisionTest {

    private Game game;
    private Player player;
    Tile board[][];
    Tile playerTile;

    @BeforeEach
    public void setUp() {
        String difficulty = "Easy";
        Dimension screenSize = new Dimension(1920, 1080);
        game = Game.createGame(difficulty, screenSize);
        game.getSound().stopBackgroundMusic();
        player = Player.createPlayer(game);

        Tile[][] board = game.getBoard().getTemplate();

        playerTile = board[player.getYPosition() / game.getTileSize()][player.getXPosition() / game.getTileSize()];
    }

    @Test
    public void collisionWithRewardTest(){
        
        Reward reward = new Reward(5, new Rectangle(11*4,10*4,13*4,14*4), null);

        playerTile.addItem(reward);
        player.update();
        
        int collected = player.getRewardsCollected();

        assertEquals(5, player.getPoints());
        assertEquals(1, collected);
        assertTrue(playerTile.getItem() == null);
        assertEquals(1, player.getRewardsCollected());

    }

    @Test
    void testCollisionWithEnemy(){

            Enemy elf = new Elf(game, 1);
            elf.setXPos(player.getXPosition());
            elf.setXPos(player.getXPosition());
            boolean result = player.collisionWith(elf);
            

            assertFalse(result);
            assertEquals(false, game.isRunning());

    }

    @Test
    void testCollisionWithBomb(){

        Reward reward = new Reward(5, new Rectangle(11*4,10*4,13*4,14*4), null);
        Punishment bomb = new Punishment(-5, new Rectangle(11*4,10*4,13*4,14*4), null);


        playerTile.addItem(reward);
        player.update();
        playerTile.addItem(bomb);
        player.update();

        assertEquals(0, player.getPoints());

        playerTile.addItem(bomb);

        assertEquals(false, game.isRunning());

    }


    @Test
    void testResetPlayer(){

        Reward reward = new Reward(5, new Rectangle(11*4,10*4,13*4,14*4), null);
        playerTile.addItem(reward);
        player.update();
        assertEquals(5, player.getPoints());

        player.resetPlayer();

        assertEquals(0, player.getPoints());
        assertEquals(0, player.getRewardsCollected());

        
    }

    @Test
    void collisionWithRudolph(){
        
    }



    

    
}
