package pet;

/**
 * Abstract base class for different mood-based strategies.
 * This class defines how a pet's mood affects its behavior.
 */
public abstract class AbstractMoodStrategy implements MoodStrategy {
  protected static final int VALUE_CEILING = 100;
  protected static final int VALUE_FLOOR = 0;

  // Constants representing rate of change for different attributes
  protected final int hungerChangeRate;
  protected final int hygieneChangeRate;
  protected final int socialChangeRate;
  protected final int sleepChangeRate;
  protected final int actionBoost;

  /**
   * Constructor for the AbstractMoodStrategy.
   *
   * @param hungerChangeRate  Rate at which hunger increases per step
   * @param hygieneChangeRate Rate at which hygiene decreases per step
   * @param socialChangeRate  Rate at which social decreases per step
   * @param sleepChangeRate   Rate at which sleep decreases per step
   * @param actionBoost       The boost applied when an action is performed
   */
  public AbstractMoodStrategy(int hungerChangeRate, int hygieneChangeRate,
                              int socialChangeRate, int sleepChangeRate, int actionBoost) {
    this.hungerChangeRate = hungerChangeRate;
    this.hygieneChangeRate = hygieneChangeRate;
    this.socialChangeRate = socialChangeRate;
    this.sleepChangeRate = sleepChangeRate;
    this.actionBoost = actionBoost;
  }

  /**
   * Applies the effects of an interaction to the pet based on the current mood.
   *
   * @param health The current health status of the pet
   * @param action The action being performed
   * @return The new health status after the interaction
   */
  public abstract HealthStatus applyInteraction(HealthStatus health, Action action);

  /**
   * Applies the natural changes that occur each step based on the pet's mood.
   *
   * @param health The current health status of the pet
   * @return The new health status after a step passes
   */
  public abstract HealthStatus applyStep(HealthStatus health);

  /**
   * Helper method to ensure values stay within the valid range [0-100].
   */
  protected int clamp(int value) {
    return Math.max(VALUE_FLOOR, Math.min(VALUE_CEILING, value));
  }


}