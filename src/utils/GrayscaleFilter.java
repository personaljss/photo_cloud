package utils;

import java.awt.image.BufferedImage;

import services.ImageMatrix;

public class GrayscaleFilter extends PhotoFilter {

    /**
	 * 
	 */
	private static final long serialVersionUID = -6390713944567909693L;



	@Override
    public BufferedImage apply(ImageMatrix imageMatrix, int value) {
        // Check if the value is within the valid range
        if (value < 0 || value > 100) {
            throw new IllegalArgumentException("Value must be between 0 and 100");
        }

        int width = imageMatrix.getWidth();
        int height = imageMatrix.getHeight();

        ImageMatrix grayscaleMatrix = new ImageMatrix(width, height);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb = imageMatrix.getRGB(x, y);

                int red = (rgb >> 16) & 0xFF;
                int green = (rgb >> 8) & 0xFF;
                int blue = rgb & 0xFF;

                // Calculate the grayscale value based on the value parameter
                int grayscale = (int) (red * 0.299 + green * 0.587 + blue * 0.114);

                // Apply the value as a modifier to the grayscale value
                grayscale += ((255 - grayscale) * value) / 100;

                // Set the new grayscale RGB value in the grayscale matrix
                grayscaleMatrix.setRGB(x, y, ImageMatrix.convertRGB(grayscale, grayscale, grayscale));
            }
        }

        return grayscaleMatrix.getBufferedImage();
    }


    
    @Override
    public String toString() {
        return "Grayscale Filter ";
    }
    
}


