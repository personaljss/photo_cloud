package utils;

import java.awt.image.BufferedImage;

import services.ImageMatrix;
import services.Logger;

public class BrightnessFilter extends PhotoFilter {
    /**
	 * 
	 */
	private static final long serialVersionUID = -40417145606231903L;

	@Override
    public BufferedImage apply(ImageMatrix image, int value) {
        // Check if the brightness value is within the valid range
		long startTime = System.currentTimeMillis();
        if (value < -255 || value > 255) {
            throw new IllegalArgumentException("value must be between -255 and 255");
        }

        // Apply the brightness adjustment to each pixel in the image
        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                int pixel = image.getRGB(i, j);

                // Extract the color components (red, green, blue, alpha) from the pixel
                int alpha = (pixel >> 24) & 0xFF;
                int red = (pixel >> 16) & 0xFF;
                int green = (pixel >> 8) & 0xFF;
                int blue = pixel & 0xFF;

                // Adjust the brightness by adding the value to each color component
                red = clamp(red + value);
                green = clamp(green + value);
                blue = clamp(blue + value);

                // Create the adjusted pixel value
                int adjustedPixel = (alpha << 24) | (red << 16) | (green << 8) | blue;

                // Set the adjusted pixel in the image matrix
                image.setRGB(i, j, adjustedPixel);
            }
        }

		long endTime = System.currentTimeMillis();
		long elapsedTime = endTime - startTime;
		Logger.getInstance().logInfo(toString()+" applied. Execution:"+elapsedTime);
        // Return the modified image as a BufferedImage
        return image.getBufferedImage();
    }

    private int clamp(int value) {
        // Ensure the value is within the valid color range (0-255)
        return Math.max(0, Math.min(value, 255));
    }

    @Override
    public String toString() {
        return "Brightness Filter";
    }

}
