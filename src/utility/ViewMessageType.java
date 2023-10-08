package utility;


/**
 * Represents a type of message to be displayed on the view.
 */
public enum ViewMessageType {
  SUCCESS("Success"),
  ERROR("Error");

  private final String title;

  ViewMessageType(String title) {
    this.title = title;
  }

  /**
   * Returns the title of the provided message type.
   *
   * @return title of the message.
   */
  public String getTitle() {
    return this.title;
  }
}
