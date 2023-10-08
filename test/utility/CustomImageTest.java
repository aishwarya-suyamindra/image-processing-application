package utility;


import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * A Junit test class for the CustomImage.
 */
public class CustomImageTest {

  private static final int MAX_PIXEL_VALUE = 255;

  private final Pixel red = new Pixel(255, 0, 0, 255);
  private final Pixel green = new Pixel(0, 255, 0, 255);
  private final Pixel blue = new Pixel(0, 0, 255, 255);
  private final Pixel black = new Pixel(0, 0, 0, 255);
  private final Pixel orange = new Pixel(255, 153, 51, 255);
  private final Pixel white = new Pixel(255, 255, 255, 255);

  private final CustomImage expectedImg = new CustomImage("expected_base",
          new Pixel[][]{{red, green, blue}, {black, orange, white}}, MAX_PIXEL_VALUE);


  @Test
  public void testConstructor() {
    CustomImage baseImg = new CustomImage("base", new Pixel[][]{
            {red, green, blue}, {black, orange, white}}, MAX_PIXEL_VALUE);
    assertEquals("base", baseImg.getName());
    assertEquals(MAX_PIXEL_VALUE, baseImg.getMaxPixelValue());
    assertEquals(3, baseImg.getWidth());
    assertEquals(2, baseImg.getHeight());
    for (int x = 0; x < baseImg.getHeight(); x++) {
      for (int y = 0; y < baseImg.getWidth(); y++) {
        assertEquals(baseImg.getPixel(x, y), expectedImg.getPixel(x, y));
      }
    }
    assertEquals(baseImg, expectedImg);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetPixelWithInvalidRow() {
    expectedImg.getPixel(5, 1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetPixelWithInvalidCol() {
    expectedImg.getPixel(1, 5);
  }


  @Test
  public void testGetHistogramData() {
    CustomImage baseImg = new CustomImage("base", new Pixel[][]{
            {red, green, blue}, {black, orange, white}}, MAX_PIXEL_VALUE);
    int[] red = baseImg.getPixelFrequency(2);
    int[] blue = baseImg.getPixelFrequency(0);
    int[] green = baseImg.getPixelFrequency(1);
    int[] intensity = baseImg.getPixelFrequencyByIntensity();

    for (int i = 0; i < red.length; i++) {
      if (i == 0 || i == 255) {
        assertEquals(3, red[i]);
      } else {
        assertEquals(0, red[i]);
      }
    }

    for (int i = 0; i < blue.length; i++) {
      if (i == 0) {
        assertEquals(3, blue[i]);
      } else if (i == 255) {
        assertEquals(2, blue[i]);
      } else if (i == 51) {
        assertEquals(1, blue[i]);
      } else {
        assertEquals(0, blue[i]);
      }
    }

    for (int i = 0; i < green.length; i++) {
      if (i == 0) {
        assertEquals(3, green[i]);
      } else if (i == 255) {
        assertEquals(2, green[i]);
      } else if (i == 153) {
        assertEquals(1, green[i]);
      } else {
        assertEquals(0, green[i]);
      }
    }

    for (int i = 0; i < intensity.length; i++) {
      if (i == 0) {
        assertEquals(1, intensity[i]);
      } else if (i == 255) {
        assertEquals(1, intensity[i]);
      } else if (i == 85) {
        assertEquals(3, intensity[i]);
      } else if (i == 153) {
        assertEquals(1, intensity[i]);
      } else {
        assertEquals(0, green[i]);
      }
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetPixelFreqWithInvalidChannel() {
    CustomImage baseImg = new CustomImage("base", new Pixel[][]{
            {red, green, blue}, {black, orange, white}}, MAX_PIXEL_VALUE);
    baseImg.getPixelFrequency(-1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetPixelFreqWithInvalidChannelGreaterThanTwo() {
    CustomImage baseImg = new CustomImage("base", new Pixel[][]{
            {red, green, blue}, {black, orange, white}}, MAX_PIXEL_VALUE);
    baseImg.getPixelFrequency(3);
  }
}