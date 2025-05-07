package com.sfu.Tiles;

import com.sfu.Entities.Entity;

/**
 * The {@code Wall} class represents a non-traversable tile in the game.
 * Walls act as obstacles that entities cannot occupy or pass through.
 * This class extends the {@code Tile} class and overrides specific behavior
 * to reflect the properties of a wall.
 */
public class Wall extends Tile {

    /**
     * Constructs a {@code Wall} object with the specified tile size.
     * Walls are set to be non-traversable by default.
     *
     * @param tileSize the size of the wall tile, in pixels
     */
    public Wall(int tileSize) {
        super(tileSize);
        super.isTraversable = false;
    }

    /**
     * Overrides the {@code occupy} method to prevent any entity from occupying the wall.
     * This method outputs a message indicating that walls cannot be occupied.
     *
     * @param entity the {@code Entity} attempting to occupy the wall
     */
    @Override
    public void occupy(Entity entity) {
        System.out.println("Wall can't be occupied");
    }

    /**
     * Overrides the {@code leave} method to prevent any entity from leaving the wall.
     * This method outputs a message indicating that walls cannot be occupied or left.
     *
     * @param entity the {@code Entity} attempting to leave the wall
     */
    @Override
    public void leave(Entity entity) {
        System.out.println("Wall can't be occupied, so you also can't leave");
    }
}
