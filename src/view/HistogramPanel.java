package view;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Font;
import java.awt.Color;
import javax.swing.JPanel;

import utility.CustomImage;

/**
 * Creates a custom JPanel to display Histogram.
 */
public class HistogramPanel extends JPanel {
  private CustomImage image;
  private int[] red;
  private int[] green;
  private int[] blue;
  private int[] intensity;
  private int maxRed;
  private final int adjustX = 20;
  private final int adjustY = 10;
  private int maxGreen;
  private int maxBlue;
  private int maxIntensity;
  private int padding;
  private int chartWidth;
  private int chartHeight;
  private int maxPixelValue;

  private int maxFrequency;

  /**
   * Create a JPanel.
   */
  public HistogramPanel() {
    super();
    image = null;
  }


  /**
   * The method will draw the histogram for the given image.
   *
   * @param image the CustomImage
   */
  public void drawHistogram(CustomImage image) {
    this.image = image;
    maxPixelValue = image.getMaxPixelValue() + 1;
    this.red = new int[maxPixelValue];
    this.green = new int[maxPixelValue];
    this.blue = new int[maxPixelValue];
    this.intensity = new int[maxPixelValue];
    padding = 40;
    chartWidth = getWidth() - padding * 2;
    chartHeight = getHeight() - padding * 2;
    computeHistogramData();
    repaint();
  }

  // helper method to get the max frequency value of frequency array.
  private int getMaxValue(int[] array) {
    int max = 0;
    for (int i = 0; i < array.length; i++) {
      if (array[i] > max) {
        max = array[i];
      }
    }
    return max;
  }

  // helper method to fetch the histogram data for the given image
  private void computeHistogramData() {

    this.red = image.getPixelFrequency(2);
    this.green = image.getPixelFrequency(1);
    this.blue = image.getPixelFrequency(0);
    this.intensity = image.getPixelFrequencyByIntensity();

    maxRed = getMaxValue(red);
    maxGreen = getMaxValue(green);
    maxBlue = getMaxValue(blue);
    maxIntensity = getMaxValue(intensity);
  }

  /**
   * Draw the histogram for the given image.
   *
   * @param g the <code>Graphics</code> object to protect
   */
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    if (image != null) {
      Graphics2D g2 = (Graphics2D) g;
      g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

      // find the max frequency across all the components
      maxFrequency = Math.max(Math.max(Math.max(maxRed, maxGreen), maxBlue), maxIntensity);

      int len = 10;

      // change font
      Font defaultFont = g2.getFont();
      Font font = new Font("Helvetica", Font.ITALIC | Font.BOLD, 12);
      g2.setFont(font);

      // write the X and Y labels
      g2.drawString("Values", padding + 10 + chartWidth / 2, padding + chartHeight + 33);
      g2.rotate(-Math.PI / 2); // Rotate 90 degrees counter-clockwise
      g2.drawString("Frequency", -padding - chartHeight / 2 - 10, padding - 20);
      g2.rotate(Math.PI / 2);

      // set default font
      g2.setFont(defaultFont);
      // increase padding to be able to write the Y - values
      padding = padding + len;
      // Draw X and Y axis lines
      g2.drawLine(padding + adjustX, padding + chartHeight - adjustY, padding
                      + chartWidth + adjustX, padding + chartHeight - adjustY);
      g2.drawLine(padding + adjustX, padding - adjustY, padding
              + adjustX, padding + chartHeight - adjustY);

      // set the size for legend
      int squareSize = 10;


      font = new Font("Helvetica", Font.ITALIC, 12);
      g2.setFont(font);

      // add red legend
      g2.setColor(Color.RED);
      g2.fillRect(padding + adjustX, padding + chartHeight - adjustY - 240,
              squareSize, squareSize);
      g2.setColor(Color.BLACK);
      g2.drawString("Red", padding + adjustX + squareSize + 5,
              padding + chartHeight - adjustY - 240 + squareSize);

      // add green legend
      g2.setColor(Color.GREEN);
      g2.fillRect(padding + adjustX + 60, padding + chartHeight - adjustY - 240,
              squareSize, squareSize);
      g2.setColor(Color.BLACK);
      g2.drawString("Green", padding + adjustX + 60 + squareSize + 5,
              padding + chartHeight - adjustY - 240 + squareSize);

      // add blue legend
      g2.setColor(Color.BLUE);
      g2.fillRect(padding + adjustX + 120, padding + chartHeight - adjustY - 240,
              squareSize, squareSize);
      g2.setColor(Color.BLACK);
      g2.drawString("Blue", padding + adjustX + 120 + squareSize + 5,
              padding + chartHeight - adjustY - 240 + squareSize);

      // add intensity legend
      g2.setColor(Color.MAGENTA);
      g2.fillRect(padding + adjustX + 180, padding + chartHeight - adjustY - 240,
              squareSize, squareSize);
      g2.setColor(Color.BLACK);
      g2.drawString("Intensity", padding + adjustX + 180 + squareSize + 5,
              padding + chartHeight - adjustY - 240 + squareSize);

      g2.setColor(Color.BLACK);
      g2.setFont(defaultFont);

      // Draw X axis values and markings
      for (int i = 0; i <= 256; i += 32) {
        int x = padding + adjustX + i * chartWidth / 256;
        g2.drawLine(x, padding + chartHeight - adjustY, x,
                padding + chartHeight + 5 - adjustY);
        String label = Integer.toString(i);
        int labelWidth = g2.getFontMetrics().stringWidth(label);
        g2.drawString(label, x - 5 - labelWidth / 2, padding + chartHeight + 20 - adjustY);
      }


      // Draw Y axis labels and markings
      for (int i = 10; i >= 1; i -= 2) {
        int y = padding - adjustY + (10 - i) * chartHeight / 10;
        g2.drawLine(padding + adjustX - 3, y, padding + adjustX, y);
        int yVal = i * maxFrequency / 10;
        /*if value is greater than or equal to 1000, then set y value to a decimal with one
        decimal place by dividing value by 1000*/
        if (yVal >= 1000) {
          double result = (double) yVal / 1000.0;
          double newYVal = Math.round(result * 10.0) / 10.0;
          g2.drawString((newYVal) + "K", padding + adjustX - 40, y + 5);
        } else {
          g2.drawString(Integer.toString(yVal), padding + adjustX - 40, y + 5);
        }
      }

      // draw line chart for each component
      drawLineChart(g2, Color.RED, red);
      drawLineChart(g2, Color.GREEN, green);
      drawLineChart(g2, Color.BLUE, blue);
      drawLineChart(g2, Color.MAGENTA, intensity);
    }

  }


  // helper method that draws the line chart for the given component with specified color
  private void drawLineChart(Graphics2D g2, Color c, int[] freq) {
    g2.setColor(c);
    for (int i = 0; i < maxPixelValue - 1; i++) {
      int x1 = padding + i * chartWidth / maxPixelValue;
      int y1 = padding + chartHeight - freq[i] * chartHeight / maxFrequency;
      int x2 = padding + (i + 1) * chartWidth / maxPixelValue;
      int y2 = padding + chartHeight - freq[i + 1] * chartHeight / maxFrequency;
      g2.drawLine(x1 + adjustX, y1 - adjustY, x2 + adjustX, y2 - adjustY);
    }
  }
}
