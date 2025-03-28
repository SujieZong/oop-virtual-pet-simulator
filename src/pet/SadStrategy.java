package pet;

/**
 * Concrete strategy for a pet in a sad mood.
 * When sad, pets decay needs more quickly and respond less to interactions.
 */
public class SadStrategy extends AbstractMoodStrategy {
  protected static final int RANDOM_CHANGE_LIMIT = 4;

  /**
   * Creates a new SadStrategy with specified parameters.
   *
   * @param hungerChangeRate  Rate at which hunger increases per step
   * @param hygieneChangeRate Rate at which hygiene decreases per step
   * @param socialChangeRate  Rate at which social decreases per step
   * @param sleepChangeRate   Rate at which sleep decreases per step
   * @param actionBoost       The boost applied when an action is performed
   */
  public SadStrategy(int hungerChangeRate, int hygieneChangeRate,
                     int socialChangeRate, int sleepChangeRate, int actionBoost) {
    super(hungerChangeRate, hygieneChangeRate, socialChangeRate, sleepChangeRate, actionBoost);
  }

  @Override
  public HealthStatus applyInteraction(HealthStatus health, Action action) {
    int newHunger = health.getHunger();
    int newHygiene = health.getHygiene();
    int newSocial = health.getSocial();
    int newSleep = health.getSleep();

    switch (action) {
      case FEED:
        // Sad pets still respond to food, but less effectively
        newHunger = clamp(newHunger - actionBoost);
        break;
      case PLAY:
        // Sad pets don't enjoy play as much, but it still helps somewhat
        // They might even get more hungry without much social benefit
        newSocial = clamp(newSocial + (actionBoost / 2));
        newHunger = clamp(newHunger + 2);
        break;
      case CLEAN:
        // Cleaning still helps hygiene
        newHygiene = clamp(newHygiene + actionBoost);
        break;
      case SLEEP:
        // Sleeping still helps sleep
        newSleep = clamp(newSleep + actionBoost);
        break;
      default:
        break;
    }

    return new HealthStatus(newHunger, newHygiene, newSocial, newSleep);
  }

  @Override
  public HealthStatus applyStep(HealthStatus health) {
    // When sad, natural decay is faster
    int newHunger = clamp(health.getHunger() + hungerChangeRate);
    int newHygiene = clamp(health.getHygiene() - (hygieneChangeRate
        + (int) (Math.random() * RANDOM_CHANGE_LIMIT)));
    int newSocial = clamp(health.getSocial() - (socialChangeRate + (int) (Math.random()
        * RANDOM_CHANGE_LIMIT)));
    int newSleep = clamp(health.getSleep() - (sleepChangeRate + (int) (Math.random()
        * RANDOM_CHANGE_LIMIT)));

    return new HealthStatus(newHunger, newHygiene, newSocial, newSleep);
  }
}