package pet;

import javax.swing.Timer;

/**
 * Controller for the Virtual Pet application.
 * Manages interaction between the pet model and the view.
 */
public class PetController {
  // Time in milliseconds between automatic steps
  private static final int STEP_INTERVAL = 5000; // 5 seconds

  // MVC components
  private final PetInterface pet;      // Model
  private final PetView view;          // View
  private Timer gameTimer;             // Timer for automatic steps

  // Prevents multiple rapid steps/interactions
  private boolean processingStep = false;

  /**
   * Creates a new PetController with the specified pet and view.
   *
   * @param pet  the pet model
   * @param view the view to be controlled
   */
  public PetController(PetInterface pet, PetView view) {
    this.pet = pet;
    this.view = view;

    // Attach action listeners to buttons
    view.getFeedButton().addActionListener(e -> handleInteraction(Action.FEED));
    view.getPlayButton().addActionListener(e -> handleInteraction(Action.PLAY));
    view.getCleanButton().addActionListener(e -> handleInteraction(Action.CLEAN));
    view.getSleepButton().addActionListener(e -> handleInteraction(Action.SLEEP));
    view.getStepButton().addActionListener(e -> stepGame());

    // Start automatic timer for game steps
    gameTimer = new Timer(STEP_INTERVAL, e -> stepGame());
    gameTimer.start();

    // Initialize view with pet's initial state
    refreshUserInterface();
  }

  /**
   * Handles pet interactions based on the action type.
   *
   * @param action the action to perform on the pet
   */
  private void handleInteraction(Action action) {
    // Don't process interactions if the pet is dead or we're processing a step
    if (pet.isDead() || processingStep) {
      return;
    }

    // Apply the action to the pet
    pet.interactWith(action);

    // Show the action animation with the current mood
    view.showActionImage(pet.getMood(), action);


    refreshUserInterface();
  }

  /**
   * Advances the game state by one step.
   */
  private void stepGame() {
    // Don't process steps if pet is dead or we're already processing
    if (pet.isDead() || processingStep) {
      return;
    }

    processingStep = true;

    try {
      // If showing an action image, cancel it
      if (view.isShowingActionImage()) {
        view.cancelActionImage();
      }

      // Show the step indicator
      view.showStepIndicator();

      // Perform the step on the model
      pet.step();

      // Update the view
      refreshUserInterface();
    } finally {
      processingStep = false;
    }
  }

  /**
   * Helper method to format health status into a readable display string.
   *
   * @param health The current health status of the pet
   * @return HTML-formatted string showing health stats
   */
  private String formatHealthStatusText(HealthStatus health) {
    return String.format(
        "<html>"
            +
            "<table width='100%%' cellspacing='10'>"
            +
            "  <tr>"
            +
            "    <td align='right' width='30%%'><b>Hunger:</b></td>"
            +
            "    <td align='left' width='10%%'>%d</td>"
            +
            "    <td width='10%%'></td>" // Empty column for spacing
            +
            "    <td align='right' width='30%%'><b>Hygiene:</b></td>"
            +
            "    <td align='left' width='10%%'>%d</td>"
            +
            "  </tr>"
            +
            "  <tr>"
            +
            "    <td align='right'><b>Social:</b></td>"
            +
            "    <td align='left'>%d</td>"
            +
            "    <td></td>" // Empty column for spacing
            +
            "    <td align='right'><b>Sleep:</b></td>"
            +
            "    <td align='left'>%d</td>"
            +
            "  </tr>"
            +
            "</table>"
            +
            "</html>",
        health.getHunger(), health.getHygiene(),
        health.getSocial(), health.getSleep());
  }

  /**
   * Updates the view to reflect the current state of the pet.
   * refresh the health panel/mood label/mood image(no action) displays.
   */
  private void refreshUserInterface() {
    // Update health display
    view.updateHealthDisplay(formatHealthStatusText(pet.getHealth()));

    // Always update mood display in the view
    view.updatePetMoodLabelAndImage(pet.getMood());

    // Check for death first
    if (pet.isDead() && !view.isShowingDeathScreen()) {
      view.showDeathScreen();
      gameTimer.stop();

    }


  }

}