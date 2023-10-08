package utility;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;

/**
 * This class contains helper methods to read and save an ASCII PPM image file.
 */
public class PPMImageHelper implements IImageHelper {
  @Override
  public CustomImage load(String filePath, String imageName) throws IOException {
    String fileContents = readPPMFile(filePath);
    Scanner sc = new Scanner(fileContents);
    String token = sc.next();
    if (!token.equals("P3")) {
      throw new IllegalArgumentException("Invalid PPM file: plain RAW file should begin with P3");
    }
    int width = sc.nextInt();
    int height = sc.nextInt();
    int maxValue = sc.nextInt();
    Pixel[][] pixels = new Pixel[height][width];
    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        pixels[i][j] = new Pixel(sc.nextInt(), sc.nextInt(), sc.nextInt(), 255);
      }
    }
    return new CustomImage(imageName, pixels, maxValue);
  }

  @Override
  public void save(CustomImage image, String filePath) throws IOException {
    StringBuilder output = new StringBuilder();
    int width = image.getWidth();
    int height = image.getHeight();
    output.append("P3\n").append(width).append(" ").append(height).append("\n")
            .append(image.getMaxPixelValue()).append("\n");
    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        Pixel originalPixel = image.getPixel(i, j);
        int red = originalPixel.getColor(2);
        int green = originalPixel.getColor(1);
        int blue = originalPixel.getColor(0);
        output.append(red).append("\n").append(green).append("\n").append(blue).append("\n");
      }
    }
    // write to the output stream
    FileOutputStream outputStream = new FileOutputStream(filePath);
    outputStream.write(output.toString().getBytes());
    outputStream.close();
  }

  // Helper method to read the contents from the input stream
  private String readPPMFile(String file) throws IOException {
    FileInputStream inputStream = new FileInputStream(file);
    Scanner sc = new Scanner(inputStream);
    StringBuilder builder = new StringBuilder();
    // read from the input stream, line by line
    while (sc.hasNextLine()) {
      String s = sc.nextLine();
      if (s.charAt(0) != '#') {
        builder.append(s).append(System.lineSeparator());
      }
    }
    return builder.toString();
  }
}