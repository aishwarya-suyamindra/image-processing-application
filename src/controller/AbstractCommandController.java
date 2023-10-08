package controller;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.function.Function;

import controller.commands.Blur;
import controller.commands.Brighten;
import controller.commands.Combine;
import controller.commands.Dither;
import controller.commands.Flip;
import controller.commands.Greyscale;
import controller.commands.Load;
import controller.commands.Save;
import controller.commands.Sepia;
import controller.commands.Sharpen;
import controller.commands.Split;
import model.ImageProcessor;
import utility.IImageHelperFactory;

/**
 * This class represents the abstract base class for concrete implementations of the command
 * controller interface. It contains the common fields and method implementations.
 */
public abstract class AbstractCommandController implements CommandController {
  protected final ImageProcessor model;
  protected final Map<String, Function<Scanner, ImageOperationCommand>> supportedOperations;
  protected final IImageHelperFactory imageHelperFactory;

  /**
   * Initialises the fields that are common to the concrete implementations of the command
   * controller interface.
   *
   * @param model              the model that performs the various image manipulations
   * @param imageHelperFactory the factory class that provides the utility class based on the
   *                           image type that is to be modified
   */
  protected AbstractCommandController(ImageProcessor model,
                                      IImageHelperFactory imageHelperFactory) {
    this.model = model;
    this.imageHelperFactory = imageHelperFactory;
    this.supportedOperations = new HashMap<>();
    addSupportedOperations();
  }

  // helper method to populate the supported operations map
  private void addSupportedOperations() {
    this.supportedOperations.put("load", (Scanner sc) -> {
      ImageOperationCommand command = new Load(sc.nextLine(), sc.nextLine(),
              this.imageHelperFactory);
      if (sc.hasNextLine()) {
        throw new IllegalArgumentException("Invalid command format");
      }
      return command;
    });

    this.supportedOperations.put("brighten", (Scanner sc) -> {
      int increment = sc.nextInt();
      sc.nextLine();
      ImageOperationCommand command = new Brighten(increment, sc.nextLine(), sc.nextLine());
      if (sc.hasNextLine()) {
        throw new IllegalArgumentException("Invalid command format");
      }
      return command;
    });

    this.supportedOperations.put("rgb-combine", (Scanner sc) -> {
      ImageOperationCommand command = new Combine(sc.nextLine(), sc.nextLine(), sc.nextLine(),
              sc.nextLine());
      if (sc.hasNextLine()) {
        throw new IllegalArgumentException("Invalid command format");
      }
      return command;
    });

    this.supportedOperations.put("vertical-flip", (Scanner sc) -> {
      ImageOperationCommand command = new Flip(sc.nextLine(), sc.nextLine(), 1);
      if (sc.hasNextLine()) {
        throw new IllegalArgumentException("Invalid command format");
      }
      return command;
    });

    this.supportedOperations.put("horizontal-flip", (Scanner sc) -> {
      ImageOperationCommand command = new Flip(sc.nextLine(), sc.nextLine(), 0);
      if (sc.hasNextLine()) {
        throw new IllegalArgumentException("Invalid command format");
      }
      return command;
    });

    this.supportedOperations.put("greyscale", (Scanner sc) -> {
      String parameter1 = sc.nextLine();
      String parameter2 = sc.nextLine();
      ImageOperationCommand command;
      if (sc.hasNextLine()) {
        command = new Greyscale(parameter1, parameter2, sc.nextLine());
      } else {
        command = new Greyscale(parameter1, parameter2);
      }
      if (sc.hasNextLine()) {
        throw new IllegalArgumentException("Invalid command format");
      }
      return command;
    });

    this.supportedOperations.put("rgb-split", (Scanner sc) -> {
      ImageOperationCommand command = new Split(sc.nextLine(), sc.nextLine(), sc.nextLine(),
              sc.nextLine());
      if (sc.hasNextLine()) {
        throw new IllegalArgumentException("Invalid command format");
      }
      return command;
    });

    this.supportedOperations.put("save", (Scanner sc) -> {
      ImageOperationCommand command = new Save(sc.nextLine(), sc.nextLine(),
              this.imageHelperFactory);
      if (sc.hasNextLine()) {
        throw new IllegalArgumentException("Invalid command format");
      }
      return command;
    });

    this.supportedOperations.put("sepia", (Scanner sc) -> {
      ImageOperationCommand command = new Sepia(sc.nextLine(), sc.nextLine());
      if (sc.hasNextLine()) {
        throw new IllegalArgumentException("Invalid command format");
      }
      return command;
    });

    this.supportedOperations.put("dither", (Scanner sc) -> {
      ImageOperationCommand command = new Dither(sc.nextLine(), sc.nextLine());
      if (sc.hasNextLine()) {
        throw new IllegalArgumentException("Invalid command format");
      }
      return command;
    });

    this.supportedOperations.put("blur", (Scanner sc) -> {
      ImageOperationCommand command = new Blur(sc.nextLine(), sc.nextLine());
      if (sc.hasNextLine()) {
        throw new IllegalArgumentException("Invalid command format");
      }
      return command;
    });

    this.supportedOperations.put("sharpen", (Scanner sc) -> {
      ImageOperationCommand command = new Sharpen(sc.nextLine(), sc.nextLine());
      if (sc.hasNextLine()) {
        throw new IllegalArgumentException("Invalid command format");
      }
      return command;
    });
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
  protected String executeCommand(String command, Scanner sc) throws UnsupportedOperationException,
          NoSuchElementException, IllegalArgumentException {
    StringBuilder output = new StringBuilder();
    Function<Scanner, ImageOperationCommand> commandToExecute =
            this.supportedOperations.getOrDefault(command, null);
    if (commandToExecute == null) {
      throw new UnsupportedOperationException("Invalid command");
    }
    commandToExecute.apply(sc).execute(this.model);
    output.append("Executed command: ").append(command);
    return output.toString();
  }

}
