package utility;

import java.io.IOException;

/**
 * This interface represents a set of operations that is to be supported by images of different
 * extensions.
 */
public interface IImageHelper {
  /**
   * Loads the image from the given file path and returns an image of type ICustomImage,
   * with the given name.
   *
   * @param filePath the filepath to load the image from
   * @return a CustomImage
   */
  CustomImage load(String filePath, String imageName) throws IOException;

  /**
   * Writes the given image with the given file name to the given file path.
   *
   * @param image    the image to save
   * @param filePath the filepath to write the image to
   */
  void save(CustomImage image, String filePath) throws IOException;
}