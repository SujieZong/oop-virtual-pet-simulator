package pet;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

/**
 * The PetView class represents the graphical user interface for the Virtual Pet application.
 * It extends JFrame and contains components to display the pet's health status, mood, and image,
 * as well as buttons for interacting with the pet.
 */
public class PetView extends JFrame {
  // Dreamy color palette
  private static final Color BG_COLOR = new Color(253, 240, 255);
  private static final Color PANEL_COLOR = new Color(226, 185, 248);
  private static final Color HEALTH_PANEL_COLOR = new Color(253, 249, 255);
  private static final Color BUTTON_COLOR = new Color(250, 228, 247);
  private static final Color TEXT_COLOR = new Color(75, 0, 130);
  private static final Color BORDER_COLOR = new Color(186, 85, 211);
  private static final Color HEALTH_BORDER_COLOR = new Color(131, 9, 131);

  // Duration to display action images (in milliseconds)
  private static final int ACTION_IMAGE_DURATION = 2000; // 2 seconds

  // Duration to display step indicator (in milliseconds)
  private static final int STEP_INDICATOR_DURATION = 1500; // 1.5 seconds

  // UI Components
  private final JLabel healthLabel;
  private final JLabel moodLabel;
  private final JLabel imageLabel;
  private final JLabel stepIndicatorLabel;
  private final JButton feedButton;
  private final JButton playButton;
  private final JButton cleanButton;
  private final JButton sleepButton;
  private final JButton stepButton;

  // Panels and layout
  private JPanel mainGamePanel;
  private JPanel selectionPanel;
  private CardLayout cardLayout;

  // State variables
  private String selectedPetType = "Pet"; // Default pet type
  private boolean showingDeathScreen = false;
  private PetController controller;
  private DisplayState currentDisplayState = DisplayState.MOOD_IMAGE;
  private MoodEnum lastKnownMood = MoodEnum.HAPPY; // Store actual mood from model
  private Action currentAction = null; // Current action being displayed (if any)
  // Timers
  private Timer actionImageTimer;
  private Timer stepIndicatorTimer;
  // Font settings
  private Font pixelFont;
  private float baseFontSize = 18f;

  /**
   * Constructs a new PetView with all UI components.
   * Sets up the window layout, buttons, labels, and mood menu.
   */
  public PetView() {
    setTitle("Virtual Pet");
    setSize(800, 750);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    // Initialize font
    pixelFont = new Font("Courier", Font.BOLD, (int) baseFontSize);

    // Apply dreamy color theme
    getContentPane().setBackground(BG_COLOR);

    // Use CardLayout to switch between pet selection and game screen
    cardLayout = new CardLayout();
    JPanel contentPanel = new JPanel(cardLayout);
    contentPanel.setBackground(BG_COLOR);
    setContentPane(contentPanel);

    // Create selection panel
    selectionPanel = createSelectionPanel();

    // Create main game panel
    mainGamePanel = new JPanel(new BorderLayout(0, 20));
    mainGamePanel.setBackground(BG_COLOR);

    // Health Display
    healthLabel = new JLabel("Health: Loading...", SwingConstants.CENTER);
    healthLabel.setFont(pixelFont.deriveFont(baseFontSize));
    healthLabel.setForeground(TEXT_COLOR);
    healthLabel.setBorder(createBorder(BORDER_COLOR, 3, 15, 15, 15, 15));

    JPanel healthPanel = new JPanel(new BorderLayout());
    healthPanel.setBackground(HEALTH_PANEL_COLOR);
    healthPanel.setBorder(createBorder(HEALTH_BORDER_COLOR, 5, 20, 20, 20, 20));
    healthPanel.add(healthLabel, BorderLayout.CENTER);

    mainGamePanel.add(healthPanel, BorderLayout.NORTH);

    // Image Display
    imageLabel = new JLabel();
    imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
    imageLabel.setPreferredSize(new Dimension(350, 400));
    imageLabel.setBackground(BG_COLOR);

    // Step indicator overlay
    stepIndicatorLabel = new JLabel();
    stepIndicatorLabel.setVisible(false); // Initially hidden

    // Create a panel with BorderLayout to keep image centered
    JPanel imagePanel = new JPanel(new BorderLayout());
    imagePanel.setBackground(BG_COLOR);
    imagePanel.setBorder(createBorder(BORDER_COLOR, 3, 10, 10, 10, 10));

    // Set imagePanel to null layout to allow absolute positioning
    imagePanel.setLayout(null);

    // Add a component listener to the image panel to handle repositioning
    imagePanel.addComponentListener(new ComponentAdapter() {
      @Override
      public void componentResized(ComponentEvent e) {
        // Reposition the pet image to be centered
        int panelWidth = imagePanel.getWidth();
        int panelHeight = imagePanel.getHeight();

        // Make the image label fill the panel
        imageLabel.setBounds(0, 0, panelWidth, panelHeight);

        // Position step indicator in upper right
        stepIndicatorLabel.setBounds(panelWidth - 150, 20, 133, 63);
      }
    });

    // Add both labels to the panel
    imagePanel.add(imageLabel);
    imagePanel.add(stepIndicatorLabel);

    mainGamePanel.add(imagePanel, BorderLayout.CENTER);

    // Create panel for mood label
    JPanel moodPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    moodPanel.setBackground(PANEL_COLOR);
    moodLabel = new JLabel("Mood: Loading...", SwingConstants.CENTER);
    moodLabel.setFont(pixelFont.deriveFont(baseFontSize));
    moodLabel.setForeground(TEXT_COLOR);
    moodPanel.add(moodLabel);

    // Interaction Buttons
    JPanel buttonPanel = new JPanel();
    buttonPanel.setLayout(new GridLayout(1, 5, 15, 15));
    buttonPanel.setBorder(createBorder(BORDER_COLOR, 3, 15, 15, 15, 15));
    buttonPanel.setBackground(PANEL_COLOR);

    feedButton = createPixelButton("Feed");
    playButton = createPixelButton("Play");
    cleanButton = createPixelButton("Clean");
    sleepButton = createPixelButton("Sleep");
    stepButton = createPixelButton("Step");

    buttonPanel.add(feedButton);
    buttonPanel.add(playButton);
    buttonPanel.add(cleanButton);
    buttonPanel.add(sleepButton);
    buttonPanel.add(stepButton);

    // Add mood panel and button panel to the south
    JPanel southPanel = new JPanel(new BorderLayout(0, 10));
    southPanel.setBackground(PANEL_COLOR);
    southPanel.setBorder(createBorder(BORDER_COLOR, 3, 5, 5, 10, 5));
    southPanel.add(moodPanel, BorderLayout.NORTH);
    southPanel.add(buttonPanel, BorderLayout.SOUTH);
    mainGamePanel.add(southPanel, BorderLayout.SOUTH);

    // Add panels to card layout
    contentPanel.add(selectionPanel, "Selection");
    contentPanel.add(mainGamePanel, "Game");

    // Start with selection screen
    cardLayout.show(contentPanel, "Selection");

    // Initialize timers
    initializeTimers();

    setLocationRelativeTo(null); // Center the window on the screen
    setVisible(true); // Make the window visible
  }

  /**
   * Initialize all timers used in the view.
   */
  private void initializeTimers() {
    // Timer for handling action images
    actionImageTimer = new Timer(ACTION_IMAGE_DURATION, e -> {
      if (currentDisplayState == DisplayState.ACTION_IMAGE) {
        // Revert to mood image when timer expires
        currentDisplayState = DisplayState.MOOD_IMAGE;
        currentAction = null; // Clear the current action
        updateImageDisplay();
      }
    });
    actionImageTimer.setRepeats(false);

    // Timer for step indicator
    stepIndicatorTimer = new Timer(STEP_INDICATOR_DURATION, e -> {
      stepIndicatorLabel.setVisible(false);
    });
    stepIndicatorTimer.setRepeats(false);
  }

  /**
   * Creates a border with customizable color, thickness, and padding.
   *
   * @param color     Border color
   * @param thickness Border thickness in pixels
   * @param top       Top padding in pixels
   * @param left      Left padding in pixels
   * @param bottom    Bottom padding in pixels
   * @param right     Right padding in pixels
   * @return A compound border with the specified properties
   */
  private Border createBorder(Color color, int thickness, int top, int left, int bottom,
                              int right) {
    Border lineBorder = new LineBorder(color, thickness);
    Border emptyBorder = new EmptyBorder(top, left, bottom, right);
    return new CompoundBorder(lineBorder, emptyBorder);
  }

  /**
   * Creates a button with pixel-art styling.
   */
  private JButton createPixelButton(String text) {
    JButton button = new JButton(text);
    button.setFont(pixelFont.deriveFont(baseFontSize - 2));
    button.setBackground(BUTTON_COLOR);
    button.setForeground(TEXT_COLOR);

    button.setBorder(new LineBorder(BORDER_COLOR, 4));
    button.setPreferredSize(new Dimension(120, 60)); // Larger buttons

    button.setContentAreaFilled(true); // Fill the button area
    button.setOpaque(true); // Make the button opaque

    // Add hover effect
    button.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseEntered(java.awt.event.MouseEvent evt) {
        button.setBackground(BUTTON_COLOR.brighter());
      }

      public void mouseExited(java.awt.event.MouseEvent evt) {
        button.setBackground(BUTTON_COLOR);
      }

      public void mousePressed(java.awt.event.MouseEvent evt) {
        button.setBackground(BUTTON_COLOR.darker());
      }

      public void mouseReleased(java.awt.event.MouseEvent evt) {
        button.setBackground(BUTTON_COLOR);
      }
    });

    return button;
  }

  /**
   * Creates the pet selection panel.
   */
  private JPanel createSelectionPanel() {
    JPanel panel = new JPanel(new BorderLayout());
    panel.setBackground(BG_COLOR);
    panel.setBorder(createBorder(BORDER_COLOR, 3, 30, 30, 30, 30));

    JLabel titleLabel = new JLabel("Choose Your Virtual Pet", SwingConstants.CENTER);
    titleLabel.setFont(pixelFont.deriveFont(baseFontSize + 8));
    titleLabel.setForeground(TEXT_COLOR);
    panel.add(titleLabel, BorderLayout.NORTH);

    JPanel selectionOptions = new JPanel(new GridLayout(3, 1, 20, 20));
    selectionOptions.setBackground(BG_COLOR);
    selectionOptions.setBorder(createBorder(BORDER_COLOR, 3, 50, 80, 50, 80));

    JButton petButton = createPixelButton("Mysterious Pet");
    JButton foxButton = createPixelButton("Fox");
    JButton snakeButton = createPixelButton("Snake");

    // Make selection buttons larger
    petButton.setPreferredSize(new Dimension(200, 80));
    foxButton.setPreferredSize(new Dimension(200, 80));
    snakeButton.setPreferredSize(new Dimension(200, 80));

    // Add actions to pet selection buttons
    petButton.addActionListener(e -> selectPet("Pet"));
    foxButton.addActionListener(e -> selectPet("Fox"));
    snakeButton.addActionListener(e -> selectPet("Snake"));

    selectionOptions.add(petButton);
    selectionOptions.add(foxButton);
    selectionOptions.add(snakeButton);

    panel.add(selectionOptions, BorderLayout.CENTER);

    return panel;
  }

  /**
   * Handles pet selection and initializes controller.
   *
   * @param petType the type of pet to select
   */
  private void selectPet(String petType) {
    selectedPetType = petType;
    PetInterface pet;

    // Create appropriate pet type
    switch (petType) {
      case "Fox":
        pet = new Fox();
        break;
      case "Snake":
        pet = new Snake();
        break;
      default:
        pet = new Pet();
        break;
    }

    // Initialize controller with selected pet
    controller = new PetController(pet, this);

    // Switch to game screen
    cardLayout.show(getContentPane(), "Game");
  }

  /**
   * Updates the image display based on current display state and pet state.
   */
  private void updateImageDisplay() {
    String fileName;

    if (showingDeathScreen) {
      fileName = "flower";
    } else if (currentDisplayState == DisplayState.ACTION_IMAGE && currentAction != null) {
      // Format: moodpettypeaction (e.g., "happysnakeFEED")
      fileName = lastKnownMood.name().toLowerCase() + selectedPetType + currentAction.name();
    } else {
      // Format: moodpettype (e.g., "happysnake")
      fileName = lastKnownMood.name().toLowerCase() + selectedPetType;
    }

    loadAndDisplayImage(fileName);
  }


  /**
   * Helper method to load and display an image by filename.
   *
   * @param fileName the base filename without extension
   * @return true if the image was loaded successfully, false otherwise
   */
  private boolean loadAndDisplayImage(String fileName) {
    // Path for images directly in src/images
    String imagePath = "/images/" + fileName + ".png";
    try {
      System.out.println("Loading image: " + imagePath);
      // Use class loader to get resource as stream
      java.io.InputStream is = getClass().getResourceAsStream(imagePath);
      if (is == null) {
        // Try alternate extension
        imagePath = "/images/" + fileName + ".PNG";
        is = getClass().getResourceAsStream(imagePath);
        if (is == null) {
          // Additional debug
          System.err.println("Image not found. Trying ClassLoader approach...");
          ClassLoader classLoader = getClass().getClassLoader();
          is = classLoader.getResourceAsStream("images/" + fileName + ".png");
          if (is == null) {
            is = classLoader.getResourceAsStream("images/" + fileName + ".PNG");
            if (is == null) {
              throw new IOException("Resource not found: " + imagePath);
            }
          }
        }
      }

      Image image = ImageIO.read(is);
      Image scaledImage = image.getScaledInstance(350, 400, Image.SCALE_SMOOTH);
      imageLabel.setIcon(new ImageIcon(scaledImage));
      return true;
    } catch (IOException e) {
      System.err.println("Could not load image: " + imagePath);
      e.printStackTrace();
      return false;
    }
  }

  /**
   * Updates the pet's mood state from the model.
   *
   * @param mood The current mood from the pet model
   */
  public void updatePetMoodLabelAndImage(MoodEnum mood) {
    // Update the internal state
    this.lastKnownMood = mood;

    // Update the mood label
    moodLabel.setText("Mood: " + mood.name());

    // Update the image if we're not showing an action
    if (currentDisplayState == DisplayState.MOOD_IMAGE) {
      updateImageDisplay();
    }
  }

  /**
   * Displays a temporary action animation.
   *
   * @param mood   the current mood of the pet
   * @param action the action being performed
   */
  public void showActionImage(MoodEnum mood, Action action) {
    if (showingDeathScreen) {
      return; // Don't show action images if pet is dead
    }

    // Update the image state
    lastKnownMood = mood;
    currentAction = action;
    currentDisplayState = DisplayState.ACTION_IMAGE;

    // Update the displayed image
    updateImageDisplay();

    // Start the timer to revert back to mood image
    if (actionImageTimer.isRunning()) {
      actionImageTimer.restart();
    } else {
      actionImageTimer.start();
    }
  }

  /**
   * Cancels any action animation in progress.
   */
  public void cancelActionImage() {
    if (actionImageTimer.isRunning()) {
      actionImageTimer.stop();
    }

    if (currentDisplayState == DisplayState.ACTION_IMAGE) {
      currentDisplayState = DisplayState.MOOD_IMAGE;
      updateImageDisplay();
    }
  }

  /**
   * Shows the death screen.
   */
  public void showDeathScreen() {
    showingDeathScreen = true;
    updateImageDisplay();

    // Create a custom dialog with pixel style
    JLabel messageLabel = new JLabel("Your pet has died! Click OK to restart with a new pet.");
    messageLabel.setFont(pixelFont.deriveFont(baseFontSize));
    messageLabel.setForeground(TEXT_COLOR);

    JPanel panel = new JPanel(new BorderLayout());
    panel.setBackground(PANEL_COLOR);
    panel.setBorder(createBorder(BORDER_COLOR, 3, 20, 20, 20, 20));
    panel.add(messageLabel, BorderLayout.CENTER);

    JOptionPane.showMessageDialog(this, panel, "Game Over", JOptionPane.INFORMATION_MESSAGE);

    // Reset to selection screen after death
    Timer timer = new Timer(1500, e -> {
      cardLayout.show(getContentPane(), "Selection");
      showingDeathScreen = false;
    });
    timer.setRepeats(false);
    timer.start();
  }

  /**
   * Shows the step indicator overlay.
   */
  public void showStepIndicator() {
    try {
      // Try both approaches for loading the step.png image
      java.io.InputStream is = getClass().getResourceAsStream("/images/step.PNG");
      if (is == null) {
        ClassLoader classLoader = getClass().getClassLoader();
        is = classLoader.getResourceAsStream("images/step.PNG");
        if (is == null) {
          throw new IOException("Step indicator image not found");
        }
      }

      Image stepImage = ImageIO.read(is);
      Image scaledStepImage = stepImage.getScaledInstance(133, 63, Image.SCALE_SMOOTH);
      stepIndicatorLabel.setIcon(new ImageIcon(scaledStepImage));

      // Make the indicator visible
      stepIndicatorLabel.setVisible(true);

      // Start/restart timer
      if (stepIndicatorTimer.isRunning()) {
        stepIndicatorTimer.restart();
      } else {
        stepIndicatorTimer.start();
      }
    } catch (IOException e) {
      System.err.println("Could not load step indicator image: /images/step.PNG");
      e.printStackTrace();
    }
  }

  /**
   * Updates the health label with formatted text.
   *
   * @param healthText the formatted health text
   */
  public void updateHealthDisplay(String healthText) {
    healthLabel.setText(healthText);
  }

  // Accessor methods for buttons
  public JButton getFeedButton() {
    return feedButton;
  }

  public JButton getPlayButton() {
    return playButton;
  }

  public JButton getCleanButton() {
    return cleanButton;
  }

  public JButton getSleepButton() {
    return sleepButton;
  }

  public JButton getStepButton() {
    return stepButton;
  }

  // Accessor methods for display state
  public boolean isShowingActionImage() {
    return currentDisplayState == DisplayState.ACTION_IMAGE;
  }

  public boolean isShowingDeathScreen() {
    return showingDeathScreen;
  }

  // Image display state
  private enum DisplayState {
    MOOD_IMAGE,    // Showing normal mood image (happy/sad)
    ACTION_IMAGE,  // Showing an action animation
    DEATH_IMAGE    // Showing death screen
  }
}