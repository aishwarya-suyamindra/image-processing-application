package view;

import java.awt.Container;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.LayoutManager;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.BoxLayout;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.SwingConstants;
import javax.swing.JFileChooser;
import javax.swing.JTextField;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import controller.Features;
import model.ImageProcessorRO;
import utility.CustomImage;
import utility.Pixel;
import utility.ViewMessageType;

/**
 * This class represents the graphical user interface to perform image operations. It contains an
 * Image panel, an options panel and a custom Histogram JPanel.
 */
public class GraphicalView extends JFrame implements IGraphicalView {
  private final ImageProcessorRO model;
  private JPanel optionsPanel;
  private JScrollPane imagePane;
  private JPanel imagePanel;
  private JButton loadImage;
  private JLabel histogramLabel;
  private HistogramPanel histogramPanel;
  private final Map<String, JButton> options;

  private enum Commands {
    LOAD,
    BLUR,
    BRIGHTEN,
    COMBINE,
    DITHER,
    FLIP,
    GREYSCALE,
    SEPIA,
    SHARPEN,
    SPLIT,
    SAVE
  }

  /**
   * Constructs the graphical view with the given header and view model.
   *
   * @param header the header for the view
   * @param model  the view model to work with
   */
  public GraphicalView(String header, ImageProcessorRO model) {
    super(header);
    this.model = model;
    this.options = new HashMap<>();
    setSize(1000, 800);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    // setup homepage
    setup();
    this.setResizable(false);
    // to set the frame in the center of the screen
    this.setLocationRelativeTo(null);
  }

  // this method prepares the initial homepage of the GUI.
  private void setup() {

    // set the buttons on the view
    setupOptionsPanel();

    // set up the image panel
    setupImagePanel();

    //set up histogram panel
    setupHistogramPanel();

    // add the panels to the view
    Container contentPane = getContentPane();
    contentPane.add(optionsPanel, BorderLayout.LINE_START);
    contentPane.add(imagePane, BorderLayout.CENTER);
    contentPane.add(histogramPanel, BorderLayout.PAGE_END);
    // initially set options pane visibility as false
    optionsPanel.setVisible(false);
    contentPane.setFocusable(true);
  }

  // Helper method to set up histogram Panel
  private void setupHistogramPanel() {

    // initialize as HistogramPanel
    this.histogramPanel = new HistogramPanel();

    // set layout, background and border with title
    histogramPanel.setLayout(new BoxLayout(histogramPanel, BoxLayout.Y_AXIS));
    this.histogramPanel.setBackground(Color.white);
    histogramPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.darkGray, 1), "HISTOGRAM",
            TitledBorder.CENTER, TitledBorder.TOP));
    TitledBorder titledBorder = (TitledBorder) histogramPanel.getBorder();
    Font titleFont = new Font(titledBorder.getTitleFont().getName(), Font.BOLD, 12);
    titledBorder.setTitleFont(titleFont);

    // add a label
    histogramLabel = new JLabel("Displays the distribution of color components and "
            + "intensity of the image");
    histogramLabel.setFont(new Font(histogramLabel.getText(), Font.ITALIC, 15));
    histogramLabel.setForeground(Color.darkGray);
    histogramLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    histogramLabel.setAlignmentY(Component.CENTER_ALIGNMENT);

    // add label to panel
    histogramPanel.add(Box.createVerticalGlue());
    histogramPanel.add(histogramLabel);
    histogramPanel.add(Box.createVerticalGlue());

    // set size
    histogramPanel.setPreferredSize(new Dimension(600, 300));
  }

  // Helper method to set up image Panel
  private void setupImagePanel() {

    this.imagePanel = new JPanel();
    this.imagePanel.setBackground(new Color(233, 233, 233));
    imagePanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.darkGray, 1)));
    imagePanel.setLayout(new BorderLayout());

    // set up loadImage button with icon
    loadImage = new JButton();
    loadImage.setMaximumSize(new Dimension(10, 10));
    URL resource = getClass().getResource("/images/uploadIcon.png");
    Image img = loadResourceImage(resource);
    // set icon to button if resource image exists
    if (img != null) {
      loadImage.setIcon(new ImageIcon(img));
    }
    loadImage.setText("<html><center>Upload<br>image</center></html>");
    loadImage.setFocusPainted(false);
    loadImage.setBorderPainted(false);
    loadImage.setOpaque(true);
    loadImage.setFont(new Font(loadImage.getText(), Font.BOLD, 15));
    loadImage.setBackground(new Color(233, 233, 233));
    loadImage.setVerticalTextPosition(SwingConstants.BOTTOM);
    loadImage.setHorizontalTextPosition(SwingConstants.CENTER);
    loadImage.setAlignmentX(Component.CENTER_ALIGNMENT);
    loadImage.setAlignmentY(Component.CENTER_ALIGNMENT);

    JPanel centerPanel = new JPanel(new GridBagLayout());
    centerPanel.setBackground(new Color(233, 233, 233));
    centerPanel.add(loadImage, new GridBagConstraints());

    imagePanel.add(centerPanel, BorderLayout.CENTER);
    imagePane = new JScrollPane();
    imagePane.setViewportView(imagePanel);
    imagePane.setPreferredSize(new Dimension(300, 200));

  }

  // Helper method to set the option buttons on the view
  private void setupOptionsPanel() {
    this.optionsPanel = new JPanel();
    optionsPanel.setPreferredSize(new Dimension(170, getHeight()));
    optionsPanel.setBackground(Color.darkGray);
    optionsPanel.setLayout(new BoxLayout(optionsPanel, BoxLayout.Y_AXIS));
    optionsPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder()));
    JLabel label = new JLabel("MENU", SwingConstants.CENTER);
    label.setFont(new Font("Serif", Font.BOLD, 18));
    label.setForeground(Color.WHITE);
    label.setAlignmentX(Component.CENTER_ALIGNMENT);

    JButton loadButton = new JButton(Commands.LOAD.name());
    JButton saveButton = new JButton(Commands.SAVE.name());
    JButton flipButton = new JButton(Commands.FLIP.name());
    JButton splitButton = new JButton(Commands.SPLIT.name());
    JButton blurButton = new JButton(Commands.BLUR.name());
    JButton sharpenButton = new JButton(Commands.SHARPEN.name());
    JButton greyscaleButton = new JButton(Commands.GREYSCALE.name());
    JButton brightenButton = new JButton(Commands.BRIGHTEN.name());
    JButton ditherButton = new JButton(Commands.DITHER.name());
    JButton combineButton = new JButton(Commands.COMBINE.name());
    JButton sepiaButton = new JButton(Commands.SEPIA.name());

    // add the buttons to the panel
    options.put(Commands.LOAD.name(), loadButton);
    options.put(Commands.SAVE.name(), saveButton);
    options.put(Commands.FLIP.name(), flipButton);
    options.put(Commands.SPLIT.name(), splitButton);
    options.put(Commands.BLUR.name(), blurButton);
    options.put(Commands.SHARPEN.name(), sharpenButton);
    options.put(Commands.GREYSCALE.name(), greyscaleButton);
    options.put(Commands.BRIGHTEN.name(), brightenButton);
    options.put(Commands.DITHER.name(), ditherButton);
    options.put(Commands.COMBINE.name(), combineButton);
    options.put(Commands.SEPIA.name(), sepiaButton);
    optionsPanel.add(label);
    optionsPanel.add(Box.createVerticalStrut(12));
    for (JButton button : options.values()) {
      button.setAlignmentX(Component.CENTER_ALIGNMENT);
      button.setMaximumSize(new Dimension(150, button.getPreferredSize().height + 8));
      button.setBorderPainted(false);
      button.setOpaque(true);
      button.setFont(new Font("Helvetica", Font.BOLD, 10));
      button.setBackground(new Color(230, 230, 230));
      button.setFocusPainted(false);
      optionsPanel.add(button);
      optionsPanel.add(Box.createVerticalStrut(8));
    }
  }

  // register the given features
  @Override
  public void addFeatures(Features features) {
    // add the action listeners
    JButton loadButton = this.options.get(Commands.LOAD.name());
    loadButton.addActionListener(a -> {
      String filePath = getFilePath();
      if (filePath != null) {
        String[] additionalParameters = new String[]{filePath};
        features.execute(loadButton.getActionCommand(), additionalParameters);
      }
    });

    loadImage.addActionListener(a -> {
      String filePath = getFilePath();
      if (filePath != null) {
        String[] additionalParameters = new String[]{filePath};
        features.execute(loadButton.getActionCommand(), additionalParameters);
      }
    });

    JButton saveButton = this.options.get(Commands.SAVE.name());
    saveButton.addActionListener(a -> {
      JFileChooser fileChooser = new JFileChooser();
      int chosenValue = fileChooser.showSaveDialog(this);
      if (chosenValue == JFileChooser.APPROVE_OPTION) {
        String filePath = fileChooser.getSelectedFile().getPath();
        String[] additionalParameters = new String[]{filePath};
        features.execute(saveButton.getActionCommand(), additionalParameters);
      }
    });

    JButton blurButton = this.options.get(Commands.BLUR.name());
    blurButton.addActionListener(a -> {
      features.execute(blurButton.getActionCommand(), new String[]{});
    });

    JButton sharpenButton = this.options.get(Commands.SHARPEN.name());
    sharpenButton.addActionListener(a -> {
      features.execute(sharpenButton.getActionCommand(), new String[]{});
    });

    JButton ditherButton = this.options.get(Commands.DITHER.name());
    ditherButton.addActionListener(a -> {
      features.execute(ditherButton.getActionCommand(), new String[]{});
    });

    // brighten
    JButton brightenButton = this.options.get(Commands.BRIGHTEN.name());
    brightenButton.addActionListener(a -> {
      String increment = displayInputDialog("Brighten",
              "Enter the increment/decrement factor:");
      if (increment != null && increment.length() > 0) {
        features.execute(brightenButton.getActionCommand(), new String[]{increment});
      }
    });

    // on click of greyscale, display the greyscale options and execute the selected option
    JButton greyscale = this.options.get(Commands.GREYSCALE.name());
    greyscale.addActionListener(a -> {
      String[] options = new String[]{"red-component", "green-component", "blue-component",
          "intensity-component", "luma-component", "value-component"};
      String selectedOption = displayOptionsDialog("Greyscale", options);
      if (selectedOption != null) {
        features.execute(greyscale.getActionCommand(), new String[]{selectedOption});
      }
    });

    // combine
    JButton combineButton = this.options.get(Commands.COMBINE.name());
    combineButton.addActionListener(a -> {
      JFileChooser fileChooser = new JFileChooser();
      fileChooser.setMultiSelectionEnabled(true);
      int chosenValue = fileChooser.showOpenDialog(this);
      if (chosenValue == JFileChooser.APPROVE_OPTION) {
        File[] files = fileChooser.getSelectedFiles();
        String[] parameters = new String[files.length];
        for (int i = 0; i < files.length; i++) {
          parameters[i] = files[i].toString();
        }
        features.execute(combineButton.getActionCommand(), parameters);
      }
    });

    // on click of color transformation, display the color transform options and execute the
    // selected option
    JButton sepiaButton = this.options.get(Commands.SEPIA.name());
    sepiaButton.addActionListener(a -> {
      features.execute(sepiaButton.getActionCommand(), new String[]{});
    });

    JButton flip = this.options.get(Commands.FLIP.name());
    flip.addActionListener(a -> {
      String selectedOption = displayOptionsDialog("Flip", new String[]{"horizontal-flip",
          "vertical-flip"});
      if (selectedOption != null) {
        features.execute(flip.getActionCommand(), new String[]{selectedOption});
      }
    });

    JButton split = this.options.get(Commands.SPLIT.name());
    split.addActionListener(a -> {
      JPanel mainPanel = new JPanel();
      LayoutManager layoutManager = new BoxLayout(mainPanel, BoxLayout.Y_AXIS);
      mainPanel.setLayout(layoutManager);
      mainPanel.setAlignmentY(Component.CENTER_ALIGNMENT);

      JLabel titleLabel = new JLabel("Enter three filenames with the required extension "
              + "to save the resulting greyscale images on split ");

      JLabel infoLabel = new JLabel("The images are saved in the path relative to the current "
              + "working directory.");
      URL resource = getClass().getResource("/images/information.png");
      Image img = loadResourceImage(resource);
      if (img != null) {
        ImageIcon icon = new ImageIcon(img);
        infoLabel.setIcon(icon);
        infoLabel.setIconTextGap(10);
      }

      JPanel redPanel = new JPanel(new BorderLayout());
      JLabel redLabel = new JLabel("Red component     ");
      redPanel.add(redLabel, BorderLayout.LINE_START);
      JTextField redTextField = new JTextField();
      redPanel.add(redTextField, BorderLayout.CENTER);

      JPanel greenPanel = new JPanel(new BorderLayout());
      JLabel greenLabel = new JLabel("Green component  ");
      greenPanel.add(greenLabel, BorderLayout.LINE_START);
      JTextField greenTextField = new JTextField();
      greenPanel.add(greenTextField, BorderLayout.CENTER);

      JPanel bluePanel = new JPanel(new BorderLayout());
      JLabel blueLabel = new JLabel("Blue component    ");
      bluePanel.add(blueLabel, BorderLayout.LINE_START);
      JTextField blueTextField = new JTextField();
      bluePanel.add(blueTextField, BorderLayout.CENTER);

      mainPanel.add(titleLabel);
      mainPanel.add(Box.createRigidArea(new Dimension(5, 7)));
      mainPanel.add(redPanel);
      mainPanel.add(Box.createRigidArea(new Dimension(5, 7)));
      mainPanel.add(greenPanel);
      mainPanel.add(Box.createRigidArea(new Dimension(5, 7)));
      mainPanel.add(bluePanel);
      mainPanel.add(Box.createRigidArea(new Dimension(5, 7)));
      mainPanel.add(infoLabel);

      int option = JOptionPane.showConfirmDialog(this, mainPanel, "",
              JOptionPane.PLAIN_MESSAGE, JOptionPane.PLAIN_MESSAGE, null);
      if (option == JOptionPane.OK_OPTION) {
        features.execute(split.getActionCommand(), new String[]{redTextField.getText(),
                greenTextField.getText(), blueTextField.getText()});
      }
    });
  }

  // helper method to display file chooser and return the selected file path
  private String getFilePath() {
    JFileChooser fileChooser = new JFileChooser();
    FileNameExtensionFilter imageFilter = new FileNameExtensionFilter("Image files",
            "png", "jpg", "bmp", "ppm", "jpeg");
    fileChooser.setFileFilter(imageFilter);
    int chosenValue = fileChooser.showOpenDialog(this);
    if (chosenValue == JFileChooser.APPROVE_OPTION) {
      return fileChooser.getSelectedFile().getPath();
    }
    return null;
  }

  // helper method to display a dialog with radio buttons for the given options and return the
  // selected option, null otherwise
  private String displayOptionsDialog(String title, String[] options) {
    JPanel panel = new JPanel();
    LayoutManager layoutManager = new BoxLayout(panel, BoxLayout.Y_AXIS);
    panel.setLayout(layoutManager);
    List<JRadioButton> buttons = new ArrayList<>();
    ButtonGroup group = new ButtonGroup();
    for (int i = 0; i < options.length; i++) {
      JRadioButton button = new JRadioButton(options[i]);
      buttons.add(button);
      group.add(button);
      panel.add(button);
    }
    int option = JOptionPane.showConfirmDialog(this, panel, title,
            JOptionPane.PLAIN_MESSAGE, JOptionPane.PLAIN_MESSAGE, null);
    if (option == JOptionPane.OK_OPTION) {
      Optional<JRadioButton> selectedButton =
              buttons.stream().filter(x -> x.isSelected()).findFirst();
      if (selectedButton.isPresent()) {
        return selectedButton.get().getActionCommand();
      }
    }
    return null;
  }

  // helper method to display a dialog with a text field and return the entered data, null otherwise
  private String displayInputDialog(String title, String message) {
    String data = (String) JOptionPane.showInputDialog(this, message, title,
            JOptionPane.PLAIN_MESSAGE, null, null, null);
    return data;
  }

  @Override
  public void setVisible() {
    this.setVisible(true);
  }

  @Override
  public void updateUI(String imageName) {

    CustomImage displayImage = model.getImage(imageName);

    BufferedImage image = getBufferedImage(displayImage);
    JLabel imageLabel = new JLabel(new ImageIcon(image));
    // remove all components and add the new image
    imagePanel.removeAll();
    imagePanel.add(imageLabel, BorderLayout.CENTER);
    imagePanel.revalidate();
    imagePanel.repaint();
    optionsPanel.setVisible(true);
    histogramLabel.setVisible(false);
    histogramPanel.drawHistogram(displayImage);
  }

  @Override
  public void echo(String message, ViewMessageType messageType) {
    // display the message to the user
    String title = messageType.getTitle();
    JLabel messageLabel = new JLabel("<html><div style='text-align: center;'>"
            + message + "</div></html>");
    URL res = getClass().getResource("/images/"
            + title.toLowerCase() + ".png");
    Image imgIcon = loadResourceImage(res);

    if (imgIcon != null) {
      ImageIcon icon = new ImageIcon(imgIcon);
      messageLabel.setIcon(icon);
    }
    JOptionPane.showMessageDialog(this, messageLabel, title,
            JOptionPane.PLAIN_MESSAGE);
  }

  // helper method to load image from resource folder
  private Image loadResourceImage(URL resource) {
    if (resource == null) {
      return null;
    }
    Image img = null;
    try {
      img = ImageIO.read(resource);
    } catch (IOException e) {
      System.out.println("Failed to load image from resources");
    }
    return img;
  }

  // helper method to convert CustomImage object to BufferedImage
  private BufferedImage getBufferedImage(CustomImage image) {
    int width = image.getWidth();
    int height = image.getHeight();
    BufferedImage outputImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        Pixel pixel = image.getPixel(i, j);
        int color = new Color(pixel.getColor(2), pixel.getColor(1), pixel.getColor(0)).getRGB();
        outputImage.setRGB(j, i, color);
      }
    }
    return outputImage;
  }
}
