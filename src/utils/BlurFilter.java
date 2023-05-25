package utils;

import java.awt.image.BufferedImage;

import services.ImageMatrix;
import services.ImageSecretary;
import services.Logger;

public class BlurFilter extends PhotoFilter{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7142024283650567724L;

	


	/**
     * Applies a Gaussian blur effect to the original image.
     * 
     * @return the blurred image
     */
	@Override
	public BufferedImage apply(ImageMatrix imageMatrix, int value) {
		long startTime = System.currentTimeMillis();

	    // Check if the blurValue is within the valid range
	    if (value < 0 || value > 100) {
	        throw new IllegalArgumentException("value must be between 0 and 100");
	    }

	    // Calculate the kernel size based on the blur value
	    int kernelSize = (int) (value * 0.05);

	    // Ensure the kernel size is odd
	    if (kernelSize % 2 == 0) {
	        kernelSize++; // Make it odd by incrementing
	    }

	    // Calculate the kernel radius
	    int kernelRadius = kernelSize / 2;

	    ImageMatrix blurredMatrix = new ImageMatrix(imageMatrix.getWidth(), imageMatrix.getHeight());

	    // Apply the blur kernel to each pixel in the image
	    for (int i = 0; i < imageMatrix.getWidth(); i++) {
	        for (int j = 0; j < imageMatrix.getHeight(); j++) {
	            int red = 0, green = 0, blue = 0;
	            int count = 0;

	            // Apply the blur kernel to the pixels around the current pixel
	            for (int ki = -kernelRadius; ki <= kernelRadius; ki++) {
	                for (int kj = -kernelRadius; kj <= kernelRadius; kj++) {
	                    int x = i + ki;
	                    int y = j + kj;

	                    // Check if the current pixel is within the image bounds
	                    if (x >= 0 && x < imageMatrix.getWidth() && y >= 0 && y < imageMatrix.getHeight()) {
	                        red += imageMatrix.getRed(x, y);
	                        green += imageMatrix.getGreen(x, y);
	                        blue += imageMatrix.getBlue(x, y);
	                        count++;
	                    }
	                }
	            }

	            // Calculate the average RGB values
	            if (count > 0) {
	                red /= count;
	                green /= count;
	                blue /= count;
	            }

	            // Set the blurred pixel color
	            blurredMatrix.setRGB(i, j, ImageMatrix.convertRGB(red, green, blue));
	        }
	    }
	    

		long endTime = System.currentTimeMillis();
		long elapsedTime = endTime - startTime;
		Logger.getInstance().logInfo(toString()+" applied. Execution:"+elapsedTime);

	    return blurredMatrix.getBufferedImage();
	}



	@Override
	public String toString() {
		return "Blur Filter";
	}


}
