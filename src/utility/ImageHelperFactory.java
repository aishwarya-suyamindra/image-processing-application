package utility;

/**
 * This factory class is used to create objects of helper classes for different image types.
 */
public final class ImageHelperFactory implements IImageHelperFactory {
  private ImageHelperFactory() {
  }

  private static final ImageHelperFactory instance = new ImageHelperFactory();

  /**
   * Returns an instance of the ImageHelperFactory.
   *
   * @return an instance of ImageHelperFactory
   */
  public static ImageHelperFactory getInstance() {
    return instance;
  }

  /**
   * Returns a concrete implementation of IImageHelper based on the file extension of the file.
   *
   * @param file the image file
   * @return IImageHelper object
   * @throws IllegalArgumentException      if there is no file extension for the given file
   * @throws UnsupportedOperationException if the file format is not supported
   */
  @Override
  public IImageHelper getHelper(String file) throws IllegalArgumentException,
          UnsupportedOperationException {
    String format = getFileExtension(file);
    if (format.equals("ppm")) {
      return new PPMImageHelper();
    } else if (format.equals("jpg") || format.equals("jpeg") || format.equals("bmp")
            || format.equals("png")) {
      return new BufferedImageHelper();
    } else {
      throw new UnsupportedOperationException("Unsupported file format. Only jpg, ppm, bmp ang png "
              + "file formats are supported.");
    }
  }

  // returns the extension of the file
  private String getFileExtension(String fileName) throws IllegalArgumentException {
    int index = fileName.lastIndexOf('.');
    if (index > 0) {
      return fileName.substring(index + 1);
    }
    throw new IllegalArgumentException("File extension is necessary.");
  }
}