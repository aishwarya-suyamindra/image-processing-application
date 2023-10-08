package controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.NoSuchElementException;
import java.util.Scanner;

import model.ImageProcessor;
import utility.IImageHelperFactory;
import view.IView;

/**
 * This class represents a controller that interprets a set of commands to manipulate images and
 * take the appropriate action.
 */
public class ImageCommandController extends AbstractCommandController {
  private InputStream inputStream;
  private final IView view;

  /**
   * Initialises a controller object with the given model, view, input stream and helper factory.
   *
   * @param model              the model that performs the various image manipulations
   * @param in                 the input stream
   * @param view               the view
   * @param imageHelperFactory the factory class that provides the utility class based on the
   *                           image type that is to be modified
   */
  public ImageCommandController(ImageProcessor model, InputStream in, IView view,
                                IImageHelperFactory imageHelperFactory) {
    super(model, imageHelperFactory);
    this.inputStream = in;
    this.view = view;
  }

  @Override
  public void process() {
    Scanner scanner = new Scanner(this.inputStream);
    StringBuilder builder = new StringBuilder();
    String commandStatus;
    // Read from the input stream
    while (scanner.hasNextLine()) {
      String line = scanner.nextLine();
      if (!line.isEmpty() && line.charAt(0) != '#') {
        // tokenize the line and check for the command, throw an error if it's an invalid command
        String[] tokens = line.trim().split("\\s+");
        String command = tokens[0];
        if (command.equals("quit")) {
          System.exit(0);
        }
        // empty the buffer and append the rest of the current user input to the buffer
        builder.setLength(0);
        for (int i = 1; i < tokens.length; i++) {
          builder.append(tokens[i]).append("\n");
        }
        try {
          // Execute the command
          commandStatus = processCommand(command, new Scanner(builder.toString()));
          view.print(commandStatus);
        } catch (NoSuchElementException ex) {
          view.print("Invalid command format");
        } catch (UnsupportedOperationException | IllegalArgumentException ex) {
          if (ex.getMessage() == null) {
            view.print("Invalid command format");
          } else {
            view.print(ex.getMessage());
          }
        } catch (RuntimeException ex) {
          view.print(ex.getMessage());
        }
      }
    }
  }

  /**
   * Executes the given command with the given input via the scanner.
   *
   * @param command the command to execute
   * @param sc      the scanner object with the input to process
   * @return the output on executing the command
   * @throws UnsupportedOperationException if the given command is not supported
   * @throws NoSuchElementException        if the given command does not contain all the required
   *                                       input to execute the command
   * @throws IllegalArgumentException      if the given command contains invalid inputs
   */
  private String processCommand(String command, Scanner sc) throws UnsupportedOperationException,
          NoSuchElementException, IllegalArgumentException {
    StringBuilder output = new StringBuilder();
    if (command.equals("run")) {
      try {
        String fileName = sc.next();
        if (sc.hasNext()) {
          throw new IllegalArgumentException("Invalid command format");
        }
        // If the command is to run a script file, call the go method of the controller with a
        // different input stream
        this.inputStream = new FileInputStream(fileName);
        this.process();
      } catch (FileNotFoundException ex) {
        output.append("Script file not found");
      }
    } else {
      output.append(executeCommand(command, sc));
    }
    return output.toString();
  }
}
