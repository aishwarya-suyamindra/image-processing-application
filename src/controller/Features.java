package controller;

/**
 * This interface represents a set of application specific operations/ callbacks required by a
 * graphical view that displays image data.
 */
public interface Features {
  /**
   * Called on an action in the view and informs the delegate that a command is to be executed.
   *
   * @param command              the command to be executed
   * @param additionalParameters the additional parameters required to perform the command.
   */
  void execute(String command, String[] additionalParameters);
}
