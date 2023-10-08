package utility;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * A Junit test class for the ImageHelperFactory class.
 */
public class ImageHelperFactoryTest {

  private final IImageHelperFactory imageHelperFactory = ImageHelperFactory.getInstance();

  @Test
  public void testGetHelperWhenPPMFileIsPassed() {
    String file = "test.ppm";
    IImageHelper imageHelperObj = imageHelperFactory.getHelper(file);
    assertTrue(imageHelperObj instanceof PPMImageHelper);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetHelperWhenInvalidFileFormatIsPassed() {
    String file = "test.tiff";
    imageHelperFactory.getHelper(file);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetHelperWhenInvalidFileWithoutExtensionIsPassed() {
    String file = "test";
    imageHelperFactory.getHelper(file);
  }

  @Test
  public void testGetHelperWhenPngFileIsPassed() {
    String file = "test.png";
    IImageHelper imageHelperObj = imageHelperFactory.getHelper(file);
    assertTrue(imageHelperObj instanceof BufferedImageHelper);
  }

  @Test
  public void testGetHelperWhenJpgFileIsPassed() {
    String file = "test.jpg";
    IImageHelper imageHelperObj = imageHelperFactory.getHelper(file);
    assertTrue(imageHelperObj instanceof BufferedImageHelper);
  }

  @Test
  public void testGetHelperWhenJpegFileIsPassed() {
    String file = "test.jpeg";
    IImageHelper imageHelperObj = imageHelperFactory.getHelper(file);
    assertTrue(imageHelperObj instanceof BufferedImageHelper);
  }

  @Test
  public void testGetHelperWhenBmpFileIsPassed() {
    String file = "test.bmp";
    IImageHelper imageHelperObj = imageHelperFactory.getHelper(file);
    assertTrue(imageHelperObj instanceof BufferedImageHelper);
  }


}