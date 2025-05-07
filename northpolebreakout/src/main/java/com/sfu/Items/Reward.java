package com.sfu.Items;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * The {@code Reward} class represents a specific type of item in the game
 * that positively affects the player's score or progress.
 * It extends the {@code Item} class and inherits its properties.
 */
public class Reward extends Item {
    
    /**
     * Constructs a new {@code Reward} item with the specified properties.
     *
     * @param points        the number of points to add to the player's score when collected
     * @param collisionArea the {@code Rectangle} defining the item's collision area
     * @param image         the {@code BufferedImage} representing the item's appearance
     */
    public Reward(int points, Rectangle collisionArea, BufferedImage image) {
        this.points = points;
        this.collisionArea = collisionArea;
        this.image = image;
    }
}
