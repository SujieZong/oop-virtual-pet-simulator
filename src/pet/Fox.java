package pet;

/**
 * Concrete implementation of a Fox pet.
 * Foxes have unique thresholds and behavior characteristics.
 */
public class Fox extends AbstractPet {

  // Death threshold constants
  private static final int HUNGER_LIMIT = 90;
  private static final int HYGIENE_LIMIT = 20;
  private static final int SOCIAL_LIMIT = 20;
  private static final int SLEEP_LIMIT = 10;

  // Mood threshold constants
  private static final int HUNGER_SAD_THRESHOLD = 70;
  private static final int HYGIENE_SAD_THRESHOLD = 35;
  private static final int SOCIAL_SAD_THRESHOLD = 35;
  private static final int SLEEP_SAD_THRESHOLD = 25;

  // Strategy constants for Happy mood
  private static final int HAPPY_HUNGER_RATE = 3;
  private static final int HAPPY_HYGIENE_RATE = 2;
  private static final int HAPPY_SOCIAL_RATE = 2;
  private static final int HAPPY_SLEEP_RATE = 2;
  private static final int HAPPY_ACTION_BOOST = 8;

  // Strategy constants for Sad mood
  private static final int SAD_HUNGER_RATE = 5;
  private static final int SAD_HYGIENE_RATE = 3;
  private static final int SAD_SOCIAL_RATE = 3;
  private static final int SAD_SLEEP_RATE = 3;
  private static final int SAD_ACTION_BOOST = 4;

  /**
   * Creates a new Fox pet with default values.
   */
  public Fox() {
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
        health.getHygiene() < HYGIENE_SAD_THRESHOLD ||
        health.getSocial() < SOCIAL_SAD_THRESHOLD ||
        health.getSleep() < SLEEP_SAD_THRESHOLD) {
      setMood(MoodEnum.SAD);
    } else {
      setMood(MoodEnum.HAPPY);
    }
  }
}