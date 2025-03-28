import pet.AbstractPet;
import pet.Action;
import pet.MoodEnum;
import pet.PetInterface;
import pet.Snake;

/**
 * Demonstrates a single Pet (Snake) undergoing various interactions without user inout.
 */
public class MyPetMain {
  /**
   * Demonstrates a single Pet (Snake) undergoing various interactions without user input.
   *
   * @param args command-line arguments (unused)
   */
  public static void main(String[] args) {
    PetInterface snake = new Snake();

    // 1. Show initial state
    System.out.println("Initial State:");
    printStatus(snake);

    // 2. Show 4 interactions while the snake is happy (e.g., feed, play, clean, sleep)
    System.out.println("\n-- Performing 4 Interactions While Happy --");
    System.out.println("Interaction #1: FEED");
    snake.interactWith(Action.FEED);
    printStatus(snake);

    System.out.println("Interaction #2: PLAY");
    snake.interactWith(Action.PLAY);
    printStatus(snake);

    System.out.println("Interaction #3: CLEAN");
    snake.interactWith(Action.CLEAN);
    printStatus(snake);

    System.out.println("Interaction #4: SLEEP");
    snake.interactWith(Action.SLEEP);
    printStatus(snake);

    // 3. Show when a long time passes with no interaction -> the pet becomes sad
    System.out.println("\n-- No Interactions for Several Steps (the pet may become sad) --");
    for (int i = 1; i <= 15; i++) {
      snake.step();
      System.out.println("Step " + i + " (no interaction)");
      printStatus(snake);
      if (isDead(snake)) {
        return;
      }
    }

    // 4. Pet is sad, interact until happy again
    System.out.println("\n-- Interact Until Pet is Happy Again --");
    int interactionCount = 0;
    while (snake.getMood() == MoodEnum.SAD && !isDead(snake)) {
      interactionCount++;
      System.out.println("Interaction #" + interactionCount + " to cheer it up: FEED");
      snake.interactWith(Action.FEED);
      printStatus(snake);
      if (snake.getMood() == MoodEnum.HAPPY) {
        System.out.println("The pet is happy again!");
      }
    }

    // 5. Run a simulation of multiple steps and periodic actions
    System.out.println("\n-- Running a Simulation of 25 Steps with Automated Interactions --");
    runSimulation(snake, 25);
    System.out.println("After simulation:");
    printStatus(snake);
    if (isDead(snake)) {
      return;
    }

    // 6. Demonstrate the pet dying (extreme hunger, hygiene, etc.)
    System.out.println("\n-- Forcing the pet to become dead (by skipping interactions) --");
    for (int i = 1; i <= 20; i++) {
      snake.step();
      System.out.println("Step " + i + " (no interaction)");
      printStatus(snake);
      if (isDead(snake)) {
        break;
      }
    }

    // 7. Show that when the pet is dead, no interactions can be made
    System.out.println("\n-- Attempting to Interact after Death --");
    System.out.println("Trying FEED interaction...");
    snake.interactWith(Action.FEED);
    System.out.println("Trying step...");
    snake.step();
    System.out.println("After trying to FEED and step while dead:");
    printStatus(snake);

    System.out.println("\nEnd of Demo.");
  }

  /**
   * Runs a simulation that interacts with the pet for a significant number of steps.
   *
   * @param pet   the pet to interact with
   * @param steps the number of steps to simulate
   */
  private static void runSimulation(PetInterface pet, int steps) {
    for (int i = 0; i < steps; i++) {
      pet.step();
      System.out.println("Simulation Step " + (i + 1) + " (pet stepped)");
      printStatus(pet);

      if (i % 5 == 0) {
        System.out.println("Interaction: FEED");
        pet.interactWith(Action.FEED);
        printStatus(pet);
      }
      if (i % 10 == 0) {
        System.out.println("Interaction: PLAY");
        pet.interactWith(Action.PLAY);
        printStatus(pet);
      }
      if (i % 15 == 0) {
        System.out.println("Interaction: CLEAN");
        pet.interactWith(Action.CLEAN);
        printStatus(pet);
      }
      if (i % 20 == 0) {
        System.out.println("Interaction: SLEEP");
        pet.interactWith(Action.SLEEP);
        printStatus(pet);
      }

      if (pet instanceof AbstractPet && ((AbstractPet) pet).isDead()) {
        System.out.println("Pet died during simulation at step " + (i + 1));
        break;
      }
      System.out.println("----------");
    }
  }

  /**
   * Helper method to print current status of the pet.
   */
  private static void printStatus(PetInterface pet) {
    System.out.println("Health: " + pet.getHealth()
        + ", Mood: " + pet.getMood());
  }

  /**
   * Checks if the pet is dead; if so, prints a message and returns true.
   */
  private static boolean isDead(PetInterface pet) {
    if (pet instanceof AbstractPet && ((AbstractPet) pet).isDead()) {
      System.out.println("The pet is dead!");
      return true;
    }
    return false;
  }
}