package com.sfu.Items;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * The {@code Item} class is an abstract representation of a game item.
 * Items have an image, a collision area, and a point value that can be extended by subclasses.
 */
public abstract class Item {
    /** The image representing the visual appearance of the item. */
    protected BufferedImage image;

    /** The collision area of the item used for detecting interactions. */
    protected Rectangle collisionArea;

    /** The point value associated with the item. */
    protected int points = 0;

    /**
     * Sets the image representing the item.
     *
     * @param image the {@code BufferedImage} to set
     */
    public void setImage(BufferedImage image) {
        this.image = image;
    }

    /**
     * Gets the image representing the item.
     *
     * @return the {@code BufferedImage} of the item
     */
    public BufferedImage getImage() {
        return image;
    }

    /**
     * Gets the point value associated with the item.
     *
     * @return the number of points the item is worth
     */
    public int getPoints() {
        return points;
    }

    /**
     * Gets the x-coordinate of the item's collision area.
     *
     * @return the x-coordinate of the collision area
     */
    public int getCollisionAreaX() {
        return collisionArea.x;
    }

    /**
     * Gets the y-coordinate of the item's collision area.
     *
     * @return the y-coordinate of the collision area
     */
    public int getCollisionAreaY() {
        return collisionArea.y;
    }

    /**
     * Gets the width of the item's collision area.
     *
     * @return the width of the collision area
     */
    public int getCollisionAreaWidth() {
        return collisionArea.width;
    }

    /**
     * Gets the height of the item's collision area.
     *
     * @return the height of the collision area
     */
    public int getCollisionAreaHeight() {
        return collisionArea.height;
    }
}
