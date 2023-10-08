package controller;

import model.ImageProcessor;

/**
 * This interface represents a set of actions that is to be executed by the various image
 * operation commands.
 */
public interface ImageOperationCommand {
  /**
   * Performs the specific operation associated with the command.
   *
   * @param model the model object
   */
  void execute(ImageProcessor model);
}
