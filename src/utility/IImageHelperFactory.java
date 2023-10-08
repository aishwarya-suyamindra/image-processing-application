package utility;

/**
 * This interface represents a set of operations that is to be supported by a helper factory class.
 */
public interface IImageHelperFactory {
  /**
   * Returns the image helper for the given file.
   *
   * @param file the image file
   * @return the image helper object
   */
  IImageHelper getHelper(String file);
}
