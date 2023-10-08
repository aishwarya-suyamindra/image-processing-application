package controller;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

import utility.CustomImage;
import view.IView;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;


/**
 * A Junit test class for the ImageCommandController class.
 */
public class ImageCommandControllerTest extends AbstractImageCommandControllerTest {
  // region Mocks
  static class MockView implements IView {
    private final StringBuilder log;

    MockView(StringBuilder log) {
      this.log = log;
    }

    @Override
    public void print(String message) {
      this.log.append(message);
    }
  }

  @Rule
  public TemporaryFolder folder = new TemporaryFolder();

  @Test
  public void testGoInvalidCommand() {
    String command = "edit";
    InputStream in = new ByteArrayInputStream(command.getBytes());
    resetLog();
    IView view = new MockView(viewLog);
    CommandController controller = new ImageCommandController(new MockImageProcessorModel(modelLog),
            in, view, new MockImageHelperFactory(false, helperLog, null));
    controller.process();
    assertEquals("Invalid command", viewLog.toString());
  }

  @Test
  public void testGoValidLoadCommand() {
    CustomImage imageToLoad = getCustomImage();
    String command = "load testImage.ppm elephant";
    InputStream in = new ByteArrayInputStream(command.getBytes());
    resetLog();
    IView view = new MockView(viewLog);
    CommandController controller = new ImageCommandController(new MockImageProcessorModel(modelLog),
            in, view, new MockImageHelperFactory(false, helperLog, imageToLoad));
    controller.process();
    assertEquals("Load: testImage.ppm, elephant", helperLog.toString());
    assertEquals("Load: " + imageToLoad, modelLog.toString());
    assertEquals("Executed command: load", viewLog.toString());
  }

  @Test
  public void testGoInvalidLoadCommand() {
    // Load command with incomplete set of arguments
    String command = "load elephant.ppm";
    CustomImage imageToLoad = getCustomImage();
    InputStream in = new ByteArrayInputStream(command.getBytes());
    resetLog();
    IView view = new MockView(viewLog);
    CommandController controller = new ImageCommandController(new MockImageProcessorModel(modelLog),
            in, view, new MockImageHelperFactory(false, helperLog, imageToLoad));
    controller.process();
    assertEquals("Invalid command format", viewLog.toString());

    // Load command with extra arguments
    resetLog();
    command = "load elephant.ppm elephant elephant1";
    in = new ByteArrayInputStream(command.getBytes());
    controller = new ImageCommandController(new MockImageProcessorModel(modelLog),
            in, new MockView(viewLog),
            new MockImageHelperFactory(false, helperLog, imageToLoad));
    controller.process();
    assertEquals("Invalid command format", viewLog.toString());
  }

  @Test
  public void testGoLoadWithInvalidFilePath() {
    String command = "load test/elephant.ppm elephant";
    CustomImage imageToLoad = getCustomImage();
    InputStream in = new ByteArrayInputStream(command.getBytes());
    resetLog();
    IView view = new MockView(viewLog);
    CommandController controller = new ImageCommandController(new MockImageProcessorModel(modelLog),
            in, view, new MockImageHelperFactory(true, helperLog, imageToLoad));
    controller.process();
    assertEquals("Failed to load the image at filepath: test/elephant.ppm",
            viewLog.toString());
  }

  @Test
  public void testGoValidBrightenCommand() {
    String command = "brighten 10 elephant elephant-brighter";
    InputStream in = new ByteArrayInputStream(command.getBytes());
    resetLog();
    IView view = new MockView(viewLog);
    CommandController controller = new ImageCommandController(new MockImageProcessorModel(modelLog),
            in, view, new MockImageHelperFactory(false, helperLog, null));
    controller.process();
    assertEquals("Executed command: brighten", viewLog.toString());
    assertEquals("Brighten: elephant, elephant-brighter, 10", modelLog.toString());

    resetLog();
    command = "brighten -10 elephant elephant-darker";
    in = new ByteArrayInputStream(command.getBytes());
    view = new MockView(viewLog);
    controller = new ImageCommandController(new MockImageProcessorModel(modelLog),
            in, view, new MockImageHelperFactory(false, helperLog, null));
    controller.process();
    controller.process();
    assertEquals("Executed command: brighten", viewLog.toString());
    assertEquals("Brighten: elephant, elephant-darker, -10", modelLog.toString());
  }

  @Test
  public void testGoInvalidBrightenCommand() {
    // Brighten command with incomplete set of arguments
    String command = "brighten       10 elephant";
    InputStream in = new ByteArrayInputStream(command.getBytes());
    resetLog();
    IView view = new MockView(viewLog);
    CommandController controller = new ImageCommandController(new MockImageProcessorModel(modelLog),
            in, view, new MockImageHelperFactory(false, helperLog, null));
    controller.process();
    assertEquals("Invalid command format", viewLog.toString());

    // Brighten command with extra arguments
    resetLog();
    command = "brighten 10 elephant elephant-brighter 0";
    in = new ByteArrayInputStream(command.getBytes());
    view = new MockView(viewLog);
    controller = new ImageCommandController(new MockImageProcessorModel(modelLog),
            in, view, new MockImageHelperFactory(false, helperLog, null));
    controller.process();
    assertEquals("Invalid command format", viewLog.toString());

    // Brighten command with incorrectly placed arguments
    resetLog();
    command = "brighten elephant elephant-brighter 10 ";
    in = new ByteArrayInputStream(command.getBytes());
    view = new MockView(viewLog);
    controller = new ImageCommandController(new MockImageProcessorModel(modelLog),
            in, view, new MockImageHelperFactory(false, helperLog, null));
    controller.process();
    assertEquals("Invalid command format", viewLog.toString());

    // Brighten command with no increment/ decrement factor
    resetLog();
    command = "brighten elephant elephant-darker";
    in = new ByteArrayInputStream(command.getBytes());
    view = new MockView(viewLog);
    controller = new ImageCommandController(new MockImageProcessorModel(modelLog),
            in, view, new MockImageHelperFactory(false, helperLog, null));
    controller.process();
    assertEquals("Invalid command format", viewLog.toString());


    // Brighten command with invalid increment factor
    resetLog();
    command = "brighten -15.5 elephant elephant-darker";
    in = new ByteArrayInputStream(command.getBytes());
    view = new MockView(viewLog);
    controller = new ImageCommandController(new MockImageProcessorModel(modelLog),
            in, view, new MockImageHelperFactory(false, helperLog, null));
    controller.process();
    assertEquals("Invalid command format", viewLog.toString());
  }

  @Test
  public void testGoValidCombineCommand() {
    String command = "rgb-combine elephant-red-tint elephant-red elephant-green elephant-blue";
    InputStream in = new ByteArrayInputStream(command.getBytes());
    resetLog();
    IView view = new MockView(viewLog);
    CommandController controller = new ImageCommandController(new MockImageProcessorModel(modelLog),
            in, view, new MockImageHelperFactory(false, helperLog, null));
    controller.process();
    assertEquals("Executed command: rgb-combine", viewLog.toString());
    assertEquals("Combine: elephant-red, elephant-green, elephant-blue, elephant-red-tint",
            modelLog.toString());
  }

  @Test
  public void testGoInvalidCombineCommand() {
    // Combine command with incomplete set of arguments
    String command = "rgb-combine elephant-red-tint elephant-red elephant-green";
    InputStream in = new ByteArrayInputStream(command.getBytes());
    resetLog();
    IView view = new MockView(viewLog);
    CommandController controller = new ImageCommandController(new MockImageProcessorModel(modelLog),
            in, view, new MockImageHelperFactory(false, helperLog, null));
    controller.process();
    assertEquals("Invalid command format", viewLog.toString());

    // Combine command with extra arguments
    resetLog();
    command = "rgb-combine elephant-red-tint elephant-red elephant-green elephant-blue "
            + "elephant-rgb";
    in = new ByteArrayInputStream(command.getBytes());
    view = new MockView(viewLog);
    controller = new ImageCommandController(new MockImageProcessorModel(modelLog),
            in, view, new MockImageHelperFactory(false, helperLog, null));
    controller.process();
    assertEquals("Invalid command format", viewLog.toString());
  }

  @Test
  public void testGoValidFlipCommand() {
    String command = "vertical-flip elephant elephant-vertical";
    InputStream in = new ByteArrayInputStream(command.getBytes());
    resetLog();
    IView view = new MockView(viewLog);
    CommandController controller = new ImageCommandController(new MockImageProcessorModel(modelLog),
            in, view, new MockImageHelperFactory(false, helperLog, null));
    controller.process();
    assertEquals("Executed command: vertical-flip", viewLog.toString());
    assertEquals("Flip: elephant, elephant-vertical, 1",
            modelLog.toString());

    resetLog();
    command = "horizontal-flip elephant elephant-horizontal";
    in = new ByteArrayInputStream(command.getBytes());
    view = new MockView(viewLog);
    controller = new ImageCommandController(new MockImageProcessorModel(modelLog),
            in, view, new MockImageHelperFactory(false, helperLog, null));
    controller.process();
    assertEquals("Executed command: horizontal-flip", viewLog.toString());
    assertEquals("Flip: elephant, elephant-horizontal, 0",
            modelLog.toString());
  }

  @Test
  public void testGoInvalidFlipCommand() {
    // Flip command with incomplete set of arguments
    String command = "vertical-flip   ";
    InputStream in = new ByteArrayInputStream(command.getBytes());
    resetLog();
    IView view = new MockView(viewLog);
    CommandController controller = new ImageCommandController(new MockImageProcessorModel(modelLog),
            in, view, new MockImageHelperFactory(false, helperLog, null));
    controller.process();
    assertEquals("Invalid command format", viewLog.toString());

    resetLog();
    command = "horizontal-flip elephant";
    in = new ByteArrayInputStream(command.getBytes());
    view = new MockView(viewLog);
    controller = new ImageCommandController(new MockImageProcessorModel(modelLog),
            in, view, new MockImageHelperFactory(false, helperLog, null));
    controller.process();
    assertEquals("Invalid command format", viewLog.toString());

    // Flip command with extra arguments
    resetLog();
    command = "vertical-flip elephant elephant-vertical vertical";
    in = new ByteArrayInputStream(command.getBytes());
    view = new MockView(viewLog);
    controller = new ImageCommandController(new MockImageProcessorModel(modelLog),
            in, view, new MockImageHelperFactory(false, helperLog, null));
    controller.process();
    assertEquals("Invalid command format", viewLog.toString());

    resetLog();
    command = "horizontal-flip elephant elephant-horizontal 20";
    in = new ByteArrayInputStream(command.getBytes());
    view = new MockView(viewLog);
    controller = new ImageCommandController(new MockImageProcessorModel(modelLog),
            in, view, new MockImageHelperFactory(false, helperLog, null));
    controller.process();
    assertEquals("Invalid command format", viewLog.toString());
  }

  @Test
  public void testGoInvalidGreyscaleCommand() {
    String command = "greyscale rgb-component elephant elephant-greyscale";
    InputStream in = new ByteArrayInputStream(command.getBytes());
    resetLog();
    IView view = new MockView(viewLog);
    CommandController controller = new ImageCommandController(new MockImageProcessorModel(modelLog),
            in, view, new MockImageHelperFactory(false, helperLog, null));
    controller.process();
    assertEquals("Invalid command", viewLog.toString());
  }

  @Test
  public void testGoValidGreyscaleChannelCommand() {
    String command = "greyscale red-component elephant elephant-red";
    InputStream in = new ByteArrayInputStream(command.getBytes());
    resetLog();
    IView view = new MockView(viewLog);
    CommandController controller = new ImageCommandController(new MockImageProcessorModel(modelLog),
            in, view, new MockImageHelperFactory(false, helperLog, null));
    controller.process();
    assertEquals("Executed command: greyscale", viewLog.toString());
    assertEquals("Visualise Channel: elephant, elephant-red, 2", modelLog.toString());

    resetLog();
    command = "greyscale green-component elephant elephant-green";
    in = new ByteArrayInputStream(command.getBytes());
    view = new MockView(viewLog);
    controller = new ImageCommandController(new MockImageProcessorModel(modelLog),
            in, view, new MockImageHelperFactory(false, helperLog, null));
    controller.process();
    assertEquals("Executed command: greyscale", viewLog.toString());
    assertEquals("Visualise Channel: elephant, elephant-green, 1", modelLog.toString());

    resetLog();
    command = " greyscale  blue-component elephant   elephant-blue ";
    in = new ByteArrayInputStream(command.getBytes());
    view = new MockView(viewLog);
    controller = new ImageCommandController(new MockImageProcessorModel(modelLog),
            in, view, new MockImageHelperFactory(false, helperLog, null));
    controller.process();
    assertEquals("Executed command: greyscale", viewLog.toString());
    assertEquals("Visualise Channel: elephant, elephant-blue, 0", modelLog.toString());
  }

  @Test
  public void testGoInvalidGreyscaleChannelCommand() {
    // Greyscale visualise channel with incomplete arguments
    String command = "greyscale red-component";
    InputStream in = new ByteArrayInputStream(command.getBytes());
    resetLog();
    IView view = new MockView(viewLog);
    CommandController controller = new ImageCommandController(new MockImageProcessorModel(modelLog),
            in, view, new MockImageHelperFactory(false, helperLog, null));
    controller.process();
    assertEquals("Invalid command format", viewLog.toString());

    resetLog();
    command = "greyscale green-component";
    in = new ByteArrayInputStream(command.getBytes());
    view = new MockView(viewLog);
    controller = new ImageCommandController(new MockImageProcessorModel(modelLog),
            in, view, new MockImageHelperFactory(false, helperLog, null));
    controller.process();
    assertEquals("Invalid command format", viewLog.toString());

    resetLog();
    command = "greyscale       elephant";
    in = new ByteArrayInputStream(command.getBytes());
    view = new MockView(viewLog);
    controller = new ImageCommandController(new MockImageProcessorModel(modelLog),
            in, view, new MockImageHelperFactory(false, helperLog, null));
    controller.process();
    assertEquals("Invalid command format", viewLog.toString());

    // Greyscale visualise channel with extra arguments
    resetLog();
    command = "greyscale red-component  elephant    elephant-red  green-component";
    in = new ByteArrayInputStream(command.getBytes());
    view = new MockView(viewLog);
    controller = new ImageCommandController(new MockImageProcessorModel(modelLog),
            in, view, new MockImageHelperFactory(false, helperLog, null));
    controller.process();
    assertEquals("Invalid command format", viewLog.toString());

    // Greyscale visualise channel with invalid channel
    resetLog();
    command = "greyscale yellow-component elephant elephant-yellow";
    in = new ByteArrayInputStream(command.getBytes());
    view = new MockView(viewLog);
    controller = new ImageCommandController(new MockImageProcessorModel(modelLog),
            in, view, new MockImageHelperFactory(false, helperLog, null));
    controller.process();
    assertEquals("Invalid command", viewLog.toString());
  }

  @Test
  public void testGoValidGreyscaleLumaCommand() {
    String command = "greyscale luma-component elephant elephant-luma-greyscale";
    InputStream in = new ByteArrayInputStream(command.getBytes());
    resetLog();
    IView view = new MockView(viewLog);
    CommandController controller = new ImageCommandController(new MockImageProcessorModel(modelLog),
            in, view, new MockImageHelperFactory(false, helperLog, null));
    controller.process();
    assertEquals("Executed command: greyscale", viewLog.toString());
    assertEquals("Visualise Luma: elephant, elephant-luma-greyscale", modelLog.toString());
  }

  @Test
  public void testGoInvalidGreyscaleLumaCommand() {
    // Greyscale luma command with incomplete set of arguments
    String command = "greyscale luma-component";
    InputStream in = new ByteArrayInputStream(command.getBytes());
    resetLog();
    IView view = new MockView(viewLog);
    CommandController controller = new ImageCommandController(new MockImageProcessorModel(modelLog),
            in, view, new MockImageHelperFactory(false, helperLog, null));
    controller.process();
    assertEquals("Invalid command format", viewLog.toString());

    // Greyscale luma command with extra arguments
    resetLog();
    command = "greyscale luma-component elephant elephant-luma-greyscale  luma-component";
    in = new ByteArrayInputStream(command.getBytes());
    view = new MockView(viewLog);
    controller = new ImageCommandController(new MockImageProcessorModel(modelLog),
            in, view, new MockImageHelperFactory(false, helperLog, null));
    controller.process();
    assertEquals("Invalid command format", viewLog.toString());
  }

  @Test
  public void testGoValidGreyscaleValueCommand() {
    String command = "greyscale value-component elephant elephant-value-greyscale";
    InputStream in = new ByteArrayInputStream(command.getBytes());
    resetLog();
    IView view = new MockView(viewLog);
    CommandController controller = new ImageCommandController(new MockImageProcessorModel(modelLog),
            in, view, new MockImageHelperFactory(false, helperLog, null));
    controller.process();
    assertEquals("Executed command: greyscale", viewLog.toString());
    assertEquals("Visualise Value: elephant, elephant-value-greyscale",
            modelLog.toString());

    resetLog();
    command = "   greyscale    value-component  elephant elephant-value-greyscale     ";
    in = new ByteArrayInputStream(command.getBytes());
    view = new MockView(viewLog);
    controller = new ImageCommandController(new MockImageProcessorModel(modelLog),
            in, view, new MockImageHelperFactory(false, helperLog, null));
    controller.process();
    assertEquals("Executed command: greyscale", viewLog.toString());
    assertEquals("Visualise Value: elephant, elephant-value-greyscale",
            modelLog.toString());
  }

  @Test
  public void testGoInvalidGreyscaleValueCommand() {
    // Greyscale value command with incomplete set of arguments
    String command = "greyscale value-component";
    InputStream in = new ByteArrayInputStream(command.getBytes());
    resetLog();
    IView view = new MockView(viewLog);
    CommandController controller = new ImageCommandController(new MockImageProcessorModel(modelLog),
            in, view, new MockImageHelperFactory(false, helperLog, null));
    controller.process();
    assertEquals("Invalid command format", viewLog.toString());

    // Greyscale value command with extra arguments
    resetLog();
    command = "greyscale value-component elephant elephant-value-greyscale luma-component";
    in = new ByteArrayInputStream(command.getBytes());
    view = new MockView(viewLog);
    controller = new ImageCommandController(new MockImageProcessorModel(modelLog),
            in, view, new MockImageHelperFactory(false, helperLog, null));
    controller.process();
    assertEquals("Invalid command format", viewLog.toString());
  }

  @Test
  public void testGoValidGreyscaleIntensityCommand() {
    String command = "greyscale intensity-component elephant elephant-intensity-greyscale";
    InputStream in = new ByteArrayInputStream(command.getBytes());
    resetLog();
    IView view = new MockView(viewLog);
    CommandController controller = new ImageCommandController(new MockImageProcessorModel(modelLog),
            in, view, new MockImageHelperFactory(false, helperLog, null));
    controller.process();
    assertEquals("Executed command: greyscale", viewLog.toString());
    assertEquals("Visualise Intensity: elephant, elephant-intensity-greyscale",
            modelLog.toString());
  }

  @Test
  public void testGoInvalidGreyscaleIntensityCommand() {
    // Greyscale intensity command with incomplete set of arguments
    String command = "greyscale intensity-component ";
    InputStream in = new ByteArrayInputStream(command.getBytes());
    resetLog();
    IView view = new MockView(viewLog);
    CommandController controller = new ImageCommandController(new MockImageProcessorModel(modelLog),
            in, view, new MockImageHelperFactory(false, helperLog, null));
    controller.process();
    assertEquals("Invalid command format", viewLog.toString());

    // Greyscale intensity command with extra arguments
    resetLog();
    command = "greyscale intensity-component elephant elephant-intensity-greyscale 1";
    in = new ByteArrayInputStream(command.getBytes());
    view = new MockView(viewLog);
    controller = new ImageCommandController(new MockImageProcessorModel(modelLog),
            in, view, new MockImageHelperFactory(false, helperLog, null));
    controller.process();
    assertEquals("Invalid command format", viewLog.toString());
  }

  @Test
  public void testGoValidSplitCommand() {
    String command = "rgb-split elephant elephant-red elephant-green elephant-blue";
    InputStream in = new ByteArrayInputStream(command.getBytes());
    resetLog();
    IView view = new MockView(viewLog);
    CommandController controller = new ImageCommandController(new MockImageProcessorModel(modelLog),
            in, view, new MockImageHelperFactory(false, helperLog, null));
    controller.process();
    assertEquals("Executed command: rgb-split", viewLog.toString());
    assertEquals("Split: elephant, elephant-red, elephant-green, elephant-blue",
            modelLog.toString());
  }

  @Test
  public void testGoInvalidSplitCommand() {
    // Split command with incomplete set of arguments
    String command = "rgb-split elephant elephant-red elephant-green";
    InputStream in = new ByteArrayInputStream(command.getBytes());
    resetLog();
    IView view = new MockView(viewLog);
    CommandController controller = new ImageCommandController(new MockImageProcessorModel(modelLog),
            in, view, new MockImageHelperFactory(false, helperLog, null));
    controller.process();
    assertEquals("Invalid command format", viewLog.toString());

    // Split command with extra arguments
    resetLog();
    command = "rgb-split elephant elephant-red elephant-green elephant-blue elephantRGB";
    in = new ByteArrayInputStream(command.getBytes());
    view = new MockView(viewLog);
    controller = new ImageCommandController(new MockImageProcessorModel(modelLog),
            in, view, new MockImageHelperFactory(false, helperLog, null));
    controller.process();
    assertEquals("Invalid command format", viewLog.toString());
  }

  @Test
  public void testGoValidSaveCommand() {
    String command = "save res/elephant-red.ppm elephant-red";
    InputStream in = new ByteArrayInputStream(command.getBytes());
    resetLog();
    IView view = new MockView(viewLog);
    CommandController controller = new ImageCommandController(new MockImageProcessorModel(modelLog),
            in, view, new MockImageHelperFactory(false, helperLog, null));
    controller.process();
    assertEquals("Executed command: save", viewLog.toString());
    assertEquals("Save: " + getCustomImage() + ", res/elephant-red.ppm",
            helperLog.toString());
  }

  @Test
  public void testGoInvalidSaveCommand() {
    // Save command with incomplete set of arguments
    String command = "save res/elephant-red.ppm";
    InputStream in = new ByteArrayInputStream(command.getBytes());
    resetLog();
    IView view = new MockView(viewLog);
    CommandController controller = new ImageCommandController(new MockImageProcessorModel(modelLog),
            in, view, new MockImageHelperFactory(false, helperLog, null));
    controller.process();
    assertEquals("Invalid command format", viewLog.toString());

    // Save command with extra arguments
    resetLog();
    command = "save res/elephant-red.ppm elephant-red 1";
    in = new ByteArrayInputStream(command.getBytes());
    view = new MockView(viewLog);
    controller = new ImageCommandController(new MockImageProcessorModel(modelLog),
            in, view, new MockImageHelperFactory(false, helperLog, null));
    controller.process();
    assertEquals("Invalid command format", viewLog.toString());
  }

  @Test
  public void testGoSaveWithInvalidFilePath() {
    String command = "save test/elephant-red.ppm elephant-red";
    CustomImage imageToLoad = getCustomImage();
    InputStream in = new ByteArrayInputStream(command.getBytes());
    resetLog();
    IView view = new MockView(viewLog);
    CommandController controller = new ImageCommandController(new MockImageProcessorModel(modelLog),
            in, view, new MockImageHelperFactory(true, helperLog, imageToLoad));
    controller.process();
    assertEquals("Failed to save the image at filepath: test/elephant-red.ppm",
            viewLog.toString());
  }

  @Test
  public void testGoModelThrowsIllegalArgumentException() {
    // Save command without extension
    String command = "save res/elephant-red.ppm elephant-red";
    InputStream in = new ByteArrayInputStream(command.getBytes());
    resetLog();
    IView view = new MockView(viewLog);
    CommandController controller = new ImageCommandController(
            new MockImageProcessorModelExceptions(modelLog),
            in, view, new MockImageHelperFactory(false, helperLog, null));
    controller.process();
    assertEquals("Image with given name cannot be found",
            viewLog.toString());
  }

  @Test
  public void testGoInputWithComments() {
    String command = "#load elephant.ppm and call it 'elephant'\n"
            + "load images/elephant.ppm elephant\n" + "#give the elephant a red tint\n"
            + "rgb-split   elephant elephant-red elephant-green elephant-blue\n"
            + "#darken just the red image\n"
            + "brighten -50 elephant-red      elephant-red";
    InputStream in = new ByteArrayInputStream(command.getBytes());
    resetLog();
    IView view = new MockView(viewLog);
    CommandController controller = new ImageCommandController(new MockImageProcessorModel(modelLog),
            in, view, new MockImageHelperFactory(false, helperLog, null));
    controller.process();
    assertEquals("Executed command: load" + "Executed command: rgb-split"
                    + "Executed command: brighten",
            viewLog.toString());
  }

  @Test
  public void testGoWithMultipleCommands() {
    StringBuilder command = new StringBuilder();
    command.append("#load elephant.ppm and call it 'elephant'\n"
            + "load images/elephant.ppm elephant\n");
    command.append("#brighten elephant by adding 30  \n"
            + "brighten 30 koala elephant-brighter-30\n");
    command.append("#flip elephant vertically\n"
            + "vertical-flip elephant elephant-vertical\n");
    command.append("#flip the vertically flipped elephant horizontally\n"
            + "horizontal-flip elephant-vertical elephant-vertical-horizontal\n");
    command.append("#create a greyscale using only the value component\n"
            + "greyscale value-component elephant elephant-greyscale\n");
    command.append("#save elephant-brighter-50\n"
            + "save images/elephant-brighter.ppm elephant-brighter-50\n");
    command.append("#save elephant-greyscale\n"
            + "save images/elephant-gs.ppm elephant-greyscale\n");
    command.append("#overwrite elephant with another file\n"
            + "load images/koala.ppm elephant\n");
    command.append("#give the koala a red tint\n"
            + "rgb-split elephant koala-red koala-green koala-blue");
    InputStream in = new ByteArrayInputStream(command.toString().getBytes());
    resetLog();
    IView view = new MockView(viewLog);
    CommandController controller = new ImageCommandController(new MockImageProcessorModel(modelLog),
            in, view, new MockImageHelperFactory(false, helperLog, null));
    controller.process();
    assertEquals("Executed command: load" + "Executed command: brighten"
                    + "Executed command: vertical-flipExecuted "
                    + "command: horizontal-flipExecuted command: greyscaleExecuted command: "
                    + "saveExecuted command: saveExecuted command: loadExecuted command: rgb-split",
            viewLog.toString());
  }

  private File createCommandsFile(String commands) {
    File commandsFile = null;
    try {
      commandsFile = folder.newFile("commands.txt");
      FileWriter fileWriter = new FileWriter(commandsFile);
      BufferedWriter bw = new BufferedWriter(fileWriter);
      bw.write(commands);
      bw.close();
    } catch (IOException ex) {
      fail("Command file creation failed");
    }
    return commandsFile;
  }

  @Test
  public void testGoWithValidRunCommand() {
    StringBuilder commands = new StringBuilder();
    commands.append("#load elephant.ppm and call it 'elephant'\n"
            + "load images/elephant.ppm elephant\n");
    commands.append("#brighten elephant by adding 30  \n"
            + "brighten 30    koala elephant-brighter-30\n");
    commands.append("#flip elephant vertically\n"
            + "vertical-flip elephant elephant-vertical\n");
    commands.append("#flip the vertically flipped elephant horizontally\n"
            + "horizontal-flip elephant-vertical elephant-vertical-horizontal\n");
    commands.append("#create a greyscale using only the value component\n"
            + "greyscale value-component elephant elephant-greyscale\n");
    commands.append("#save elephant-brighter-50\n"
            + "save  images/elephant-brighter.ppm elephant-brighter-50\n");
    commands.append("#save elephant-greyscale\n"
            + "save images/elephant-gs.ppm    elephant-greyscale\n");
    commands.append("#overwrite elephant with another file\n"
            + "load images/koala.ppm elephant\n");
    commands.append("#give the elephant a red tint\n"
            + "rgb-split elephant koala-red koala-green koala-blue");

    File commandsFile = createCommandsFile(commands.toString());
    resetLog();
    String command = "run " + commandsFile.getPath();
    InputStream in = new ByteArrayInputStream(command.getBytes());
    IView view = new MockView(viewLog);
    CommandController controller = new ImageCommandController(
            new MockImageProcessorModel(modelLog),
            in, view, new MockImageHelperFactory(false, helperLog, null));
    controller.process();
    assertEquals("Executed command: load" + "Executed command: brighten"
                    + "Executed command: vertical-flipExecuted "
                    + "command: horizontal-flipExecuted command: greyscaleExecuted command: "
                    + "saveExecuted command: saveExecuted command: loadExecuted command: rgb-split",
            viewLog.toString());
  }

  @Test
  public void testGoInvalidRunCommand() {
    // Save command with incomplete set of arguments
    String command = "run";
    InputStream in = new ByteArrayInputStream(command.getBytes());
    resetLog();
    IView view = new MockView(viewLog);
    CommandController controller = new ImageCommandController(new MockImageProcessorModel(modelLog),
            in, view, new MockImageHelperFactory(false, helperLog, null));
    controller.process();
    assertEquals("Invalid command format", viewLog.toString());

    // Save command with extra arguments
    resetLog();
    File commandsFile = createCommandsFile("");
    command = "run " + commandsFile.getPath() + " file";
    in = new ByteArrayInputStream(command.getBytes());
    view = new MockView(viewLog);
    controller = new ImageCommandController(new MockImageProcessorModel(modelLog),
            in, view, new MockImageHelperFactory(false, helperLog, null));
    controller.process();
    assertEquals("Invalid command format", viewLog.toString());
  }

  @Test
  public void testGoRunWithInvalidFilePath() {
    String command = "run testCommands.txt";
    CustomImage imageToLoad = getCustomImage();
    InputStream in = new ByteArrayInputStream(command.getBytes());
    resetLog();
    IView view = new MockView(viewLog);
    CommandController controller = new ImageCommandController(new MockImageProcessorModel(modelLog),
            in, view, new MockImageHelperFactory(true, helperLog, imageToLoad));
    controller.process();
    assertEquals("Script file not found", viewLog.toString());
  }

  @Test
  public void testGoWithCommandsFromDifferentInputStreams() {
    StringBuilder commands = new StringBuilder();
    commands.append("#brighten elephant by adding 30  \n"
            + "brighten 30    koala elephant-brighter-30\n");
    commands.append("#flip elephant vertically\n"
            + "vertical-flip elephant elephant-vertical\n");
    commands.append("#flip the vertically flipped elephant horizontally\n"
            + "horizontal-flip elephant-vertical elephant-vertical-horizontal\n");
    commands.append("#create a greyscale using only the value component\n"
            + "   greyscale value-component elephant elephant-greyscale\n");
    commands.append("#save elephant-brighter-50\n"
            + "save  images/elephant-brighter.ppm  elephant-brighter-50    \n");
    commands.append("#save  elephant-greyscale\n"
            + "save images/elephant-gs.ppm    elephant-greyscale\n");
    commands.append("#overwrite elephant with another file\n"
            + "load images/koala.ppm elephant\n");
    commands.append("#give the koala a red tint\n"
            + "rgb-split  elephant koala-red koala-green koala-blue");

    resetLog();
    String command = "#load elephant.ppm and call it 'elephant'\n"
            + "load images/elephant.ppm elephant\n";
    InputStream in = new ByteArrayInputStream(command.getBytes());
    IView view = new MockView(viewLog);
    CommandController controller = new ImageCommandController(
            new MockImageProcessorModel(modelLog),
            in, view, new MockImageHelperFactory(false, helperLog, null));
    controller.process();
    assertEquals("Executed command: load", viewLog.toString());

    File commandsFile = createCommandsFile(commands.toString());
    command = "run " + commandsFile.getPath();
    in = new ByteArrayInputStream(command.getBytes());
    view = new MockView(viewLog);
    controller = new ImageCommandController(
            new MockImageProcessorModel(modelLog),
            in, view, new MockImageHelperFactory(false, helperLog, null));
    controller.process();
    assertEquals("Executed command: loadExecuted command: brighten"
                    + "Executed command: vertical-flipExecuted "
                    + "command: horizontal-flipExecuted command: greyscaleExecuted command: "
                    + "saveExecuted command: saveExecuted command: loadExecuted command: rgb-split",
            viewLog.toString());

    command =
            " horizontal-flip elephant-vertical elephant-vertical-horizontal \n";
    in = new ByteArrayInputStream(command.getBytes());
    view = new MockView(viewLog);
    controller = new ImageCommandController(
            new MockImageProcessorModel(modelLog),
            in, view, new MockImageHelperFactory(false, helperLog, null));
    controller.process();
    assertEquals("Executed command: loadExecuted command: brighten"
                    + "Executed command: vertical-flipExecuted "
                    + "command: horizontal-flipExecuted command: greyscaleExecuted command: "
                    + "saveExecuted command: saveExecuted command: loadExecuted command: "
                    + "rgb-splitExecuted command: horizontal-flip",
            viewLog.toString());
  }

  @Test
  public void testGoValidSepiaCommand() {
    String command = "sepia elephant elephant-sepia";
    InputStream in = new ByteArrayInputStream(command.getBytes());
    resetLog();
    IView view = new MockView(viewLog);
    CommandController controller = new ImageCommandController(new MockImageProcessorModel(modelLog),
            in, view, new MockImageHelperFactory(false, helperLog, null));
    controller.process();
    assertEquals("Executed command: sepia", viewLog.toString());
    assertEquals("Sepia: elephant, elephant-sepia", modelLog.toString());

    resetLog();
    command = "sepia elephant-sepia elephant-sepia-sepia";
    in = new ByteArrayInputStream(command.getBytes());
    view = new MockView(viewLog);
    controller = new ImageCommandController(new MockImageProcessorModel(modelLog),
            in, view, new MockImageHelperFactory(false, helperLog, null));
    controller.process();
    assertEquals("Executed command: sepia", viewLog.toString());
    assertEquals("Sepia: elephant-sepia, elephant-sepia-sepia", modelLog.toString());
  }

  @Test
  public void testGoInvalidSepiaCommand() {
    // Sepia with incomplete set of arguments
    String command = "sepia elephant";
    InputStream in = new ByteArrayInputStream(command.getBytes());
    resetLog();
    IView view = new MockView(viewLog);
    CommandController controller = new ImageCommandController(new MockImageProcessorModel(modelLog),
            in, view, new MockImageHelperFactory(false, helperLog, null));
    controller.process();
    assertEquals("Invalid command format", viewLog.toString());

    // Sepia command with extra arguments
    resetLog();
    command = "sepia elephant elephant-sepia elephant-sepia-sepia 1";
    in = new ByteArrayInputStream(command.getBytes());
    view = new MockView(viewLog);
    controller = new ImageCommandController(new MockImageProcessorModel(modelLog),
            in, view, new MockImageHelperFactory(false, helperLog, null));
    controller.process();
    assertEquals("Invalid command format", viewLog.toString());
  }

  @Test
  public void testGoValidDitherCommand() {
    String command = "dither elephant elephant-dither";
    InputStream in = new ByteArrayInputStream(command.getBytes());
    resetLog();
    IView view = new MockView(viewLog);
    CommandController controller = new ImageCommandController(new MockImageProcessorModel(modelLog),
            in, view, new MockImageHelperFactory(false, helperLog, null));
    controller.process();
    assertEquals("Executed command: dither", viewLog.toString());
    assertEquals("Dither: elephant, elephant-dither", modelLog.toString());

    resetLog();
    command = "dither elephant-sepia elephant-sepia-dither";
    in = new ByteArrayInputStream(command.getBytes());
    view = new MockView(viewLog);
    controller = new ImageCommandController(new MockImageProcessorModel(modelLog),
            in, view, new MockImageHelperFactory(false, helperLog, null));
    controller.process();
    assertEquals("Executed command: dither", viewLog.toString());
    assertEquals("Dither: elephant-sepia, elephant-sepia-dither", modelLog.toString());
  }

  @Test
  public void testGoInvalidDitherCommand() {
    // dither with incomplete set of arguments
    String command = "dither elephant";
    InputStream in = new ByteArrayInputStream(command.getBytes());
    resetLog();
    IView view = new MockView(viewLog);
    CommandController controller = new ImageCommandController(new MockImageProcessorModel(modelLog),
            in, view, new MockImageHelperFactory(false, helperLog, null));
    controller.process();
    assertEquals("Invalid command format", viewLog.toString());

    // dither command with extra arguments
    resetLog();
    command = "dither elephant elephant-sepia elephant-sepia-dither 1";
    in = new ByteArrayInputStream(command.getBytes());
    view = new MockView(viewLog);
    controller = new ImageCommandController(new MockImageProcessorModel(modelLog),
            in, view, new MockImageHelperFactory(false, helperLog, null));
    controller.process();
    assertEquals("Invalid command format", viewLog.toString());
  }

  @Test
  public void testGoValidSharpenCommand() {
    String command = "sharpen elephant elephant-sharpen";
    InputStream in = new ByteArrayInputStream(command.getBytes());
    resetLog();
    IView view = new MockView(viewLog);
    CommandController controller = new ImageCommandController(new MockImageProcessorModel(modelLog),
            in, view, new MockImageHelperFactory(false, helperLog, null));
    controller.process();
    assertEquals("Executed command: sharpen", viewLog.toString());
    assertEquals("Sharpen: elephant, elephant-sharpen", modelLog.toString());

    resetLog();
    command = "sharpen elephant-sepia elephant-sepia-sharpen";
    in = new ByteArrayInputStream(command.getBytes());
    view = new MockView(viewLog);
    controller = new ImageCommandController(new MockImageProcessorModel(modelLog),
            in, view, new MockImageHelperFactory(false, helperLog, null));
    controller.process();
    assertEquals("Executed command: sharpen", viewLog.toString());
    assertEquals("Sharpen: elephant-sepia, elephant-sepia-sharpen", modelLog.toString());
  }

  @Test
  public void testGoInvalidSharpenCommand() {
    // sharpen with incomplete set of arguments
    String command = "sharpen elephant";
    InputStream in = new ByteArrayInputStream(command.getBytes());
    resetLog();
    IView view = new MockView(viewLog);
    CommandController controller = new ImageCommandController(new MockImageProcessorModel(modelLog),
            in, view, new MockImageHelperFactory(false, helperLog, null));
    controller.process();
    assertEquals("Invalid command format", viewLog.toString());

    // sharper command with extra arguments
    resetLog();
    command = "sharpen elephant elephant elephant-sharpen 1";
    in = new ByteArrayInputStream(command.getBytes());
    view = new MockView(viewLog);
    controller = new ImageCommandController(new MockImageProcessorModel(modelLog),
            in, view, new MockImageHelperFactory(false, helperLog, null));
    controller.process();
    assertEquals("Invalid command format", viewLog.toString());
  }

  @Test
  public void testGoValidBlurCommand() {
    String command = "blur elephant elephant-blur";
    InputStream in = new ByteArrayInputStream(command.getBytes());
    resetLog();
    IView view = new MockView(viewLog);
    CommandController controller = new ImageCommandController(new MockImageProcessorModel(modelLog),
            in, view, new MockImageHelperFactory(false, helperLog, null));
    controller.process();
    assertEquals("Executed command: blur", viewLog.toString());
    assertEquals("Blur: elephant, elephant-blur", modelLog.toString());

    resetLog();
    command = "blur elephant-dither elephant-dither-blur";
    in = new ByteArrayInputStream(command.getBytes());
    view = new MockView(viewLog);
    controller = new ImageCommandController(new MockImageProcessorModel(modelLog),
            in, view, new MockImageHelperFactory(false, helperLog, null));
    controller.process();
    assertEquals("Executed command: blur", viewLog.toString());
    assertEquals("Blur: elephant-dither, elephant-dither-blur", modelLog.toString());
  }

  @Test
  public void testGoInvalidBlurCommand() {
    // blur with incomplete set of arguments
    String command = "blur elephant";
    InputStream in = new ByteArrayInputStream(command.getBytes());
    resetLog();
    IView view = new MockView(viewLog);
    CommandController controller = new ImageCommandController(new MockImageProcessorModel(modelLog),
            in, view, new MockImageHelperFactory(false, helperLog, null));
    controller.process();
    assertEquals("Invalid command format", viewLog.toString());

    // blur command with extra arguments
    resetLog();
    command = "blur elephant elephant elephant-blur 1";
    in = new ByteArrayInputStream(command.getBytes());
    view = new MockView(viewLog);
    controller = new ImageCommandController(new MockImageProcessorModel(modelLog),
            in, view, new MockImageHelperFactory(false, helperLog, null));
    controller.process();
    assertEquals("Invalid command format", viewLog.toString());
  }

  @Test
  public void testGoValidGreyscaleColorTransformationCommand() {
    String command = "greyscale elephant elephant-greyscale";
    InputStream in = new ByteArrayInputStream(command.getBytes());
    resetLog();
    IView view = new MockView(viewLog);
    CommandController controller = new ImageCommandController(new MockImageProcessorModel(modelLog),
            in, view, new MockImageHelperFactory(false, helperLog, null));
    controller.process();
    assertEquals("Executed command: greyscale", viewLog.toString());
    assertEquals("Visualise Luma: elephant, elephant-greyscale",
            modelLog.toString());
  }

  @Test
  public void testGoInvalidGreyscaleColorTransformationCommand() {
    // Greyscale color transformation command with incomplete set of arguments
    String command = "greyscale elephant ";
    InputStream in = new ByteArrayInputStream(command.getBytes());
    resetLog();
    IView view = new MockView(viewLog);
    CommandController controller = new ImageCommandController(new MockImageProcessorModel(modelLog),
            in, view, new MockImageHelperFactory(false, helperLog, null));
    controller.process();
    assertEquals("Invalid command format", viewLog.toString());

    // Greyscale intensity command with extra arguments
    resetLog();
    command = "greyscale elephant elephant-intensity-greyscale 1";
    in = new ByteArrayInputStream(command.getBytes());
    view = new MockView(viewLog);
    controller = new ImageCommandController(new MockImageProcessorModel(modelLog),
            in, view, new MockImageHelperFactory(false, helperLog, null));
    controller.process();
    assertEquals("Invalid command", viewLog.toString());
  }

  @Test
  public void testGoWithValidAndInvalidCommands() {
    StringBuilder commands = new StringBuilder();
    commands.append("#load elephant.ppm and call it 'elephant'\n"
            + "load images/elephant.ppm elephant\n");
    commands.append("#brighten elephant by adding 30  \n"
            + "brighten 30  elephant elephant-brighter-30\n");
    commands.append("#flip elephant vertically\n"
            + "vertical-flip elephant elephant-vertical\n");
    commands.append("#flip the vertically flipped elephant horizontally\n"
            + "horizontal-flip elephant-vertical elephant-vertical-horizontal\n");
    commands.append("#create a greyscale using only the value component\n"
            + "greyscale value-component elephant elephant-greyscale\n");
    commands.append("#save elephant-brighter-50\n"
            + "save  images/elephant-brighter.ppm elephant-brighter-50\n");
    commands.append("#Invalid brighten command\n"
            + "brighten elephant elephant-brighter\n");
    commands.append("#save elephant-greyscale\n"
            + "save images/elephant-gs.jpg    elephant-greyscale\n");
    commands.append("#overwrite elephant with another file\n"
            + "load images/koala.bmp elephant\n");
    commands.append("#invalid load command\n"
            + "load koala\n");
    commands.append("#greyscale koala\n"
            + "greyscale green-component elephant elephant-greyscale-green\n");
    commands.append("#greyscale koala\n"
            + "greyscale elephant elephant-greyscale-luma\n");
    commands.append("#blur koala\n"
            + "blur elephant elephant-blur\n");
    commands.append("#sharpen koala\n"
            + "sharpen elephant-sharpen\n");
    commands.append("#dither koala\n"
            + "dither elephant elephant-dither\n");
    commands.append("#flip elephant vertically\n"
            + "vertical-flip elephant-dither elephant-dither-vertical\n");
    commands.append("#greyscale koala\n"
            + "greyscale yellow-component elephant elephant-greyscale \n");
    commands.append("#sepia elephant vertically\n"
            + "sepia elephant-dither-vertical elephant-vertical-sepia\n");
    commands.append("#save elephant sepia dither\n"
            + "save elephant-dither-vertical.jpg elephant-vertical-sepia\n");
    commands.append("#greyscale koala\n"
            + "greyscale elephant \n");

    File commandsFile = createCommandsFile(commands.toString());
    resetLog();
    String command = "run " + commandsFile.getPath();
    InputStream in = new ByteArrayInputStream(command.getBytes());
    IView view = new MockView(viewLog);
    CommandController controller = new ImageCommandController(
            new MockImageProcessorModel(modelLog),
            in, view, new MockImageHelperFactory(false, helperLog, null));
    controller.process();
    StringBuilder expected = new StringBuilder();
    expected.append("Executed command: load").append("Executed command: brighten")
            .append("Executed command: vertical-flip")
            .append("Executed command: horizontal-flip")
            .append("Executed command: greyscale").append("Executed command: save")
            .append("Invalid command format").append("Executed command: save")
            .append("Executed command: load").append("Invalid command format")
            .append("Executed command: greyscale").append("Executed command: greyscale")
            .append("Executed command: blur")
            .append("Invalid command format").append("Executed command: dither")
            .append("Executed command: vertical-flip").append("Invalid command")
            .append("Executed command: sepia")
            .append("Executed command: save").append("Invalid command format");
    assertEquals(expected.toString(), viewLog.toString());
  }
}
