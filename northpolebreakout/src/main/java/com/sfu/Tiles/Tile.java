package com.sfu.Tiles;

import com.sfu.Entities.Entity;
import com.sfu.Items.Item;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Objects;

/**
 * Abstract class representing a tile in the game world.
 * Each tile can hold entities and items, and can be traversable or not.
 */
public abstract class Tile {

    protected HashMap<Integer, Entity> entities = new HashMap<>();  // HashMap to store entities on the tile
    protected boolean isTraversable = true;  // Flag indicating whether the tile is traversable
    private BufferedImage image;  // Image representing the tile
    protected int key;  // Unique identifier for the tile
    protected static int numOfTiles = 0;  // Counter to generate unique keys for each tile
    protected int xPosition;  // X position of the tile
    protected int yPosition;  // Y position of the tile
    protected Item item;  // Item associated with the tile
    int tileSize;

    /**
     * Constructor that generates a unique key for each tile.
     */
    public Tile(int tileSize) {
        this.tileSize = tileSize;
        key = ++numOfTiles;
    }

    /**
     * Compares this tile with another object for equality.
     * @param o The object to compare with.
     * @return true if the objects are the same, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tile tile = (Tile) o;
        return key == tile.key;
    }

    /**
     * Generates a hash code for this tile based on its key.
     * @return The hash code of the tile.
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(key);
    }

    /**
     * Sets the image for the tile.
     * @param image The image to set for the tile.
     */
    public void setImage(BufferedImage image) {
        this.image = image;
    }

    /**
     * Gets the image representing the tile.
     * @return The image of the tile.
     */
    public BufferedImage getImage() {
        return image;
    }

    /**
     * Checks if the tile is traversable.
     * @return true if the tile is traversable, false otherwise.
     */
    public boolean isTraversable() {
        return isTraversable;
    }

    /**
     * Gets the unique key of the tile.
     * @return The unique key of the tile.
     */
    public int getKey() {
        return key;
    }

    /**
     * Occupies the tile with the given entity.
     * This method should be implemented by subclasses to handle specific tile behaviors.
     * @param entity The entity to occupy the tile.
     */
    abstract public void occupy(Entity entity);

    /**
     * Removes the entity from the tile.
     * This method should be implemented by subclasses to handle specific tile behaviors.
     * @param entity The entity to remove from the tile.
     */
    abstract public void leave(Entity entity);

    /**
     * Gets the entities currently occupying the tile.
     * @return A map of entities occupying the tile.
     */
    public HashMap<Integer, Entity> getEntities() {
        return entities;
    }

    /**
     * Adds an item to the tile.
     * @param item The item to add to the tile.
     */
    public void addItem(Item item) {
        this.item = item;
    }

    /**
     * Checks if the tile contains an item.
     * @return true if the tile contains an item, false otherwise.
     */
    public boolean hasItem() {
        return item != null;
    }

    /**
     * Gets the image of the item on the tile.
     * @return The image of the item, or null if no item is present.
     */
    public BufferedImage getItemImage() {
        return item.getImage();
    }

    public Item getItem(){
        return this.item;
    }

    /**
     * Checks if the item on the tile intersects with the player's collision box.
     * If they intersect, the player's points are increased, and the item is removed from the tile.
     * @param playerCollisionBox The collision box of the player.
     * @return The points gained from the item if there is an intersection, or 0 if no intersection occurs.
     */
    public int itemIntersects(Rectangle playerCollisionBox) {
        if ((item!= null) && (new Rectangle(item.getCollisionAreaX() + xPosition, item.getCollisionAreaY() + yPosition,
                item.getCollisionAreaWidth(), item.getCollisionAreaHeight()).intersects(playerCollisionBox))) {
            int points = item.getPoints();
            item = null;
            return points;
        }
        return 0;
    }
}


//    public Tile(Entity occupiedBy){
//        currTile = numOfTiles++;
//        this.isOccupiedBy = occupiedBy;
//        if(occupiedBy instanceof Wall){
//            isTraversable = false;
//        }
//    }

    /**
     * Occupy a tile by the given entity
     * @param e entity to occupt the tile
     * @return if tile was occupied by given entity or not
     */
//    public boolean occupy(Entity e){
//
//       if(this.isOccupiedBy instanceof Wall){
//        return false;
//       } else if(this.isOccupiedBy instanceof Hole){
//        //subtract 5 points
//       } else if(this.isOccupiedBy instanceof Ground){
//        //move player into tile
//       } else if(this.isOccupiedBy instanceof Reward){
//        //add 5 to points
//        isOccupiedBy = new Ground();
//       }
//
//
//        return true;
//
//    }


    

