package model;

import utility.CustomImage;

/**
 * This interface represents a set of operations that can be performed on an image,
 * of type {@code CustomImage}.
 */
public interface ImageProcessor extends ImageProcessorRO {

  /**
   * Loads a given CustomImage.
   *
   * @param image the CustomImage to be loaded.
   */
  void loadImage(CustomImage image);

  /**
   * Creates a greyscale image, where the channel of each pixel is equal to the specified
   * channel value of that pixel in the given original image.
   *
   * @param originalImageName the name of the image that is to be visualised
   * @param resultImageName   the name of the resulting greyscale image
   * @param channel           the channel of the pixel
   */
  void visualiseChannel(String originalImageName, String resultImageName, int channel);

  /**
   * Creates a new image, where the channel of each pixel is equal to the maximum channel
   * value of that pixel in the given original image(excluding opacity component).
   *
   * @param originalImageName the name of the image to visualise the value of
   * @param resultImageName   the name of the result image
   */
  void visualiseValue(String originalImageName, String resultImageName);

  /**
   * Creates a new image, where the channel of each pixel is equal to the average of the channel
   * values of that pixel in the given original image(excluding opacity component).
   *
   * @param originalImageName the name of the image to visualise the intensity of
   * @param resultImageName   the name of the result image
   */
  void visualiseIntensity(String originalImageName, String resultImageName);

  /**
   * Creates a new image, where the channel of each pixel is equal to the weighted sum of the
   * channel values of that pixel in the given original image(excluding opacity component).
   *
   * @param originalImageName the name of the image to visualise the luma of
   * @param resultImageName   the name of the result image
   */
  void visualiseLuma(String originalImageName, String resultImageName);

  /**
   * Creates a flipped image of the given original image.
   *
   * @param originalImageName the name of the image that is to be flipped
   * @param resultImageName   the name of the resulting flipped image
   * @param axis              the axis along which the image has to be flipped.
   */
  void flip(String originalImageName, String resultImageName, int axis);

  /**
   * Creates a new image, with each pixel brightened by the given {@code increment} value.
   *
   * @param originalImageName the name of the image that is to be brightened
   * @param resultImageName   the name of the resulting brightened image
   * @param increment         the constant value by which the image is to be brightened
   */
  void brighten(String originalImageName, String resultImageName, int increment);

  /**
   * Splits the given original image into three grayscale images with the given result image names.
   *
   * @param originalImageName    the name of the image that is to be split
   * @param resultRedImageName   the name of the grayscale image of the red channel
   * @param resultGreenImageName the name of the grayscale image of the green channel
   * @param resultBlueImageName  the name of the grayscale image of the blue channel
   */
  void split(String originalImageName, String resultRedImageName,
             String resultGreenImageName, String resultBlueImageName);


  /**
   * Combines the given greyscale images into a single image that takes each of its channel
   * values from the given three images respectively.
   *
   * @param redImageName    the name of the greyscale image for the red channel
   * @param greenImageName  the name of the greyscale image for the green channel
   * @param blueImageName   the name of the greyscale image for the blue channel
   * @param resultImageName the name of the resulting combined image
   */
  void combine(String redImageName,
               String greenImageName, String blueImageName, String resultImageName);


  /**
   * Creates a blurred version of the given image, by applying a filter to every channel
   * of every pixel of the original image.
   *
   * @param originalImageName the name of the image that is to be blurred
   * @param resultImageName   the name of the resulting blurred image
   */
  void blur(String originalImageName, String resultImageName);


  /**
   * Creates a sharpened version of the given image, by applying a filter to every channel
   * of every pixel of the original image.
   *
   * @param originalImageName the name of the image that is to be blurred
   * @param resultImageName   the name of the resulting sharpened image
   */
  void sharpen(String originalImageName, String resultImageName);

  /**
   * Creates a sepia-toned version of the given image, by applying a color transformation matrix
   * to every channel of every pixel in the original image.
   *
   * @param originalImageName the name of the image to get the sepia-tone of
   * @param resultImageName   the name of the resulting image
   */
  void sepia(String originalImageName, String resultImageName);

  /**
   * Creates an image made of dots using a few colors from the given original image that has many
   * colors.
   *
   * @param originalImageName the name of the image that is to be dithered
   * @param resultImageName   the name of the resulting dithered image
   */
  void dither(String originalImageName, String resultImageName);
}
