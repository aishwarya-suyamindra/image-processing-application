package model;

import org.junit.Before;
import org.junit.Test;

import utility.CustomImage;
import utility.Pixel;

import static org.junit.Assert.assertEquals;

/**
 * A Junit test class for the ImageProcessorImpl class.
 */
public class ImageProcessorImplTest {
  private CustomImage baseImg;
  private CustomImage overrideBaseImg;
  private CustomImage expectedRedGrayScale;
  private CustomImage expectedGreenGrayScale;
  private CustomImage expectedBlueGrayScale;
  private CustomImage expectedValueGrayScale;
  private CustomImage expectedIntensityGrayScale;
  private CustomImage expectedLumaGrayScale;
  private CustomImage expectedFlipHorizontal;
  private CustomImage expectedFlipVertical;
  private CustomImage expectedFlip_HV;
  private CustomImage expectedFlip_HVV;
  private CustomImage expectedFlipMultipleTime;
  private CustomImage expectedBrightenImg;
  private CustomImage expectedBrightenReduceImg;
  private CustomImage expectedCombineBrightenRedImg;
  private CustomImage truncatedBaseImg;
  private CustomImage baseImage;
  private CustomImage baseSharpImg;
  private CustomImage expectedBaseSharpImgOne;
  private CustomImage expectedBaseSharpImgTwo;
  private CustomImage expectedBaseImgBlurOne;
  private CustomImage expectedBaseImgBlurTwo;
  private CustomImage expectedImgOnMultipleOperations;
  private CustomImage expectedSepiaImage;
  private CustomImage expectedDitherImage;
  private static final int MAX_PIXEL_VALUE = 255;
  private ImageProcessor model;
  private CustomImage expectedImgOnMultipleOperationsWithNewFeature;

  @Before
  public void setUp() {

    model = new ImageProcessorImpl();

    Pixel red = new Pixel(255, 0, 0, 255);
    Pixel green = new Pixel(0, 255, 0, 255);
    Pixel blue = new Pixel(0, 0, 255, 255);
    Pixel black = new Pixel(0, 0, 0, 255);
    Pixel orange = new Pixel(255, 153, 51, 255);
    Pixel white = new Pixel(255, 255, 255, 255);

    Pixel red_redGreyScale = new Pixel(255, 255, 255, 255);
    Pixel green_redGreyScale = new Pixel(0, 0, 0, 255);
    Pixel blue_redGreyScale = new Pixel(0, 0, 0, 255);
    Pixel black_redGreyScale = new Pixel(0, 0, 0, 255);
    Pixel orange_redGreyScale = new Pixel(255, 255, 255, 255);
    Pixel white_redGreyScale = new Pixel(255, 255, 255, 255);

    Pixel red_blueGreyScale = new Pixel(0, 0, 0, 255);
    Pixel green_blueGreyScale = new Pixel(0, 0, 0, 255);
    Pixel blue_blueGreyScale = new Pixel(255, 255, 255, 255);
    Pixel black_blueGreyScale = new Pixel(0, 0, 0, 255);
    Pixel orange_blueGreyScale = new Pixel(51, 51, 51, 255);
    Pixel white_blueGreyScale = new Pixel(255, 255, 255, 255);

    Pixel red_greenGreyScale = new Pixel(0, 0, 0, 255);
    Pixel green_greenGreyScale = new Pixel(255, 255, 255, 255);
    Pixel blue_greenGreyScale = new Pixel(0, 0, 0, 255);
    Pixel black_greenGreyScale = new Pixel(0, 0, 0, 255);
    Pixel orange_greenGreyScale = new Pixel(153, 153, 153, 255);
    Pixel white_greenGreyScale = new Pixel(255, 255, 255, 255);

    Pixel red_valueGreyScale = new Pixel(255, 255, 255, 255);
    Pixel green_valueGreyScale = new Pixel(255, 255, 255, 255);
    Pixel blue_valueGreyScale = new Pixel(255, 255, 255, 255);
    Pixel black_valueGreyScale = new Pixel(0, 0, 0, 255);
    Pixel orange_valueGreyScale = new Pixel(255, 255, 255, 255);
    Pixel white_valueGreyScale = new Pixel(255, 255, 255, 255);

    Pixel red_intensityGreyScale = new Pixel(85, 85, 85, 255);
    Pixel green_intensityGreyScale = new Pixel(85, 85, 85, 255);
    Pixel blue_intensityGreyScale = new Pixel(85, 85, 85, 255);
    Pixel black_intensityGreyScale = new Pixel(0, 0, 0, 255);
    Pixel orange_intensityGreyScale = new Pixel(153, 153, 153, 255);
    Pixel white_intensityGreyScale = new Pixel(255, 255, 255, 255);

    Pixel red_lumaGreyScale = new Pixel(55, 55, 55, 255);
    Pixel green_lumaGreyScale = new Pixel(182, 182, 182, 255);
    Pixel blue_lumaGreyScale = new Pixel(18, 18, 18, 255);
    Pixel black_lumaGreyScale = new Pixel(0, 0, 0, 255);
    Pixel orange_lumaGreyScale = new Pixel(168, 168, 168, 255);
    Pixel white_lumaGreyScale = new Pixel(255, 255, 255, 255);

    Pixel red_brighten = new Pixel(255, 10, 10, 255);
    Pixel green_brighten = new Pixel(10, 255, 10, 255);
    Pixel blue_brighten = new Pixel(10, 10, 255, 255);
    Pixel black_brighten = new Pixel(10, 10, 10, 255);
    Pixel orange_brighten = new Pixel(255, 163, 61, 255);
    Pixel white_brighten = new Pixel(255, 255, 255, 255);

    Pixel red_brightness_reduce = new Pixel(245, 0, 0, 255);
    Pixel green_brightness_reduce = new Pixel(0, 245, 0, 255);
    Pixel blue_brightness_reduce = new Pixel(0, 0, 245, 255);
    Pixel black_brightness_reduce = new Pixel(0, 0, 0, 255);
    Pixel orange_brightness_reduce = new Pixel(245, 143, 41, 255);
    Pixel white_brightness_reduce = new Pixel(245, 245, 245, 255);

    Pixel red_combine_bright = new Pixel(255, 0, 0, 255);
    Pixel green_combine_bright = new Pixel(30, 255, 0, 255);
    Pixel blue_combine_bright = new Pixel(30, 0, 255, 255);
    Pixel black_combine_bright = new Pixel(30, 0, 0, 255);
    Pixel orange_combine_bright = new Pixel(255, 153, 51, 255);
    Pixel white_combine_bright = new Pixel(255, 255, 255, 255);

    Pixel red_multipleOperations = new Pixel(255, 85, 0, 255);
    Pixel green_multipleOperations = new Pixel(50, 85, 0, 255);
    Pixel blue_multipleOperations = new Pixel(50, 85, 235, 255);
    Pixel black_multipleOperations = new Pixel(50, 0, 0, 255);
    Pixel orange_multipleOperations = new Pixel(255, 153, 31, 255);
    Pixel white_multipleOperations = new Pixel(255, 255, 235, 255);

    Pixel lightBlue = new Pixel(62, 207, 255, 255);
    Pixel purple = new Pixel(179, 60, 255, 255);
    Pixel yellow = new Pixel(191, 255, 12, 255);

    Pixel pink = new Pixel(239, 28, 255, 255);
    Pixel seaGreen = new Pixel(8, 135, 133, 255);
    Pixel brown = new Pixel(135, 79, 29, 255);
    Pixel grey = new Pixel(135, 135, 101, 255);
    Pixel lightPink = new Pixel(135, 75, 131, 255);
    Pixel forestGreen = new Pixel(38, 135, 7, 255);
    Pixel bluePurple = new Pixel(77, 53, 135, 255);
    Pixel darkPurple = new Pixel(135, 9, 129, 255);
    Pixel pinkPurple = new Pixel(135, 10, 114, 255);
    Pixel lightGreen = new Pixel(73, 135, 125, 255);
    Pixel darkGrey = new Pixel(167, 164, 168, 255);
    Pixel greyBlue = new Pixel(90, 96, 135, 255);
    Pixel brickRed = new Pixel(135, 42, 4, 255);
    Pixel yellowGreen = new Pixel(135, 133, 37, 255);
    Pixel darkRed = new Pixel(135, 1, 10, 255);
    Pixel mustard = new Pixel(168, 168, 2, 255);

    Pixel redBlurOne = new Pixel(80, 41, 3, 255);
    Pixel greenBlurOne = new Pixel(80, 99, 54, 255);
    Pixel blueBlurOne = new Pixel(48, 73, 99, 255);
    Pixel blackBlurOne = new Pixel(83, 65, 54, 255);
    Pixel orangeBlurOne = new Pixel(150, 138, 109, 255);
    Pixel whiteBlurOne = new Pixel(131, 134, 119, 255);
    Pixel lightBlueBlurOne = new Pixel(54, 69, 99, 255);
    Pixel purpleBlurOne = new Pixel(124, 108, 119, 255);
    Pixel yellowBlurOne = new Pixel(118, 113, 70, 255);

    Pixel redBlurTwo = new Pixel(50, 39, 21, 255);
    Pixel greenBlurTwo = new Pixel(68, 69, 51, 255);
    Pixel blueBlurTwo = new Pixel(48, 56, 53, 255);
    Pixel blackBlurTwo = new Pixel(69, 60, 51, 255);
    Pixel orangeBlurTwo = new Pixel(109, 104, 87, 255);
    Pixel whiteBlurTwo = new Pixel(85, 87, 75, 255);
    Pixel lightBlueBlurTwo = new Pixel(49, 48, 53, 255);
    Pixel purpleBlurTwo = new Pixel(85, 79, 75, 255);
    Pixel yellowBlurTwo = new Pixel(71, 67, 54, 255);


    Pixel redSharpOne = new Pixel(233, 5, 0, 255);
    Pixel greenSharpOne = new Pixel(74, 255, 23, 255);
    Pixel blueSharpOne = new Pixel(96, 67, 255, 255);
    Pixel blackSharpOne = new Pixel(89, 96, 28, 255);
    Pixel orangeSharpOne = new Pixel(255, 255, 194, 255);
    Pixel whiteSharpOne = new Pixel(255, 255, 255, 255);
    Pixel lightBlueSharpOne = new Pixel(74, 145, 255, 255);
    Pixel purpleSharpOne = new Pixel(255, 190, 255, 255);
    Pixel yellowSharpOne = new Pixel(255, 255, 97, 255);

    Pixel pinkSharpOne = new Pixel(255, 62, 255, 255);
    Pixel seaGreenSharpOne = new Pixel(58, 106, 147, 255);
    Pixel brownSharpOne = new Pixel(255, 235, 211, 255);
    Pixel greySharpOne = new Pixel(171, 146, 124, 255);
    Pixel lightPinkSharpOne = new Pixel(244, 244, 192, 255);
    Pixel forestGreenSharpOne = new Pixel(41, 159, 11, 255);
    Pixel bluePurpleSharpOne = new Pixel(106, 56, 255, 255);
    Pixel darkPurpleSharpOne = new Pixel(255, 136, 255, 255);
    Pixel pinkPurpleSharpOne = new Pixel(242, 7, 148, 255);
    Pixel lightGreenSharpOne = new Pixel(195, 255, 142, 255);
    Pixel darkGreySharpOne = new Pixel(181, 184, 168, 255);
    Pixel greyBlueSharpOne = new Pixel(89, 39, 118, 255);
    Pixel brickRedSharpOne = new Pixel(181, 26, 43, 255);
    Pixel yellowGreenSharpOne = new Pixel(150, 31, 0, 255);
    Pixel darkRedSharpOne = new Pixel(203, 82, 54, 255);
    Pixel mustardSharpOne = new Pixel(183, 167, 40, 255);

    Pixel redSharpTwo = new Pixel(221, 43, 0, 255);
    Pixel greenSharpTwo = new Pixel(139, 255, 48, 255);
    Pixel blueSharpTwo = new Pixel(192, 164, 255, 255);
    Pixel blackSharpTwo = new Pixel(161, 212, 52, 255);
    Pixel orangeSharpTwo = new Pixel(255, 255, 255, 255);
    Pixel whiteSharpTwo = new Pixel(255, 255, 255, 255);
    Pixel lightBlueSharpTwo = new Pixel(117, 211, 255, 255);
    Pixel purpleSharpTwo = new Pixel(255, 255, 255, 255);
    Pixel yellowSharpTwo = new Pixel(255, 255, 255, 255);

    Pixel pinkSharpTwo = new Pixel(255, 95, 255, 255);
    Pixel seaGreenSharpTwo = new Pixel(117, 94, 193, 255);
    Pixel brownSharpTwo = new Pixel(255, 255, 255, 255);
    Pixel greySharpTwo = new Pixel(231, 220, 195, 255);
    Pixel lightPinkSharpTwo = new Pixel(255, 255, 255, 255);
    Pixel forestGreenSharpTwo = new Pixel(90, 255, 64, 255);
    Pixel bluePurpleSharpTwo = new Pixel(164, 78, 255, 255);
    Pixel darkPurpleSharpTwo = new Pixel(255, 146, 255, 255);
    Pixel pinkPurpleSharpTwo = new Pixel(255, 95, 200, 255);
    Pixel lightGreenSharpTwo = new Pixel(255, 255, 152, 255);
    Pixel darkGreySharpTwo = new Pixel(232, 255, 173, 255);
    Pixel greyBlueSharpTwo = new Pixel(103, 15, 162, 255);
    Pixel brickRedSharpTwo = new Pixel(238, 0, 113, 255);
    Pixel yellowGreenSharpTwo = new Pixel(241, 0, 0, 255);
    Pixel darkRedSharpTwo = new Pixel(255, 117, 72, 255);
    Pixel mustardSharpTwo = new Pixel(211, 210, 75, 255);

    Pixel redIntermediate = new Pixel(145, 93, 22, 255);
    Pixel greenIntermediate = new Pixel(255, 255, 234, 255);
    Pixel blueIntermediate = new Pixel(150, 203, 200, 255);
    Pixel blackIntermediate = new Pixel(255, 255, 234, 255);
    Pixel orangeIntermediate = new Pixel(255, 255, 255, 255);
    Pixel whiteIntermediate = new Pixel(255, 255, 255, 255);
    Pixel lightBlueIntermediate = new Pixel(155, 146, 200, 255);
    Pixel purpleIntermediate = new Pixel(255, 255, 255, 255);
    Pixel yellowIntermediate = new Pixel(255, 255, 228, 255);

    Pixel redSepia = new Pixel(64, 57, 44, 255);
    Pixel greenSepia = new Pixel(118, 105, 82, 255);
    Pixel blueSepia = new Pixel(94, 83, 65, 255);
    Pixel blackSepia = new Pixel(93, 83, 64, 255);
    Pixel orangeSepia = new Pixel(186, 165, 129, 255);
    Pixel whiteSepia = new Pixel(177, 158, 123, 255);
    Pixel lightBlueSepia = new Pixel(93, 83, 65, 255);
    Pixel purpleSepia = new Pixel(154, 137, 107, 255);
    Pixel yellowSepia = new Pixel(147, 130, 102, 255);

    Pixel redDither = new Pixel(0, 0, 0, 255);
    Pixel greenDither = new Pixel(255, 255, 255, 255);
    Pixel blueDither = new Pixel(0, 0, 0, 255);
    Pixel blackDither = new Pixel(255, 255, 255, 255);
    Pixel orangeDither = new Pixel(255, 255, 255, 255);
    Pixel whiteDither = new Pixel(255, 255, 255, 255);
    Pixel lightBlueDither = new Pixel(0, 0, 0, 255);
    Pixel purpleDither = new Pixel(255, 255, 255, 255);
    Pixel yellowDither = new Pixel(255, 255, 255, 255);


    baseImage = new CustomImage("base", new Pixel[][]{
            {red, green, blue},
            {black, orange, white},
            {lightBlue, purple, yellow}
    }, MAX_PIXEL_VALUE);

    expectedBaseImgBlurOne = new CustomImage("baseBlurOne", new Pixel[][]{
            {redBlurOne, greenBlurOne, blueBlurOne},
            {blackBlurOne, orangeBlurOne, whiteBlurOne},
            {lightBlueBlurOne, purpleBlurOne, yellowBlurOne}
    }, MAX_PIXEL_VALUE);

    expectedBaseImgBlurTwo = new CustomImage("baseBlurTwo", new Pixel[][]{
            {redBlurTwo, greenBlurTwo, blueBlurTwo},
            {blackBlurTwo, orangeBlurTwo, whiteBlurTwo},
            {lightBlueBlurTwo, purpleBlurTwo, yellowBlurTwo}
    }, MAX_PIXEL_VALUE);

    baseSharpImg = new CustomImage("baseSharp", new Pixel[][]{
            {red, green, blue, pink, seaGreen},
            {black, orange, white, brown, grey},
            {lightBlue, purple, yellow, lightPink, forestGreen},
            {bluePurple, darkPurple, pinkPurple, lightGreen, darkGrey},
            {greyBlue, brickRed, yellowGreen, darkRed, mustard}
    }, MAX_PIXEL_VALUE);

    expectedBaseSharpImgOne = new CustomImage("baseSharpOne", new Pixel[][]{
            {redSharpOne, greenSharpOne, blueSharpOne, pinkSharpOne, seaGreenSharpOne},
            {blackSharpOne, orangeSharpOne, whiteSharpOne, brownSharpOne, greySharpOne},
            {lightBlueSharpOne, purpleSharpOne, yellowSharpOne, lightPinkSharpOne,
             forestGreenSharpOne},
            {bluePurpleSharpOne, darkPurpleSharpOne, pinkPurpleSharpOne, lightGreenSharpOne,
             darkGreySharpOne},
            {greyBlueSharpOne, brickRedSharpOne, yellowGreenSharpOne, darkRedSharpOne,
             mustardSharpOne}
    }, MAX_PIXEL_VALUE);

    expectedBaseSharpImgTwo = new CustomImage("baseSharpTwo", new Pixel[][]{
            {redSharpTwo, greenSharpTwo, blueSharpTwo, pinkSharpTwo, seaGreenSharpTwo},
            {blackSharpTwo, orangeSharpTwo, whiteSharpTwo, brownSharpTwo, greySharpTwo},
            {lightBlueSharpTwo, purpleSharpTwo, yellowSharpTwo,
             lightPinkSharpTwo, forestGreenSharpTwo},
            {bluePurpleSharpTwo, darkPurpleSharpTwo, pinkPurpleSharpTwo,
             lightGreenSharpTwo, darkGreySharpTwo},
            {greyBlueSharpTwo, brickRedSharpTwo, yellowGreenSharpTwo,
             darkRedSharpTwo, mustardSharpTwo}
    }, MAX_PIXEL_VALUE);

    expectedImgOnMultipleOperationsWithNewFeature = new CustomImage("intermediateImage",
            new Pixel[][]{
                {redIntermediate, greenIntermediate, blueIntermediate},
                {blackIntermediate, orangeIntermediate, whiteIntermediate},
                {lightBlueIntermediate, purpleIntermediate, yellowIntermediate}
            }, MAX_PIXEL_VALUE);

    expectedSepiaImage = new CustomImage("sepiaImage", new Pixel[][]{
            {redSepia, greenSepia, blueSepia},
            {blackSepia, orangeSepia, whiteSepia},
            {lightBlueSepia, purpleSepia, yellowSepia}
    }, MAX_PIXEL_VALUE);

    expectedDitherImage = new CustomImage("ditherImage", new Pixel[][]{
            {redDither, greenDither, blueDither},
            {blackDither, orangeDither, whiteDither},
            {lightBlueDither, purpleDither, yellowDither}
    }, MAX_PIXEL_VALUE);

    expectedImgOnMultipleOperations = new CustomImage("multipleOperations", new Pixel[][]{
            {red_multipleOperations, green_multipleOperations, blue_multipleOperations},
            {black_multipleOperations, orange_multipleOperations, white_multipleOperations}
    }, MAX_PIXEL_VALUE);

    baseImg = new CustomImage("base", new Pixel[][]{
            {red, green, blue},
            {black, orange, white}
    }, MAX_PIXEL_VALUE);

    truncatedBaseImg = new CustomImage("base_truncated", new Pixel[][]{
            {red, green},
            {black, orange}
    }, MAX_PIXEL_VALUE);

    overrideBaseImg = new CustomImage("base", new Pixel[][]{
            {blue, green, red},
            {black, orange, white}
    }, MAX_PIXEL_VALUE);

    expectedRedGrayScale = new CustomImage("expected_red_greyscale", new Pixel[][]{
            {red_redGreyScale, green_redGreyScale, blue_redGreyScale},
            {black_redGreyScale, orange_redGreyScale, white_redGreyScale}
    }, MAX_PIXEL_VALUE);

    expectedGreenGrayScale = new CustomImage("expected_red_greyscale", new Pixel[][]{
            {red_greenGreyScale, green_greenGreyScale, blue_greenGreyScale},
            {black_greenGreyScale, orange_greenGreyScale, white_greenGreyScale}
    }, MAX_PIXEL_VALUE);

    expectedBlueGrayScale = new CustomImage("expected_red_greyscale", new Pixel[][]{
            {red_blueGreyScale, green_blueGreyScale, blue_blueGreyScale},
            {black_blueGreyScale, orange_blueGreyScale, white_blueGreyScale}
    }, MAX_PIXEL_VALUE);

    expectedValueGrayScale = new CustomImage("expected_value_greyscale", new Pixel[][]{
            {red_valueGreyScale, green_valueGreyScale, blue_valueGreyScale},
            {black_valueGreyScale, orange_valueGreyScale, white_valueGreyScale}
    }, MAX_PIXEL_VALUE);

    expectedIntensityGrayScale = new CustomImage("expected_intensity_greyscale",
            new Pixel[][]{
                {red_intensityGreyScale, green_intensityGreyScale, blue_intensityGreyScale},
                {black_intensityGreyScale, orange_intensityGreyScale, white_intensityGreyScale}
            }, MAX_PIXEL_VALUE);

    expectedLumaGrayScale = new CustomImage("expected_luma_greyscale", new Pixel[][]{
            {red_lumaGreyScale, green_lumaGreyScale, blue_lumaGreyScale},
            {black_lumaGreyScale, orange_lumaGreyScale, white_lumaGreyScale}
    }, MAX_PIXEL_VALUE);

    expectedFlipVertical = new CustomImage("expected_flip_horizontal", new Pixel[][]{
            {black, orange, white},
            {red, green, blue}
    }, MAX_PIXEL_VALUE);

    expectedFlipHorizontal = new CustomImage("expected_flip_vertical", new Pixel[][]{
            {blue, green, red},
            {white, orange, black}
    }, MAX_PIXEL_VALUE);

    expectedFlip_HV = new CustomImage("expected_flip_horizontal_vertical", new Pixel[][]{
            {white, orange, black},
            {blue, green, red}
    }, MAX_PIXEL_VALUE);

    expectedFlip_HVV = new CustomImage("expected_flip_horizontal_vertical_vertical",
            new Pixel[][]{
                {blue, green, red},
                {white, orange, black}
            }, MAX_PIXEL_VALUE);

    // flip H,V,V,H,V
    expectedFlipMultipleTime = new CustomImage("expected_flip_multiple", new Pixel[][]{
            {black, orange, white},
            {red, green, blue}

    }, MAX_PIXEL_VALUE);

    expectedBrightenImg = new CustomImage("expected_brighten_increase", new Pixel[][]{
            {red_brighten, green_brighten, blue_brighten},
            {black_brighten, orange_brighten, white_brighten}
    }, MAX_PIXEL_VALUE);

    expectedBrightenReduceImg = new CustomImage("expected_brighten_decrease", new Pixel[][]{
            {red_brightness_reduce, green_brightness_reduce, blue_brightness_reduce},
            {black_brightness_reduce, orange_brightness_reduce, white_brightness_reduce}
    }, MAX_PIXEL_VALUE);

    expectedCombineBrightenRedImg = new CustomImage("expected_combine_red_brighten",
            new Pixel[][]{
                {red_combine_bright, green_combine_bright, blue_combine_bright},
                {black_combine_bright, orange_combine_bright, white_combine_bright}
            }, MAX_PIXEL_VALUE);
  }

  @Test
  public void testLoadImage() {
    model.loadImage(baseImg);
    CustomImage actualBaseImg = model.getImage(baseImg.getName());
    assertEquals(baseImg.getWidth(), actualBaseImg.getWidth());
    assertEquals(baseImg.getHeight(), actualBaseImg.getHeight());
    assertEquals(baseImg, actualBaseImg);
  }

  @Test
  public void testLoadImageOverrideWhenSameImageNameIsPassed() {
    model.loadImage(baseImg);
    CustomImage actualBaseImg = model.getImage(baseImg.getName());
    assertEquals(baseImg.getWidth(), actualBaseImg.getWidth());
    assertEquals(baseImg.getHeight(), actualBaseImg.getHeight());
    assertEquals(baseImg, actualBaseImg);
    // The object overrideBaseImage is a different image but with same name as the baseImg.
    model.loadImage(overrideBaseImg);
    actualBaseImg = model.getImage(baseImg.getName());
    assertEquals(overrideBaseImg.getWidth(), actualBaseImg.getWidth());
    assertEquals(overrideBaseImg.getHeight(), actualBaseImg.getHeight());
    assertEquals(overrideBaseImg, actualBaseImg);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testLoadWhenFilePathIsNullThrowsException() {
    model.loadImage(null);
  }

  @Test
  public void testGetImageWhenImageIsLoaded() {
    model.loadImage(baseImg);
    CustomImage actualBaseImg = model.getImage(baseImg.getName());
    assertEquals(baseImg.getWidth(), actualBaseImg.getWidth());
    assertEquals(baseImg.getHeight(), actualBaseImg.getHeight());
    assertEquals(baseImg, actualBaseImg);
  }

  @Test
  public void testGetImageWhenLoadedImageIsOverridden() {
    model.loadImage(baseImg);
    model.loadImage(overrideBaseImg);
    CustomImage actualBaseImg = model.getImage(baseImg.getName());
    assertEquals(overrideBaseImg.getWidth(), actualBaseImg.getWidth());
    assertEquals(overrideBaseImg.getHeight(), actualBaseImg.getHeight());
    assertEquals(overrideBaseImg, actualBaseImg);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetImageWhenGivenImageIsNotFound() {
    model.loadImage(baseImg);
    model.getImage("base123");
  }


  @Test(expected = IllegalArgumentException.class)
  public void testVisualiseChannelWhenChannelValueIsInvalid() {
    model.loadImage(baseImg);
    model.visualiseChannel("base", "base_red_greyscale", 3);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testVisualiseChannelWhenOriginalImageNotFound() {
    model.loadImage(baseImg);
    model.visualiseChannel("base1", "base_red_greyscale", 1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testVisualiseChannelWhenOriginalImageNameIsNull() {
    model.loadImage(baseImg);
    model.visualiseChannel(null, "base_red_greyscale", 1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testVisualiseChannelWhenResultImageNameIsNull() {
    model.loadImage(baseImg);
    model.visualiseChannel("base", null, 1);
  }

  @Test
  public void testVisualiseChannelRed() {
    model.loadImage(baseImg);
    model.visualiseChannel("base", "base_red_greyscale", 2);
    CustomImage actualRedGreyScale = model.getImage("base_red_greyscale");
    assertEquals(expectedRedGrayScale.getWidth(), actualRedGreyScale.getWidth());
    assertEquals(expectedRedGrayScale.getHeight(), actualRedGreyScale.getHeight());
    assertEquals(expectedRedGrayScale, actualRedGreyScale);
  }

  @Test
  public void testVisualiseChannelGreen() {
    model.loadImage(baseImg);
    model.visualiseChannel("base", "base_green_greyscale", 1);
    CustomImage actualGreenGreyScale = model.getImage("base_green_greyscale");
    assertEquals(expectedGreenGrayScale.getWidth(), actualGreenGreyScale.getWidth());
    assertEquals(expectedGreenGrayScale.getHeight(), actualGreenGreyScale.getHeight());
    assertEquals(expectedGreenGrayScale, actualGreenGreyScale);
  }

  @Test
  public void testVisualiseChannelBlue() {
    model.loadImage(baseImg);
    model.visualiseChannel("base", "base_blue_greyscale", 0);
    CustomImage actualBlueGreyScale = model.getImage("base_blue_greyscale");
    assertEquals(expectedBlueGrayScale.getWidth(), actualBlueGreyScale.getWidth());
    assertEquals(expectedBlueGrayScale.getHeight(), actualBlueGreyScale.getHeight());
    assertEquals(expectedBlueGrayScale, actualBlueGreyScale);
  }


  @Test(expected = IllegalArgumentException.class)
  public void testVisualiseValueWhenOriginalImageNotFound() {
    model.loadImage(baseImg);
    model.visualiseValue("base1", "base_value_visualise");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testVisualiseValueWhenOriginalImageNameIsNull() {
    model.loadImage(baseImg);
    model.visualiseValue(null, "base_value_visualise");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testVisualiseValueWhenResultImageNameIsNull() {
    model.loadImage(baseImg);
    model.visualiseValue("base", null);
  }

  @Test
  public void testVisualiseValue() {
    model.loadImage(baseImg);
    model.visualiseValue("base", "base_value_visualise");
    CustomImage actualValueGreyScale = model.getImage("base_value_visualise");
    assertEquals(expectedValueGrayScale.getWidth(), actualValueGreyScale.getWidth());
    assertEquals(expectedValueGrayScale.getHeight(), actualValueGreyScale.getHeight());
    assertEquals(expectedValueGrayScale, actualValueGreyScale);
  }

  @Test
  public void testVisualiseIntensity() {
    model.loadImage(baseImg);
    model.visualiseIntensity("base", "base_intensity_visualise");
    CustomImage actualIntensityGreyScale = model.getImage("base_intensity_visualise");
    assertEquals(expectedIntensityGrayScale.getWidth(), actualIntensityGreyScale.getWidth());
    assertEquals(expectedIntensityGrayScale.getHeight(), actualIntensityGreyScale.getHeight());
    assertEquals(expectedIntensityGrayScale, actualIntensityGreyScale);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testVisualiseIntensityWhenOriginalImageNotFound() {
    model.loadImage(baseImg);
    model.visualiseIntensity("base2", "base_intensity_visualise");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testVisualiseIntensityWhenOriginalImageNameIsNull() {
    model.loadImage(baseImg);
    model.visualiseIntensity(null, "base_intensity_visualise");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testVisualiseIntensityWhenResultImageNameIsNull() {
    model.loadImage(baseImg);
    model.visualiseIntensity("base", null);
  }

  @Test
  public void testVisualiseLuma() {
    model.loadImage(baseImg);
    model.visualiseLuma("base", "base_luma_visualise");
    CustomImage actualLumaGreyScale = model.getImage("base_luma_visualise");
    assertEquals(expectedLumaGrayScale.getWidth(), actualLumaGreyScale.getWidth());
    assertEquals(expectedLumaGrayScale.getHeight(), actualLumaGreyScale.getHeight());
    assertEquals(expectedLumaGrayScale, actualLumaGreyScale);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testVisualiseLumaWhenOriginalImageNotFound() {
    model.loadImage(baseImg);
    model.visualiseValue("base2", "base_luma_visualise");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testVisualiseLumaWhenOriginalImageNameIsNull() {
    model.loadImage(baseImg);
    model.visualiseValue(null, "base_luma_visualise");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testVisualiseLumaWhenResultImageNameIsNull() {
    model.loadImage(baseImg);
    model.visualiseValue("base", null);
  }


  @Test(expected = IllegalArgumentException.class)
  public void testFlipWhenGivenAxisIsInvalid() {
    model.loadImage(baseImg);
    model.flip("base", "base_flip_H", 3);
  }


  @Test(expected = IllegalArgumentException.class)
  public void testFlipWhenOriginalImageIsNotFound() {
    model.loadImage(baseImg);
    model.flip("base2", "base_flip_H", 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFlipWhenOriginalImageNameIsNull() {
    model.loadImage(baseImg);
    model.flip(null, "base_flip_H", 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFlipWhenResultImageNameIsNull() {
    model.loadImage(baseImg);
    model.flip("base", null, 0);
  }

  @Test
  public void testFlipHorizontally() {
    model.loadImage(baseImg);
    model.flip("base", "base_flip_horizontal", 0);
    CustomImage actualHorizontalFlippedImg = model.getImage("base_flip_horizontal");
    assertEquals(expectedFlipHorizontal.getWidth(), actualHorizontalFlippedImg.getWidth());
    assertEquals(expectedFlipHorizontal.getHeight(), actualHorizontalFlippedImg.getHeight());
    assertEquals(expectedFlipHorizontal, actualHorizontalFlippedImg);
  }

  @Test
  public void testFlipVertically() {
    model.loadImage(baseImg);
    model.flip("base", "base_flip_vertical", 1);
    CustomImage actualVerticalFlippedImg = model.getImage("base_flip_vertical");
    assertEquals(expectedFlipVertical.getWidth(), actualVerticalFlippedImg.getWidth());
    assertEquals(expectedFlipVertical.getHeight(), actualVerticalFlippedImg.getHeight());
    assertEquals(expectedFlipVertical, actualVerticalFlippedImg);
  }

  @Test
  public void testFlipHorizontalAndVertically() {
    model.loadImage(baseImg);
    model.flip("base", "base_flip_horizontal", 0);
    model.flip("base_flip_horizontal",
            "base_flip_horizontal_vertical", 1);
    CustomImage actualHorizontalVerticalFlippedImg =
            model.getImage("base_flip_horizontal_vertical");
    assertEquals(expectedFlip_HV.getWidth(), actualHorizontalVerticalFlippedImg.getWidth());
    assertEquals(expectedFlip_HV.getHeight(), actualHorizontalVerticalFlippedImg.getHeight());
    assertEquals(expectedFlip_HV, actualHorizontalVerticalFlippedImg);
  }

  @Test
  public void testFlipMultipleTimes() {
    model.loadImage(baseImg);
    // flip H,V,V,H,V
    model.flip("base", "base_flip_horizontal", 0);
    model.flip("base_flip_horizontal", "base_flip_vertical", 1);
    model.flip("base_flip_vertical", "base_flip_vertical_2", 1);
    CustomImage actualFlipImgIntermediate = model.getImage("base_flip_vertical_2");
    assertEquals(expectedFlip_HVV.getWidth(), actualFlipImgIntermediate.getWidth());
    assertEquals(expectedFlip_HVV.getHeight(), actualFlipImgIntermediate.getHeight());
    assertEquals(expectedFlip_HVV, actualFlipImgIntermediate);
    model.flip("base_flip_vertical_2",
            "base_flip_horizontal_2", 0);
    model.flip("base_flip_horizontal_2", "base_flip_multiple",
            1);
    CustomImage actualFlipImgMultipleTime = model.getImage("base_flip_multiple");
    assertEquals(expectedFlipMultipleTime.getWidth(), actualFlipImgMultipleTime.getWidth());
    assertEquals(expectedFlipMultipleTime.getHeight(), actualFlipImgMultipleTime.getHeight());
    assertEquals(expectedFlipMultipleTime, actualFlipImgMultipleTime);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testBrightenWhenOriginalImageIsNotFound() {
    model.loadImage(baseImg);
    model.brighten("base132", "base_brighten", 10);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testBrightenWhenOriginalImageNameIsNull() {
    model.loadImage(baseImg);
    model.brighten(null, "base_brighten", 10);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testBrightenWhenResultImageNameIsNull() {
    model.loadImage(baseImg);
    model.brighten("base", null, 10);
  }

  @Test
  public void testBrightenByTenSomeValuesAreClamped() {
    model.loadImage(baseImg);
    model.brighten("base", "base_brighten", 10);
    CustomImage actualBrightenImg = model.getImage("base_brighten");
    assertEquals(expectedBrightenImg.getWidth(), actualBrightenImg.getWidth());
    assertEquals(expectedBrightenImg.getHeight(), actualBrightenImg.getHeight());
    assertEquals(expectedBrightenImg, actualBrightenImg);
  }


  @Test
  public void testBrightenByDecreasingByTenSomeValuesAreClamped() {
    model.loadImage(baseImg);
    model.brighten("base", "base_brighten_reduce", -10);
    CustomImage actualBrightenDecreasedImg = model.getImage("base_brighten_reduce");
    assertEquals(expectedBrightenReduceImg.getWidth(), actualBrightenDecreasedImg.getWidth());
    assertEquals(expectedBrightenReduceImg.getHeight(), actualBrightenDecreasedImg.getHeight());
    assertEquals(expectedBrightenReduceImg, actualBrightenDecreasedImg);
  }

  @Test
  public void testBrightenByZeroPixelValueRemainSame() {
    model.loadImage(baseImg);
    model.brighten("base", "base_brighten_same", 0);
    CustomImage actualBrightenByZeroImg = model.getImage("base_brighten_same");
    assertEquals(baseImg.getWidth(), actualBrightenByZeroImg.getWidth());
    assertEquals(baseImg.getHeight(), actualBrightenByZeroImg.getHeight());
    assertEquals(baseImg, actualBrightenByZeroImg);
  }

  @Test
  public void testSplit() {
    model.loadImage(baseImg);
    model.split("base", "base_red_split",
            "base_green_split", "base_blue_split");
    CustomImage actualRedSplitImg = model.getImage("base_red_split");
    CustomImage actualGreenSplitImg = model.getImage("base_green_split");
    CustomImage actualBlueSplitImg = model.getImage("base_blue_split");
    assertEquals(expectedRedGrayScale.getWidth(), actualRedSplitImg.getWidth());
    assertEquals(expectedRedGrayScale.getHeight(), actualRedSplitImg.getHeight());
    assertEquals(expectedRedGrayScale, actualRedSplitImg);

    assertEquals(expectedGreenGrayScale.getWidth(), actualGreenSplitImg.getWidth());
    assertEquals(expectedGreenGrayScale.getHeight(), actualGreenSplitImg.getHeight());
    assertEquals(expectedGreenGrayScale, actualGreenSplitImg);

    assertEquals(expectedBlueGrayScale.getWidth(), actualBlueSplitImg.getWidth());
    assertEquals(expectedBlueGrayScale.getHeight(), actualBlueSplitImg.getHeight());
    assertEquals(expectedBlueGrayScale, actualBlueSplitImg);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSplitWhenOriginalImageIsNotFound() {
    model.loadImage(baseImg);
    model.split("base123", "base_red_split",
            "base_green_split", "base_blue_split");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSplitWhenOriginalImageNameIsNull() {
    model.loadImage(baseImg);
    model.split(null, "base_red_split",
            "base_green_split", "base_blue_split");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSplitWhenRedImageNameIsNull() {
    model.loadImage(baseImg);
    model.split("base", null,
            "base_green_split", "base_blue_split");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSplitWhenGreenImageNameIsNull() {
    model.loadImage(baseImg);
    model.split("base", "base_red_split",
            null, "base_blue_split");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSplitWhenBlueImageNameIsNull() {
    model.loadImage(baseImg);
    model.split("base", "base_red_split",
            "base_green_split", null);
  }

  @Test
  public void testCombineRGBGreyScaleToFormOriginalImage() {
    model.loadImage(baseImg);
    model.split("base", "base_red_split",
            "base_green_split", "base_blue_split");
    model.combine("base_red_split", "base_green_split",
            "base_blue_split", "combine_rgb");
    CustomImage actualCombineImg = model.getImage("combine_rgb");
    assertEquals(baseImg.getWidth(), actualCombineImg.getWidth());
    assertEquals(baseImg.getHeight(), actualCombineImg.getHeight());
    assertEquals(baseImg, actualCombineImg);
  }

  @Test
  public void testCombineBrightRedBlueGreenToFormOriginalImageWithBrightRed() {
    model.loadImage(baseImg);
    model.split("base", "base_red_split",
            "base_green_split", "base_blue_split");
    model.brighten("base_red_split", "base_red_brighten",
            30);
    model.combine("base_red_brighten", "base_green_split",
            "base_blue_split", "combine_rgb_red_brighten");
    CustomImage actualCombineRedBrightImg = model.getImage("combine_rgb_red_brighten");
    assertEquals(expectedCombineBrightenRedImg.getWidth(), actualCombineRedBrightImg.getWidth());
    assertEquals(expectedCombineBrightenRedImg.getHeight(), actualCombineRedBrightImg.getHeight());
    assertEquals(expectedCombineBrightenRedImg, actualCombineRedBrightImg);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCombineWhenRedImageNotFound() {
    model.loadImage(baseImg);
    model.combine("base_red_brighten1", "base_green_split",
            "base_blue_split", "combine_rgb");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCombineWhenGreenImageNotFound() {
    model.loadImage(baseImg);
    model.combine("base_red_brighten", "base_green_split1",
            "base_blue_split", "combine_rgb");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCombineWhenBlueImageNotFound() {
    model.loadImage(baseImg);
    model.combine("base_red_brighten1",
            "base_green_split", "base_blue_split1",
            "combine_rgb");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCombineWhenRedImageSizeNotSameAsGreenImageNotFound() {
    model.loadImage(baseImg);
    model.split("base", "base_red_split",
            "base_green_split", "base_blue_split");
    model.loadImage(truncatedBaseImg);
    model.combine("base_truncated", "base_green_split",
            "base_blue_split", "combine_rgb");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCombineWhenRedImageSizeNotSameAsBlueImageNotFound() {
    model.loadImage(baseImg);
    model.split("base", "base_red_split",
            "base_green_split", "base_blue_split");
    model.loadImage(truncatedBaseImg);
    model.combine("base_truncated", "base_green_split",
            "base_blue_split1", "combine_rgb");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCombineWhenGreenImageSizeNotSameAsBlueImageNotFound() {
    model.loadImage(baseImg);
    model.split("base", "base_red_split",
            "base_green_split", "base_blue_split");
    model.loadImage(truncatedBaseImg);
    model.combine("base_red_split", "base_truncated",
            "base_blue_split", "combine_rgb");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCombineWhenRedImageNameIsNull() {
    model.loadImage(baseImg);
    model.split("base", "base_red_split",
            "base_green_split", "base_blue_split");
    model.combine(null, "base_green_split",
            "base_blue_split", "combine_rgb");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCombineWhenGreenImageNameIsNull() {
    model.loadImage(baseImg);
    model.split("base", "base_red_split",
            "base_green_split", "base_blue_split");
    model.combine("base_red_split", null,
            "base_blue_split", "combine_rgb");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCombineWhenBlueImageNameIsNull() {
    model.loadImage(baseImg);
    model.split("base", "base_red_split",
            "base_green_split", "base_blue_split");
    model.combine("base_red_split", "base_green_split",
            null, "combine_rgb");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCombineWhenResultImageNameIsNull() {
    model.loadImage(baseImg);
    model.split("base", "base_red_split",
            "base_green_split", "base_blue_split");
    model.combine("base_red_split", "base_green_split",
            "base_blue_split", null);
  }

  @Test
  public void testBlurImageOnce() {
    model.loadImage(baseImage);
    model.blur("base", "baseBlurOne");
    CustomImage actualBlurOne = model.getImage("baseBlurOne");
    assertEquals(expectedBaseImgBlurOne.getWidth(), actualBlurOne.getWidth());
    assertEquals(expectedBaseImgBlurOne.getHeight(), actualBlurOne.getHeight());
    assertEquals(expectedBaseImgBlurOne, actualBlurOne);
  }

  @Test
  public void testBlurImageTwice() {
    model.loadImage(baseImage);
    model.blur("base", "baseBlurOne");
    model.blur("baseBlurOne", "baseBlurTwo");
    CustomImage actualBlurTwo = model.getImage("baseBlurTwo");
    assertEquals(expectedBaseImgBlurTwo.getWidth(), actualBlurTwo.getWidth());
    assertEquals(expectedBaseImgBlurTwo.getHeight(), actualBlurTwo.getHeight());
    assertEquals(expectedBaseImgBlurTwo, actualBlurTwo);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testBlurWhenOriginalImageNotFound() {
    model.loadImage(baseImage);
    model.blur("base1", "baseBlurOne");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testBlurWhenOriginalImageNameIsNull() {
    model.loadImage(baseImage);
    model.blur(null, "baseBlurOne");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testBlurWhenResultImageNameIsNull() {
    model.loadImage(baseImage);
    model.blur("base", null);
  }

  @Test
  public void testSharpImageOnce() {
    model.loadImage(baseSharpImg);
    model.sharpen("baseSharp", "baseSharpOne");
    CustomImage actualSharpOne = model.getImage("baseSharpOne");
    assertEquals(expectedBaseSharpImgOne.getWidth(), actualSharpOne.getWidth());
    assertEquals(expectedBaseSharpImgOne.getHeight(), actualSharpOne.getHeight());
    assertEquals(expectedBaseSharpImgOne, actualSharpOne);
  }

  @Test
  public void testSharpImageTwice() {
    model.loadImage(baseSharpImg);
    model.sharpen("baseSharp", "baseSharpOne");
    model.sharpen("baseSharpOne", "baseSharpTwo");
    CustomImage actualSharpTwo = model.getImage("baseSharpTwo");
    assertEquals(expectedBaseSharpImgTwo.getWidth(), actualSharpTwo.getWidth());
    assertEquals(expectedBaseSharpImgTwo.getHeight(), actualSharpTwo.getHeight());
    assertEquals(expectedBaseSharpImgTwo, actualSharpTwo);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSharpWhenOriginalImageNotFound() {
    model.loadImage(baseSharpImg);
    model.sharpen("base1", "baseBlurOne");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSharpWhenOriginalImageNameIsNull() {
    model.loadImage(baseSharpImg);
    model.sharpen(null, "baseSharpOne");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSharpWhenResultImageNameIsNull() {
    model.loadImage(baseSharpImg);
    model.sharpen("baseSharp", null);
  }

  @Test
  public void testSepia() {
    model.loadImage(baseImage);
    String resultImageName = "base_sepia_toned";
    Pixel[][] expectedPixels = {{new Pixel(100, 89, 69, MAX_PIXEL_VALUE),
        new Pixel(196, 175, 136, MAX_PIXEL_VALUE),
        new Pixel(48, 43, 33, MAX_PIXEL_VALUE)}, {
        new Pixel(0, 0, 0, MAX_PIXEL_VALUE),
        new Pixel(228, 203, 158, MAX_PIXEL_VALUE),
        new Pixel(255, 255, 239, MAX_PIXEL_VALUE)}, {
        new Pixel(232, 206, 161, MAX_PIXEL_VALUE),
        new Pixel(165, 146, 114, MAX_PIXEL_VALUE),
        new Pixel(255, 244, 190, MAX_PIXEL_VALUE)
      }};
    CustomImage expected = new CustomImage(resultImageName, expectedPixels, MAX_PIXEL_VALUE);
    model.sepia("base", resultImageName);
    CustomImage actual = model.getImage(resultImageName);
    assertEquals(expected, actual);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSepiaOriginalImageNotFound() {
    model.loadImage(baseImage);
    model.sepia("image", "base_sepia_toned");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSepiaOriginalImageNameIsNull() {
    model.loadImage(baseImage);
    model.sepia(null, "base_sepia_toned");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSepiaResultImageNameIsNull() {
    model.loadImage(baseImage);
    model.sepia("image", null);
  }

  @Test
  public void testDither() {
    model.loadImage(baseImage);
    String resultImageName = "base_dither";
    Pixel[][] expectedPixels = {{new Pixel(0, 0, 0, MAX_PIXEL_VALUE),
        new Pixel(255, 255, 255, MAX_PIXEL_VALUE),
        new Pixel(0, 0, 0, MAX_PIXEL_VALUE)}, {
        new Pixel(0, 0, 0, MAX_PIXEL_VALUE),
        new Pixel(255, 255, 255, MAX_PIXEL_VALUE),
        new Pixel(255, 255, 255, MAX_PIXEL_VALUE)}, {
        new Pixel(255, 255, 255, MAX_PIXEL_VALUE),
        new Pixel(0, 0, 0, MAX_PIXEL_VALUE),
        new Pixel(255, 255, 255, MAX_PIXEL_VALUE)
      }};
    CustomImage expected = new CustomImage(resultImageName, expectedPixels, MAX_PIXEL_VALUE);
    model.dither("base", resultImageName);
    CustomImage actual = model.getImage(resultImageName);
    assertEquals(expected, actual);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testDitherOriginalImageNotFound() {
    model.loadImage(baseImage);
    model.sepia("image", "base_sepia_toned");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testDitherOriginalImageNameIsNull() {
    model.loadImage(baseImage);
    model.sepia(null, "base_sepia_toned");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testDitherResultImageNameIsNull() {
    model.loadImage(baseImage);
    model.sepia("image", null);
  }


  @Test
  public void testMultipleOperations() {
    model.loadImage(baseImg);
    model.split("base", "red_baseImg",
            "green_baseImg", "blue_baseImg");
    model.visualiseIntensity("base", "intensity_baseImg");
    model.brighten("red_baseImg", "bright_red_baseImg", 50);
    model.brighten("blue_baseImg", "reduce_blue_baseImg",
            -20);
    model.combine("bright_red_baseImg", "intensity_baseImg",
            "reduce_blue_baseImg", "combine_img");
    CustomImage actualImage = model.getImage("combine_img");
    assertEquals(expectedImgOnMultipleOperations.getWidth(), actualImage.getWidth());
    assertEquals(expectedImgOnMultipleOperations.getHeight(), actualImage.getHeight());
    assertEquals(expectedImgOnMultipleOperations, actualImage);

  }

  @Test
  public void testMultipleOperationsWithNewFeatures() {
    model.loadImage(baseImage);
    model.split("base", "red_baseImg",
            "green_baseImg", "blue_baseImg");
    model.visualiseIntensity("base", "intensity_baseImg");
    model.brighten("red_baseImg", "bright_red_baseImg", 50);
    model.brighten("blue_baseImg", "reduce_blue_baseImg",
            -20);
    model.combine("bright_red_baseImg", "intensity_baseImg",
            "reduce_blue_baseImg", "combine_img");
    model.blur("base", "blurBase");
    model.blur("blurBase", "blurBase2");
    model.sharpen("blurBase", "sharpBlurredBase");
    model.sharpen("sharpBlurredBase", "sharpTwice");
    CustomImage actualIntermediateImage = model.getImage("sharpTwice");
    assertEquals(expectedImgOnMultipleOperationsWithNewFeature.getWidth(),
            actualIntermediateImage.getWidth());
    assertEquals(expectedImgOnMultipleOperationsWithNewFeature.getHeight(),
            actualIntermediateImage.getHeight());
    assertEquals(expectedImgOnMultipleOperationsWithNewFeature, actualIntermediateImage);

    model.sepia("blurBase", "sepiaBlurred");
    CustomImage actualSepiaImage = model.getImage("sepiaBlurred");
    assertEquals(expectedSepiaImage.getWidth(), actualSepiaImage.getWidth());
    assertEquals(expectedSepiaImage.getHeight(), actualSepiaImage.getHeight());
    assertEquals(expectedSepiaImage, actualSepiaImage);

    model.sharpen("blurBase", "sharpBlur");
    model.dither("sharpBlur", "ditherBase");

    CustomImage actualDitherImage = model.getImage("ditherBase");
    assertEquals(expectedDitherImage.getWidth(), actualDitherImage.getWidth());
    assertEquals(expectedDitherImage.getHeight(), actualDitherImage.getHeight());
    assertEquals(expectedDitherImage, actualDitherImage);

  }


}
