package controller.commands;

import controller.ImageOperationCommand;
import model.ImageProcessor;

/**
 * This class stores the information required to convert a given image to a sepia-tone image.
 */
public class Sepia implements ImageOperationCommand {
  private final String imageName;
  private final String resultingImageName;

  /**
   * Constructs a sepia command object given the original image name and the resulting image name.
   *
   * @param imageName          the name of the image to get the sepia-tone of
   * @param resultingImageName the name of the resulting image
   */
  public Sepia(String imageName, String resultingImageName) {
    this.imageName = imageName;
    this.resultingImageName = resultingImageName;
  }

  @Override
  public void execute(ImageProcessor model) {
    model.sepia(this.imageName, this.resultingImageName);
  }
}
