package controller.commands;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import controller.ImageOperationCommand;
import model.ImageProcessor;

/**
 * This class stores the information required to convert a given image to a greyscale image.
 */
public class Greyscale implements ImageOperationCommand {
  private final String imageName;
  private final String resultingImageName;
  private final String greyscaleConversionMethod;
  private final Map<String, Consumer<ImageProcessor>> supportedGreyscaleConversionMethods;

  /**
   * Constructs a greyscale command object with the given image name, resulting image and the
   * method to convert the given color image to a greyscale image.
   *
   * @param method             the method to convert the given image to a greyscale image
   * @param imageName          the name of the image to get the grayscale image of
   * @param resultingImageName the name of the resulting greyscale image
   */
  public Greyscale(String method, String imageName, String resultingImageName) {
    this.greyscaleConversionMethod = method;
    this.imageName = imageName;
    this.resultingImageName = resultingImageName;
    this.supportedGreyscaleConversionMethods = new HashMap<>();
    setGreyscaleConversionMethods();
  }

  /**
   * Constructs a greyscale command object with the given image name, resulting image. The
   * greyscale conversion method used is luma component by default.
   *
   * @param imageName          the name of the image to get the grayscale image of
   * @param resultingImageName the name of the resulting greyscale image
   */
  public Greyscale(String imageName, String resultingImageName) {
    this.imageName = imageName;
    this.resultingImageName = resultingImageName;
    this.supportedGreyscaleConversionMethods = new HashMap<>();
    this.greyscaleConversionMethod = "luma-component";
    setGreyscaleConversionMethods();
  }

  private void setGreyscaleConversionMethods() {
    this.supportedGreyscaleConversionMethods.put("red-component",
        model -> model.visualiseChannel(this.imageName, this.resultingImageName, 2));
    this.supportedGreyscaleConversionMethods.put("green-component",
        model -> model.visualiseChannel(this.imageName, this.resultingImageName, 1));
    this.supportedGreyscaleConversionMethods.put("blue-component",
        model -> model.visualiseChannel(this.imageName, this.resultingImageName, 0));
    this.supportedGreyscaleConversionMethods.put("intensity-component",
        model -> model.visualiseIntensity(this.imageName, this.resultingImageName));
    this.supportedGreyscaleConversionMethods.put("luma-component",
        model -> model.visualiseLuma(this.imageName, this.resultingImageName));
    this.supportedGreyscaleConversionMethods.put("value-component",
        model -> model.visualiseValue(this.imageName, this.resultingImageName));
  }

  /**
   * Executes the greyscale command depending on the type of greyscale conversion method.
   *
   * @param model the model object
   */
  @Override
  public void execute(ImageProcessor model) {
    Consumer<ImageProcessor> commandToExecute = this.supportedGreyscaleConversionMethods
            .getOrDefault(this.greyscaleConversionMethod, null);
    if (commandToExecute == null) {
      throw new IllegalArgumentException("Invalid command");
    }
    commandToExecute.accept(model);
  }
}
