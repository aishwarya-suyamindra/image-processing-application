package controller.commands;

import java.io.IOException;

import controller.ImageOperationCommand;
import model.ImageProcessor;
import utility.CustomImage;
import utility.IImageHelper;
import utility.IImageHelperFactory;

/**
 * This class represents the functionality to save a CustomImage with the given name,
 * to the given filepath.
 */
public class Save implements ImageOperationCommand {
  private final String imageName;
  private final String filePath;
  private final IImageHelper imageHelper;

  /**
   * Constructs a save command object with the given filepath, image name and image helper to
   * save the file.
   *
   * @param filePath           the filepath to save the image to
   * @param imageName          the name of the image that is to be saved
   * @param imageHelperFactory the imageHelperFactory which gives the image helper class based on
   *                           the file to save
   */
  public Save(String filePath, String imageName, IImageHelperFactory imageHelperFactory) {
    this.filePath = filePath;
    this.imageName = imageName;
    this.imageHelper = imageHelperFactory.getHelper(filePath);
  }

  /**
   * Executes the save command.
   *
   * @param model the model object
   */
  @Override
  public void execute(ImageProcessor model) {
    try {
      CustomImage image = model.getImage(this.imageName);
      this.imageHelper.save(image, this.filePath);
    } catch (IOException e) {
      throw new RuntimeException("Failed to save the image at filepath: " + this.filePath);
    }
  }
}
