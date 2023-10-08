package controller.commands;

import controller.ImageOperationCommand;
import model.ImageProcessor;

/**
 * This class stores the information required to combine the given greyscale images into a single
 * resulting image.
 */
public class Combine implements ImageOperationCommand {
  private final String redImage;
  private final String greenImage;
  private final String blueImage;
  private final String resultingImageName;

  /**
   * Constructs a Combine command object with the given greyscale images and resulting image name.
   *
   * @param resultImageName the name for the resulting combined image
   * @param redImageName    the greyscale image for the red component
   * @param greenImageName  the greyscale image for the green component
   * @param blueImageName   the greyscale image for the blue component
   */
  public Combine(String resultImageName, String redImageName,
                 String greenImageName, String blueImageName) {
    this.resultingImageName = resultImageName;
    this.redImage = redImageName;
    this.greenImage = greenImageName;
    this.blueImage = blueImageName;
  }

  /**
   * Executes the combine command.
   *
   * @param model the model object
   */
  @Override
  public void execute(ImageProcessor model) {
    model.combine(this.redImage, this.greenImage, this.blueImage, this.resultingImageName);
  }
}
