package model;

import org.junit.Test;

import controller.AbstractImageCommandControllerTest;
import utility.CustomImage;
import utility.Pixel;

import static org.junit.Assert.assertEquals;

/**
 * A Junit test class for the ImageProcessorROImpl class.
 */
public class ImageProcessorROImplTest {
  private static class MockModel extends
          AbstractImageCommandControllerTest.MockImageProcessorModel {
    private final CustomImage imageToReturn;

    /**
     * Constructs a mock model object.
     * @param log StringBuilder to maintain log data
     * @param imageToReturn the image to be returned.
     */
    public MockModel(StringBuilder log, CustomImage imageToReturn) {
      super(log);
      this.imageToReturn = imageToReturn;
    }

    @Override
    public CustomImage getImage(String imageName) {
      log.append("Get Image: ").append(imageName);
      return imageToReturn;
    }
  }

  @Test
  public void testGetImage() {
    StringBuilder log = new StringBuilder();
    ImageProcessor model = new MockModel(log,
            new CustomImage("test", null, 0));
    ImageProcessorRO roModel = new ImageProcessorROImpl(model);
    roModel.getImage("test");
    assertEquals("Get Image: test", log.toString());

    log.setLength(0);
    roModel.getImage("");
    assertEquals("Get Image: ", log.toString());
  }

  @Test
  public void testGetImageReturn() {
    StringBuilder log = new StringBuilder();
    CustomImage image = new CustomImage("test", new Pixel[1][1], 0);
    ImageProcessor model = new MockModel(log, image);
    ImageProcessorRO roModel = new ImageProcessorROImpl(model);
    CustomImage actual = roModel.getImage("test");
    assertEquals(image, actual);
  }

}