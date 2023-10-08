package utility;

/**
 * This class represents a pixel with colors RGB(red, blue, green) and,
 * alpha that determines the opacity. Each channel of the Pixel is an 8-bit representation and
 * can have values ranging from 0-255.
 */
public class Pixel {
  private final int red;
  private final int blue;
  private final int green;
  private final int alpha;

  /**
   * It constructs a Pixel given the values for each color (RGB) and alpha.
   *
   * @param red   the value for intensity of the color red.
   * @param green the value for intensity of the color green.
   * @param blue  the value for intensity of the color blue.
   * @param alpha the value for intensity of the color alpha.
   */
  public Pixel(int red, int green, int blue, int alpha) {
    this.red = red;
    this.blue = blue;
    this.green = green;
    this.alpha = alpha;
  }


  /**
   * Returns the value of a particular color.
   *
   * @param color is an integer value that indicates the color to be returns.
   *              Red - 2
   *              Green - 1
   *              Blue - 0
   * @return the value of the color in the Pixel.
   */
  public int getColor(int color) {
    if (color == 2) {
      return this.red;
    } else if (color == 1) {
      return this.green;
    } else if (color == 0) {
      return this.blue;
    } else {
      throw new IllegalArgumentException("The Pixel does not have the requested color.");
    }
  }

  public int getAlpha() {
    return this.alpha;
  }


  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }

    if (!(o instanceof Pixel)) {
      return false;
    }
    Pixel p = (Pixel) o;

    return this.blue == p.getColor(0) && this.red == p.getColor(2)
            && this.green == p.getColor(1) && this.alpha == p.getAlpha();
  }

  @Override
  public int hashCode() {
    return this.blue * this.green * this.red * this.alpha;
  }
}
