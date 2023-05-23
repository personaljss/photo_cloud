package utils;

import java.awt.image.BufferedImage;
import java.io.Serializable;

import services.ImageMatrix;

public abstract class PhotoFilter implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1953003547212957846L;

	public abstract BufferedImage apply(ImageMatrix image, int value);
}