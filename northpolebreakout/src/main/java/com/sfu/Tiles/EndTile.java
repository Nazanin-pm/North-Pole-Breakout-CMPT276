package com.sfu.Tiles;

import com.sfu.Entities.Entity;
import com.sfu.Entities.Player;

import java.awt.*;
import java.util.HashMap;


/**
 * The EndTile class represents a special tile on the game board where the player wins the game if they reach it with the required points.
 * Extends the Ground tile and overrides the occupy method to check for winning conditions.
 */

public class EndTile extends Ground {

    /**
     * Constructs an EndTile at the specified x and y coordinates.
     *
     * @param x The x-coordinate of the EndTile.
     * @param y The y-coordinate of the EndTile.
     */
    private final Rectangle collisionArea;

    public EndTile(int x, int y, int tileSize) {
        super(x, y, tileSize);
        collisionArea = new Rectangle(x, y, tileSize, tileSize);
    }

    /**
     * Occupies the tile with the specified entity. If the entity is an instance of Player and has the required points,
     * the game is marked as won.
     *
     * @param entity The entity occupying this tile.
     */
    @Override
    public void occupy(Entity entity){


        // Check if the entity is a Player and has collected the required points
        if(entity instanceof Player p){
            if(collisionArea.contains(p.createFutureCollisionHitBox())) {
                p.winCondition(this);
            }
        }

        // Initialize entities if null, and add the current entity to the tile's entity list
        if(entities == null){
            entities = new HashMap<>();
        }
        entities.put(entity.getKey(), entity);
    }
}
