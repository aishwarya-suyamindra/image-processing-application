package model;

import utility.CustomImage;

/**
 * This interface represents a set of read-only operations that can be performed on images that
 * are processed.
 */
public interface ImageProcessorRO {
  /**
   * Fetches the image associated with the specified name.
   *
   * @param imageName the name of the image to be fetched
   * @return an image of type ICustomImage
   */
  CustomImage getImage(String imageName);
}
