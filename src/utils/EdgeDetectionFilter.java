package utils;

import java.awt.image.BufferedImage;

import services.ImageMatrix;

/**
 * The EdgeDetectionFilter class applies an edge detection algorithm to an image.
 * It detects the edges in the image using the Sobel operator.
 */
public class EdgeDetectionFilter extends PhotoFilter {

    /**
     * Applies the edge detection filter to the given image matrix.
     *
     * @param imageMatrix the image matrix to apply the filter to
     * @param value       an optional value for the filter (not used in this implementation)
     * @return the filtered image as a BufferedImage
     */
    @Override
    public BufferedImage apply(ImageMatrix imageMatrix, int value) {
        // Convert the image matrix to a grayscale image
        GrayscaleFilter grayscaleFilter = new GrayscaleFilter();
        ImageMatrix grayscaleImage = new ImageMatrix(grayscaleFilter.apply(imageMatrix, value));

        // Apply a slight blur to the grayscale image
        BlurFilter blurFilter = new BlurFilter();
        ImageMatrix blurredImage = new ImageMatrix(blurFilter.apply(grayscaleImage, 5));

        int width = imageMatrix.getWidth();
        int height = imageMatrix.getHeight();

        ImageMatrix edgeImage = new ImageMatrix(width, height);

        // Define the Sobel operator kernels
        int[][] xKernel = { { -1, 0, 1 }, { -2, 0, 2 }, { -1, 0, 1 } };
        int[][] yKernel = { { -1, -2, -1 }, { 0, 0, 0 }, { 1, 2, 1 } };

        // Apply the Sobel operator to detect edges
        for (int y = 1; y < height - 1; y++) {
            for (int x = 1; x < width - 1; x++) {
                int xGradient = applyKernel(blurredImage, xKernel, x, y);
                int yGradient = applyKernel(blurredImage, yKernel, x, y);

                int gradient = (int) Math.sqrt(xGradient * xGradient + yGradient * yGradient);

                // Set the gradient value as the RGB value for the edge pixel
                edgeImage.setRGB(x, y, ImageMatrix.convertRGB(gradient, gradient, gradient));
            }
        }

        return edgeImage.getBufferedImage();
    }

    /**
     * Applies the given kernel to the image matrix at the specified location.
     *
     * @param imageMatrix the image matrix to apply the kernel to
     * @param kernel      the kernel to apply
     * @param x           the x-coordinate of the pixel
     * @param y           the y-coordinate of the pixel
     * @return the result of applying the kernel to the pixel
     */
    private int applyKernel(ImageMatrix imageMatrix, int[][] kernel, int x, int y) {
        int result = 0;

        int kSize = kernel.length;
        int kHalf = kSize / 2;

        for (int j = -kHalf; j <= kHalf; j++) {
            for (int i = -kHalf; i <= kHalf; i++) {
                int intensity = imageMatrix.getRed(x + i, y + j); // Use red channel for intensity

                result += kernel[j + kHalf][i + kHalf] * intensity;
            }
        }

        return result;
    }

    /**
     * Returns a string representation of the filter.
     *
     * @return the name of the filter
     */
    @Override
    public String toString() {
        return "Edge Detection Filter";
    }
}
