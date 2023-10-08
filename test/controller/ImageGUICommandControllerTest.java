package controller;

import org.junit.Test;

import model.ImageProcessor;
import utility.CustomImage;
import utility.IImageHelperFactory;
import utility.ViewMessageType;
import view.IGraphicalView;

import static org.junit.Assert.assertEquals;

/**
 * This is a JUnit test class for the ImageGUICommandController class.
 */
public class ImageGUICommandControllerTest extends AbstractImageCommandControllerTest {

  static class MockView implements IGraphicalView {
    private final StringBuilder viewLog;
    private Features features;

    MockView(StringBuilder viewLog) {
      this.viewLog = viewLog;
    }

    @Override
    public void addFeatures(Features features) {
      this.features = features;
      viewLog.append("Features set").append("\n");
    }

    @Override
    public void setVisible() {
      viewLog.append("View made visible").append("\n");
    }

    @Override
    public void updateUI(String imageName) {
      viewLog.append("Update UI: ").append(imageName).append("\n");
    }

    @Override
    public void echo(String message, ViewMessageType messageType) {
      viewLog.append("Echo: ").append(message).append("\n").append("Message type: ")
              .append(messageType.getTitle()).append(
                      "\n");
    }

    // triggers the call to the features method
    public void trigger(String command, String[] additionalParameters) {
      features.execute(command, additionalParameters);
    }
  }

  @Test
  public void testProcessViewVisible() {
    resetLog();
    IGraphicalView view = new MockView(viewLog);
    ImageProcessor model = new MockImageProcessorModel(modelLog);
    IImageHelperFactory helperFactory = new MockImageHelperFactory(false,
            helperLog, null);
    CommandController controller = new ImageGUICommandController(model, view, helperFactory);
    controller.process();
    String[] viewLogData = viewLog.toString().split("\n");
    assertEquals("View made visible", viewLogData[viewLogData.length - 1]);
  }

  @Test
  public void testProcessAddFeatures() {
    resetLog();
    IGraphicalView view = new MockView(viewLog);
    ImageProcessor model = new MockImageProcessorModel(modelLog);
    IImageHelperFactory helperFactory = new MockImageHelperFactory(false,
            helperLog, null);
    CommandController controller = new ImageGUICommandController(model, view, helperFactory);
    controller.process();
    String[] viewLogData = viewLog.toString().split("\n");
    assertEquals("Features set", viewLogData[0]);
  }

  @Test
  public void testViewCallBackOnLoadCommandWithValidParameters() {
    CustomImage imageToLoad = getCustomImage();
    resetLog();
    MockView view = new MockView(viewLog);
    ImageProcessor model = new MockImageProcessorModel(modelLog);
    IImageHelperFactory helperFactory = new MockImageHelperFactory(false,
            helperLog, imageToLoad);
    CommandController controller = new ImageGUICommandController(model, view, helperFactory);
    controller.process();

    view.trigger("load", new String[]{"panda.jpg"});
    assertEquals("Load: panda.jpg, base", helperLog.toString());
    assertEquals("Load: " + imageToLoad, modelLog.toString());
    String[] viewLogData = viewLog.toString().split("\n");
    assertEquals("Update UI: base", viewLogData[viewLogData.length - 1]);

    resetLog();
    view.trigger("LOAD", new String[]{"panda.jpg"});
    assertEquals("Load: panda.jpg, base", helperLog.toString());
    assertEquals("Load: " + imageToLoad, modelLog.toString());
    viewLogData = viewLog.toString().split("\n");
    assertEquals("Update UI: base", viewLogData[viewLogData.length - 1]);

    resetLog();
    view.trigger("Load", new String[]{"panda.jpg"});
    assertEquals("Load: panda.jpg, base", helperLog.toString());
    assertEquals("Load: " + imageToLoad, modelLog.toString());
    viewLogData = viewLog.toString().split("\n");
    assertEquals("Update UI: base", viewLogData[viewLogData.length - 1]);
  }

  @Test
  public void testCallBackOnLoadCommandWithInvalidParameters() {
    // null file path
    CustomImage imageToLoad = getCustomImage();
    resetLog();
    MockView view = new MockView(viewLog);
    ImageProcessor model = new MockImageProcessorModel(modelLog);
    IImageHelperFactory helperFactory = new MockImageHelperFactory(false,
            helperLog, imageToLoad);
    CommandController controller = new ImageGUICommandController(model, view, helperFactory);
    controller.process();

    view.trigger("load", null);
    String[] viewLogData = viewLog.toString().split("\n");
    assertEquals("Echo: Invalid operation.",
            viewLogData[viewLogData.length - 2]);
    assertEquals("Message type: Error", viewLogData[viewLogData.length - 1]);

    // Load command with extra arguments
    resetLog();
    view.trigger("LOAD", new String[]{"panda.jpg", "panda1.ppm"});
    viewLogData = viewLog.toString().split("\n");
    assertEquals("Echo: Invalid command format",
            viewLogData[viewLogData.length - 2]);
    assertEquals("Message type: Error", viewLogData[viewLogData.length - 1]);
  }

  @Test
  public void testCallBackOnLoadCommandWithInvalidFilePath() {
    CustomImage imageToLoad = getCustomImage();
    resetLog();
    MockView view = new MockView(viewLog);
    ImageProcessor model = new MockImageProcessorModel(modelLog);
    IImageHelperFactory helperFactory = new MockImageHelperFactory(true,
            helperLog, imageToLoad);
    CommandController controller = new ImageGUICommandController(model, view, helperFactory);
    controller.process();

    view.trigger("load", new String[]{"test/panda.jpg"});
    String[] viewLogData = viewLog.toString().split("\n");
    assertEquals("Echo: Failed to load the image at filepath: test/panda.jpg",
            viewLogData[viewLogData.length - 2]);
    assertEquals("Message type: Error", viewLogData[viewLogData.length - 1]);
  }

  @Test
  public void testViewCallBackOnBrightenCommandWithValidParameters() {
    CustomImage imageToLoad = getCustomImage();
    resetLog();
    MockView view = new MockView(viewLog);
    ImageProcessor model = new MockImageProcessorModel(modelLog);
    IImageHelperFactory helperFactory = new MockImageHelperFactory(false,
            helperLog, imageToLoad);
    CommandController controller = new ImageGUICommandController(model, view, helperFactory);
    controller.process();

    view.trigger("brighten", new String[]{"15"});
    assertEquals("Brighten: base, base, 15", modelLog.toString());
    String[] viewLogData = viewLog.toString().split("\n");
    assertEquals("Update UI: base", viewLogData[viewLogData.length - 1]);

    resetLog();
    view.trigger("Brighten", new String[]{"-15"});
    assertEquals("Brighten: base, base, -15", modelLog.toString());
    viewLogData = viewLog.toString().split("\n");
    assertEquals("Update UI: base", viewLogData[viewLogData.length - 1]);

    resetLog();
    view.trigger("BRIGHTEN", new String[]{"2"});
    assertEquals("Brighten: base, base, 2", modelLog.toString());
    viewLogData = viewLog.toString().split("\n");
    assertEquals("Update UI: base", viewLogData[viewLogData.length - 1]);
  }

  @Test
  public void testCallBackOnBrightenCommandWithInvalidParameters() {
    // null parameters
    CustomImage imageToLoad = getCustomImage();
    resetLog();
    MockView view = new MockView(viewLog);
    ImageProcessor model = new MockImageProcessorModel(modelLog);
    IImageHelperFactory helperFactory = new MockImageHelperFactory(false,
            helperLog, imageToLoad);
    CommandController controller = new ImageGUICommandController(model, view, helperFactory);
    controller.process();

    view.trigger("brighten", null);
    String[] viewLogData = viewLog.toString().split("\n");
    assertEquals("Echo: Invalid operation.",
            viewLogData[viewLogData.length - 2]);
    assertEquals("Message type: Error", viewLogData[viewLogData.length - 1]);

    // Brighten command with extra arguments
    resetLog();
    view.trigger("Brighten", new String[]{"10", "-15"});
    viewLogData = viewLog.toString().split("\n");
    assertEquals("Echo: Invalid operation.",
            viewLogData[viewLogData.length - 2]);
    assertEquals("Message type: Error", viewLogData[viewLogData.length - 1]);

    // Brighten command with incorrect arguments
    resetLog();
    view.trigger("brighten", new String[]{"test"});
    viewLogData = viewLog.toString().split("\n");
    assertEquals("Echo: Invalid value to brighten by. Please enter an integer value.",
            viewLogData[viewLogData.length - 2]);
    assertEquals("Message type: Error", viewLogData[viewLogData.length - 1]);

    resetLog();
    view.trigger("brighten", new String[]{"20.4"});
    viewLogData = viewLog.toString().split("\n");
    assertEquals("Echo: Invalid value to brighten by. Please enter an integer value.",
            viewLogData[viewLogData.length - 2]);
    assertEquals("Message type: Error", viewLogData[viewLogData.length - 1]);
  }

  @Test
  public void testViewCallBackOnFlipCommandWithValidParameters() {
    CustomImage imageToLoad = getCustomImage();
    resetLog();
    MockView view = new MockView(viewLog);
    ImageProcessor model = new MockImageProcessorModel(modelLog);
    IImageHelperFactory helperFactory = new MockImageHelperFactory(false,
            helperLog, imageToLoad);
    CommandController controller = new ImageGUICommandController(model, view, helperFactory);
    controller.process();

    view.trigger("Flip", new String[]{"vertical-flip"});
    assertEquals("Flip: base, base, 1", modelLog.toString());
    String[] viewLogData = viewLog.toString().split("\n");
    assertEquals("Update UI: base", viewLogData[viewLogData.length - 1]);

    resetLog();
    view.trigger("flip", new String[]{"horizontal-flip"});
    assertEquals("Flip: base, base, 0", modelLog.toString());
    viewLogData = viewLog.toString().split("\n");
    assertEquals("Update UI: base", viewLogData[viewLogData.length - 1]);

    resetLog();
    view.trigger("FLIP", new String[]{"vertical-flip"});
    assertEquals("Flip: base, base, 1", modelLog.toString());
    viewLogData = viewLog.toString().split("\n");
    assertEquals("Update UI: base", viewLogData[viewLogData.length - 1]);
  }

  @Test
  public void testCallBackOnFlipCommandWithInvalidParameters() {
    // FLip command with null parameters
    CustomImage imageToLoad = getCustomImage();
    resetLog();
    MockView view = new MockView(viewLog);
    ImageProcessor model = new MockImageProcessorModel(modelLog);
    IImageHelperFactory helperFactory = new MockImageHelperFactory(false,
            helperLog, imageToLoad);
    CommandController controller = new ImageGUICommandController(model, view, helperFactory);
    controller.process();

    view.trigger("FLIP", null);
    String[] viewLogData = viewLog.toString().split("\n");
    assertEquals("Echo: Invalid operation.",
            viewLogData[viewLogData.length - 2]);
    assertEquals("Message type: Error", viewLogData[viewLogData.length - 1]);

    // Flip command with extra arguments
    resetLog();
    view.trigger("flip", new String[]{"vertical-flip", "horizontal-flip"});
    viewLogData = viewLog.toString().split("\n");
    assertEquals("Echo: Invalid operation.",
            viewLogData[viewLogData.length - 2]);
    assertEquals("Message type: Error", viewLogData[viewLogData.length - 1]);

    // Flip command with incorrect arguments
    resetLog();
    view.trigger("Flip", new String[]{"vertical"});
    viewLogData = viewLog.toString().split("\n");
    assertEquals("Echo: Invalid command",
            viewLogData[viewLogData.length - 2]);
    assertEquals("Message type: Error", viewLogData[viewLogData.length - 1]);
  }

  @Test
  public void testViewCallBackOnCombineCommandWithValidParameters() {
    CustomImage imageToLoad = getCustomImage();
    resetLog();
    MockView view = new MockView(viewLog);
    ImageProcessor model = new MockImageProcessorModel(modelLog);
    IImageHelperFactory helperFactory = new MockImageHelperFactory(false,
            helperLog, imageToLoad);
    CommandController controller = new ImageGUICommandController(model, view, helperFactory);
    controller.process();

    String expected =
            "Load: " + getCustomImage() + "Load: " + getCustomImage() + "Load: " + getCustomImage()
                    + "Combine: base-red, base-green, base-blue, base";

    view.trigger("Combine", new String[]{"file1.jpg", "file2.jpg", "file3.jpg"});
    assertEquals(expected, modelLog.toString());
    String[] viewLogData = viewLog.toString().split("\n");
    assertEquals("Update UI: base", viewLogData[viewLogData.length - 1]);

    resetLog();
    view.trigger("combine", new String[]{"file1.jpg", "file2.jpg", "file3.jpg"});
    assertEquals(expected, modelLog.toString());
    viewLogData = viewLog.toString().split("\n");
    assertEquals("Update UI: base", viewLogData[viewLogData.length - 1]);

    resetLog();
    view.trigger("COMBINE", new String[]{"file1.jpg", "file2.jpg", "file3.jpg"});
    assertEquals(expected, modelLog.toString());
    viewLogData = viewLog.toString().split("\n");
    assertEquals("Update UI: base", viewLogData[viewLogData.length - 1]);
  }

  @Test
  public void testCallBackOnCombineCommandWithInvalidParameters() {
    // Combine command with null parameters
    CustomImage imageToLoad = getCustomImage();
    resetLog();
    MockView view = new MockView(viewLog);
    ImageProcessor model = new MockImageProcessorModel(modelLog);
    IImageHelperFactory helperFactory = new MockImageHelperFactory(false,
            helperLog, imageToLoad);
    CommandController controller = new ImageGUICommandController(model, view, helperFactory);
    controller.process();

    view.trigger("combine", null);
    String[] viewLogData = viewLog.toString().split("\n");
    assertEquals("Echo: Invalid operation.",
            viewLogData[viewLogData.length - 2]);
    assertEquals("Message type: Error", viewLogData[viewLogData.length - 1]);

    // Combine command with extra arguments
    resetLog();
    view.trigger("Combine", new String[]{"file1.png", "file2.png", "file3.bmp", "file4.ppm"});
    viewLogData = viewLog.toString().split("\n");
    assertEquals("Echo: Invalid number of files provided for the combine operation. "
                    + "Please select three files.",
            viewLogData[viewLogData.length - 2]);
    assertEquals("Message type: Error", viewLogData[viewLogData.length - 1]);

    // Combine command with incorrect arguments
    resetLog();
    view.trigger("Combine", new String[]{"file1.png", "file2.png"});
    viewLogData = viewLog.toString().split("\n");
    assertEquals("Echo: Invalid number of files provided for the combine operation. "
                    + "Please select three files.",
            viewLogData[viewLogData.length - 2]);
    assertEquals("Message type: Error", viewLogData[viewLogData.length - 1]);
  }

  @Test
  public void testViewCallBackOnGreyscaleCommandWithValidParameters() {
    CustomImage imageToLoad = getCustomImage();
    resetLog();
    MockView view = new MockView(viewLog);
    ImageProcessor model = new MockImageProcessorModel(modelLog);
    IImageHelperFactory helperFactory = new MockImageHelperFactory(false,
            helperLog, imageToLoad);
    CommandController controller = new ImageGUICommandController(model, view, helperFactory);
    controller.process();

    // channel
    view.trigger("greyscale", new String[]{"red-component"});
    assertEquals("Visualise Channel: base, base, 2", modelLog.toString());
    String[] viewLogData = viewLog.toString().split("\n");
    assertEquals("Update UI: base", viewLogData[viewLogData.length - 1]);

    resetLog();
    view.trigger("Greyscale", new String[]{"green-component"});
    assertEquals("Visualise Channel: base, base, 1", modelLog.toString());
    viewLogData = viewLog.toString().split("\n");
    assertEquals("Update UI: base", viewLogData[viewLogData.length - 1]);

    resetLog();
    view.trigger("Greyscale", new String[]{"BLUE-component"});
    assertEquals("Visualise Channel: base, base, 0", modelLog.toString());
    viewLogData = viewLog.toString().split("\n");
    assertEquals("Update UI: base", viewLogData[viewLogData.length - 1]);

    resetLog();
    view.trigger("Greyscale", new String[]{"BLUE-COMPONENT"});
    assertEquals("Visualise Channel: base, base, 0", modelLog.toString());
    viewLogData = viewLog.toString().split("\n");
    assertEquals("Update UI: base", viewLogData[viewLogData.length - 1]);


    // luma
    resetLog();
    view.trigger("GREYSCALE", new String[]{"luma-component"});
    assertEquals("Visualise Luma: base, base", modelLog.toString());
    viewLogData = viewLog.toString().split("\n");
    assertEquals("Update UI: base", viewLogData[viewLogData.length - 1]);

    resetLog();
    view.trigger("GREYSCALE", new String[]{"Luma-COMPONENT"});
    assertEquals("Visualise Luma: base, base", modelLog.toString());
    viewLogData = viewLog.toString().split("\n");
    assertEquals("Update UI: base", viewLogData[viewLogData.length - 1]);

    // value
    resetLog();
    view.trigger("greyscale", new String[]{"value-Component"});
    assertEquals("Visualise Value: base, base", modelLog.toString());
    viewLogData = viewLog.toString().split("\n");
    assertEquals("Update UI: base", viewLogData[viewLogData.length - 1]);

    // intensity
    resetLog();
    view.trigger("Greyscale", new String[]{"Intensity-component"});
    assertEquals("Visualise Intensity: base, base", modelLog.toString());
    viewLogData = viewLog.toString().split("\n");
    assertEquals("Update UI: base", viewLogData[viewLogData.length - 1]);

    resetLog();
    view.trigger("Greyscale", new String[]{"intensity-component"});
    assertEquals("Visualise Intensity: base, base", modelLog.toString());
    viewLogData = viewLog.toString().split("\n");
    assertEquals("Update UI: base", viewLogData[viewLogData.length - 1]);
  }

  @Test
  public void testCallBackOnGreyscaleCommandWithInvalidParameters() {
    // Greyscale command with null parameters
    CustomImage imageToLoad = getCustomImage();
    resetLog();
    MockView view = new MockView(viewLog);
    ImageProcessor model = new MockImageProcessorModel(modelLog);
    IImageHelperFactory helperFactory = new MockImageHelperFactory(false,
            helperLog, imageToLoad);
    CommandController controller = new ImageGUICommandController(model, view, helperFactory);
    controller.process();

    view.trigger("greyscale", null);
    String[] viewLogData = viewLog.toString().split("\n");
    assertEquals("Echo: Invalid operation.",
            viewLogData[viewLogData.length - 2]);
    assertEquals("Message type: Error", viewLogData[viewLogData.length - 1]);

    resetLog();
    view.trigger("GreyScale", null);
    viewLogData = viewLog.toString().split("\n");
    assertEquals("Echo: Invalid operation.",
            viewLogData[viewLogData.length - 2]);
    assertEquals("Message type: Error", viewLogData[viewLogData.length - 1]);

    resetLog();
    view.trigger("GREYSCALE", new String[]{""});
    viewLogData = viewLog.toString().split("\n");
    assertEquals("Echo: Invalid command",
            viewLogData[viewLogData.length - 2]);
    assertEquals("Message type: Error", viewLogData[viewLogData.length - 1]);

    // Greyscale command with extra arguments
    resetLog();
    view.trigger("greyscale", new String[]{"red-component", "green-component"});
    viewLogData = viewLog.toString().split("\n");
    assertEquals("Echo: Invalid operation.",
            viewLogData[viewLogData.length - 2]);
    assertEquals("Message type: Error", viewLogData[viewLogData.length - 1]);

    resetLog();
    view.trigger("greyscale", new String[]{"Luma-component", "component"});
    viewLogData = viewLog.toString().split("\n");
    assertEquals("Echo: Invalid operation.",
            viewLogData[viewLogData.length - 2]);
    assertEquals("Message type: Error", viewLogData[viewLogData.length - 1]);

    resetLog();
    view.trigger("greyscale", new String[]{"Luma-component", "value - component"});
    viewLogData = viewLog.toString().split("\n");
    assertEquals("Echo: Invalid operation.",
            viewLogData[viewLogData.length - 2]);
    assertEquals("Message type: Error", viewLogData[viewLogData.length - 1]);

    resetLog();
    view.trigger("greyscale", new String[]{"Intensity-Component", ""});
    viewLogData = viewLog.toString().split("\n");
    assertEquals("Echo: Invalid operation.",
            viewLogData[viewLogData.length - 2]);
    assertEquals("Message type: Error", viewLogData[viewLogData.length - 1]);

    // Greyscale command with incorrect arguments
    resetLog();
    view.trigger("Greyscale", new String[]{"rgb-component"});
    viewLogData = viewLog.toString().split("\n");
    assertEquals("Echo: Invalid command",
            viewLogData[viewLogData.length - 2]);
    assertEquals("Message type: Error", viewLogData[viewLogData.length - 1]);

    resetLog();
    view.trigger("Greyscale", new String[]{"yellow-component"});
    viewLogData = viewLog.toString().split("\n");
    assertEquals("Echo: Invalid command",
            viewLogData[viewLogData.length - 2]);
    assertEquals("Message type: Error", viewLogData[viewLogData.length - 1]);

    resetLog();
    view.trigger("greyscale", new String[]{"Luma"});
    viewLogData = viewLog.toString().split("\n");
    assertEquals("Echo: Invalid command",
            viewLogData[viewLogData.length - 2]);
    assertEquals("Message type: Error", viewLogData[viewLogData.length - 1]);

    resetLog();
    view.trigger("greyscale", new String[]{"VaLue"});
    viewLogData = viewLog.toString().split("\n");
    assertEquals("Echo: Invalid command",
            viewLogData[viewLogData.length - 2]);
    assertEquals("Message type: Error", viewLogData[viewLogData.length - 1]);

    resetLog();
    view.trigger("greyscale", new String[]{"intensity"});
    viewLogData = viewLog.toString().split("\n");
    assertEquals("Echo: Invalid command",
            viewLogData[viewLogData.length - 2]);
    assertEquals("Message type: Error", viewLogData[viewLogData.length - 1]);
  }

  @Test
  public void testViewCallBackOnSepiaCommand() {
    CustomImage imageToLoad = getCustomImage();
    resetLog();
    MockView view = new MockView(viewLog);
    ImageProcessor model = new MockImageProcessorModel(modelLog);
    IImageHelperFactory helperFactory = new MockImageHelperFactory(false,
            helperLog, imageToLoad);
    CommandController controller = new ImageGUICommandController(model, view, helperFactory);
    controller.process();

    view.trigger("Sepia", new String[]{});
    assertEquals("Sepia: base, base", modelLog.toString());
    String[] viewLogData = viewLog.toString().split("\n");
    assertEquals("Update UI: base", viewLogData[viewLogData.length - 1]);

    resetLog();
    view.trigger("sepia", null);
    assertEquals("Sepia: base, base", modelLog.toString());
    viewLogData = viewLog.toString().split("\n");
    assertEquals("Update UI: base", viewLogData[viewLogData.length - 1]);

    resetLog();
    view.trigger("SEPIA", new String[]{});
    assertEquals("Sepia: base, base", modelLog.toString());
    viewLogData = viewLog.toString().split("\n");
    assertEquals("Update UI: base", viewLogData[viewLogData.length - 1]);
  }

  @Test
  public void testViewCallBackOnDitherCommand() {
    CustomImage imageToLoad = getCustomImage();
    resetLog();
    MockView view = new MockView(viewLog);
    ImageProcessor model = new MockImageProcessorModel(modelLog);
    IImageHelperFactory helperFactory = new MockImageHelperFactory(false,
            helperLog, imageToLoad);
    CommandController controller = new ImageGUICommandController(model, view, helperFactory);
    controller.process();

    view.trigger("dither", new String[]{});
    assertEquals("Dither: base, base", modelLog.toString());
    String[] viewLogData = viewLog.toString().split("\n");
    assertEquals("Update UI: base", viewLogData[viewLogData.length - 1]);

    resetLog();
    view.trigger("Dither", null);
    assertEquals("Dither: base, base", modelLog.toString());
    viewLogData = viewLog.toString().split("\n");
    assertEquals("Update UI: base", viewLogData[viewLogData.length - 1]);

    resetLog();
    view.trigger("DITHER", new String[]{});
    assertEquals("Dither: base, base", modelLog.toString());
    viewLogData = viewLog.toString().split("\n");
    assertEquals("Update UI: base", viewLogData[viewLogData.length - 1]);
  }

  @Test
  public void testViewCallBackOnSharpenCommand() {
    CustomImage imageToLoad = getCustomImage();
    resetLog();
    MockView view = new MockView(viewLog);
    ImageProcessor model = new MockImageProcessorModel(modelLog);
    IImageHelperFactory helperFactory = new MockImageHelperFactory(false,
            helperLog, imageToLoad);
    CommandController controller = new ImageGUICommandController(model, view, helperFactory);
    controller.process();

    view.trigger("sharpen", new String[]{});
    assertEquals("Sharpen: base, base", modelLog.toString());
    String[] viewLogData = viewLog.toString().split("\n");
    assertEquals("Update UI: base", viewLogData[viewLogData.length - 1]);

    resetLog();
    view.trigger("Sharpen", null);
    assertEquals("Sharpen: base, base", modelLog.toString());
    viewLogData = viewLog.toString().split("\n");
    assertEquals("Update UI: base", viewLogData[viewLogData.length - 1]);

    resetLog();
    view.trigger("SHARPEN", new String[]{});
    assertEquals("Sharpen: base, base", modelLog.toString());
    viewLogData = viewLog.toString().split("\n");
    assertEquals("Update UI: base", viewLogData[viewLogData.length - 1]);
  }

  @Test
  public void testViewCallBackOnBlurCommand() {
    CustomImage imageToLoad = getCustomImage();
    resetLog();
    MockView view = new MockView(viewLog);
    ImageProcessor model = new MockImageProcessorModel(modelLog);
    IImageHelperFactory helperFactory = new MockImageHelperFactory(false,
            helperLog, imageToLoad);
    CommandController controller = new ImageGUICommandController(model, view, helperFactory);
    controller.process();

    view.trigger("blur", new String[]{});
    assertEquals("Blur: base, base", modelLog.toString());
    String[] viewLogData = viewLog.toString().split("\n");
    assertEquals("Update UI: base", viewLogData[viewLogData.length - 1]);

    resetLog();
    view.trigger("Blur", null);
    assertEquals("Blur: base, base", modelLog.toString());
    viewLogData = viewLog.toString().split("\n");
    assertEquals("Update UI: base", viewLogData[viewLogData.length - 1]);

    resetLog();
    view.trigger("BLUR", new String[]{});
    assertEquals("Blur: base, base", modelLog.toString());
    viewLogData = viewLog.toString().split("\n");
    assertEquals("Update UI: base", viewLogData[viewLogData.length - 1]);
  }

  @Test
  public void testViewCallBackOnSplitCommandWithValidParameters() {
    CustomImage imageToLoad = getCustomImage();
    resetLog();
    MockView view = new MockView(viewLog);
    ImageProcessor model = new MockImageProcessorModel(modelLog);
    IImageHelperFactory helperFactory = new MockImageHelperFactory(false,
            helperLog, imageToLoad);
    CommandController controller = new ImageGUICommandController(model, view, helperFactory);
    controller.process();

    String expectedHelperLogString =
            "Save: " + getCustomImage() + ", file1.jpg" +  "Save: " + getCustomImage()
                    + ", file2.png" +  "Save: " + getCustomImage() + ", file3.bmp";

    view.trigger("split", new String[]{"file1.jpg", "file2.png", "file3.bmp"});
    assertEquals("Split: base, file1, file2, file3", modelLog.toString());
    assertEquals(expectedHelperLogString, helperLog.toString());
    String[] viewLogData = viewLog.toString().split("\n");
    assertEquals("Echo: Split greyscale images saved successfully.",
            viewLogData[viewLogData.length - 2]);
    assertEquals("Message type: Success",
            viewLogData[viewLogData.length - 1]);

    resetLog();
    view.trigger("SplIt", new String[]{"file1.jpg", "file2.png", "file3.bmp"});
    assertEquals("Split: base, file1, file2, file3", modelLog.toString());
    assertEquals(expectedHelperLogString, helperLog.toString());
    viewLogData = viewLog.toString().split("\n");
    assertEquals("Echo: Split greyscale images saved successfully.",
            viewLogData[viewLogData.length - 2]);
    assertEquals("Message type: Success",
            viewLogData[viewLogData.length - 1]);

    resetLog();
    view.trigger("SPLIT", new String[]{"file1.jpg", "file2.png", "file3.bmp"});
    assertEquals("Split: base, file1, file2, file3", modelLog.toString());
    assertEquals(expectedHelperLogString, helperLog.toString());
    viewLogData = viewLog.toString().split("\n");
    assertEquals("Echo: Split greyscale images saved successfully.",
            viewLogData[viewLogData.length - 2]);
    assertEquals("Message type: Success",
            viewLogData[viewLogData.length - 1]);
  }

  @Test
  public void testCallBackOnSplitCommandWithInvalidParameters() {
    // Split command with null parameters
    CustomImage imageToLoad = getCustomImage();
    resetLog();
    MockView view = new MockView(viewLog);
    ImageProcessor model = new MockImageProcessorModel(modelLog);
    IImageHelperFactory helperFactory = new MockImageHelperFactory(false,
            helperLog, imageToLoad);
    CommandController controller = new ImageGUICommandController(model, view, helperFactory);
    controller.process();

    view.trigger("split", null);
    String[] viewLogData = viewLog.toString().split("\n");
    assertEquals("Echo: Invalid operation.",
            viewLogData[viewLogData.length - 2]);
    assertEquals("Message type: Error", viewLogData[viewLogData.length - 1]);

    // Split command with extra arguments
    resetLog();
    view.trigger("Split", new String[]{"file1.png", "file2.png", "file3.bmp", "file4.ppm"});
    viewLogData = viewLog.toString().split("\n");
    assertEquals("Echo: Invalid number of files provided to save the result of the split "
                    + "operation. Please provide three filenames.",
            viewLogData[viewLogData.length - 2]);
    assertEquals("Message type: Error", viewLogData[viewLogData.length - 1]);

    // Split command with incorrect arguments
    resetLog();
    view.trigger("sPLIT", new String[]{"file1.png", "file2.png"});
    viewLogData = viewLog.toString().split("\n");
    assertEquals("Echo: Invalid number of files provided to save the result of the split "
                    + "operation. Please provide three filenames.",
            viewLogData[viewLogData.length - 2]);
    assertEquals("Message type: Error", viewLogData[viewLogData.length - 1]);

    // Split command without file extensions
    resetLog();
    view.trigger("split", new String[]{"file1.png", "file2.png", "file3"});
    viewLogData = viewLog.toString().split("\n");
    assertEquals("Echo: File extension is necessary.",
            viewLogData[viewLogData.length - 2]);
    assertEquals("Message type: Error", viewLogData[viewLogData.length - 1]);

    resetLog();
    view.trigger("split", new String[]{"", "file2.png", "file3.jpg"});
    viewLogData = viewLog.toString().split("\n");
    assertEquals("Echo: File extension is necessary.",
            viewLogData[viewLogData.length - 2]);
    assertEquals("Message type: Error", viewLogData[viewLogData.length - 1]);
  }

  @Test
  public void testViewCallBackOnSaveCommandWithValidParameters() {
    CustomImage imageToLoad = getCustomImage();
    resetLog();
    MockView view = new MockView(viewLog);
    ImageProcessor model = new MockImageProcessorModel(modelLog);
    IImageHelperFactory helperFactory = new MockImageHelperFactory(false,
            helperLog, imageToLoad);
    CommandController controller = new ImageGUICommandController(model, view, helperFactory);
    controller.process();

    view.trigger("save", new String[]{"res/panda.jpg"});
    assertEquals("Save: " + getCustomImage() + ", res/panda.jpg", helperLog.toString());
    String[] viewLogData = viewLog.toString().split("\n");
    assertEquals("Echo: Image saved successfully.", viewLogData[viewLogData.length - 2]);
    assertEquals("Message type: Success", viewLogData[viewLogData.length - 1]);

    resetLog();
    view.trigger("SAVE", new String[]{"res/panda.jpg"});
    assertEquals("Save: " + getCustomImage() + ", res/panda.jpg", helperLog.toString());
    viewLogData = viewLog.toString().split("\n");
    assertEquals("Echo: Image saved successfully.", viewLogData[viewLogData.length - 2]);
    assertEquals("Message type: Success", viewLogData[viewLogData.length - 1]);

    resetLog();
    view.trigger("Save", new String[]{"panda.ppm"});
    assertEquals("Save: " + getCustomImage() + ", panda.ppm", helperLog.toString());
    viewLogData = viewLog.toString().split("\n");
    assertEquals("Echo: Image saved successfully.", viewLogData[viewLogData.length - 2]);
    assertEquals("Message type: Success", viewLogData[viewLogData.length - 1]);
  }

  @Test
  public void testCallBackOnSaveCommandWithInvalidParameters() {
    // null file path
    CustomImage imageToLoad = getCustomImage();
    resetLog();
    MockView view = new MockView(viewLog);
    ImageProcessor model = new MockImageProcessorModel(modelLog);
    IImageHelperFactory helperFactory = new MockImageHelperFactory(false,
            helperLog, imageToLoad);
    CommandController controller = new ImageGUICommandController(model, view, helperFactory);
    controller.process();

    view.trigger("save", null);
    String[] viewLogData = viewLog.toString().split("\n");
    assertEquals("Echo: Invalid operation.",
            viewLogData[viewLogData.length - 2]);
    assertEquals("Message type: Error", viewLogData[viewLogData.length - 1]);

    // Load command with extra arguments
    resetLog();
    view.trigger("save", new String[]{"panda.jpg", "panda1.ppm"});
    viewLogData = viewLog.toString().split("\n");
    assertEquals("Echo: Invalid command format",
            viewLogData[viewLogData.length - 2]);
    assertEquals("Message type: Error", viewLogData[viewLogData.length - 1]);
  }

  @Test
  public void testCallBackOnSaveCommandWithInvalidFilePath() {
    CustomImage imageToLoad = getCustomImage();
    resetLog();
    MockView view = new MockView(viewLog);
    ImageProcessor model = new MockImageProcessorModel(modelLog);
    IImageHelperFactory helperFactory = new MockImageHelperFactory(true,
            helperLog, imageToLoad);
    CommandController controller = new ImageGUICommandController(model, view, helperFactory);
    controller.process();

    view.trigger("save", new String[]{"test/panda.jpg"});
    String[] viewLogData = viewLog.toString().split("\n");
    assertEquals("Echo: Failed to save the image at filepath: test/panda.jpg",
            viewLogData[viewLogData.length - 2]);
    assertEquals("Message type: Error", viewLogData[viewLogData.length - 1]);
  }
}