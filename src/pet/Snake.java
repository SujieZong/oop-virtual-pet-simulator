package pet;

/**
 * Concrete implementation of a Snake pet.
 * Snakes have unique thresholds and behavior characteristics.
 */
public class Snake extends AbstractPet {

  // Death threshold constants
  private static final int HUNGER_LIMIT = 80;
  private static final int HYGIENE_LIMIT = 15;
  private static final int SOCIAL_LIMIT = 5;  // Snakes need less social interaction
  private static final int SLEEP_LIMIT = 5;

  // Mood threshold constants. Snakes are sad if they are hungry, sleepy, or lonely.
  // I deliberately omitted the hygiene threshold because snakes are not as concerned with hygiene.
  // also i do this to show that some thresholds are not always necessary, and are dependent on the pet type.
  // but they will still die if their hygiene is too low.
  private static final int HUNGER_SAD_THRESHOLD = 60;
  private static final int SOCIAL_SAD_THRESHOLD = 10;
  private static final int SLEEP_SAD_THRESHOLD = 30;

  // Strategy constants for Happy mood
  private static final int HAPPY_HUNGER_RATE = 2;
  private static final int HAPPY_HYGIENE_RATE = 1;
  private static final int HAPPY_SOCIAL_RATE = 1;
  private static final int HAPPY_SLEEP_RATE = 1;
  private static final int HAPPY_ACTION_BOOST = 10;

  // Strategy constants for Sad mood
  private static final int SAD_HUNGER_RATE = 4;
  private static final int SAD_HYGIENE_RATE = 2;
  private static final int SAD_SOCIAL_RATE = 2;
  private static final int SAD_SLEEP_RATE = 2;
  private static final int SAD_ACTION_BOOST = 3;

  /**
   * Creates a new Snake pet with default values.
   */
  public Snake() {
    super(); // This will call updateMood() and updateMoodStrategy()
  }

  @Override
  protected void initializeThreshold() {
    // Initialize death thresholds
    this.threshold = new DeathThreshold(
        HUNGER_LIMIT,
        HYGIENE_LIMIT,
        SOCIAL_LIMIT,
        SLEEP_LIMIT
    );
  }

  @Override
  protected void updateMoodStrategy() {
    if (currentMood == MoodEnum.HAPPY) {
      this.moodStrategy = new HappyStrategy(
          HAPPY_HUNGER_RATE,
          HAPPY_HYGIENE_RATE,
          HAPPY_SOCIAL_RATE,
          HAPPY_SLEEP_RATE,
          HAPPY_ACTION_BOOST
      );
    } else {
      this.moodStrategy = new SadStrategy(
          SAD_HUNGER_RATE,
          SAD_HYGIENE_RATE,
          SAD_SOCIAL_RATE,
          SAD_SLEEP_RATE,
          SAD_ACTION_BOOST
      );
    }
  }

  @Override
  protected void updateMood() {
    // Determine mood based on current health status
    if (health.getHunger() > HUNGER_SAD_THRESHOLD ||
        health.getSocial() < SOCIAL_SAD_THRESHOLD ||
        health.getSleep() < SLEEP_SAD_THRESHOLD) {
      setMood(MoodEnum.SAD);
    } else {
      setMood(MoodEnum.HAPPY);
    }
  }
}