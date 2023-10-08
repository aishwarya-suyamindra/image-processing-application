package controller;

/**
 * This interface represents a set of operations that give the control to the controller to
 * process a command, to operate on images.
 */
public interface CommandController {
  /**
   * Gives the control to the controller to start the program.
   */
  void process();
}
