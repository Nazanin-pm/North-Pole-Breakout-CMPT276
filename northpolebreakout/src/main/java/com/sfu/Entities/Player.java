package com.sfu.Entities;
import com.sfu.Direction;
import com.sfu.Game;
import com.sfu.Items.Reward;
import com.sfu.KeyInput;
import com.sfu.Tiles.EndTile;
import com.sfu.Tiles.Tile;
import javax.swing.*;
import java.awt.*;

public class Player extends Entity{

    private int points = 0;
    private final int diagonalSpeed;
    private static Player instance;
    private int rewardsCollected = 0;
    private boolean allRewardsCollected = false;


    private final int screenXPosition;
    private final int screenYPosition;

    private final KeyInput keyInput;

    /**
     * Singleton pattern, only one instance can exist at a time
     * TODO separate this code into private functions
     */
    private Player(Game game){
        super(game.getTileSize());
        this.game = game;

        //Character Paths
        String idleCharacterLeftPath = "/Player/LeftIdle.png";
        String idleCharacterUpPath = "/Player/LeftIdle.png";
        String idleCharacterRightPath = "/Player/RightIdle.png";
        String idleCharacterDownPath = "/Player/RightIdle.png";
        String movingCharLeftPath = "/Player/LeftMoving.png";
        String movingCharUpPath = "/Player/LeftMoving.png";
        String movingCharRightPath = "/Player/RightMoving.png";
        String movingCharDownPath = "/Player/RightMoving.png";

        loadImages(movingCharUpPath, idleCharacterUpPath, movingCharDownPath,
                idleCharacterDownPath, movingCharRightPath, idleCharacterRightPath,
                movingCharLeftPath, idleCharacterLeftPath);

        screenXPosition = game.getScreenWidth()/2 - (tileSize/2);
        screenYPosition = game.getScreenHeight()/2 - (tileSize/2);

        // find the parameters by putting a sprite overtop a canvas of the same resolution
        // x,y are the pixels that don't cause collision
        // x is the pixel count from left side of the image to the left most non collision pixel of the sprite
        // y is the pixel count from the top of the image to the top most non collision pixel of the sprite
        // draw a rectangle over the parts you want to be collision able
        // width and height are width/height of that rectangle
        // *4 because player is drawn at tileSize x tileSize/96x96, and player img is 24x24
        super.collisionArea = new Rectangle(12*4,8*4,10*4,15*4);

        Point spawn = game.getPlayerSpawn();

        if (spawn != null) {
            if(!validateSpawn(spawn.x * tileSize, spawn.y * tileSize)){
                System.out.println("spawn failed");
            }
        }

        super.speed = 5;
        diagonalSpeed = (int) (speed * 0.7);

        keyInput = new KeyInput(game);


    }

    public static Player createPlayer(Game game){
        instance = new Player(game);
        return instance;
    }

    public void resetPlayer(){

        Point spawn = findOpenSpace();
        xPosition = spawn.x * tileSize;
        yPosition = spawn.y * tileSize;

        points = 0;
        rewardsCollected = 0;

        futureXPos = xPosition;
        futureYPos = yPosition;

    }

    /**
     * @param otherX x location of object
     * @param otherY y location of object
     * @return True if object location is visible to player, else false
     */
    public boolean isVisibleToPlayer(int otherX, int otherY){
        return (otherX + tileSize > xPosition - screenXPosition &&
                otherX - tileSize < xPosition + screenXPosition &&
                otherY + tileSize > yPosition - screenYPosition &&
                otherY - tileSize < yPosition + screenYPosition);
    }

    /**
     * @param otherX x location of object
     * @param otherY y location of object
     * @param scale object size relative to tile size, if scale is 1, object is 1 x 1 tiles big
     * @return True if object location is visible to player, else false
     */
    public boolean isVisibleToPlayer(int otherX, int otherY, double scale){
        return (otherX + scale * tileSize > xPosition - screenXPosition &&
                otherX - scale * tileSize < xPosition + screenXPosition &&
                otherY + scale * tileSize > yPosition - screenYPosition &&
                otherY - scale * tileSize < yPosition + screenYPosition);
    }

    /**
     * Checks for collision with items and other entities
     */
    private void checkCollision() {
        for(Tile tile : occupying){
            if(tile.hasItem()){
                
                boolean isReward = tile.getItem() instanceof Reward;
                int gained = tile.itemIntersects(this.createCollisionHitBox());

                if(gained != 0){

                points += gained;

                    if(this.points < 0){
                        game.endGame(false);
                    }

                    if(isReward){
                         rewardsCollected++;
                         game.getSound().playPresentSound();
                         if (rewardsCollected == game.getRewardsNeeded()){
                             this.allRewardsCollected = true;
                         }
                    }
                    else{
                        game.getSound().playBombSound();
                    }
                }
            }
            for (Entity entity : tile.getEntities().values()) {
                entityCollision(entity);
            }
        }
    }

    public int getRewardsCollected(){
        return rewardsCollected;
    }


    // double dispatch design pattern
    /**
     * Checks if a collision will occur with the parameter Entity
     *
     * @param other The other entity to check for collision with
     * @return True if a collision occurs, false otherwise
     */
    protected boolean entityCollision(Entity other){
        return other.collisionWith(this);
    }

    // this method will be called by an enemy class
    /**
     * Checks for a collision with an enemy entity. If a collision occurs, it triggers the game's end.
     *
     * @param enemy The enemy entity to check for collision with
     * @return false
     */
    public boolean collisionWith(Enemy enemy){
        if (this.createCollisionHitBox().intersects(enemy.createFutureCollisionHitBox())){
            game.getSound().playEnemyCollision();
            game.endGame(false);
        }
        return false; // return false so that a player can't collide with enemy, but collision can be detected
    }

    /**
     * Checks for a collision a rudolph entity. If a collision occurs, it returns true, else false
     *
     * @param rudolph The rudolph entity to check for collision with
     * @return false
     */
    protected boolean collisionWith(Rudolph rudolph){
        boolean collision = this.createCollisionHitBox().intersects(rudolph.createCollisionHitBox());
        if (collision){
            game.getSound().playPresentSound();
        }
        return this.createCollisionHitBox().intersects(rudolph.createCollisionHitBox());
    }

    /**
     * Checks for a collision a player entity
     *
     * @param player The rudolph player to check for collision with
     * @return false, since there is only ever 1 player entity
     */
    protected boolean collisionWith(Player player){
        return false;
    }

    /**
     *  Updates the character every frame by moving the character and checking for possible collisions
     */
    public void update(){
        super.spriteCount++;
        String movingDirection = keyInput.determineMovingDirection();
        if(!terrainCollision(movingDirection)){
            xPosition = futureXPos;
            yPosition = futureYPos;
            checkCollision();
        }
        else{
            game.getSound().playWallCollision();
        }
        updateCollisionValues();
    }


    /**
     * Utility method to calculate the future array position of a side of the player's collision box, which is then
     * used to determine terrain collision
     *
     * @param side The x/y position of a side of the player's collision box
     * @param direction The direction the side will move in
     *
     * @return The row/column index of the entity's future collision box. If an X is passed, a column index is returned.
     * If a Y is passed, a row index is returned
     */
    protected int getFuturePositionDiagonal(int side, Direction direction){
        if (direction == Direction.Down || direction == Direction.Right){
            return (side + diagonalSpeed)/tileSize;
        }
        return (side - diagonalSpeed)/tileSize;
    }

    /**
     * Checks for terrain collision by test moving in the given direction.
     *
     * @param keyInput a string representing the directional input
     * @return {@code true} if a collision is detected after the move; {@code false} otherwise.
     */
    protected boolean terrainCollision(String keyInput){

        updateCollisionValues();

        switch (keyInput){
            case "":
                return false;

            case "WD":
                facing = Direction.Right;
                futureXPos += diagonalSpeed;
                futureYPos -= diagonalSpeed;
                topLengthTileIndex = getFuturePositionDiagonal(topLengthY, Direction.Up);
                rightWidthTileIndex = getFuturePositionDiagonal(rightWidthX, Direction.Right);
                break;

            case "WA":
                facing = Direction.Left;
                futureXPos -= diagonalSpeed;
                futureYPos -= diagonalSpeed;
                topLengthTileIndex = getFuturePositionDiagonal(topLengthY, Direction.Up);
                leftWidthTileIndex = getFuturePositionDiagonal(leftWidthX, Direction.Left);
                break;

            case "SD":
                facing = Direction.Right;
                futureXPos += diagonalSpeed;
                futureYPos += diagonalSpeed;
                bottomLengthTileIndex = getFuturePositionDiagonal(bottomLengthY, Direction.Down);
                rightWidthTileIndex = getFuturePositionDiagonal(rightWidthX, Direction.Right);
                break;

            case "SA":
                facing = Direction.Left;
                futureXPos -= diagonalSpeed;
                futureYPos += diagonalSpeed;
                bottomLengthTileIndex = getFuturePositionDiagonal(bottomLengthY, Direction.Down);
                leftWidthTileIndex = getFuturePositionDiagonal(leftWidthX, Direction.Left);
                break;

            case "W":
                facing = Direction.Up;
                futureYPos -= speed;
                topLengthTileIndex = getFutureArrayPosition(topLengthY, Direction.Up);
                break;

            case "A":
                facing = Direction.Left;
                futureXPos -= speed;
                leftWidthTileIndex = getFutureArrayPosition(leftWidthX, Direction.Left);
                break;

            case "S":
                facing = Direction.Down;
                futureYPos += speed;
                bottomLengthTileIndex = getFutureArrayPosition(bottomLengthY, Direction.Down);
                break;

            case "D":
                facing = Direction.Right;
                futureXPos += speed;
                rightWidthTileIndex = getFutureArrayPosition(rightWidthX, Direction.Right);
                break;
        }
        return !occupyTiles();
    }

    public static void resetInstance(){
        instance = null;
    }

    /**
     * Draws the character in the middle of the screen
     */
    public void draw(Graphics2D g2d){
        // draw thing at x location, y location, with width size, length size
        g2d.drawImage(determineSprite(), screenXPosition, screenYPosition, null);
        //g2d.drawRect(screenXPosition + collisionArea.x, screenYPosition + collisionArea.y, collisionArea.width, collisionArea.height);

    }

    /**
     * Finds and returns the coordinates of an open space on the game board. An open space is a tile that:
     * <ul>
     *   <li>Is traversable</li>
     *   <li>Does not contain any items</li>
     *   <li>Has no entities present</li>
     * </ul>
     * If no suitable tile is found, the method defaults to returning the center of the game board.
     *
     * @return a {@link Point} representing the column and row of an open space, or the center of the board
     *         if no open space is found.
     */
    public Point findOpenSpace() {
        Tile[][] map = game.getBoard().getTemplate();
        int rows = map.length;
        int cols = map[0].length;

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                Tile tile = map[row][col];
                if (tile.isTraversable() && !tile.hasItem() && tile.getEntities().isEmpty()) {
                    return new Point(col, row);
                }
            }
        }
        return new Point(rows / 2, cols / 2);
    }
    /**
     * Checks whether the win condition for the game has been met. If all rewards have been collected,
     * the game is won.
     *
     * @param endTile the {@link EndTile} that represents the end point or goal tile in the game.
     */
    public void winCondition(EndTile endTile){
        if (this.allRewardsCollected){
            game.endGame(true);
        }
    }

    public void addPoints(int points){
        this.points += points;
    }

    public int getPoints(){
        return this.points;
    }

    @Override
    public boolean isMoving() {
        return !keyInput.isEmpty();
    }

    public int getScreenXPosition(){
        return screenXPosition;
    }

    public int getScreenYPosition(){
        return screenYPosition;
    }

    /**
     * @param otherX x location of object
     * @return x position relative to player
     */
    public int getScreenXRelativeTo(int otherX){
        return otherX + screenXPosition - xPosition;
    }

    /**
     * @param otherY y location of object
     * @return y position relative to player
     */
    public int getScreenYRelativeTo(int otherY){
        return otherY + screenYPosition - yPosition;
    }

    public Action getWAction() {
        return keyInput.getWAction();
    }

    public Action getAAction() {
        return keyInput.getAAction();
    }

    public Action getSAction() {
        return keyInput.getSAction();
    }

    public Action getDAction() {
        return keyInput.getDAction();
    }
}