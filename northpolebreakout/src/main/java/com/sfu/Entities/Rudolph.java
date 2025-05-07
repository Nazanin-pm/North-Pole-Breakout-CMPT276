package com.sfu.Entities;

import com.sfu.Game;

import java.awt.*;

/**
 * The {@code Rudolph} class represents an enemy character in the game
 * that grants points to the player upon collision and periodically
 * appears and disappears on the screen.
 */
public class Rudolph extends Enemy {
    private boolean isVisible;
    private long spawnTime;
    private final int points;

    // Constructor
    public Rudolph(Game game) {
        super(game);
        this.game = game;
        this.isVisible = false;
        loadImages();
        points = 20;
        super.collisionArea = new Rectangle(11*4,10*4,13*4,14*4);
    }

    // Load images for Rudolph's sprites
    private void loadImages() {
        super.loadImages("/Rudolph/Rudolph.png", "/Rudolph/Rudolph.png",
                   "/Rudolph/Rudolph.png", "/Rudolph/Rudolph.png",
                   "/Rudolph/Rudolph.png", "/Rudolph/Rudolph.png", 
                   "/Rudolph/Rudolph.png", "/Rudolph/Rudolph.png");
    }


    public void update() {
        long currentTime = System.currentTimeMillis();
        if (isVisible && (currentTime - spawnTime > 5000)) { // If visible for more than 5 seconds
            isVisible = false; // Hide Rudolph
            spawnTime = currentTime; // Reset the spawn time for next appearance
        } 
        else if (!isVisible && (currentTime - spawnTime > 3000)) { // If hidden for more than 3 seconds
            if(!spawnAtRandomOpenSpace(5,5)){ // Randomly position Rudolph
                System.out.println("spawn failed");
            }
            isVisible = true; // Make Rudolph visible
            spawnTime = currentTime; // Reset the spawn time for next disappearance
        }
    }

    // Draw Rudolph on the screen
    public void draw(Graphics2D g2d) {
        if (isVisible && game.locationVisibleToPlayerScreen(xPosition, yPosition)) {
            g2d.drawImage(determineSprite(), game.convertXtoPlayerScreenX(xPosition), game.convertYtoPlayerScreenY(yPosition), null);
        }
    }

    @Override
    protected boolean entityCollision(Entity other) {
        return other.collisionWith(this);
    }

    @Override
    protected boolean collisionWith(Enemy enemy) {
        return this.createCollisionHitBox().intersects(enemy.createFutureCollisionHitBox()) && isVisible;
    }

    @Override
    public boolean collisionWith(Player player) {
        if(this.createCollisionHitBox().intersects(player.createFutureCollisionHitBox()) && isVisible) {
            player.addPoints(points);
            spawnTime = System.currentTimeMillis();
            isVisible = false;
            game.getSound().playPresentSound();

        }
        return false;
    }

    public boolean collisionWith(Rudolph rudolph) {
        return false;
    }

    // Getter for visibility
    public boolean isVisible() {
        return isVisible;
    }

}
