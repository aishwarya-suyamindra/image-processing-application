package utility;

import org.junit.Before;
import org.junit.Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;


/**
 * A Junit test class for the PPMImageHelper Class.
 */
public class PPMImageHelperTest extends ImageHelperAbstractTest {


  @Before
  public void setUp() {
    Pixel red = new Pixel(255, 0, 0, 255);
    Pixel green = new Pixel(0, 255, 0, 255);
    Pixel blue = new Pixel(0, 0, 255, 255);
    Pixel black = new Pixel(0, 0, 0, 255);
    Pixel orange = new Pixel(255, 153, 51, 255);
    Pixel white = new Pixel(255, 255, 255, 255);
    pixels = new Pixel[][]{{red, green, blue}, {black, orange, white}};
    expectedImage = new CustomImage("expected_test_image", pixels, 255);
    imageHelper = new PPMImageHelper();
  }

  private File createTestPPMImageFile(String fileName, String ppmImageString) throws IOException {
    File file = folder.newFile(fileName);
    FileWriter writer = new FileWriter(file);
    BufferedWriter bw1 = new BufferedWriter(writer);
    bw1.write(ppmImageString);
    bw1.close();
    return file;
  }

  @Test
  public void testLoadValidFilePath() {
    File file = null;
    try {
      String validPPMImageString = "P3\n" + "3 2\n"
              + "255\n" + "255\n" + "0\n" + "0\n" + "0\n" + "255\n"
              + "0\n" + "0\n" + "0\n" + "255\n" + "0\n" + "0\n"
              + "0\n" + "255\n" + "153\n" + "51\n" + "255\n" + "255\n" + "255";
      file = createTestPPMImageFile("testImage.ppm", validPPMImageString);
    } catch (IOException ex) {
      fail("Unable to create test image file");
    }

    String filePath = file.getAbsolutePath();
    String imageName = "Test image";
    CustomImage expectedImage = new CustomImage(imageName, this.pixels, 255);
    try {
      CustomImage actualImage = imageHelper.load(filePath, imageName);
      assertEquals("Test image", actualImage.getName());
      assertEquals(expectedImage, actualImage);
    } catch (IOException ex) {
      fail("Unexpected exception");
    }
  }

  @Test(expected = IOException.class)
  public void testLoadInvalidFilePath() throws IOException {
    String filePath = "testImage.ppm";
    String imageName = "Test image";
    imageHelper.load(filePath, imageName);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testLoadInvalidASCIIPPMFileFormat() throws IOException {
    File file = null;
    try {
      String invalidPPMImageString = "3 2\n"
              + "255\n" + "255\n" + "0\n" + "0\n" + "0\n" + "255\n"
              + "0\n" + "0\n" + "0\n" + "255\n" + "0\n" + "0\n"
              + "0\n" + "255\n" + "153\n" + "51\n" + "255\n" + "255\n" + "255";
      file = createTestPPMImageFile("TestInvalidPPMFile.ppm", invalidPPMImageString);
    } catch (IOException ex) {
      fail("Unable to create test image file");
    }

    String imageName = "Test image";
    imageHelper.load(file.getAbsolutePath(), imageName);
  }

  @Test
  public void testLoadValidFileContentsWithComments() {
    File file = null;
    try {
      String ppmImageString = "P3\n" + "# Created by GIMP\n"
              + "3 2\n"
              + "255\n" + "255\n" + "0\n" + "0\n" + "0\n" + "255\n"
              + "0\n" + "0\n" + "0\n" + "255\n" + "0\n" + "0\n"
              + "0\n" + "255\n" + "153\n" + "51\n" + "255\n" + "255\n" + "255";
      file = createTestPPMImageFile("testImage.ppm", ppmImageString);
    } catch (IOException ex) {
      fail("Unable to create test image file");
    }

    String filePath = file.getAbsolutePath();
    String imageName = "Test image";
    CustomImage expectedImage = new CustomImage(imageName, this.pixels, 255);
    try {
      CustomImage actualImage = imageHelper.load(filePath, imageName);
      assertEquals("Test image", actualImage.getName());
      assertEquals(expectedImage, actualImage);
    } catch (IOException ex) {
      fail("Unexpected exception");
    }
  }

  @Test
  public void testSaveValidFilePath() {
    String filePath = folder.getRoot().getAbsolutePath() + "/savedTestImage.ppm";
    String imageName = "Test image";
    CustomImage imageToSave = new CustomImage(imageName, this.pixels, 255);
    try {
      imageHelper.save(imageToSave, filePath);
      // validate that the file has been saved at the given path
      File savedFile = new File(filePath);
      assertTrue(savedFile.exists());
      // validate the contents of the file
      Scanner sc = new Scanner(savedFile);
      String token = sc.next();
      assertEquals("P3", token);
      int width = sc.nextInt();
      int height = sc.nextInt();
      int maxValue = sc.nextInt();
      assertEquals(3, width);
      assertEquals(2, height);
      assertEquals(255, maxValue);
      Pixel[][] imagePixels = new Pixel[height][width];
      for (int i = 0; i < height; i++) {
        for (int j = 0; j < width; j++) {
          imagePixels[i][j] = new Pixel(sc.nextInt(), sc.nextInt(), sc.nextInt(), 255);
          assertEquals(pixels[i][j], imagePixels[i][j]);
        }
      }
    } catch (IOException ex) {
      fail("Unexpected exception");
    }
  }


}
