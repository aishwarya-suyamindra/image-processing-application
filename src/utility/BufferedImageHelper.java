package utility;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * This class contains helper methods to read and save image of formats supported by
 * BufferedImage class(currently supports - png, jpg, jpeg, bmp).
 */
public class BufferedImageHelper implements IImageHelper {

  @Override
  public CustomImage load(String filePath, String imageName) throws IOException {
    FileInputStream inputStream = new FileInputStream(filePath);
    BufferedImage loadedImage = ImageIO.read(inputStream);
    int width = loadedImage.getWidth();
    int height = loadedImage.getHeight();
    int bits = loadedImage.getColorModel().getComponentSize(0);
    int maxValue = (int) Math.pow(2, bits) - 1;
    Pixel[][] pixels = new Pixel[height][width];
    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        int color = loadedImage.getRGB(j, i);
        Color c = new Color(color);
        pixels[i][j] = new Pixel(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha());
      }
    }
    return new CustomImage(imageName, pixels, maxValue);
  }

  @Override
  public void save(CustomImage image, String filePath) throws IOException {
    int width = image.getWidth();
    int height = image.getHeight();
    BufferedImage outputImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        Pixel pixel = image.getPixel(i, j);
        int color = new Color(pixel.getColor(2), pixel.getColor(1), pixel.getColor(0)).getRGB();
        outputImage.setRGB(j, i, color);
      }
    }
    FileOutputStream outputStream = new FileOutputStream(filePath);
    ImageIO.write(outputImage, "png", outputStream);
    outputStream.close();
  }

}
