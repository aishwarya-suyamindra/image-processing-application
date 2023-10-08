package model;

import utility.Pixel;

/**
 * This interface supports simple operations on IPixel.
 */
interface PixelOperation {
  /**
   * Modifies the originalPixel by performing the required operation on it.
   *
   * @param originalPixel the pixel to be modified
   * @param maxPixelValue maximum value a channel of the pixel can have.
   * @return modified Pixel
   */
  Pixel modifyPixel(Pixel originalPixel, int maxPixelValue);
}
