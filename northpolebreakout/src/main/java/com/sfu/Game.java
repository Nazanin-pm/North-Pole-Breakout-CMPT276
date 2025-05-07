package com.sfu;

import com.sfu.Entities.*;
import com.sfu.Menus.EndMenu;
import com.sfu.Pathfinding.Pathfinder;
import com.sfu.Tiles.Board;
import com.sfu.Tiles.Tile;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Game class manages the game logic, screen rendering, and user interactions.
 * It handles initializing the game entities, managing difficulty levels, and controlling the game loop.
 */
public class Game extends JPanel implements Runnable {

    // Screen settings
    final int originalTileSize = 16; // a tile is 16x16
    final int scale = 6;
    final int tileSize = originalTileSize * scale; // tile displayed on screen is 96x96, divisible by 16/32

    final int fps = 120;

    private Dimension screenSize;

    // World settings
    public int columns; // total tiles
    public int rows; // eventually initialize this based on input board size

    private boolean running;
    public static Game instance;
    private static String difficultyLevel;

    private boolean showExitMessage = false;
    public boolean exitMessageShown = false;
    private double messageEndTime = 0;
    private int rewardsNeeded = 0;
    private final Sounds sound;
    Thread gameThread;
    BufferedImage background; // Declare the background variable here

    Board board;
    Player player;
    ArrayList<Entity> entities;
    ArrayList<Entity> enemies;

    Pathfinder pathfinder;
    private final Time time;

    public Sounds getSound(){
        return sound;
    }

    /**
     * Private constructor to initialize the game board, player, enemies, and background image.
     */
    private Game(String difficultyLevel, Dimension size) {
        setDifficulty(difficultyLevel);

        screenSize = size;
        this.setPreferredSize(size);

        time = new Time();


        sound = new Sounds();
        // Start background music
        sound.playBackgroundMusic();

        setBackground();
        board = new Board(this);

        rows = board.getRows();
        columns = board.getColumns();
        rewardsNeeded = board.getTotalPresents();

        pathfinder = new Pathfinder(this);

        entities = new ArrayList<>();
        enemies = new ArrayList<>();

        createEnemies();
    }

    /**
     * Helper method to make code organized
     */
    private void setBackground(){

        try {
            background = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Tiles/background.jpeg")));
            if (background != null) {
                background = resizeImage(background, screenSize.width, screenSize.height);
            } else {
                System.out.println("Background image not found or failed to load.");
            }
        } catch (IOException | NullPointerException e) {
            System.out.println("Error loading background image: " + e.getMessage());
            e.printStackTrace();
        }
    }
    

    /**
     * Create enemies and rudolphs in the game
     */
    private void createEnemies(){

        int totalEnemies = switch(difficultyLevel){
            case "Easy" -> 6;
            case "Medium" -> 8;
            case "Hard" -> 10;
            default -> 0;
        };

        int totalRudolphs = switch(difficultyLevel){
            case "Easy", "Medium" -> 2;
            case "Hard" -> 1;
            default -> 0;
        };

        // Loop to create and add Elves to the enemies list
        for (int i = 0; i < totalEnemies-1; i++) {
            Elf elf = new Elf(this, getDifficultySpeedAdjustment());
            entities.add(elf);
            enemies.add(elf);
        }

        if(totalEnemies != 0) {
            Point santaSpawn = board.getSantaSpawnPoint();
            Santa santa = new Santa(this, santaSpawn.x * tileSize, santaSpawn.y * tileSize, getDifficultySpeedAdjustment());
            entities.add(santa);
            enemies.add(santa);
        }

        // Loop to create and add rudolphs to enemy list
        for (int i = 0; i < totalRudolphs; i++) {
            Rudolph rudolph = new Rudolph(this);
            entities.add(rudolph);
        }
    }

    

    /**
     * Creates a new game instance with a specified difficulty level.
     *
     * @param difficulty The difficulty level of the game.
     * @param size Dimension variable representing the size of the player's screen
     * @return The created Game instance.
     */
    public static Game createGame(String difficulty, Dimension size) {
        instance = new Game(difficulty, size); // Set difficulty level
        instance.player = Player.createPlayer(instance);
        return instance;
    }

    /**
     * Sets the difficulty level of the game.
     * @param difficulty The difficulty level to be set.
     */
    public void setDifficulty(String difficulty) {
        difficultyLevel = switch (difficulty){
            case "Easy"   -> "Easy";
            case "Medium" -> "Medium";
            case "Hard"   -> "Hard";
            case "Test"   -> "Test";
            default       -> "Easy";
        };
    }

    public String getDifficulty(){
        return difficultyLevel;
    }

    /**
     * Resizes a given BufferedImage to specified dimensions.
     * @param originalImage The original image to resize.
     * @param width The target width.
     * @param height The target height.
     * @return The resized BufferedImage.
     */
    private BufferedImage resizeImage(BufferedImage originalImage, int width, int height) {
        if (originalImage == null) {
            return null;
        }
        BufferedImage resizedImage = new BufferedImage(width, height, originalImage.getType());
        Graphics2D g2d = resizedImage.createGraphics();
        g2d.drawImage(originalImage, 0, 0, width, height, null);
        g2d.dispose();
        return resizedImage;
    }

    /**
     * Starts the game thread, beginning the game loop.
     */
    public void startGameThread() {
        running = true;
        gameThread = new Thread(this);
        gameThread.start();
        time.start();

    }

    /**
     * Stops the game and joins the game thread.
     */
    public void stop() {

        if (running) {
            running = false;
            try {
                gameThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

    }
    }

    @Override
    public void run() {
        double drawInterval = (double) 1000000000 / fps;
        double delta = 0;
        boolean firstDraw = false;
        boolean secondDraw = false;
        boolean thirdDraw = false;
        boolean fourthDraw = false;


        long lastTime = System.nanoTime();

        while (running) {
            long currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            lastTime = currentTime;

            if (delta >= 0 && !firstDraw) {
                repaint();
                firstDraw = true;
            }
            else if (delta >= 0.5 && !secondDraw) {
                repaint();
                secondDraw = true;
            }
            else if (delta >= 1 && !thirdDraw) {
                update();
                repaint();
                thirdDraw = true;
            }
            else if (delta >= 1.5 && !fourthDraw) {
                repaint();
                fourthDraw = true;
            }
            else if (delta >= 2) {
                repaint();
                delta = 0;
                firstDraw = false;
                secondDraw = false;
                thirdDraw = false;
                fourthDraw = false;
            }
        }
    }

    /**
     * Updates the game logic including player and enemy positions.
     */
    public void update() {
        for (Entity entity : entities) {
            entity.update();
        }

        player.update();

        if(player.getRewardsCollected() == board.getTotalPresents() && !exitMessageShown){
            showExitMessage = true;
            exitMessageShown = true;
            messageEndTime = time.getElapsedTimeInSeconds() + 10;
        }
    }
    
    /**
     * Display an exit message for a time
     * @param g2d uses this to draw
     */
    public void showExitMessage(Graphics2D g2d){
        if (showExitMessage) {
            g2d.setFont(new Font("Calibiri", Font.BOLD, 42));
            g2d.setColor(Color.RED);
            g2d.drawString("Find Santa's house to escape!", 450, 50);
    
            if (time.getElapsedTimeInSeconds() > messageEndTime) {
                showExitMessage = false;
            }
        }
    }

    /**
     * Adjusts enemy speed based on difficulty level.
     * @return Speed adjustment factor for enemies.
     */
    public double getDifficultySpeedAdjustment() {
        return switch (difficultyLevel) {
            case "Hard" -> 1.2;  // Fast speed multiplier
            case "Medium" -> 1.0;
            case "Easy" -> 0.8;
            default -> 0.8;  // Default normal speed if no valid difficulty
        };
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        board.draw(g2d);
        for (Entity entity : entities) {
            entity.draw(g2d);
        }
        player.draw(g2d);

        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Calibiri", Font.BOLD, 36));


        g2d.drawString(player.getRewardsCollected() + "/" + rewardsNeeded + " presents collected", 30, 100);
        g2d.drawString("Score: " + player.getPoints(), 30 , 50);
        g2d.drawString("Time: " + time.getFormattedTime(), 1080, 50);


        showExitMessage(g2d);

        g2d.dispose();
    }

    public void setTimer(int time){
        this.time.setTime(time);
    }

    // Getter methods

    public int getTileSize() {
        return tileSize;
    }

    public int getColumns(){
        return columns;
    }

    public int getRows(){
        return rows;
    }

    public int getScreenWidth() {
        return screenSize.width;
    }

    public int getScreenHeight() {
        return screenSize.height;
    }

    public int getFPS(){
        return fps;
    }
    
    public Player getPlayer() {
        return this.player;
    }

    public Point getPlayerSpawn(){
        return board.getPlayerSpawnPoint();
    }

    /**
     * Ends the game and resets player instance.
     */
    public void endGame(boolean playerWon) {
        sound.stopBackgroundMusic();  // Stop background music when the game ends
        time.stop();

        try{
        JFrame mainFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        if (mainFrame != null) {
            // the score
            int score = player.getPoints();
            double timePlayed = time.getElapsedTimeInSeconds();

            EndMenu end = new EndMenu(mainFrame, difficultyLevel, score, timePlayed, playerWon);
            end.setVisible(true);
        }
    }catch(Exception e){
        e.printStackTrace();
    }

        stop();
        synchronized (this) { 
            Player.resetInstance();
            instance = null; 
        }
    }

    public ArrayList<Point> getTraversableTiles() {
        return board.getTraversableTiles();
    }

    // Additional game functions

    public Pathfinder getPathfinder(){
        return this.pathfinder;
    }
    public Point getPlayerPosition() {
        return new Point(player.getXPosition(), player.getYPosition());
    }
    public int getPlayerScreenXPosition(){
        return player.getScreenXPosition();
    }
    public int getPlayerScreenYPosition(){
        return player.getScreenYPosition();
    }
    public int convertXtoPlayerScreenX(int x){
        return player.getScreenXRelativeTo(x);
    }
    public int convertYtoPlayerScreenY(int y){
        return player.getScreenYRelativeTo(y);
    }
    public boolean locationVisibleToPlayerScreen(int x, int y){
        return player.isVisibleToPlayer(x, y);
    }
    public boolean locationVisibleToPlayerScreen(int x, int y, double scale){
        return player.isVisibleToPlayer(x, y, scale);
    }
    public boolean locationNotTraversable(int row, int column){
        return !board.getTileAt(row, column).isTraversable();
    }
    public Tile getTileAt(int row, int column){
        return board.getTileAt(row,column);
    }
    public int getScale(){
        return scale;
    }

    public Board getBoard(){
        return this.board;
    }
    public int getRewardsNeeded(){
        return rewardsNeeded;
    }

    public boolean isRunning(){
        return running;
    }

    public void addEnemy(Enemy e){
        entities.add(e);
        enemies.add(e);
    }
}