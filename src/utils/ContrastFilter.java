package utils;

import java.awt.image.BufferedImage;

import services.ImageMatrix;
import services.Logger;

/**
 * The ContrastFilter class represents a photo filter that adjusts the contrast of an image.
 * It increases or decreases the difference between the light and dark regions of the image,
 * resulting in a more pronounced or muted contrast.
 */
public class ContrastFilter extends PhotoFilter {

    private static final long serialVersionUID = 123456789L;

    /**
     * Applies the contrast filter to the given image.
     *
     * @param imageMatrix the image matrix representing the image
     * @param value       the value representing the strength of the contrast adjustment
     *                    A value of 0 represents no change, negative values decrease the contrast,
     *                    and positive values increase the contrast.
     * @return the filtered image with adjusted contrast
     */
    @Override
    public BufferedImage apply(ImageMatrix imageMatrix, int value) {
        double contrast = 1.0 + (double) value / 100.0; // Convert the value to a contrast factor
		long startTime = System.currentTimeMillis();
        int width = imageMatrix.getWidth();
        int height = imageMatrix.getHeight();

        ImageMatrix filteredImage = new ImageMatrix(width, height);

        // Apply the contrast filter to each pixel
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb = imageMatrix.getRGB(x, y);

                int red = (rgb >> 16) & 0xFF;
                int green = (rgb >> 8) & 0xFF;
                int blue = rgb & 0xFF;

                // Adjust the contrast of each color channel
                red = adjustContrast(red, contrast);
                green = adjustContrast(green, contrast);
                blue = adjustContrast(blue, contrast);

                int filteredRGB = (red << 16) | (green << 8) | blue;
                filteredImage.setRGB(x, y, filteredRGB);
            }
        }
		long endTime = System.currentTimeMillis();
		long elapsedTime = endTime - startTime;
		Logger.getInstance().logInfo(toString()+" applied. Execution:"+elapsedTime);
        return filteredImage.getBufferedImage();
    }

    /**
     * Adjusts the contrast of a color value based on the contrast factor.
     *
     * @param color    the color value to adjust
     * @param contrast the contrast factor
     * @return the adjusted color value
     */
    private int adjustContrast(int color, double contrast) {
        // Adjust the contrast of the color value
        double adjustedColor = ((color / 255.0 - 0.5) * contrast + 0.5) * 255.0;

        // Clamp the color value to the valid range [0, 255]
        if (adjustedColor < 0) {
            adjustedColor = 0;
        } else if (adjustedColor > 255) {
            adjustedColor = 255;
        }

        return (int) adjustedColor;
    }

    /**
     * Returns a string representation of the ContrastFilter object.
     *
     * @return a string describing the filter
     */
    @Override
    public String toString() {
        return "Contrast Filter";
    }
}
