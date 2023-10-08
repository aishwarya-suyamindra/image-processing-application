package controller.commands;

import java.io.IOException;

import controller.ImageOperationCommand;
import model.ImageProcessor;
import utility.CustomImage;
import utility.IImageHelper;
import utility.IImageHelperFactory;

/**
 * This class represents the functionality to load an image from the given filepath and refer to it
 * by the given image name.
 */
public class Load implements ImageOperationCommand {
  private final String filePath;
  private final String imageName;
  private final IImageHelper imageHelper;

  /**
   * Constructs a load command object with the given filepath, image name and image helper to
   * load the file.
   *
   * @param filePath           the filepath to load the image from
   * @param imageName          the name to refer to the image
   * @param imageHelperFactory the imageHelperFactory which gives the image helper class based on
   *                           the file to load
   */
  public Load(String filePath, String imageName, IImageHelperFactory imageHelperFactory) {
    this.filePath = filePath;
    this.imageName = imageName;
    this.imageHelper = imageHelperFactory.getHelper(this.filePath);
  }

  /**
   * Executes the load command.
   *
   * @param model the model object
   */
  @Override
  public void execute(ImageProcessor model) {
    try {
      CustomImage image = imageHelper.load(filePath, imageName);
      model.loadImage(image);
    } catch (IOException e) {
      throw new RuntimeException("Failed to load the image at filepath: " + this.filePath);
    }
  }
}