package com.sfu.Tiles;
import com.sfu.Entities.Entity;
import java.util.HashMap;

/**
 * Empty ground, nothing happens if player moves into a tile occupied by ground
 */
public class Ground extends Tile {
    
    public Ground(int x, int y, int tileSize) {
        super(tileSize);
        xPosition = x;
        yPosition = y;
    }

    /**
     * Adds the entity provided to the Tile's list of entities, if the entity is already in the tile, nothing happens
     * @param entity An entity
     */
    public void occupy(Entity entity){
        if(entities == null){
            entities = new HashMap<>();
        }
        entities.put(entity.getKey(), entity);
    }

    /**
     * removes the entity provided to from the Tile's list of entities, if the entity is not in the tile, nothing happens
     * @param entity An entity
     */
    public void leave(Entity entity){
        if(entities == null){
            entities = new HashMap<>();
        }
        entities.remove(entity.getKey());
    }
}
