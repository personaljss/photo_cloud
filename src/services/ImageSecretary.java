package services;

import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

/**
 * ImageSecretary is used for basic I/O operations in terms of images.
 * 
 * @author osman.yasal
 *
 */
public class ImageSecretary {

	private ImageSecretary() {

	}

	/**
	 * Reads the image from your resources.
	 * 
	 * @param imageLocation    name of the file
	 * @param extension of the file
	 * @return new ImageMatrix
	 * @throws IOException
	 */
	public static ImageMatrix readResourceImage(String imageLocation) throws IOException {
		return new ImageMatrix(ImageIO.read(new File(imageLocation )));
	}

	/**
	 * Writes the rendered image to your resources with the given name and extension
	 * 
	 * @param image     rendered image
	 * @param name      of the file
	 * @param extension of the file
	 * @return
	 */
	public static boolean writeFilteredImageToResources(ImageMatrix image, String imagePath) {
		return writeImageToResources(image.getBufferedImage(), imagePath);
	}

	public static List<String> getRawImageNames(String dirPath) {
		List<String> res = new ArrayList<>();
		File[] files = new File(dirPath).listFiles();

		for (File file : files) {
			if (file.isFile() && !file.getName().contains("_")) {
				res.add(file.getName());
			}
		}
		return res;
	}

	/**
	 * Writes the rendered image to your resources with the given name as jpg
	 * 
	 * @param image
	 * @param name
	 * @return
	 */
	private static boolean writeImageToResources(RenderedImage image, String imagePath) {
		boolean result = true;
		try {
			ImageIO.write(image, "jpg", new File(imagePath));
		} catch (IOException e) {
			e.printStackTrace();
			result = false;
		}

		return result;
	}

}
