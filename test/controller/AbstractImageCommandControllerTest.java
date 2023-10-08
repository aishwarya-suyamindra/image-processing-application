package controller;

import java.io.FileNotFoundException;
import java.io.IOException;

import model.ImageProcessor;
import utility.CustomImage;
import utility.IImageHelper;
import utility.IImageHelperFactory;
import utility.Pixel;

/**
 * This class contains the methods and fields common to the test cases of the concrete
 * implementations of the command controller interface.
 */
public abstract class AbstractImageCommandControllerTest {
  // region mocks

  /**
   * Mock implementation of ImageProcessorImpl model class.
   */
  public static class MockImageProcessorModel implements ImageProcessor {
    protected final StringBuilder log;

    /**
     * Constructs the log.
     * @param log StringBuilder log data
     */
    public MockImageProcessorModel(StringBuilder log) {
      this.log = log;
    }

    @Override
    public void loadImage(CustomImage image) {
      log.append("Load: ").append(image);
    }

    @Override
    public void visualiseChannel(String originalImageName, String resultImageName, int channel) {
      log.append("Visualise Channel: ").append(originalImageName).append(", ")
              .append(resultImageName).append(", ").append(channel);
    }

    @Override
    public void visualiseValue(String originalImageName, String resultImageName) {
      log.append("Visualise Value: ").append(originalImageName).append(", ")
              .append(resultImageName);
    }

    @Override
    public void visualiseIntensity(String originalImageName, String resultImageName) {
      log.append("Visualise Intensity: ").append(originalImageName).append(", ")
              .append(resultImageName);
    }

    @Override
    public void visualiseLuma(String originalImageName, String resultImageName) {
      log.append("Visualise Luma: ").append(originalImageName).append(", ")
              .append(resultImageName);
    }

    @Override
    public void flip(String originalImageName, String resultImageName, int axis) {
      log.append("Flip: ").append(originalImageName).append(", ")
              .append(resultImageName).append(", ").append(axis);
    }

    @Override
    public void brighten(String originalImageName, String resultImageName, int increment) {
      log.append("Brighten: ").append(originalImageName).append(", ")
              .append(resultImageName).append(", ").append(increment);
    }

    @Override
    public void split(String originalImageName, String resultRedImageName,
                      String resultGreenImageName, String resultBlueImageName) {
      log.append("Split: ").append(originalImageName).append(", ")
              .append(resultRedImageName).append(", ").append(resultGreenImageName)
              .append(", ").append(resultBlueImageName);
    }

    @Override
    public void combine(String redImageName, String greenImageName,
                        String blueImageName, String resultImageName) {
      log.append("Combine: ").append(redImageName).append(", ").append(greenImageName)
              .append(", ").append(blueImageName).append(", ").append(resultImageName);
    }

    @Override
    public CustomImage getImage(String imageName) {
      return getCustomImage();
    }

    @Override
    public void blur(String originalImageName, String resultImageName) {
      log.append("Blur: ").append(originalImageName).append(", ")
              .append(resultImageName);
    }

    @Override
    public void sharpen(String originalImageName, String resultImageName) {
      log.append("Sharpen: ").append(originalImageName).append(", ")
              .append(resultImageName);
    }

    @Override
    public void sepia(String originalImageName, String resultImageName) {
      log.append("Sepia: ").append(originalImageName).append(", ")
              .append(resultImageName);
    }

    @Override
    public void dither(String originalImageName, String resultImageName) {
      log.append("Dither: ").append(originalImageName).append(", ")
              .append(resultImageName);
    }
  }

  protected static class MockImageProcessorModelExceptions extends MockImageProcessorModel {
    MockImageProcessorModelExceptions(StringBuilder log) {
      super(log);
    }

    @Override
    public CustomImage getImage(String imageName) {
      throw new IllegalArgumentException("Image with given name cannot be found");
    }
  }

  protected static class MockImageHelperFactory implements IImageHelperFactory {
    protected final boolean isToThrowException;
    protected final StringBuilder log;
    protected final CustomImage imageToLoad;

    protected MockImageHelperFactory(boolean isToThrowException, StringBuilder log,
                                     CustomImage image) {
      this.isToThrowException = isToThrowException;
      this.log = log;
      this.imageToLoad = image;
    }

    private static class MockImageHelper implements IImageHelper {
      private final StringBuilder log;
      private final CustomImage image;

      MockImageHelper(StringBuilder log, CustomImage image) {
        this.log = log;
        this.image = image;
      }

      @Override
      public CustomImage load(String filePath, String imageName) throws IOException {
        log.append("Load: ").append(filePath).append(", ").append(imageName);
        return image;
      }

      @Override
      public void save(CustomImage image, String filePath) throws IOException {
        log.append("Save: ").append(image).append(", ").append(filePath);
      }
    }

    private static class MockImageHelperExceptions extends MockImageHelper {

      MockImageHelperExceptions(StringBuilder log, CustomImage image) {
        super(log, image);
      }

      @Override
      public CustomImage load(String filePath, String imageName) throws IOException {
        throw new FileNotFoundException();
      }

      @Override
      public void save(CustomImage image, String filePath) throws IOException {
        throw new FileNotFoundException();
      }
    }


    @Override
    public IImageHelper getHelper(String imageExtension) {
      if (isToThrowException) {
        return new MockImageHelperFactory.MockImageHelperExceptions(this.log, this.imageToLoad);
      }
      return new MockImageHelperFactory.MockImageHelper(this.log, this.imageToLoad);
    }
  }
  // endregion

  protected final StringBuilder modelLog = new StringBuilder();
  protected final StringBuilder viewLog = new StringBuilder();
  protected final StringBuilder helperLog = new StringBuilder();

  protected static CustomImage getCustomImage() {
    Pixel[][] pixels;
    Pixel red = new Pixel(255, 0, 0, 255);
    Pixel green = new Pixel(0, 255, 0, 255);
    Pixel blue = new Pixel(0, 0, 255, 255);
    Pixel black = new Pixel(0, 0, 0, 255);
    Pixel orange = new Pixel(255, 153, 51, 255);
    Pixel white = new Pixel(255, 255, 255, 255);
    pixels = new Pixel[][]{{red, green, blue}, {black, orange, white}};
    return new CustomImage("TestImage", pixels, 255);
  }

  protected void resetLog() {
    modelLog.setLength(0);
    viewLog.setLength(0);
    helperLog.setLength(0);
  }

}
