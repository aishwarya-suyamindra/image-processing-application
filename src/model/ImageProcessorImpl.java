package model;

import utility.CustomImage;
import utility.Pixel;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * This class represents an Image Processor, which can perform a set of manipulations on an image
 * It is given by a collection of base image and its manipulated images.
 */
public class ImageProcessorImpl implements ImageProcessor {
  private static final int MIN_PIXEL_VALUE = 0;
  private final Map<String, CustomImage> processedImages;

  public ImageProcessorImpl() {
    this.processedImages = new HashMap<>();
  }

  @Override
  public void loadImage(CustomImage image) {
    if (image == null) {
      throw new IllegalArgumentException("Image data cannot be null");
    }
    processedImages.put(image.getName(), image);
  }


  protected void checkImageName(String imageName) {
    if (imageName == null) {
      throw new IllegalArgumentException("Image name cannot be null");
    }
  }

  private Pixel[][] pixelOperationHelper(PixelOperation operation, CustomImage baseImg,
                                         String resultImageName) {
    checkImageName(resultImageName);
    int row = baseImg.getHeight();
    int col = baseImg.getWidth();
    Pixel[][] newPixels = new Pixel[row][col];
    for (int i = 0; i < row; i++) {
      for (int j = 0; j < col; j++) {
        Pixel originalPixel = baseImg.getPixel(i, j);
        newPixels[i][j] = operation.modifyPixel(originalPixel, baseImg.getMaxPixelValue());
      }
    }
    return newPixels;
  }

  /**
   * Creates a greyscale image, where the channel of each pixel is equal to the specified
   * channel value of that pixel in the given original image.
   *
   * @param originalImageName the name of the image that is to be visualised
   * @param resultImageName   the name of the resulting greyscale image
   * @param channel           the channel of the pixel.
   *                          0 - Blue,
   *                          1 - Green,
   *                          2 - Red
   */
  @Override
  public void visualiseChannel(String originalImageName, String resultImageName,
                               int channel) throws IllegalArgumentException {

    if (channel < 0 || channel > 2) {
      throw new IllegalArgumentException("Invalid channel value.");
    }
    checkImageName(originalImageName);
    CustomImage baseImg = this.getImage(originalImageName);
    Pixel[][] newPixels = pixelOperationHelper((originalPixel, maxPixelValue) -> {
      int newComponentVal = originalPixel.getColor(channel);
      return new Pixel(newComponentVal, newComponentVal, newComponentVal, originalPixel.getAlpha());
    }, baseImg, resultImageName);
    CustomImage channelVisualisedImg = new CustomImage(resultImageName,
            newPixels, baseImg.getMaxPixelValue());
    processedImages.put(resultImageName, channelVisualisedImg);
  }

  @Override
  public void visualiseValue(String originalImageName, String resultImageName) {
    checkImageName(originalImageName);
    CustomImage baseImg = this.getImage(originalImageName);
    Pixel[][] newPixels = pixelOperationHelper((originalPixel, maxPixelValue) -> {
      int maxComponent = Math.max(Math.max(originalPixel.getColor(2),
              originalPixel.getColor(1)), originalPixel.getColor(0));
      return new Pixel(maxComponent, maxComponent, maxComponent, originalPixel.getAlpha());
    }, baseImg, resultImageName);
    CustomImage valueVisualisedImg = new CustomImage(resultImageName,
            newPixels, baseImg.getMaxPixelValue());
    processedImages.put(resultImageName, valueVisualisedImg);
  }

  @Override
  public void visualiseIntensity(String originalImageName, String resultImageName) {
    checkImageName(originalImageName);
    CustomImage baseImg = this.getImage(originalImageName);
    Pixel[][] newPixels = pixelOperationHelper((originalPixel, maxPixelValue) -> {
      int avgComponent = roundToInt(((originalPixel.getColor(2)
              + originalPixel.getColor(1) + originalPixel.getColor(0)) / 3.0));
      return new Pixel(avgComponent, avgComponent, avgComponent, originalPixel.getAlpha());
    }, baseImg, resultImageName);
    CustomImage intensityVisualisedImg = new CustomImage(resultImageName,
            newPixels, baseImg.getMaxPixelValue());
    processedImages.put(resultImageName, intensityVisualisedImg);
  }

  /**
   * Helper method to clamp the pixel value to range within the given maxPixelValue.
   *
   * @param val           the value to clamp
   * @param maxPixelValue the maximum value a pixel can hold
   * @return a clamped value
   */
  protected int clampPixelValue(int val, int maxPixelValue) {
    return Math.max(MIN_PIXEL_VALUE, Math.min(maxPixelValue, val));
  }

  /**
   * Helper method to return a weighted sum of pixels. This method takes a 3 * 3 matrix and
   * returns a 2D  array of pixels, where the channel of each pixel is equal to the matrix
   * weighted sum of the channel values of that pixel in the given original image.
   *
   * @param originalImage the original image to work with
   * @param matrix        the weight matrix
   * @return 2D array of pixels
   */
  protected Pixel[][] weightedSum(CustomImage originalImage, double[][] matrix) {
    if (matrix.length == 0 || matrix.length != 3 || matrix[0].length != 3) {
      throw new IllegalArgumentException("Invalid transformation matrix");
    }
    int height = originalImage.getHeight();
    int width = originalImage.getWidth();
    Pixel[][] newPixels = new Pixel[height][width];
    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        Pixel pixel = originalImage.getPixel(i, j);
        int r = pixel.getColor(2);
        int g = pixel.getColor(1);
        int b = pixel.getColor(0);
        int alpha = originalImage.getMaxPixelValue();
        int rNew =
                clampPixelValue(roundToInt(r * matrix[0][0] + g * matrix[0][1]
                        + b * matrix[0][2]), alpha);
        int gNew = clampPixelValue(roundToInt(r * matrix[1][0] + g * matrix[1][1]
                + b * matrix[1][2]), alpha);
        int bNew = clampPixelValue(roundToInt(r * matrix[2][0] + g * matrix[2][1]
                + b * matrix[2][2]), alpha);
        Pixel newPixel = new Pixel(rNew, gNew, bNew, pixel.getAlpha());
        newPixels[i][j] = newPixel;
      }
    }
    return newPixels;
  }

  /**
   * Helper method to round a given double value to an integer value.
   *
   * @param val the value to round
   * @return the rounded integer value
   */
  protected int roundToInt(double val) {
    return (int) (Math.round(val * Math.pow(10, 0)) / Math.pow(10, 0));
  }

  /**
   * Creates a new image, where the channel of each pixel is equal to the weighted sum of the
   * channel values of that pixel in the given original image(excluding opacity component).
   * weighted sum : 0.2126*r + 0.7152*g + 0.0722*b
   *
   * @param originalImageName the name of the image to visualise the luma of
   * @param resultImageName   the name of the result image
   */
  @Override
  public void visualiseLuma(String originalImageName, String resultImageName) {
    checkImageName(originalImageName);
    checkImageName(resultImageName);
    CustomImage baseImg = this.getImage(originalImageName);

    double[][] transformationMatrix = {{0.216, 0.7152, 0.0722}, {0.216, 0.7152, 0.0722},
        {0.216, 0.7152, 0.0722}};
    Pixel[][] newPixels = weightedSum(baseImg, transformationMatrix);
    CustomImage lumaVisualisedImg = new CustomImage(resultImageName, newPixels,
            baseImg.getMaxPixelValue());
    processedImages.put(resultImageName, lumaVisualisedImg);
  }

  /**
   * Creates a flipped image of the given original image.
   *
   * @param originalImageName the name of the image that is to be flipped
   * @param resultImageName   the name of the resulting flipped image
   * @param axis              the axis along which the image has to be flipped.
   *                          0- Horizontal,
   *                          1 - Vertical
   */
  @Override
  public void flip(String originalImageName, String resultImageName, int axis) {
    checkImageName(originalImageName);
    checkImageName(resultImageName);
    if (axis != 0 && axis != 1) {
      throw new IllegalArgumentException("Invalid axis value");
    }
    CustomImage baseImg = this.getImage(originalImageName);
    int row = baseImg.getHeight();
    int col = baseImg.getWidth();
    Pixel[][] newPixels = new Pixel[row][col];
    if (axis == 0) { //Horizontal
      for (int i = 0; i < row; i++) {
        for (int j = 0; j <= col / 2; j++) {
          newPixels[i][col - j - 1] = baseImg.getPixel(i, j);
          newPixels[i][j] = baseImg.getPixel(i, col - j - 1);
        }
      }
    } else if (axis == 1) { //Vertical
      for (int i = 0; i <= row / 2; i++) {
        for (int j = 0; j < col; j++) {
          newPixels[row - i - 1][j] = baseImg.getPixel(i, j);
          newPixels[i][j] = baseImg.getPixel(row - i - 1, j);
        }
      }
    }
    CustomImage newImageFlipped = new CustomImage(resultImageName,
            newPixels, baseImg.getMaxPixelValue());
    processedImages.put(resultImageName, newImageFlipped);
  }

  @Override
  public void brighten(String originalImageName, String resultImageName, int increment) {
    checkImageName(originalImageName);
    CustomImage baseImg = this.getImage(originalImageName);
    PixelOperation brightenFunction = (originalPixel, maxPixelValue) -> new Pixel(
            (clampPixelValue(originalPixel.getColor(2)
                    + increment, baseImg.getMaxPixelValue())),
            (clampPixelValue(originalPixel.getColor(1)
                    + increment, baseImg.getMaxPixelValue())),
            (clampPixelValue(originalPixel.getColor(0)
                    + increment, baseImg.getMaxPixelValue())),
            originalPixel.getAlpha());

    Pixel[][] newPixels = pixelOperationHelper(brightenFunction, baseImg, resultImageName);
    CustomImage brightenedImg = new CustomImage(resultImageName,
            newPixels, baseImg.getMaxPixelValue());
    processedImages.put(resultImageName, brightenedImg);
  }

  @Override
  public void split(String originalImageName, String resultRedImageName,
                    String resultGreenImageName, String resultBlueImageName) {

    this.visualiseChannel(originalImageName, resultBlueImageName, 0);
    this.visualiseChannel(originalImageName, resultGreenImageName, 1);
    this.visualiseChannel(originalImageName, resultRedImageName, 2);
  }

  @Override
  public void combine(String redImageName, String greenImageName, String blueImageName,
                      String resultImageName) {
    checkImageName(redImageName);
    checkImageName(greenImageName);
    checkImageName(blueImageName);
    checkImageName(resultImageName);
    CustomImage redImg = this.getImage(redImageName);
    CustomImage greenImg = this.getImage(greenImageName);
    CustomImage blueImg = this.getImage(blueImageName);
    if (redImg.getWidth() != greenImg.getWidth() || greenImg.getWidth() != blueImg.getWidth()
            || redImg.getHeight() != greenImg.getHeight()
            || greenImg.getHeight() != blueImg.getHeight()) {
      throw new IllegalArgumentException("The width and height of the"
              + " given three images are not same.");
    }
    int row = redImg.getHeight();
    int col = redImg.getWidth();
    Pixel[][] newPixels = new Pixel[row][col];

    for (int i = 0; i < row; i++) {
      for (int j = 0; j < col; j++) {
        Pixel redComponent = redImg.getPixel(i, j);
        Pixel greenComponent = greenImg.getPixel(i, j);
        Pixel blueComponent = blueImg.getPixel(i, j);
        Pixel newPixelVal = new Pixel(redComponent.getColor(2), greenComponent.getColor(1),
                blueComponent.getColor(0), redComponent.getAlpha());
        newPixels[i][j] = newPixelVal;
      }
    }
    CustomImage finalCombinedImg = new CustomImage(resultImageName,
            newPixels, redImg.getMaxPixelValue());
    processedImages.put(resultImageName, finalCombinedImg);
  }

  /**
   * Creates a sepia-toned version of the given image, by applying a color transformation matrix
   * to every channel of every pixel in the original image.
   * matrix used
   * {{0.393, 0.769, 0.189},
   * {0.349, 0.686, 0.168},
   * {0.272, 0.534, 0.131}}
   *
   * @param originalImageName the name of the image to get the sepia-tone of
   * @param resultImageName   the name of the resulting image
   */
  @Override
  public void sepia(String originalImageName, String resultImageName) {
    checkImageName(originalImageName);
    checkImageName(resultImageName);
    CustomImage image = getImage(originalImageName);
    double[][] transformationMatrix = {{0.393, 0.769, 0.189}, {0.349, 0.686, 0.168},
        {0.272, 0.534, 0.131}};
    Pixel[][] newPixels = weightedSum(image, transformationMatrix);
    CustomImage sepiaTonedImage = new CustomImage(resultImageName, newPixels,
            image.getMaxPixelValue());
    processedImages.put(resultImageName, sepiaTonedImage);
  }

  /*
   Returns the value (0 or 255), whichever is closest to the value of the channel.
   */
  private int getClosestColor(int channelValue, int maxValue) {
    if ((maxValue - channelValue) <= channelValue) {
      return maxValue;
    }
    return 0;
  }

  /**
   * Helper method to add a given value to the pixel and return a new pixel.
   *
   * @param originalPixel the pixel to add the given value to
   * @param value         the value to add
   * @param maxPixelValue the maximum value a pixel can hold
   * @return a new pixel, which has the clamped value of adding the value to the given pixel
   */
  protected Pixel addHelper(Pixel originalPixel, double value, int maxPixelValue) {
    int r = clampPixelValue(roundToInt(originalPixel.getColor(2) + value), maxPixelValue);
    int g = clampPixelValue(roundToInt(originalPixel.getColor(1) + value), maxPixelValue);
    int b = clampPixelValue(roundToInt(originalPixel.getColor(0) + value), maxPixelValue);
    return new Pixel(r, g, b, originalPixel.getAlpha());
  }

  @Override
  public void dither(String originalImageName, String resultImageName) {
    checkImageName(originalImageName);
    checkImageName(resultImageName);
    CustomImage image = getImage(originalImageName);
    int height = image.getHeight();
    int width = image.getWidth();
    // initially holds the image pixels of the original image, which is modified to hold the
    // propagated error
    Pixel[][] imagePixels = new Pixel[height][width];
    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        imagePixels[i][j] = image.getPixel(i, j);
      }
    }

    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        Pixel pixel = imagePixels[i][j];
        // use the green component to convert to greyscale
        int oldColor = pixel.getColor(1);
        int newColor = getClosestColor(oldColor, image.getMaxPixelValue());
        imagePixels[i][j] = new Pixel(newColor, newColor, newColor, pixel.getAlpha());
        int error = oldColor - newColor;
        // add the error term to the neighbouring pixels
        if (j + 1 < width) {
          Pixel newPixelValue = addHelper(imagePixels[i][j + 1], (error * (7.0 / 16.0)),
                  image.getMaxPixelValue());
          imagePixels[i][j + 1] = newPixelValue;
        }

        if (j - 1 >= 0 && i + 1 < height) {
          Pixel newPixelValue = addHelper(imagePixels[i + 1][j - 1], (error * (3.0 / 16.0)),
                  image.getMaxPixelValue());
          imagePixels[i + 1][j - 1] = newPixelValue;
        }

        if (i + 1 < height) {
          Pixel newPixelValue = addHelper(imagePixels[i + 1][j], (error * (5.0 / 16.0)),
                  image.getMaxPixelValue());
          imagePixels[i + 1][j] = newPixelValue;
        }

        if (j + 1 < width && i + 1 < height) {
          Pixel newPixelValue = addHelper(imagePixels[i + 1][j + 1], (error * (1.0 / 16.0)),
                  image.getMaxPixelValue());
          imagePixels[i + 1][j + 1] = newPixelValue;
        }
      }
    }
    CustomImage ditheredImage = new CustomImage(resultImageName, imagePixels,
            image.getMaxPixelValue());
    processedImages.put(resultImageName, ditheredImage);
  }

  /**
   * Helper method to apply a filter on the given image. This method takes a 2D kernel and
   * returns a 2D array of pixels, which is the filtered version of the pixels of the given image.
   *
   * @param originalImage the original image to work with
   * @param kernel        the kernel to filter with
   * @return 2D array of pixels
   */
  protected Pixel[][] applyFilter(CustomImage originalImage, int[][] kernel) {
    if (kernel.length % 2 == 0) {
      throw new IllegalArgumentException("filter contains a incomplete row");
    }
    if (kernel[0].length % 2 == 0) {
      throw new IllegalArgumentException("filter contains a incomplete col");
    }
    if (kernel.length % kernel[0].length != 0) {
      throw new IllegalArgumentException("filter contains a incomplete row or col");
    }
    int kernelSize = kernel.length;
    double kernelSum = Arrays.stream(kernel)
            .flatMapToInt(Arrays::stream)
            .sum();
    int row = originalImage.getHeight();
    int col = originalImage.getWidth();
    Pixel[][] newPixels = new Pixel[row][col];

    for (int y = 0; y < row; y++) {
      for (int x = 0; x < col; x++) {
        int rSum = 0;
        int gSum = 0;
        int bSum = 0;
        for (int j = -kernelSize / 2; j <= kernelSize / 2; j++) {
          for (int i = -kernelSize / 2; i <= kernelSize / 2; i++) {
            if (x + i >= 0 && x + i < col && y + j >= 0 && y + j < row) {
              int weight = kernel[j + kernelSize / 2][i + kernelSize / 2];
              Pixel originalPixel = originalImage.getPixel(y + j, x + i);
              rSum += weight * originalPixel.getColor(2);
              gSum += weight * originalPixel.getColor(1);
              bSum += weight * originalPixel.getColor(0);
            }
          }
        }
        int newR = clampPixelValue(roundToInt(rSum / kernelSum),
                originalImage.getMaxPixelValue());
        int newG = clampPixelValue(roundToInt(gSum / kernelSum),
                originalImage.getMaxPixelValue());
        int newB = clampPixelValue(roundToInt(bSum / kernelSum),
                originalImage.getMaxPixelValue());
        Pixel newPixel = new Pixel(newR, newG, newB, originalImage.getPixel(y, x).getAlpha());
        newPixels[y][x] = newPixel;
      }
    }
    return newPixels;
  }

  /**
   * Creates a blurred version of the given image, by applying a filter to every channel
   * of every pixel of the original image.
   * Gaussian Blur of 3 by 3 is used.
   * {{1, 2, 1},
   * {2, 4, 2},
   * {1, 2, 1}}
   *
   * @param originalImageName the name of the image that is to be blurred
   * @param resultImageName   the name of the resulting blurred image
   */
  @Override
  public void blur(String originalImageName, String resultImageName) {
    checkImageName(originalImageName);
    checkImageName(resultImageName);
    CustomImage originalImage = this.getImage(originalImageName);
    int[][] kernel = {{1, 2, 1}, {2, 4, 2}, {1, 2, 1}};
    Pixel[][] newPixels = applyFilter(originalImage, kernel);
    CustomImage blurredImage = new CustomImage(resultImageName, newPixels,
            originalImage.getMaxPixelValue());
    processedImages.put(resultImageName, blurredImage);
  }

  /**
   * Creates a sharpened version of the given image, by applying a filter to every channel
   * of every pixel of the original image.
   * Kernel used for sharpen-
   * {{-1, -1, -1, -1, -1},
   * {-1, 2, 2, 2, -1},
   * {-1, 2, 8, 2, -1},
   * {-1, 2, 2, 2, -1},
   * {-1, -1, -1, -1, -1}}
   *
   * @param originalImageName the name of the image that is to be blurred
   * @param resultImageName   the name of the resulting sharpened image
   */
  @Override
  public void sharpen(String originalImageName, String resultImageName) {
    checkImageName(originalImageName);
    checkImageName(resultImageName);
    CustomImage originalImage = this.getImage(originalImageName);
    int[][] kernel = {{-1, -1, -1, -1, -1}, {-1, 2, 2, 2, -1}, {-1, 2, 8, 2, -1}, {-1, 2, 2, 2, -1},
        {-1, -1, -1, -1, -1}};
    Pixel[][] newPixels = applyFilter(originalImage, kernel);
    CustomImage sharpenedImage = new CustomImage(resultImageName, newPixels,
            originalImage.getMaxPixelValue());
    processedImages.put(resultImageName, sharpenedImage);
  }

  @Override
  public CustomImage getImage(String imageName) {
    if (processedImages.containsKey(imageName)) {
      return processedImages.get(imageName);
    } else {
      throw new IllegalArgumentException("An Image with the given name not found.");
    }
  }

}
