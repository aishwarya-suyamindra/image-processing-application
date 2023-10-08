package view;

import java.io.PrintStream;

/**
 * This class represents the view.
 */
public class View implements IView {

  private final PrintStream out;

  /**
   * Constructs a View object with the given PrintStream.
   *
   * @param out PrintStream
   */
  public View(PrintStream out) {
    this.out = out;
  }

  @Override
  public void print(String message) {
    out.println(message);
  }
}
