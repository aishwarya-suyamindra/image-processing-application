package controller.commands;

import controller.ImageOperationCommand;
import model.ImageProcessor;

/**
 * This class stores the information required to flip the given image along the given axis.
 */
public class Flip implements ImageOperationCommand {
  private final String imageName;
  private final String resultingImageName;
  private final int axis;

  /**
   * Constructs a Flip command object with the given image, resulting image name and axis to flip
   * the image along.
   *
   * @param imageName          the name of the image to be flipped
   * @param resultingImageName the name of the resulting flipped image
   * @param axis               the axis along which the image has to be flipped
   */
  public Flip(String imageName, String resultingImageName, int axis) {
    this.imageName = imageName;
    this.resultingImageName = resultingImageName;
    this.axis = axis;
  }

  /**
   * Executes the flip command.
   *
   * @param model the model object
   */
  @Override
  public void execute(ImageProcessor model) {
    model.flip(imageName, resultingImageName, axis);
  }
}
