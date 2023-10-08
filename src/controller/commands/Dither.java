package controller.commands;

import controller.ImageOperationCommand;
import model.ImageProcessor;

/**
 * This class stores the information to convert a given color image to a dithered image (reduce
 * the colors in the image to just a few colors).
 */
public class Dither implements ImageOperationCommand {
  private final String imageName;
  private final String resultingImageName;

  /**
   * Constructs a dither command object given the original image name and the resulting image name.
   *
   * @param imageName          the name of the image to get the sepia-tone of
   * @param resultingImageName the name of the resulting image
   */
  public Dither(String imageName, String resultingImageName) {
    this.imageName = imageName;
    this.resultingImageName = resultingImageName;
  }

  @Override
  public void execute(ImageProcessor model) {
    model.dither(this.imageName, this.resultingImageName);
  }
}
