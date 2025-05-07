package com.sfu.Tiles;
import com.sfu.Game;
import com.sfu.ImageScaler;
import com.sfu.Items.Punishment;
import com.sfu.Items.Reward;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Objects;

/**
 * The Board class represents the game board, including tiles, items, and background images.
 * It initializes the game map based on a template file and handles drawing tiles and items on the screen.
 */

public class Board {

    private final Game game;
    private Tile[][] tiles;
    private int[][] boardTemplate;
    private int rows;
    private int columns;
    private int totalPresents = 0;
    private ArrayList<Point> openSpaces;
    private final int tileSize;
    private Point playerSpawnPoint;
    private Point santaSpawnPoint;

    /**
     * Constructs a new Board object.
     *
     * @param game The game instance associated with this board.
     */

    public Board(Game game) {
        this.game = game;
        this.tileSize = game.getTileSize();
        boardInstantiation(game.getDifficulty());

    }

    private void boardInstantiation(String difficulty){
        String boardFilePath = switch (difficulty) {
            case "Easy" -> "/Tiles/testBoard2.txt";
            case "Medium" -> "/Tiles/testBoard2.txt";
            case "Hard" -> "/Tiles/testBoard2.txt";
            case "Test" -> "/Tiles/test.txt";
            default -> "";
        };

        findBoardDimensions(boardFilePath);
        tiles = new Tile[rows][columns];
        boardTemplate = new int[rows][columns];
        loadMapTemplate(boardFilePath);
        getTileImage();

    }

    /**
     * Retrieves the tile at the specified row and column.
     *
     * @param row The row of the tile.
     * @param column The column of the tile.
     * @return The Tile object at the specified position.
     */
    public Tile getTileAt(int row, int column){
        return tiles[row][column];
    }


    // TODO have board file path as a parameter, or game difficulty as a parameter that selects a board file path
    /**
     * Determines the dimensions of the game board by reading a text file. The method calculates the number
     * of rows and columns based on the file's structure, where each line represents a row, and the number
     * of space-separated elements in a line represents the columns. Then sets this class's row and column variables
     * equal to the values determined.
     * */
    private void findBoardDimensions(String boardFilePath){
        BufferedReader reader;
        InputStream is = this.getClass().getResourceAsStream(boardFilePath);
        try {

            reader = new BufferedReader(new InputStreamReader(is));
            String line = reader.readLine();
            int row = 0;

            while (line != null) {
                String[] split = line.split(" ");
                this.columns = split.length;
                line = reader.readLine();
                row++;
            }
            this.rows = row;
        } catch (IOException e) {
            System.out.println("Mark the resources directory as a Resources Root");
        }
    }

    /**
     * Loads the map template from a file and populates the boardTemplate array with tile data.
     */
    private void loadMapTemplate(String boardFilePath) {
        BufferedReader reader;
        InputStream is = this.getClass().getResourceAsStream(boardFilePath);
        try {

            reader = new BufferedReader(new InputStreamReader(is));
            String line = reader.readLine();
            int row = 0;

            while (line != null) {
                String[] split = line.split(" ");
                for (int column = 0; column < split.length; column++) {
                    boardTemplate[row][column] = Integer.parseInt(split[column]);
                }
                line = reader.readLine();
                row++;
            }
        } catch (IOException e) {
            System.out.println("Mark the resources directory as a Resources Root");
        }

    }

    /**
     * Reads and loads a {@link BufferedImage} image from the specified file path.
     *
     * @param path the path to the image file, relative to the class's resource directory.
     * @return the loaded {@link BufferedImage}, or {@code null} if an error occurs during loading.
     */
    private BufferedImage readImage(String path){

        BufferedImage image = null;
        try{
        image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream(path)));
        }catch(IOException e){
            e.printStackTrace();
        }

        return image;
    }

    /**
     * Scales the given image to the specified tile size using an {@link ImageScaler}.
     *
     * @param image the {@link BufferedImage} to be scaled.
     * @return a new {@link BufferedImage} scaled to the specified dimensions.
     */
    private BufferedImage scaleImage(BufferedImage image){
        ImageScaler imageScaler = new ImageScaler();
        image = imageScaler.scaleImage(image, tileSize, tileSize);
        return image;
    }

    /**
     * Loads tile images based on the values in the boardTemplate.
     * Different integers in the template correspond to different types of tiles.
     */
    private void getTileImage() {

            BufferedImage snow = readImage("/Tiles/Snow1.png");
            BufferedImage tree = readImage("/Tiles/TreeSnow.png");
            BufferedImage door = readImage("/Tiles/Door.png");
            BufferedImage present = readImage("/Items/present.png");
            BufferedImage bomb = readImage("/Items/bomb.png");


            snow = scaleImage(snow);
            tree = scaleImage(tree);
            door = scaleImage(door);
            bomb = scaleImage(bomb);

            present = scaleImage(present);
            bomb = scaleImage(bomb);


            for (int row = 0; row < rows; row++) {
                for (int column = 0; column < columns; column++) {
                    switch (boardTemplate[row][column]) {
                        case 0 -> {
                            tiles[row][column] = new Ground(column * tileSize, row * tileSize, tileSize);
                            tiles[row][column].setImage(snow);
                        }

                        case 1 -> {
                            tiles[row][column] = new Wall(tileSize);
                            tiles[row][column].setImage(tree);
                        }

                        case 2 -> {
                            tiles[row][column] = new Ground(column * tileSize, row * tileSize, tileSize);
                            tiles[row][column].setImage(snow);
                            tiles[row][column].addItem(new Reward(5, new Rectangle(9 * 4, 5 * 4, 11 * 4, 17 * 4), present));
                            this.totalPresents++;
                        }

                        case 3 -> {
                            tiles[row][column] = new Ground(column * tileSize, row * tileSize, tileSize);
                            tiles[row][column].setImage(snow);
                            tiles[row][column].addItem(new Punishment(-5, new Rectangle(9 * 4, 10 * 4, 8 * 4, 8 * 4), bomb));
                        }

                        case 4 -> {
                            BufferedImage Exit1 = readImage("/Tiles/Exit3/Exit1.png");
                            BufferedImage Exit2 = readImage("/Tiles/Exit3/Exit2.png");
                            BufferedImage Exit3 = readImage("/Tiles/Exit3/Exit3.png");
                            BufferedImage Exit4 = readImage("/Tiles/Exit3/Exit4.png");

                            Exit1 = scaleImage(Exit1);
                            Exit2 = scaleImage(Exit2);
                            Exit3 = scaleImage(Exit3);
                            Exit4 = scaleImage(Exit4);

                            tiles[row][column] = new Wall(tileSize);
                            tiles[row][column].setImage(Exit1);

                            tiles[row][column + 1] = new Wall(tileSize);
                            tiles[row][column + 1].setImage(Exit2);
                            boardTemplate[row][column + 1] = -1;

                            tiles[row + 1][column] = new EndTile(column * tileSize, row * tileSize + tileSize, tileSize);
                            tiles[row + 1][column].setImage(Exit3);
                            boardTemplate[row + 1][column] = -1;

                            tiles[row + 1][column + 1] = new Wall(tileSize);
                            tiles[row + 1][column + 1].setImage(Exit4);
                            boardTemplate[row + 1][column + 1] = -1;
                        }

                        case 5 ->{
                            tiles[row][column] = new Ground(column * tileSize, row * tileSize, tileSize);
                            tiles[row][column].setImage(snow);
                            playerSpawnPoint = new Point(column, row);
                        }

                        case 6 ->{
                            tiles[row][column] = new Ground(column * tileSize, row * tileSize, tileSize);
                            tiles[row][column].setImage(snow);
                            santaSpawnPoint = new Point(column, row);
                        }
                    }

                }
            }
    
                
    }

    public int getTotalPresents(){
        return this.totalPresents;
    }

    /**
     * Gets a list of all traversable tiles on the game board. A traversable tile is:
     * <ul>
     *   <li>marked as traversable.</li>
     *   <li>item less</li>
     * </ul>
     *
     * The list is only created once, and subsequent method calls will return the same list.
     * It is possible an entity has spawned on a tile, so the tiles returned should be also checked for entity collision.
     *
     * @return an {@link ArrayList} of {@link Point} objects, where each point represents the column and row index
     *         of a traversable tile.  The point should be multiplied by the tile size to get the actual game position.
     */
    public ArrayList<Point> getTraversableTiles() {
        if(this.openSpaces == null){
            this.openSpaces = new ArrayList<>();
            for (int row = 0; row < rows; row++) {
                for (int column = 0; column < columns; column++) {
                    Tile tile = getTileAt(row, column);
                    if (tile.isTraversable() && !tile.hasItem() && tile.getEntities().isEmpty()) {
                        this.openSpaces.add(new Point(column, row));
                    }
                }
            }
        }
        return this.openSpaces;
    }

    public void setTotalPresents(int t){
        this.totalPresents = t;
    }


    /**
     * Draws the tiles and items on the game board, rendering only those visible on the player’s screen.
     *
     * @param g2d The Graphics object used to draw the board elements.
     */
    public void draw(Graphics g2d) {
        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                // draw thing at x location, y location, with width size, length size
                if (game.locationVisibleToPlayerScreen(column*tileSize,row*tileSize)) {
                    g2d.drawImage(tiles[row][column].getImage(), game.convertXtoPlayerScreenX(column*tileSize),
                            game.convertYtoPlayerScreenY(row*tileSize), null);
                    if(tiles[row][column].hasItem()){
                        g2d.drawImage(tiles[row][column].getItemImage(),game.convertXtoPlayerScreenX(column*tileSize),
                                game.convertYtoPlayerScreenY(row*tileSize), null);
                        // draw item collision box
                        // g2d.drawRect(game.convertXtoPlayerScreenX(tiles[row][column].item.getCollisionAreaX() + tiles[row][column].xPosition),
                                //game.convertYtoPlayerScreenY(tiles[row][column].item.getCollisionAreaY() + tiles[row][column].yPosition),
                                //tiles[row][column].item.getCollisionAreaWidth(),
                                //tiles[row][column].item.getCollisionAreaHeight());
                    }
                }
            }
        }
    }

    public int getRows() {
        return rows;
    }

    public Tile[][] getTemplate(){
        return this.tiles;
    }

    public int getColumns() {
        return columns;
    }

    public Point getPlayerSpawnPoint(){
        return playerSpawnPoint;
    }

    public Point getSantaSpawnPoint(){
        return santaSpawnPoint;
    }

}
