package com.sfu;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * The {@code ImageScaler} class provides functionality to scale images to a desired size.
 * It supports scaling an image to default dimensions or to specified width and height.
 */
public class ImageScaler {
    private int defaultWidth, defaultHeight;

    /**
     * Default constructor for {@code ImageScaler}.
     * This constructor initializes the scaler without any default dimensions.
     */
    public ImageScaler() {}

    /**
     * Constructs an {@code ImageScaler} object with specified default dimensions.
     *
     * @param width  the default width to scale images to
     * @param height the default height to scale images to
     */
    public ImageScaler(int width, int height) {
        defaultWidth = width;
        defaultHeight = height;
    }

    /**
     * Scales the given image to the default width and height set for this {@code ImageScaler}.
     *
     * @param originalImage the {@code BufferedImage} to be scaled
     * @return a new {@code BufferedImage} scaled to the default dimensions
     */
    public BufferedImage scaleImage(BufferedImage originalImage) {
        BufferedImage scaledImage = new BufferedImage(defaultWidth, defaultHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = scaledImage.createGraphics();
        graphics.drawImage(originalImage, 0, 0, defaultWidth, defaultHeight, null);
        graphics.dispose();
        return scaledImage;
    }

    /**
     * Scales the given image to the specified width and height.
     *
     * @param originalImage the {@code BufferedImage} to be scaled
     * @param newWidth      the desired width of the scaled image
     * @param newHeight     the desired height of the scaled image
     * @return a new {@code BufferedImage} scaled to the specified dimensions
     */
    public BufferedImage scaleImage(BufferedImage originalImage, int newWidth, int newHeight) {
        BufferedImage scaledImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = scaledImage.createGraphics();
        graphics.drawImage(originalImage, 0, 0, newWidth, newHeight, null);
        graphics.dispose();
        return scaledImage;
    }
}
