package com.sfu;


import com.sfu.Entities.Elf;
import com.sfu.Entities.Enemy;
import com.sfu.Entities.Rudolph;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;



import static org.junit.jupiter.api.Assertions.*;

import java.awt.Dimension;

public class EnemyTest {
    private Game game;
    private Enemy elf;

    @BeforeEach
    public void setUp() {
        String difficulty = "Easy";
        Dimension screenSize = new Dimension(1920, 1080);
        game = Game.createGame(difficulty, screenSize);
        game.getSound().stopBackgroundMusic();
        elf = new Elf(game, 1);
    }

    @Test
    void testUpdatePosition(){
        int playerX = elf.getXPosition() + 50
            , playerY = elf.getYPosition() + 50;


        elf.updatePosition(playerX, playerY);
        assertTrue(elf.getXPosition() > 200 || elf.getYPosition() > 200); //Moving towards player

    }

    @Test
    void testMovementOutOfRange(){

        int playerX = elf.getXPosition() + (game.getTileSize() * 10);
        int playerY = elf.getYPosition() + (game.getTileSize() * 10);

        elf.updatePosition(playerX, playerY);
        assertNotEquals(elf.getXPosition(), 200);

    }

    @Test
    void testCollsionWithPlayerEndGame(){

        game.startGameThread();
        assertTrue(game.isRunning());
        elf.setXPos(game.player.getXPosition());
        elf.setYPos(game.player.getYPosition());

        assertFalse(elf.collisionWith(game.player));
        assertFalse(game.isRunning());
    }


    @Test
    void testEnemyDirectionChange(){

        int playerX = elf.getXPosition() + (game.getTileSize()*4);
        int playerY = elf.getYPosition() + (game.getTileSize()*4);

        int framesToSimulate = game.getFPS() * 4;
        
        for (int i = 0; i < framesToSimulate; i++) {
            elf.updatePosition(playerX, playerY);
        }

        assertNotNull(elf.getFacing());
        assertEquals(0, elf.getFramesOutOfRange());


    }

    @Test
    void testOutofPlayerRange(){

        int playerX = elf.getXPosition() + (game.getTileSize()*40);
        int playerY = elf.getYPosition() + (game.getTileSize()*40);

        int framesToSimulate = game.getFPS() * 2;

        
        
        for (int i = 0; i < framesToSimulate; i++) {
            elf.updatePosition(playerX, playerY);
        }

        assertEquals(240, elf.getFramesOutOfRange());
        assertNotNull(elf.getFacing());

        //to make sure all cases are covered
        for (int i = 0; i < framesToSimulate*60; i++) {
            elf.updatePosition(playerX, playerY);
        }

        assertEquals(240, elf.getFramesOutOfRange());

    }

    @Test
    void testEntityMethods(){

        int playerX = elf.getXPosition() + (game.getTileSize()*40);
        int playerY = elf.getYPosition() + (game.getTileSize()*40);

        int framesToSimulate = game.getFPS() * 60;

        for (int i = 0; i < framesToSimulate; i++) {
            elf.updatePosition(playerX, playerY);
            elf.determineSprite();
        }

        assertNotNull(elf.getFacing());
        elf.setFacing(Direction.Down);
        assertEquals(Direction.Down, elf.getFacing());
        
        Enemy elf2 = new Elf(game, 1);
        assertFalse(elf.equals(elf2));
        assertTrue(elf2.equals(elf2));
        assertFalse(elf.equals(null));

    }

    @Test
    void testEnemyCollisionWithRudolph(){

        Rudolph rudolph = new Rudolph(game);
        int rX = rudolph.getXPosition();
        int rY = rudolph.getYPosition();
        elf.setXPos(rX);
        elf.setYPos(rY);

        assertTrue(elf.collisionWith(rudolph));
    }






}
