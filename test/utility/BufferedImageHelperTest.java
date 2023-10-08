package utility;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * A Junit test class for the BufferedImageHelper Class.
 */
public class BufferedImageHelperTest extends ImageHelperAbstractTest {

  private CustomImage expectedImageJpg;

  private String ppmImageString;


  @Before
  public void setUp() {
    Pixel red = new Pixel(255, 0, 0, 255);
    Pixel green = new Pixel(0, 255, 0, 255);
    Pixel blue = new Pixel(0, 0, 255, 255);
    Pixel black = new Pixel(0, 0, 0, 255);
    Pixel orange = new Pixel(255, 153, 51, 255);
    Pixel white = new Pixel(255, 255, 255, 255);

    Pixel redJpg = new Pixel(102, 83, 0, 255);
    Pixel greenJpg = new Pixel(154, 135, 43, 255);
    Pixel blueJpg = new Pixel(45, 33, 167, 255);
    Pixel blackJpg = new Pixel(36, 17, 0, 255);
    Pixel orangeJpg = new Pixel(208, 189, 97, 255);
    Pixel whiteJpg = new Pixel(232, 220, 255, 255);
    expectedImage = new CustomImage("expected_test_image", new Pixel[][]{
            {red, green, blue},
            {black, orange, white}
    }, 255);

    expectedImageJpg = new CustomImage("expected_test_image_jpg", new Pixel[][]{
            {redJpg, greenJpg, blueJpg},
            {blackJpg, orangeJpg, whiteJpg}
    }, 255);
    imageHelper = new BufferedImageHelper();
    ppmImageString = "P3\n"
            + "3 2\n"
            + "255\n" + "255\n" + "0\n" + "0\n" + "0\n" + "255\n"
            + "0\n" + "0\n" + "0\n" + "255\n" + "0\n" + "0\n"
            + "0\n" + "255\n" + "153\n" + "51\n" + "255\n" + "255\n" + "255";

  }

  @Rule
  public TemporaryFolder folder = new TemporaryFolder();

  private File createTestImageFile(String filename, String ppmImageData, String format,
                                   int bufferedImageType) throws IOException {
    File file = folder.newFile(filename);
    //convert ppmImage data to bufferedImage
    String[] tokens = ppmImageData.split("\\s+");
    int width = Integer.parseInt(tokens[1]);
    int height = Integer.parseInt(tokens[2]);

    BufferedImage bufferedImage = new BufferedImage(width, height, bufferedImageType);

    int pixelIndex = 0;

    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        int red = Integer.parseInt(tokens[4 + pixelIndex]);
        int green = Integer.parseInt(tokens[5 + pixelIndex]);
        int blue = Integer.parseInt(tokens[6 + pixelIndex]);
        pixelIndex += 3;

        int rgb = new Color(red, green, blue).getRGB();

        bufferedImage.setRGB(x, y, rgb);
      }
    }

    ImageIO.write(bufferedImage, format, file);
    return file;
  }


  @Test
  public void testLoadValidBmpFile() {
    String imageName = "test_image";
    String fileName = "test.bmp";
    try {
      CustomImage actualImage = testLoad(fileName, "bmp",
              BufferedImage.TYPE_INT_RGB, imageName);
      assertEquals(imageName, actualImage.getName());
      assertEquals(expectedImage, actualImage);
    } catch (IOException ioe) {
      fail("Unexpected exception");
    }
  }

  @Test
  public void testValidSaveBmp() {
    String filePath = folder.getRoot().getAbsolutePath() + "/savedTestImage.bmp";
    boolean fileExists;
    try {
      fileExists = testSave(filePath);
      assertTrue(fileExists);
    } catch (IOException ex) {
      fail("Unexpected exception");
    }
  }

  @Test
  public void testLoadValidPngFile() {
    String imageName = "test_image";
    String fileName = "test.png";
    try {
      CustomImage actualImage = testLoad(fileName, "png",
              BufferedImage.TYPE_INT_RGB, imageName);
      assertEquals(imageName, actualImage.getName());
      assertEquals(expectedImage, actualImage);
    } catch (IOException ioe) {
      fail("Unexpected exception");
    }
  }

  @Test
  public void testValidSavePng() {
    String filePath = folder.getRoot().getAbsolutePath() + "/savedTestImage.png";
    boolean fileExists;
    try {
      fileExists = testSave(filePath);
      assertTrue(fileExists);
    } catch (IOException ex) {
      fail("Unexpected exception");
    }
  }

  @Test
  public void testLoadValidJpgFile() {
    String imageName = "test_image";
    String fileName = "test.jpg";
    try {
      CustomImage actualImage = testLoad(fileName, "jpg",
              BufferedImage.TYPE_INT_RGB, imageName);
      assertEquals(imageName, actualImage.getName());
      assertEquals(expectedImageJpg, actualImage);
    } catch (IOException ioe) {
      fail("Unexpected exception");
    }
  }

  @Test
  public void testLoadValidJpegFile() {
    String imageName = "test_image";
    String fileName = "test.jpeg";
    try {
      CustomImage actualImage = testLoad(fileName, "jpeg",
              BufferedImage.TYPE_INT_RGB, imageName);
      assertEquals(imageName, actualImage.getName());
      assertEquals(expectedImageJpg, actualImage);
    } catch (IOException ioe) {
      fail("Unexpected exception");
    }
  }

  private CustomImage testLoad(String fileName, String format, int bufferedImageType,
                               String imageName) throws IOException {
    File file = createTestImageFile(fileName, ppmImageString, format, bufferedImageType);
    String filePath = file.getAbsolutePath();
    return imageHelper.load(filePath, imageName);

  }

  private boolean testSave(String filePath) throws IOException {
    imageHelper.save(expectedImage, filePath);
    File savedFile = new File(filePath);
    return savedFile.exists();
  }

}
