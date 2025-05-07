package com.sfu.Entities;
import com.sfu.Direction;
import com.sfu.Game;
import com.sfu.ImageScaler;
import com.sfu.Tiles.Tile;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Objects;

public abstract class Entity {
    protected int xPosition, yPosition;
    protected int futureXPos, futureYPos;

    protected Game game;
    protected int speed = 2;
    protected Direction facing = Direction.Left;
    protected boolean isMoving = false;
    protected double scale = 1;
    protected int tileSize;

    protected Rectangle collisionArea;
    protected int spriteChooser = 1;
    protected int spriteCount = 0;
    protected int spriteChangeSpeed = 20; // edit spriteChangeSpeed to determine speed of sprite change

    // the coordinates on the board of the entity's sprite's collision area
    protected int leftWidthX;
    protected int rightWidthX;
    protected int topLengthY;
    protected int bottomLengthY;

    // the array indexes of the entity's sprite's collision area
    protected int leftWidthTileIndex;
    protected int rightWidthTileIndex;
    protected int topLengthTileIndex;
    protected int bottomLengthTileIndex;

    protected ArrayList<Tile> occupying;
    protected ArrayList<Tile> newlyOccupied;

    private static int numOfEntities = 0;
    private final int key;

    Entity(int tileSize){
        this.tileSize = tileSize;
        key = ++numOfEntities;
        occupying = new ArrayList<>();
        newlyOccupied = new ArrayList<>();

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Entity other = (Entity) o;
        return key == other.key;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(key);
    }

    protected BufferedImage up1,up2,down1,down2,right1,right2,left1,left2;


    public Direction getFacing() {
        return facing;
    }

    public void setFacing(Direction facing) {
        this.facing = facing;
    }

    public int getXPosition(){
        return xPosition;
    }

    public int getYPosition(){
        return yPosition;
    }

    public boolean isMoving(){
        return isMoving;
    }

    public double getScale(){
        return scale;
    }

    public int getKey() {
        return key;
    }

    /**
     * Checks if a collision will occur with the parameter Entity
     *
     * @param other The other entity to check for collision with
     * @return true if the collision is accepted, false otherwise
     */
    protected abstract boolean entityCollision(Entity other);

    /**
     * Checks if a collision will occur with the parameter Entity
     * Since entity is an abstract class, this method should not be called
     *
     * @param entity The other entity to check for collision with
     * @return true if the collision is accepted, false otherwise
     */
    protected boolean collisionWith(Entity entity) {
        System.out.println("this shouldn't ever be called");
        return this.createCollisionHitBox().intersects(entity.createFutureCollisionHitBox());
    }

    /**
     * Checks for a collision with another enemy entity.
     *
     * @param enemy The enemy entity to check for collision with
     * @return true if a collision occurs, false otherwise
     */
    protected abstract boolean collisionWith(Enemy enemy);

    /**
     * Checks for a collision with another player entity.
     *
     * @param player The enemy entity to check for collision with
     * @return true if a collision occurs, false otherwise
     */
    protected abstract boolean collisionWith(Player player);


    /**
     * Checks for a collision with a rudolph entity.
     *
     * @param rudolph The enemy entity to check for collision with
     * @return true if a collision occurs, false otherwise
     */
    protected abstract boolean collisionWith(Rudolph rudolph);


    /**
     * Occupies the tiles the entity is on, if it tries to occupy a non-traversable tile, it does not change
     * it's occupied tiles and uses it's previously occupied tiles. Modifies occupying and newlyOccupied variables
     * @return False if the entity tries to occupy a non-traversable tile, else true
     */
    protected boolean occupyTiles(){
        boolean collision = false;
        ArrayList <Tile> previouslyOccupied = new ArrayList<>(occupying);
        newlyOccupied.clear();
        occupying.clear();

        for(int i = topLengthTileIndex; i <= bottomLengthTileIndex; i++){
            for(int j = leftWidthTileIndex; j <= rightWidthTileIndex; j++){
                Tile tile = game.getTileAt(i,j);
                if(tile.isTraversable()) {
                    occupying.add(tile);
                    tile.occupy(this);
                    if(!previouslyOccupied.contains(tile)) {
                        newlyOccupied.add(tile);
                    }
                }
                else{
                    collision = true;
                }
            }
        }

        if(!collision){
            for(Tile tile: previouslyOccupied){
                if(!occupying.contains(tile)) {
                    tile.leave(this);
                }
            }
        }
        else{
            for(Tile tile: newlyOccupied){
                    tile.leave(this);
            }
            newlyOccupied.clear();
            occupying = previouslyOccupied;
        }

        return !collision;
    }

    /**
     * Updates the values used to calculate collisions.
     * Should be called at the start of an entities movement to calculate its current collision values,
     * and at the end of an entities movement so that the collisions values are calculated with new positional values
     */
    protected void updateCollisionValues(){
        // the coordinates on the board of the entity's sprite's collision area
        leftWidthX = xPosition + collisionArea.x;
        rightWidthX = xPosition + collisionArea.x + collisionArea.width;
        topLengthY = yPosition + collisionArea.y;
        bottomLengthY = yPosition + collisionArea.y + collisionArea.height;

        // reset values of future position
        futureXPos = xPosition;
        futureYPos = yPosition;

        // the array indexes of the entity's sprite's collision area
        // length corresponds to board row, width corresponds to board column
        leftWidthTileIndex = leftWidthX /tileSize;
        rightWidthTileIndex = rightWidthX /tileSize;
        topLengthTileIndex = topLengthY /tileSize ;
        bottomLengthTileIndex = bottomLengthY /tileSize;
    }

    // Loads images from file path of type string
    // !! up1/down1/right1/left1 should be the images in "motion", while 2 should be the images that are still
    protected void loadImages(String up1, String up2, String down1, String down2,
                              String right1, String right2, String left1, String left2) {

        try{
            this.left1 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream(left1)));
            this.left2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream(left2)));
            this.up1 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream(up1)));
            this.up2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream(up2)));
            this.right1 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream(right1)));
            this.right2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream(right2)));
            this.down1 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream(down1)));
            this.down2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream(down2)));

            ImageScaler imageScaler = new ImageScaler();

            this.left1 = imageScaler.scaleImage(this.left1, tileSize, tileSize);
            this.left2 = imageScaler.scaleImage(this.left2, tileSize, tileSize);
            this.up1 = imageScaler.scaleImage(this.up1, tileSize, tileSize);
            this.up2 = imageScaler.scaleImage(this.up2, tileSize, tileSize);
            this.right1 = imageScaler.scaleImage(this.right1, tileSize, tileSize);
            this.right2 = imageScaler.scaleImage(this.right2, tileSize, tileSize);
            this.down1 = imageScaler.scaleImage(this.down1, tileSize, tileSize);
            this.down2 = imageScaler.scaleImage(this.down2, tileSize, tileSize);

        }
        catch(Exception e){
            System.out.println("Mark the resources directory as a Resources Root");
            e.printStackTrace();
        }
    }

    /**
     * @return A BufferedImage of the sprite the entity should be currently displaying
     */
    public BufferedImage determineSprite(){
        BufferedImage image = null;

        switch(facing){
            case Up:
                if (spriteChooser == 1 && isMoving()){
                    image = up1;
                }
                else{
                    image = up2;
                }
                break;
            case Left:
                if (spriteChooser == 1 && isMoving()){
                    image = left1;
                }
                else{
                    image = left2;
                }
                break;
            case Down:
                if (spriteChooser == 1 && isMoving()){
                    image = down1;
                }
                else{
                    image = down2;
                }
                break;
            case Right:
                if (spriteChooser == 1 && isMoving()){
                    image = right1;
                }
                else{
                    image = right2;
                }
                break;
        }

        if (spriteCount == spriteChangeSpeed){
            if (spriteChooser == 2) {
                spriteChooser = 1;
            }
            else{
                spriteChooser = 2;
            }
            spriteCount = 0;
        }
        return image;
    }

    public ArrayList<Tile> getOccupying(){
        return this.occupying;
    }

    /**
     * @param position The current position (x or y) of the entity
     * @param direction The direction to move in
     *
     * @return the row/column index of the tile the entity will be occupying,
     * if it continues to move in the parameter direction
     */
    protected int getFutureArrayPosition(int position, Direction direction){
        if (direction == Direction.Down || direction == Direction.Right){
            return (position + speed)/tileSize;
        }
        return (position - speed)/tileSize;
    }

    /**
     * @return Rectangle representing the entity's current collision box
     */
    public Rectangle createCollisionHitBox(){
        return new Rectangle(this.collisionArea.x + this.xPosition, this.collisionArea.y + this.yPosition,
                this.collisionArea.width, this.collisionArea.height);
    }

    /**
     * @return Rectangle representing the entity's future collision box (can be the same as the current collision box)
     */
    public Rectangle createFutureCollisionHitBox(){
        return new Rectangle(this.collisionArea.x + this.futureXPos, this.collisionArea.y + this.futureYPos,
                this.collisionArea.width, this.collisionArea.height);
    }

    public void drawCollisionBox(Graphics2D g){
        g.drawRect(game.convertXtoPlayerScreenX(xPosition + collisionArea.x), game.convertYtoPlayerScreenY(yPosition + collisionArea.y), collisionArea.width, collisionArea.height);
    }

    public Rectangle getCollisionArea() {
        return collisionArea;
    }

    /**
     * Validates the spawning of the entity at the parameter position. This will only check terrain collision.
     * If successful the entity's xPosition and yPosition will be set the parameter values, and it's collision values
     * will be adjusted. If unsuccessful, it will set the entity's xPosition and yPosition to 0,0 and return false.
     *
     * @param x The x location the entity will spawn at
     * @param y The y location the entity will spawn at
     * @return True if the entity has no collision with non-traversable tiles, else false
     */
    public boolean validateSpawn(int x, int y){
        this.xPosition = x;
        this.yPosition = y;
        updateCollisionValues();
        if (occupyTiles()){
            return true;
        }
        else{
            this.xPosition = 0;
            this.yPosition = 0;
            return false;
        }
    }

    public abstract void update();

    public abstract void draw(Graphics2D g2d);


}
