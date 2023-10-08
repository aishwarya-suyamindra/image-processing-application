package utility;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.IOException;

/**
 * An abstract test class capturing the common scenarios between classes implementing IImageHelper.
 */
public abstract class ImageHelperAbstractTest {

  protected Pixel[][] pixels;
  protected CustomImage expectedImage;
  @Rule
  public TemporaryFolder folder = new TemporaryFolder();

  protected IImageHelper imageHelper;


  @Test(expected = NullPointerException.class)
  public void testSaveNullFilePath() throws IOException {
    String filePath = null;
    imageHelper.save(expectedImage, filePath);
  }

  @Test(expected = IOException.class)
  public void testSaveEmptyFilePath() throws IOException {
    String filePath = "";
    imageHelper.save(expectedImage, filePath);
  }

  @Test(expected = NullPointerException.class)
  public void testLoadNullFilePath() throws IOException {
    String filePath = null;
    String imageName = "Test image";
    imageHelper.load(filePath, imageName);
  }

  @Test(expected = IOException.class)
  public void testLoadEmptyFilePath() throws IOException {
    String filePath = "";
    String imageName = "Test image";
    imageHelper.load(filePath, imageName);
  }
}
