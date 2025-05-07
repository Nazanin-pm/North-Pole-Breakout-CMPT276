package com.sfu.Entities;

import com.sfu.Game;

import java.awt.*;

public class Santa extends Enemy {
    /**
     * Constructs a Santa entity that moves towards the player.
     * 
     */

     private final String SantaUpIdle = "/Santa/Santa.png";
     private final String SantaUpMoving = "/Santa/Santa.png";
     private final String SantaRightIdle = "/Santa/Santa.png";
     private final String SantaRightMoving = "/Santa/Santa.png";
     private final String SantaDownIdle = "/Santa/Santa.png";
     private final String SantaDownMoving = "/Santa/Santa.png";
     private final String SantaLeftIdle = "/Santa/Santa.png";
     private final String SantaLeftMoving = "/Santa/Santa.png";


    public Santa(Game game, double speedAdjustment) {
        super(game);
        this.game = game;
        super.collisionArea = new Rectangle(9 * 4, 2 * 4, 9 * 16, 20 * 6);
        if (!spawnAtRandomOpenSpace(columnAggroRange, rowAggroRange)){
            System.out.println("spawn failed");
        }
        super.speed += speedAdjustment;
        super.defaultSpeed = speed;
        super.scale = 1.5; // manually set scale if it should be different from default
        loadImages(SantaUpMoving, SantaUpIdle, SantaDownMoving,
                SantaDownIdle, SantaRightMoving, SantaRightIdle,
                SantaLeftMoving, SantaLeftIdle);
    }

    public Santa(Game game, int x, int y, double speedAdjustment) {
        super(game);
        this.game = game;
        super.collisionArea = new Rectangle(9 * 2, 2 * 4, 9 * 10, 20 * 6);
        xPosition = x;
        yPosition = y;
        updateCollisionValues();
        occupyTiles();
        super.speed += speedAdjustment;
        super.defaultSpeed = speed;
        super.scale = 1.5; // manually set scale if it should be different from default
        loadImages(SantaUpMoving, SantaUpIdle, SantaDownMoving,
                SantaDownIdle, SantaRightMoving, SantaRightIdle,
                SantaLeftMoving, SantaLeftIdle);
    }

    @Override
    public void update(){
        Point playerPosition = game.getPlayerPosition();
        super.updatePosition(playerPosition.x, playerPosition.y);
    }
}
