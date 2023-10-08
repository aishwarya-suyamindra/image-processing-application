package utility;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * A Junit test class for the Pixel class.
 */
public class PixelTest {
  Pixel expectedPixel = new Pixel(255, 0, 0, 255);

  @Test
  public void testConstructor() {
    Pixel p = new Pixel(255, 0, 0, 255);
    int red = p.getColor(2);
    int green = p.getColor(1);
    int blue = p.getColor(0);
    int alpha = p.getAlpha();

    assertEquals(255, red);
    assertEquals(0, green);
    assertEquals(0, blue);
    assertEquals(255, alpha);
    assertEquals(expectedPixel, p);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetColorInvalidParameter() {
    Pixel p = new Pixel(255, 0, 0, 255);
    p.getColor(4);
  }
}