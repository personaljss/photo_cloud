package utils;

import java.awt.image.BufferedImage;

import services.ImageMatrix;

public class BlurFilter implements ImageFilter{
	/**
     * Applies a Gaussian blur effect to the original image.
     * 
     * @return the blurred image
     */
	@Override
	public BufferedImage apply(ImageMatrix imageMatrix) {
		// TODO Auto-generated method stub
		// Use a 5x5 Gaussian blur kernel for more blur
        int[][] kernel = { { 1, 4, 6, 4, 1 }, { 4, 16, 24, 16, 4 }, { 6, 24, 36, 24, 6 }, { 4, 16, 24, 16, 4 },
                { 1, 4, 6, 4, 1 } };
        int kernelSum = 256; // Sum of all elements in the kernel

        ImageMatrix blurredMatrix = new ImageMatrix(imageMatrix.getWidth(), imageMatrix.getHeight());

        // Apply the blur kernel to each pixel in the image
        for (int i = 2; i < imageMatrix.getWidth() - 2; i++) {
            for (int j = 2; j < imageMatrix.getHeight() - 2; j++) {
                int red = 0, green = 0, blue = 0;

                for (int ki = -2; ki <= 2; ki++) {
                    for (int kj = -2; kj <= 2; kj++) {
                        red += kernel[ki + 2][kj + 2] * imageMatrix.getRed(i + ki, j + kj);
                        green += kernel[ki + 2][kj + 2] * imageMatrix.getGreen(i + ki, j + kj);
                        blue += kernel[ki + 2][kj + 2] * imageMatrix.getBlue(i + ki, j + kj);
                    }
                }

                red /= kernelSum;
                green /= kernelSum;
                blue /= kernelSum;

                blurredMatrix.setRGB(i, j, ImageMatrix.convertRGB(red, green, blue));
            }
        }

        return blurredMatrix.getBufferedImage();
	}

}
