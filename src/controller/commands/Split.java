package controller.commands;

import controller.ImageOperationCommand;
import model.ImageProcessor;

/**
 * This class stores all the information required to split an image into three greyscale images.
 */
public class Split implements ImageOperationCommand {
  private final String imageName;
  private final String resultingRedImageName;
  private final String resultingGreenImageName;
  private final String resultingBlueImageName;

  /**
   * Constructs a split command object with the given image and the name of the resulting
   * greyscale images for the individual channels.
   *
   * @param imageName            the name of the image to be split
   * @param resultRedImageName   the name of the resulting greyscale image of the red channel
   * @param resultGreenImageName the name of the resulting greyscale image of the green channel
   * @param resultBlueImageName  the name of the resulting greyscale image of the blue channel
   */
  public Split(String imageName, String resultRedImageName,
               String resultGreenImageName, String resultBlueImageName) {
    this.imageName = imageName;
    this.resultingRedImageName = resultRedImageName;
    this.resultingGreenImageName = resultGreenImageName;
    this.resultingBlueImageName = resultBlueImageName;
  }

  /**
   * Executes the split command.
   *
   * @param model the model object
   */
  @Override
  public void execute(ImageProcessor model) {
    model.split(imageName, resultingRedImageName, resultingGreenImageName, resultingBlueImageName);
  }
}
