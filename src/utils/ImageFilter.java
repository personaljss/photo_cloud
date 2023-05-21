package utils;

import java.awt.image.BufferedImage;

import services.ImageMatrix;

interface ImageFilter {
	BufferedImage apply(ImageMatrix image);
}