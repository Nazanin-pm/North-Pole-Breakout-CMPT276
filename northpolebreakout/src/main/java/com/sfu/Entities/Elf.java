package com.sfu.Entities;

import com.sfu.Game;

import javax.imageio.ImageIO;
import java.awt.*;
import java.util.Objects;

/**
 * The Elf class represents an enemy character that moves towards the player in the game.
 * It extends the Enemy class and manages the graphical representation and movement of the elf.
 */
public class Elf extends Enemy {
    /**
     * The file path for the elf's idle and moving images in various directions.
     */
    private final String elfUpIdle = "/Elf/Elf2Copy.png";
    private final String elfUpMoving = "/Elf/Elf2Copy.png";
    private final String elfRightIdle = "/Elf/Elf2Copy.png";
    private final String elfRightMoving = "/Elf/Elf2Copy.png";
    private final String elfDownIdle = "/Elf/Elf2Copy.png";
    private final String elfDownMoving = "/Elf/Elf2Copy.png";
    private final String elfLeftIdle = "/Elf/Elf2Copy.png";
    private final String elfLeftMoving = "/Elf/Elf2Copy.png";

    /**
     * Constructs an Elf entity that moves towards the player.
     *
     * @param game     The game instance that this elf is associated with.
     * @param speedAdjustment sets elf's speed to its current speed + this value
     * @param tilesFromPlayer the minimum amount of tiles an elf can spawn from the player
     */
    public Elf(Game game, double speedAdjustment, int tilesFromPlayer) {
        super(game);
        this.game = game;
        super.collisionArea = new Rectangle(9 * 4, 2 * 4, 9 * 4, 20 * 4);
        if (!spawnAtRandomOpenSpace(tilesFromPlayer, tilesFromPlayer)) {
            System.out.println("spawn failed");
        }
        try{
            super.aggroImage = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Elf/Elf1.png")));
        }
        catch(Exception e){
            System.out.println("Mark the resources directory as a Resources Root");
            e.printStackTrace();
        }

        super.speed += speedAdjustment;
        super.defaultSpeed = speed;
        super.scale = 1; // manually set scale if it should be different from default
        loadImages(elfUpMoving, elfUpIdle, elfDownMoving,
                elfDownIdle, elfRightMoving, elfRightIdle,
                elfLeftMoving, elfLeftIdle);
    }

    /**
     * Constructs an Elf entity that moves towards the player.
     *
     * @param game     The game instance that this elf is associated with.
     * @param speedAdjustment sets elf's speed to its current speed + this value
     */
    public Elf(Game game, double speedAdjustment) {
        super(game);
        this.game = game;
        super.collisionArea = new Rectangle(9 * 4, 2 * 4, 9 * 4, 20 * 4);
        if (!spawnAtRandomOpenSpace(columnAggroRange, rowAggroRange)){
            System.out.println("spawn failed");
        }
        try{
            super.aggroImage = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Elf/Elf1.png")));
        }
        catch(Exception e){
            System.out.println("Mark the resources directory as a Resources Root");
            e.printStackTrace();
        }

        super.speed += speedAdjustment;
        super.defaultSpeed = speed;
        super.scale = 1; // manually set scale if it should be different from default
        loadImages(elfUpMoving, elfUpIdle, elfDownMoving,
                elfDownIdle, elfRightMoving, elfRightIdle,
                elfLeftMoving, elfLeftIdle);
    }
}
