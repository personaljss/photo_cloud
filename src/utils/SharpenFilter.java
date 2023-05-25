package utils;

import java.awt.image.BufferedImage;

import services.ImageMatrix;
import services.Logger;

public class SharpenFilter extends PhotoFilter {
    /**
     *
     */
    private static final long serialVersionUID = -7653226720981526309L;
    private static final double SHARPEN_FACTOR = 1;

    @Override
    public BufferedImage apply(ImageMatrix image, int value) {
		long startTime = System.currentTimeMillis();
        // Apply the blur filter to the original image
        BlurFilter blurFilter = new BlurFilter();
        BufferedImage blurredImage = blurFilter.apply(image, value);

        // Get the dimensions of the image
        int width = image.getWidth();
        int height = image.getHeight();

        // Create a new image matrix to store the sharpened image
        ImageMatrix sharpenedMatrix = new ImageMatrix(width, height);

        // Iterate over each pixel in the image
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                // Get the original pixel value
                int originalPixel = image.getRGB(i, j);

                // Get the corresponding pixel value from the blurred image
                int blurredPixel = blurredImage.getRGB(i, j);

                // Extract the red, green, and blue components of the pixels
                int originalRed = (originalPixel >> 16) & 0xFF;
                int originalGreen = (originalPixel >> 8) & 0xFF;
                int originalBlue = originalPixel & 0xFF;

                int blurredRed = (blurredPixel >> 16) & 0xFF;
                int blurredGreen = (blurredPixel >> 8) & 0xFF;
                int blurredBlue = blurredPixel & 0xFF;

                // Calculate the sharpened pixel values
                int sharpenedRed = clamp(originalRed + (int) ((originalRed - blurredRed) * SHARPEN_FACTOR));
                int sharpenedGreen = clamp(originalGreen + (int) ((originalGreen - blurredGreen) * SHARPEN_FACTOR));
                int sharpenedBlue = clamp(originalBlue + (int) ((originalBlue - blurredBlue) * SHARPEN_FACTOR));

                // Combine the sharpened components into a single pixel value
                int sharpenedPixel = (sharpenedRed << 16) | (sharpenedGreen << 8) | sharpenedBlue;

                // Set the pixel value in the sharpened image matrix
                sharpenedMatrix.setRGB(i, j, sharpenedPixel);
            }
        }
		long endTime = System.currentTimeMillis();
		long elapsedTime = endTime - startTime;
		Logger.getInstance().logInfo(toString()+" applied. Execution:"+elapsedTime);

        // Return the sharpened image as a BufferedImage
        return sharpenedMatrix.getBufferedImage();
    }

    private int clamp(int value) {
        return Math.max(0, Math.min(value, 255));
    }

    @Override
    public String toString() {
        return "Sharpen Filter";
    }
}
