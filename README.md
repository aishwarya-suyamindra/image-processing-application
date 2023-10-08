# Assignment6

The program is modelled on MVC design - The controller contains the functionality to parse the user input and take the corresponding action. The model contains the actual functionality associated with the operations supported by the program. The view contains the functionality to display data to the user. As each component performs specific operations, these components are independent. A single model can have be used for different types of views. 

In our design, the controller ImageCommandController is the first to process the input provided. It maintains a map of the supported operations. The controller processes the command by tokenising the input and takes the appropriate action. It takes in an object of the view and the model to work with, so it acts as a link between the view and the model. It calls the appropriate methods on the model and instructs the view on when to present information to the user. 
The model, ImageProcessorImpl contains only the application logic. It does not provide or maintain any information regarding the disply of the data to the user. It represents a set of operations that can be performed on an image. It is not coupled to the controller or the view, so it has no information about how its being used. 
The view View contains the presentation information. It is not coupled to the controller or the model. 
In this way, the MVC design is used to group the logical units of the program together. 

End to end flow:
- The user input is received by the controller, the command is identified and the rest of the command is given as an input to create an object of the respective command class. 
- The appropriate method on the model is called by the command class. 
- Once the action is performed by the model, the controller tells the view to display the appropriate message.

Package controller:
ImageCommandController is the controller which parses and supports a given set of commands. The command design pattern is used to encapsulate the data required for performing a command in one object. A new class is maintained for each command, which has the responsibility of executing this command, given the data.
Using the command design patten makes it easier to extend support for new operations and requires minimal changes in the controller. It would require adding a new command class that executes the command and adding this new command to the map of supported operations in the controller. 
The controller also takes as input an instance of IImageHelperFactory. The factory design pattern is used to be able to support reading/ saving of images of different extension types. The image helper factory gives the appropriate image helper class based on the image extension being worked on. Using the factory design pattern makes it easier to extend support to multiple image extensions, that might have their own logic and dependencies to load/save data, without having to make any changes in the controller or the model. 
The files:
  1. CommandController.java - This interface represents the operations that hand over the control to the controller. It specifies a method, which is to be called by the initiating code.
  2. ImageOperationCommand.java - This interface represents the set of operations to be executed by the various image manipulation commands.
  3. ImageCommandController.java - This is the controller class that implements the CommandController interface. It interprets the commands that are input and takes the appropriate action.

  Package commands
  1. Brighten.java - This class implements the ImageOperationCommand interface. It stores the information required to brighten an image by the given value.
  2. Combine.java - This class implements the ImageOperationCommand interface. It stores the information required to combine the given three greyscale images.
  3. Flip.java - This class implements the ImageOperationCommand interface. It stores the information required to flip an image along the given axis.
  4. Greyscale.java -  This class implements the ImageOperationCommand interface. It stores the information required to convert an image to a greyscale image.
  5. Load.java - This class implements the ImageOperationCommand interface. It stores the information required to load an image from the given file path and refer to it by the given name.
  6. Save.java - This class implements the ImageOperationCommand interface. It stores the information required to save a specified image to a specified file path.
  7. Split.java - This class implements the ImageOperationCommand interface. It stores the information required to split an image into three greyscale images.


DESIGN CHANGES/ MODIFICATIONS:
  To support the new commands (sepia, blur, sharpen, dither), 4 new command classes are added in package commands:
  1. Sepia.java - This class implements the ImageOperationCommand interface. It stores the information required to convert an image to a sepia-toned image.
  2. Blur.java - This class implements the ImageOperationCommand interface. It stores the information required to blur a given image.
  3. Sharpen.java - This class implements the ImageOperationCommand interface. It stores the information required to blur a given image.
  4. Dither.java - This class implements the ImageOperationCommand interface. It stores the information required to dither a given image.
  The new color transformation command (greyscale), which converts a given image to a greyscale image, uses the luma method to this. As this is an existing greyscale operation, a new constructor is added in the greyscale command class (Greyscale.java) to support this new color transformation command, which has only 3 arguments. This constructor sets the greyscale conversion method as luma-component by default. 

  The map of supported commands in the controller is updated to provide support to the new commands. The new commands, along with the appropriate command class are added to this map.
  As the existing model interface is updated with the new functionalities it offers, the controller continues to work with the same interface. No changes are made in the existing functionality offered by the interface, so there is no need to modify the type of the model that the controller or the command classes work with. This ensures backward compatibility, as the controller can still use only the old functionality of the model. It can also choose to use the new functionality provided by the model. 
  This approach ensures that there is minimal change in the controller. It would need the controller to be modified only to support new commands. Both the controller and the command continue to work with the same model. 

DESIGN CHANGES/ MODIFICATIONS (TO SUPPORT GUI):
  To support the new GUI, the following changes are made in package controller:
  1. ImageGUICommandController.java - A new controller is added in, which manages the new GUI and the model. It is the first to respond to the events raised on the view.
  2. AbstractCommandController.java  - To abstract the common fields and functionality between the two controllers that implement the CommandController interface, a new abstract class is added in. This abstract class contains the map of supported commands and the method to execute a command, given its parameters. The two controllers extend the AbstractCommandController.
  3. To support the abstraction of the map of supported commands that create the appropriate command, the lambda functions that create the command objects are modified to split on newline, rather than whitespace.
  4. ImageCommandController.java - The existing controller is refactored by abstracting common code and made to extend the AbstractCommandController. A minimal change is made to build the parameters using the newline character, rather than using whitespace.
 5. Features.java - A new interface is added in, which represents the callback operations that a graphical view uses to notify a delegate about its various events. 


This abstraction ensures that when a new command is to be added in, either to be supported only by the GUI or the console view, or to be supported by both, the command needs to be added to the map only in one place.
  
The ImageGUICommandController contains an inner class FeaturesImpl that implements the Features interface, which contains the functionality to respond to the events on the view. It contains a map to transform the commands sent from the view to the existing command structure. In the process method of the controller, this features object is registered as the delegate on the view and the view is made visible. The controller itself is not made to implement the Features interface in order to avoid the view having access to the controller. Implementing it as an inner class ensures that the view only has limited access to the methods provided in the features interface, rather than all of the public methods on the controller. 


Package model:
ImageProcessorImpl is the base model that performs all the operations on the images. It contains a hashmap of all the images that it is working with. The model works with images of type ICustomImage. 
  1. ImageProcessor.java - This interface defines the set of operations that can be performed on an image of type CustomImage.
  2. ImageProcessorImpl.java - This is the model class and it implements the ImageProcessor interface. The model represents the set of operations that can be performed on images.

DESIGN CHANGES/ MODIFICATIONS:
  To support the new operations that can be performed on images the existing interface ImageProcessor is modified. The new operations to be provided are added to the interface.
  The old methods provided by the interface are retained as is, their method name and signature remains the same. Adding these new methods to the existing interface to provide the new operations does not require modification of the existing methods in the concrete implementation. 
  It also ensures backward compatibility, as other classes using this interface can still continue to use the interface. New functionality can be provided without having to change the type of model objects the classes are working with to use the new operations. Depending on the requirement, these classes can choose to use or not use these new functionalities. 

  As new functionality is to be provided, the concrete implementation of the model interface is modified. The model class provides the implementation of these new operations. The changes made to existing implementation in the model is to abstract common code, based on feedback. 
  Files:
  1. ImageProcessor.java - new methods to perform sepia, dither, blur, sharpen are added to the interface.
  2. PixelOperation.java - a new interface is added, which represents simple operations that can be performed on pixels. This helps abstract code, as the operation to be performed on the pixel can be specified as a function and passed to a method that can execute this function.
  3. ImageProcessorImpl.java - 
  - Implementation of the required new methods. 
  - The duplicate code is abstracted from the visualise and brighten methods in the ImageProcessorImpl class. Protected helper methods are added for this purpose. All helper methods are marked protected so that it can be accessible by classes that extend the ImageProcessorImpl class in the future.

  As the new operations are added to the existing interface, currently the application offers all the features. 
  If there is a need to support incremental versions, or make a few operations available only to a few editions in the future, this can be done by creating a new interface, which extends the existing interface. A new implementation would be created for this new interface and the required classes, such as the controller, would have to be modified to work with the new interface and its implementation. 

DESIGN CHANGES/ MODIFICATIONS (TO SUPPORT GUI):
  1. ImageProcessorRO.java - A new interface is added, which contains the operations that only give read view of the model. 
  2. ImageProcessROImpl.java - A new class  is added, which implements the ImageProcessorRO interface. It implements the necessary operations by using the existing model of type ImageProcessor. It takes a ImageProcessor model in its constructor and delegates the execution of the functionality of the model. 
  3. ImageProcessor.java - The existing interface is refactored to move the read only operations and extend the ImageProcessorRO interface. 
No changes were made to the existing model ImageProcessorImpl to support this refactoring. The model remains as is.

Package utility:
The utility package contains classes that are referenced across different components of the program. 
  1. IImageHelper.java- This interface represents a set of operations that is to be supported by images of different extension types. Currently, we are working with ppm images, so there is 	one ppmImageHelper. To be able to work with images
  2. IImageHelperFactory.java - This interface represents a set of operations that is to be provided by a image helper factory.
  3. ImageHelperFactory.java - This class is the factory class and implements the IImageHelperFactory interface. It creates and returns the appropriate image helper class  based on the 	type of the image that is being processed.The singleton design pattern is used to maintain only a single instance of the ImageHelperFactory throughout the program. As the helper factory is only used to get the appropriate imageHelper instance based on the image extension, a single instance of it used. 
  4. PPMImageHelper.java - This class contains the helper methods required to load a PPM Image from a file and save a PPM Image to a file.
  5. Pixel.java - This class represents a pixel in an image. It is given by the red, blue, green colors and the alpha (opacity) values. The channels are represented as 8 bit integers and 	can take on values ranging from 0-255. It offers getters to fetch the properties of the pixel and also defines what it means for two pixels to be equal.
  6. CustomImage.java - This class represents the image that is loaded and is to be manipulated on. Each image is given by the name, a 2D array of pixels and its maximum pixel value. It offers getters to fetch the properties of the image like the width, height, maxPixelValue, name and its pixel values. It also defines what it means for two images to be equal.

DESIGN CHANGES/ MODIFICATIONS:
  To support working with images of different extension types, new helper methods are added.
  Files:
  1. BufferedImageHelper.java - This new class contains the methods required to load and save images of extension types supported by ImageIO. It implements the IImageHelper interface. 
  2. ImageHelperFactory.java - This class is modified to support the new image extensions, along with the existing support for ppm. The getHelper method is modified, to return the new BufferedImageHelper when the image extension is of type png, bmp, jpeg, jpg. 
  As the factory design pattern is used, support for images of different extensions can be added with minimal changes to the helper factory. 

DESIGN CHANGES/ MODIFICATIONS (TO SUPPORT GUI):
  1. ImageHelperFactory.java - minimal change to make the error message thrown as part of the exception more appropriate.
  2. ViewMessageType.java - a new enum is added, which represents the type of the message to be displayed on the view, so the view can then show it in an appropriate manner.
  3. CustomImage.java - Two new public methods and a private helper method are added to support the display of the histogram on the view. These method return information about pixel frequency.

Package view:
  Files:
  1. IView.java - This contains the view interface, which represents a set operations that is to be supported by the view.
  2. View.java - This class represents the view and implements the IView interface. 
No changes are made in the view package.

DESIGN CHANGES/ MODIFICATIONS (TO SUPPORT GUI):
  1. IGraphicalView.java - a new interface is added, which represents a set of operations to be supported by a graphical view. 
  2. GraphicalView.java - a new class is added, which represents the graphical view and implements the IGraphicalView interface. 
  3. HistogramPanel.java - a new class is added, which creates a custom panel to display the histogram of an image. This panel is used in the GraphicalView class.

The graphical view works with a view model of type ImageProcessorRO. When an action is performed, the view notifies the delegate object of the action and waits for its response to take further action. When the update method of the view is triggered, the view updates itself by fetching the appropriate image to display using the RO model and updates its histogram panel as well.


No changes are made to the existing view interface and view implementation.

Main.java:
  The main class creates the object of the model, the view and the controller. It gives the view and the model to the controller and gives the control to the controller to process the input.
DESIGN CHANGES/ MODIFICATIONS:
  The Main.java class is modified to support command line arguments to provide a script file to run.

DESIGN CHANGES/ MODIFICATIONS (TO SUPPORT GUI):
   The Main.java class is modified to support:
 1. new command line arguments ("-text"), to run the program in interactive text mode.
 2. Rendering the new graphical view on running the program in default mode.
 

The model contains the logic of the application and has been modelled using functional design to implement its operations. The model maintains the data and works with it. In this approach, adding new functionality to the model is easily supported. It can be done by creating a new interface that extends the existing interface, or by modifying the existing interface. Both these approaches ensure backward compatibility, so code that uses this model does is not affected. 

The program supports the execution of all the commands specified in the USEME file. All the required operations are implemented and supported end to end. 

CITATION FOR THE IMAGE:
The provided image is downloaded from pixabay. The image has been resized to meet the file size restrictions.
Image: https://pixabay.com/vectors/sea-turtle-floral-flowers-2952470/
License: https://pixabay.com/service/terms/
