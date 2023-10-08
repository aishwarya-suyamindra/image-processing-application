package utility;

import java.util.Arrays;

/**
 * This class represents an image. Each image is identified by a name and,
 * a 2D array of Pixel that contains the actual data of the image.
 */
public class CustomImage {
  private final String name;
  private final Pixel[][] pixels;
  private final int maxPixelValue;

  /**
   * Constructs a Custom Image with the given name and 2D array of Pixel.
   *
   * @param name          is the name used to identify the CustomImage
   * @param pixels        the actual pixel values of the image
   * @param maxPixelValue the maximum value a channel of the Pixel can have
   */
  public CustomImage(String name, Pixel[][] pixels, int maxPixelValue) {
    this.name = name;
    this.pixels = pixels;
    this.maxPixelValue = maxPixelValue;
  }

  /**
   * Returns the height of the image.
   *
   * @return height of image
   */
  public int getHeight() {
    return this.pixels.length;
  }

  /**
   * Returns the width of the image.
   *
   * @return width of the image.
   */
  public int getWidth() {
    return this.pixels[0].length;
  }

  /**
   * Returns the max value that a channel of the Pixel can have.
   *
   * @return max value of a channel of Pixel.
   */
  public int getMaxPixelValue() {
    return this.maxPixelValue;
  }

  /**
   * Returns the Pixel at the given position from the 2D array.
   *
   * @param row the row index
   * @param col the col index
   * @return the Pixel at the given row and col.
   */
  public Pixel getPixel(int row, int col) {
    if (row >= this.getHeight() || col >= this.getWidth()) {
      throw new IllegalArgumentException("The row and col position exceeds "
              + "the height and width of the image.");
    }
    return this.pixels[row][col];
  }

  /**
   * Returns the name of the CustomImage.
   *
   * @return the name of the image.
   */
  public String getName() {
    return this.name;
  }

  // helper method to compute the frequency of each distinct value for the given component.
  private int[] getFrequency(int component) {
    int[] freq = new int[256];
    for (int i = 0; i < this.getHeight(); i++) {
      for (int j = 0; j < this.getWidth(); j++) {
        Pixel p = this.getPixel(i, j);
        if (component == 3) {
          int val = (p.getColor(2) + p.getColor(1) + p.getColor(0)) / 3;
          freq[val]++;
        } else {
          freq[p.getColor(component)]++;
        }
      }
    }
    return freq;
  }

  /**
   * Returns an array with frequency of each distinct value in the range of 0-255, for the given
   * channel.
   *
   * @param channel the channel for which frequency should be calculated
   * @return array of frequency for the given channel.
   * @throws IllegalArgumentException if invalid channel is provided.
   */
  public int[] getPixelFrequency(int channel) throws IllegalArgumentException {
    if (channel < 0 || channel > 2) {
      throw new IllegalArgumentException("Invalid channel value");
    }
    return getFrequency(channel);
  }

  /**
   * Returns an array with frequency of each distinct channel values 0-255 on average of all
   * three components.
   *
   * @return int array of frequency.
   */
  public int[] getPixelFrequencyByIntensity() {
    return getFrequency(3);
  }

  /**
   * Compares if two CustomImage objects are equal. Two CustomImage Objects are equal if they have,
   * the same width, height and, pixel values at each position in the 2D array of Pixels
   *
   * @param o Object to be compares with {@code this}
   * @return boolean value after comparing the two objects.
   */
  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }

    if (!(o instanceof CustomImage)) {
      return false;
    }
    CustomImage img2 = (CustomImage) o;

    if (this.getWidth() == img2.getWidth() && this.getHeight() == img2.getHeight()) {
      for (int x = 0; x < this.getHeight(); x++) {
        for (int y = 0; y < this.getWidth(); y++) {
          if (!this.getPixel(x, y).equals(img2.getPixel(x, y))) {
            return false;
          }
        }
      }
    } else {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    return Arrays.deepHashCode(this.pixels) * this.name.hashCode();
  }
}
