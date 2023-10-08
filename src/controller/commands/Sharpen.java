package controller.commands;

import controller.ImageOperationCommand;
import model.ImageProcessor;

/**
 * This class stores the information required to sharpen a given image.
 */
public class Sharpen implements ImageOperationCommand {
  private final String imageName;
  private final String resultingImageName;

  /**
   * Constructs a sharpen command object given the original image name and the resulting image name.
   *
   * @param imageName          the name of the image to sharpen
   * @param resultingImageName the name of the resulting image
   */
  public Sharpen(String imageName, String resultingImageName) {
    this.imageName = imageName;
    this.resultingImageName = resultingImageName;
  }

  @Override
  public void execute(ImageProcessor model) {
    model.sharpen(this.imageName, this.resultingImageName);
  }
}
