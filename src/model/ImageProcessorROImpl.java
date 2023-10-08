package model;

import utility.CustomImage;

/**
 * This class represents a read-only version of the image processor model.
 */
public class ImageProcessorROImpl implements ImageProcessorRO {
  private final ImageProcessor model;

  /**
   * Constructs a ImageProcessorROImpl model with the given model.
   *
   * @param model the ImageProcessor model.
   */
  public ImageProcessorROImpl(ImageProcessor model) {
    this.model = model;
  }

  @Override
  public CustomImage getImage(String imageName) {
    return model.getImage(imageName);
  }
}
