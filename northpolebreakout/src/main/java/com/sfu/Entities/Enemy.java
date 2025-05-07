package com.sfu.Entities;

import com.sfu.Direction;
import com.sfu.Game;
import com.sfu.Pathfinding.Pathfinder;
import com.sfu.Tiles.Tile;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;


/**
 * Represents an enemy entity in the game, extending from the Entity class.
 * The Enemy class handles collision detection, movement towards the player,
 * and random movement when the player is out of range.
 */
public abstract class Enemy extends Entity {
    protected int framesOutOfPlayerRange = 0; // Counter for frames the enemy is out of player range
    protected int defaultSpeed;
    protected boolean aggro = false;
    protected boolean pathing = false;
    protected BufferedImage aggroImage;
    protected int columnAggroRange = 6;
    protected int rowAggroRange = 5;


    /**
     * Checks for collisions with non-player entities within the tiles occupied by the enemy.
     *
     * @return true if there is a collision with another entity, false otherwise
     */
    public boolean nonPlayerEntityCollision() {
        boolean enemyCollision = false;
        for (Tile tile : occupying) {
            for (Entity entity : tile.getEntities().values()) {
                 if (entityCollision(entity)) {
                     enemyCollision = true;
                 }
            }
        }

        if(enemyCollision){
            for (Tile tile : newlyOccupied) {
                tile.leave(this);
            }
        }

        return enemyCollision;
    }

    /**
     * Parameterized constructor for the Enemy class.
     */
    public Enemy(Game game) {
        super(game.getTileSize());
    }

    /**
     * Checks if a collision will occur with the parameter Entity
     *
     * @param other The other entity to check for collision with
     * @return true if a collision occurs, false otherwise
     */
    @Override
    protected boolean entityCollision(Entity other) {
        return other.collisionWith(this);
    }

    /**
     * Checks for a collision with another enemy entity.
     *
     * @param enemy The enemy entity to check for collision with
     * @return true if a collision occurs, false otherwise
     */
    protected boolean collisionWith(Enemy enemy) {
        return this.createCollisionHitBox().intersects(enemy.createFutureCollisionHitBox()) && !this.equals(enemy);
    }

    /**
     * Checks for a collision with a rudolph entity.
     *
     * @param rudolph The rudolph entity to check for collision with
     * @return true if a collision occurs, false otherwise
     */
    public boolean collisionWith(Rudolph rudolph){
        return this.createCollisionHitBox().intersects(rudolph.createCollisionHitBox());
    }


    /**
     * Checks for a collision with a player entity.
     * If a collision occurs, it triggers the game's end.
     *
     * @param player The player entity to check for collision with
     * @return false to allow the enemy to walk into the player
     */
    public boolean collisionWith(Player player) {
        if (this.createCollisionHitBox().intersects(player.createFutureCollisionHitBox())) {
            game.getSound().playEnemyCollision();
            game.endGame(false);
        }
        return false; // return false so enemy will walk into player
    }

    /**
     * Checks for terrain collisions based on the current speed and facing direction of the enemy.
     * @return true if a terrain collision occurs, false otherwise
     */
    protected boolean terrainCollision() {

        switch (facing) {
            case Up:
                futureYPos = yPosition - speed;
                topLengthTileIndex = getFutureArrayPosition(topLengthY, Direction.Up);
                break;

            case Down:
                futureYPos = yPosition + speed;
                bottomLengthTileIndex = getFutureArrayPosition(bottomLengthY, Direction.Down);
                break;

            case Left:
                futureXPos = xPosition - speed;
                leftWidthTileIndex = getFutureArrayPosition(leftWidthX, Direction.Left);

                break;
            case Right:
                futureXPos = xPosition + speed;
                rightWidthTileIndex = getFutureArrayPosition(rightWidthX, Direction.Right);
        }

        return !occupyTiles();
    }

    protected void pathingToPlayerMovement(Point playerPosition) {
        framesOutOfPlayerRange = 0;
        aggro = true;

        Rectangle playerCollisionArea = game.getPlayer().getCollisionArea();

        int goalColumn = (playerPosition.x + playerCollisionArea.x) / tileSize;
        int goalRow = (playerPosition.y + playerCollisionArea.y) / tileSize;
        searchPath(goalColumn, goalRow);
    }


    protected void withinOneTileMovement(int deltaX, int deltaY) {
        framesOutOfPlayerRange = 0;
        aggro = true;
        pathing = false;
        speed = defaultSpeed;
        if (Math.abs(deltaX) > Math.abs(deltaY)) {
            facing = (deltaX > 0) ? Direction.Right : Direction.Left;
        }
        else {
            facing = (deltaY > 0) ? Direction.Down : Direction.Up;
        }
        framesOutOfPlayerRange = 0;
    }

    protected void withinPlayerTilesMovement(Point playerPoint){
        framesOutOfPlayerRange = 0;
        aggro = true;
        pathing = true;
        speed = defaultSpeed;

        Rectangle playerCollisionArea = game.getPlayer().getCollisionArea();

        int goalColumn = (playerPoint.x + playerCollisionArea.x) / tileSize;
        int goalRow = (playerPoint.y + playerCollisionArea.y) / tileSize;
        searchPath(goalColumn, goalRow);
    }

    protected void outOfRangeMovement() {
        aggro = false;
        pathing = false;
        speed = defaultSpeed / 2;
        framesOutOfPlayerRange++;
        chooseRandomDirection();

    }

    private void chooseRandomDirection() {
        if (framesOutOfPlayerRange >= game.getFPS() * 3) {
            switch (((int) (Math.random() * 4)) % 4) {
                case 0:
                    facing = Direction.Left;
                    break;
                case 1:
                    facing = Direction.Right;
                    break;
                case 2:
                    facing = Direction.Down;
                    break;
                case 3:
                    facing = Direction.Up;
                    break;
            }
            framesOutOfPlayerRange = 0;
        }
        else {
            isMoving = false;
        }
    }

    /**
     * Updates the enemy's position based on the player's location and the enemy's current state.
     * The enemy moves toward the player if within aggro range, follows a path, or moves randomly
     * if out of range. The method handles collision checks and updates the enemy's sprite count.
     */
    public void update() {
        Point point = game.getPlayerPosition();
        super.spriteCount++;
        updateCollisionValues();

        int deltaX = point.x - xPosition;
        int deltaY = point.y - yPosition;

        isMoving = true;

        if (isOutOfRange(deltaX, deltaY)) {
            outOfRangeMovement();
        } else if (pathing) {
            pathingToPlayerMovement(point);
        } else if (isWithinAggroRange(deltaX, deltaY)) {
            withinPlayerTilesMovement(point);
        }

        if (!terrainCollision() && !nonPlayerEntityCollision()) {
            moveInFacingDirection();
        }

        updateCollisionValues();
    }

    /**
     * Updates the enemy's position based on the player's coordinates.
     * The enemy moves toward the player if within aggro range; otherwise, it performs out-of-range movement.
     *
     * @param playerX The x-coordinate of the player's position.
     * @param playerY The y-coordinate of the player's position.
     */
    public void updatePosition(int playerX, int playerY) {
        super.spriteCount++;
        updateCollisionValues();

        int deltaX = playerX - xPosition;
        int deltaY = playerY - yPosition;

        isMoving = true;

        if (isWithinAggroRange(deltaX, deltaY)) {
            speed = defaultSpeed;
            moveTowardsPlayer(deltaX, deltaY);
            framesOutOfPlayerRange = 0;
        } else {
            outOfRangeMovement();
        }

        updateCollisionValues();
    }

    /**
     * Determines if the enemy is out of range of the player.
     * The range is determined based on the specified aggro range and the size of the game tiles.
     *
     * @param deltaX The difference in X position between the player and the enemy.
     * @param deltaY The difference in Y position between the player and the enemy.
     * @return true if the enemy is out of range, false otherwise.
     */
    private boolean isOutOfRange(int deltaX, int deltaY) {
        return Math.abs(deltaX) > (tileSize * columnAggroRange)
                || Math.abs(deltaY) > (tileSize * rowAggroRange);
    }

    /**
     * Determines if the enemy is within the aggro range of the player.
     * The range is determined based on the specified aggro range and the size of the game tiles.
     *
     * @param deltaX The difference in X position between the player and the enemy.
     * @param deltaY The difference in Y position between the player and the enemy.
     * @return true if the enemy is within range, false otherwise.
     */
    private boolean isWithinAggroRange(int deltaX, int deltaY) {
        return Math.abs(deltaX) < (tileSize * columnAggroRange)
                && Math.abs(deltaY) < (tileSize * rowAggroRange);
    }

    /**
     * Moves the enemy in the direction it is currently facing.
     * The movement is determined by the facing direction (Up, Down, Left, Right).
     * The method handles position updates for each direction.
     */
    private void moveInFacingDirection() {
        switch (facing) {
            case Up:
                yPosition -= speed;
                break;
            case Down:
                yPosition += speed;
                break;
            case Left:
                xPosition -= speed;
                break;
            case Right:
                xPosition += speed;
                break;
        }
    }

    /**
     * Moves the enemy toward the player based on the differences in X and Y positions.
     * The movement prioritizes the axis with the larger difference (either X or Y).
     * The enemy's facing direction is updated based on the direction of movement.
     *
     * @param deltaX The difference in X position between the player and the enemy.
     * @param deltaY The difference in Y position between the player and the enemy.
     */
    private void moveTowardsPlayer(int deltaX, int deltaY) {
        if (Math.abs(deltaX) > Math.abs(deltaY)) {
            facing = (deltaX > 0) ? Direction.Right : Direction.Left;
            if (!terrainCollision() && !nonPlayerEntityCollision()) {
                xPosition += (deltaX > 0) ? speed : -speed;
            }
        } else {
            facing = (deltaY > 0) ? Direction.Down : Direction.Up;
            if (!terrainCollision() && !nonPlayerEntityCollision()) {
                yPosition += (deltaY > 0) ? speed : -speed;
            }
        }
    }

    /**
     * Draws the enemy on the screen at its current position.
     *
     * @param g2d The graphics context to draw on
     */
    public void draw(Graphics2D g2d) {
        if (game.locationVisibleToPlayerScreen(xPosition, yPosition, scale)) {
            g2d.drawImage(determineOneSprite(), game.convertXtoPlayerScreenX(xPosition), game.convertYtoPlayerScreenY(yPosition),
                    (int) (scale*tileSize), (int) (scale*tileSize), null);
        }
    }

    public void setXPos(int x){
        this.xPosition = x;
    }

    public void setYPos(int y){
        this.yPosition = y;
    }

    // TODO add a not within x tiles of player condition
    /**
     * Spawns an enemy at a random open space that will not cause terrain collision and non player
     * entity collision
     *
     * @return True if the enemy successfully spawns at a location, else false
     */
    protected boolean spawnAtRandomOpenSpace(int xTilesFromPlayer, int yTilesFromPlayer) {
        ArrayList<Point> openSpaces = this.game.getTraversableTiles();

        if (!openSpaces.isEmpty()) {
            Random random = new Random();
            Point randomPoint = openSpaces.get(random.nextInt(openSpaces.size()));
            Point playerPoint = game.getPlayerSpawn();

            int count = 0;
            while(!validateSpawn(randomPoint.x * tileSize, randomPoint.y * tileSize)
                    || Math.abs(playerPoint.x - randomPoint.x) <= xTilesFromPlayer
                    || Math.abs(playerPoint.y - randomPoint.y) <= yTilesFromPlayer) {
                if (count < openSpaces.size()){
                    break;
                }
                randomPoint = openSpaces.get(random.nextInt(openSpaces.size()));
                count++;
            }
            return count != openSpaces.size();
        }
        return false;
    }

    protected void searchPath(int goalColumn, int goalRow) {
        int startColumn = leftWidthTileIndex;
        int startRow = topLengthTileIndex;

        Pathfinder pathfinder = game.getPathfinder();

        pathfinder.setNodes(startColumn, startRow, goalColumn, goalRow);

        if(pathfinder.search()){
            int nextX = pathfinder.getPathList().get(0).getColumn() * tileSize;
            int nextY = pathfinder.getPathList().get(0).getRow() * tileSize;

            if (topLengthY > nextY && leftWidthX > nextX && rightWidthX < (nextX + tileSize)) {
                facing = Direction.Up;
            }
            else if (topLengthY < nextY && leftWidthX >= nextX && rightWidthX < (nextX + tileSize)) {
                facing = Direction.Down;
            }
            else if (topLengthY >= nextY && bottomLengthY < (nextY + tileSize)) {
                if (leftWidthX > nextX) {
                    facing = Direction.Left;
                }
                if (leftWidthX < nextX) {
                    facing = Direction.Right;
                }
            }
            else if (topLengthY > nextY && leftWidthX > nextX) {
                facing = Direction.Up;
                if (terrainCollision() || nonPlayerEntityCollision()){
                    facing = Direction.Left;
                }
            }
            else if (topLengthY > nextY && leftWidthX < nextX) {
                facing = Direction.Up;
                if (terrainCollision() || nonPlayerEntityCollision()){
                    facing = Direction.Right;
                }
            }

            else if (topLengthY < nextY && leftWidthX > nextX) {
                facing = Direction.Down;
                if (terrainCollision() || nonPlayerEntityCollision()){
                    facing = Direction.Left;
                }
            }

            else if (topLengthY < nextY && leftWidthX < nextX) {
                facing = Direction.Down;
                if (terrainCollision() || nonPlayerEntityCollision()){
                    facing = Direction.Right;
                }
            }
            updateCollisionValues();
            int nextColumn = pathfinder.getPathList().get(0).getColumn();
            int nextRow = pathfinder.getPathList().get(0).getRow();
            if (nextColumn == goalColumn && nextRow == goalRow){
                pathing = false;
            }
        }
    }

    protected BufferedImage determineOneSprite(){
        BufferedImage image = null;
        if(aggroImage != null && aggro){
            image = aggroImage;
        }
        else{
            image = determineSprite();
        }
        return image;
    }

    /**
     * Validates the spawning of the enemy at the parameter position. This will check terrain collision and non player
     * entity collision. If successful the enemy's xPosition and yPosition will be set the parameter values, and it's
     * collision values will be adjusted.
     * If unsuccessful, it will set the enemy's xPosition and yPosition to 0,0 and return false.
     *
     * @param x The x location the enemy will spawn at
     * @param y The y location the enemy will spawn at
     * @return True if the entity has no collision with non-traversable tiles and non player entities, else false
     */
    // TODO enemy can spawn on top of player, make it so that the coordinates passed here never overlap the player
    @Override
    public boolean validateSpawn(int x, int y){
        return super.validateSpawn(x, y) && !nonPlayerEntityCollision();
    }


    public int getFramesOutOfRange(){
        return this.framesOutOfPlayerRange;
    }
}
