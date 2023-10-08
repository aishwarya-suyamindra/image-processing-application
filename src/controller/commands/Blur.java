package controller.commands;

import controller.ImageOperationCommand;
import model.ImageProcessor;

/**
 * This class stores the information required to blur a given image.
 */
public class Blur implements ImageOperationCommand {
  private final String imageName;
  private final String resultingImageName;

  /**
   * Constructs a blur command object given the original image name and the resulting image name.
   *
   * @param imageName          the name of the image to blur
   * @param resultingImageName the name of the resulting image
   */
  public Blur(String imageName, String resultingImageName) {
    this.imageName = imageName;
    this.resultingImageName = resultingImageName;
  }

  @Override
  public void execute(ImageProcessor model) {
    model.blur(this.imageName, this.resultingImageName);
  }
}
