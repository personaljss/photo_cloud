package utils;

import services.ImageMatrix;

interface ImageFilter {
    void apply(ImageMatrix image);
}