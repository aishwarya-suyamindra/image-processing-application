package controller.commands;

import controller.ImageOperationCommand;
import model.ImageProcessor;

/**
 * This class stores the information required to brighten an image by the given value.
 */
public class Brighten implements ImageOperationCommand {
  private final int incrementValue;
  private final String imageToBrighten;
  private final String resultingImageName;

  /**
   * Constructs a brighten command object with the given image name, resulting image name and
   * increment value.
   *
   * @param imageToBrighten    the name of the image be brightened
   * @param resultingImageName the name of the resulting brightened image
   * @param incrementValue     the constant value by which the image is to be brightened
   */
  public Brighten(int incrementValue, String imageToBrighten, String resultingImageName) {
    this.incrementValue = incrementValue;
    this.imageToBrighten = imageToBrighten;
    this.resultingImageName = resultingImageName;
  }

  /**
   * Executes the brighten command.
   *
   * @param model the model object
   */
  @Override
  public void execute(ImageProcessor model) {
    model.brighten(imageToBrighten, resultingImageName, incrementValue);
  }
}
