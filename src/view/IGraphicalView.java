package view;


import controller.Features;
import utility.ViewMessageType;

/**
 * This interface represents the set of operations to be supported by a graphical user interface.
 */
public interface IGraphicalView {
  /**
   * Registers the given features object as the callback for the various events on the view.
   *
   * @param features the callback for various events on the view.
   */
  void addFeatures(Features features);

  /**
   * Makes the view visible.
   */
  void setVisible();

  /**
   * Updates the view with the image of the given name.
   */
  void updateUI(String imageName);

  /**
   * Displays the given message.
   *
   * @param message the string message to be displayed.
   */
  void echo(String message, ViewMessageType messageType);
}
