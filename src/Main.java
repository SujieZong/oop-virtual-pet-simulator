import pet.PetView;

/**
 * Main class to start the Pet Game application.
 * This class initializes the view and starts the game.
 */
public class Main {
  /**
   * Main method to start the application.
   * It creates the view and initializes the controller.
   *
   * @param args command line arguments (not used)
   */
  public static void main(String[] args) {
    // Create the view first (without a pet)
    PetView view = new PetView();

    // Let the view handle initial pet selection and then start the controller
    // The controller will be initialized in the view after pet selection
  }
}