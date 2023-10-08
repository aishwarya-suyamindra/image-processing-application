import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import javax.swing.UIManager;

import controller.CommandController;
import controller.ImageCommandController;
import controller.ImageGUICommandController;
import model.ImageProcessor;
import model.ImageProcessorImpl;
import model.ImageProcessorRO;
import model.ImageProcessorROImpl;
import utility.ImageHelperFactory;
import view.GraphicalView;
import view.IGraphicalView;
import view.IView;
import view.View;

/**
 * This is main class. This class represents the entry point of the program.
 */
public class Main {
  /**
   * It creates a model, view and controller object,
   * then passes the control to the controller.
   */
  public static void main(String[] args) {
    // Create the model object and view object to pass to the controller
    // If a script file is passed via command line arguments, pass the script file as the source
    // of the commands.
    ImageProcessor model = new ImageProcessorImpl();
    IView view = new View(System.out);
    if (args.length > 0) {
      if (args[0].equals("-file") && args.length > 1) {
        try {
          handOver(model, new FileInputStream(args[1]), view);
        } catch (FileNotFoundException e) {
          System.out.println("Invalid file");
        }
        System.exit(0);
      } else if (args[0].equals("-text")) {
        // open in interactive text mode
        handOver(model, System.in, view);
      } else {
        System.out.println("Invalid arguments");
      }
    }

    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (Exception ex) {
      System.out.println("Failed to set look and feel");
    }
    // display the graphical view to the user
    ImageProcessorRO roModel = new ImageProcessorROImpl(model);
    IGraphicalView graphicalView = new GraphicalView("Image Processing Application",
            roModel);
    CommandController controller = new ImageGUICommandController(model, graphicalView,
            ImageHelperFactory.getInstance());
    controller.process();
  }

  private static void handOver(ImageProcessor model, InputStream in, IView view) {
    CommandController controller = new ImageCommandController(model, in, view,
            ImageHelperFactory.getInstance());
    controller.process();
  }
}