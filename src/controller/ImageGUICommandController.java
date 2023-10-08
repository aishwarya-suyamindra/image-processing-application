package controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Function;

import model.ImageProcessor;
import utility.IImageHelperFactory;
import utility.ViewMessageType;
import view.IGraphicalView;

/**
 * This class represents a controller that interprets a set of commands from a graphical user
 * interface that works with images.
 */
public class ImageGUICommandController extends AbstractCommandController {
  private final IGraphicalView view;

  /**
   * Initialises the controller object with the given model, view and helper factory.
   *
   * @param model              the model that performs the various image manipulations
   * @param view               the view to work with
   * @param imageHelperFactory the factory class that provides the utility class based on the
   *                           image type that is to be modified
   */

  public ImageGUICommandController(ImageProcessor model, IGraphicalView view,
                                   IImageHelperFactory imageHelperFactory) {
    super(model, imageHelperFactory);
    this.view = view;
  }

  @Override
  public void process() {
    // render the view
    Features featuresObj = new FeaturesControllerImpl("base");
    view.addFeatures(featuresObj);
    view.setVisible();
  }

  /**
   * This class contains the functionality to act on events generated from a graphical view that
   * works with images.
   */
  private class FeaturesControllerImpl implements Features {
    private final String imageName;
    private final Map<String, Function<String[], String[]>> commands;

    private FeaturesControllerImpl(String imageName) {
      this.imageName = imageName;
      this.commands = new HashMap<>();
      addCommands();
    }

    // Helper method to add the commands that transform the commands coming in from the view
    private void addCommands() {
      this.commands.put("load", s ->
              new String[]{"load", String.join("\n", s) + "\n" + this.imageName});

      this.commands.put("brighten", s -> {
        if (s.length != 1) {
          throw new IllegalArgumentException("Invalid operation.");
        }
        try {
          Integer.parseInt(s[0]);
          return new String[]{"brighten",
                  this.format(s[0], this.imageName, this.imageName)};
        } catch (NullPointerException | NumberFormatException ex) {
          throw new IllegalArgumentException("Invalid value to brighten by. Please enter an "
                  + "integer value.");
        }
      });

      this.commands.put("flip", s -> {
        if (s.length != 1) {
          throw new IllegalArgumentException("Invalid operation.");
        }
        return new String[]{s[0].toLowerCase(),
                this.format(this.imageName, this.imageName)};
      });

      this.commands.put("save", s -> new String[]{"save",
              String.join("\n", s) + "\n" + this.imageName});

      this.commands.put("combine", s -> new String[]{"rgb-combine", processCombine(s)});

      this.commands.put("greyscale", s -> {
        if (s.length != 1) {
          throw new IllegalArgumentException("Invalid operation.");
        }
        return new String[]{"greyscale",
                this.format(s[0].toLowerCase(), this.imageName, this.imageName)};
      });

      this.commands.put("blur", s -> new String[]{"blur",
              this.format(this.imageName, this.imageName)});

      this.commands.put("sharpen", s -> new String[]{"sharpen",
              this.format(this.imageName, this.imageName)});

      this.commands.put("dither", s -> new String[]{"dither",
              this.format(this.imageName, this.imageName)});

      this.commands.put("sepia", s -> new String[]{"sepia",
              this.format(this.imageName, this.imageName)});
    }

    @Override
    public void execute(String command, String[] additionalParameters) {
      try {
        if (command.equalsIgnoreCase("split")) {
          processSplit(additionalParameters);
          view.echo("Split greyscale images saved successfully.", ViewMessageType.SUCCESS);
          return;
        } else {
          String[] commandToExecute =
                  this.commands.getOrDefault(command.toLowerCase(), null)
                          .apply(additionalParameters);
          Scanner sc = new Scanner(commandToExecute[1]);
          executeCommand(commandToExecute[0], sc);
        }
      } catch (RuntimeException ex) {
        String message = ex.getMessage();
        if (message != null) {
          view.echo(message, ViewMessageType.ERROR);
          return;
        }
        view.echo("Invalid operation.", ViewMessageType.ERROR);
        return;
      }
      if (command.equalsIgnoreCase("save")) {
        view.echo("Image saved successfully.", ViewMessageType.SUCCESS);
      } else {
        view.updateUI(this.imageName);
      }
    }

    // helper method to process combine command
    private String processCombine(String[] additionalParameters) {
      if (additionalParameters.length != 3) {
        throw new IllegalArgumentException("Invalid number of files provided for the combine "
                + "operation. Please select three files.");
      }

      // load the given images
      String parameters = this.format(additionalParameters[0], this.imageName
              + "-red");
      Scanner sc = new Scanner(parameters);
      executeCommand("load", sc);
      parameters = this.format(additionalParameters[1], this.imageName + "-green");
      sc = new Scanner(parameters);
      executeCommand("load", sc);
      parameters = this.format(additionalParameters[2], this.imageName + "-blue");
      sc = new Scanner(parameters);
      executeCommand("load", sc);

      return this.format(this.imageName, this.imageName + "-red", this.imageName
              + "-green", this.imageName + "-blue");
    }

    private void processSplit(String[] additionalParameters) {
      if (additionalParameters.length != 3) {
        throw new IllegalArgumentException("Invalid number of files provided to save the result"
                + " of the split operation. Please provide three filenames.");
      }
      // split the image and save the resulting images in files.
      String redImage = getFileName(additionalParameters[0]);
      String greenImage = getFileName(additionalParameters[1]);
      String blueImage = getFileName(additionalParameters[2]);

      String parameters = this.format(this.imageName, redImage, greenImage, blueImage);
      Scanner sc = new Scanner(parameters);
      executeCommand("rgb-split", sc);

      // save the split image files
      parameters = this.format(additionalParameters[0], redImage);
      sc = new Scanner(parameters);
      executeCommand("save", sc);
      parameters = this.format(additionalParameters[1], greenImage);
      sc = new Scanner(parameters);
      executeCommand("save", sc);
      parameters = this.format(additionalParameters[2], blueImage);
      sc = new Scanner(parameters);
      executeCommand("save", sc);
    }

    // helper method to get the filename when the extension is part of the file path
    private String getFileName(String filePath) {
      int index = filePath.indexOf(".");
      if (index == -1) {
        throw new IllegalArgumentException("File extension is necessary.");
      }
      return filePath.substring(0, index);
    }

    // helper method to get a joined string with newline as delimiter, from the given string values
    private String format(String... values) {
      return String.join("\n", values);
    }
  }
}
