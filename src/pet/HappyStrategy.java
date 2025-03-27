package pet;

/**
 * Concrete strategy for a pet in a happy mood.
 * When happy, pets decay needs more slowly and respond better to interactions.
 */
public class HappyStrategy extends AbstractMoodStrategy {

  /**
   * Creates a new HappyStrategy with specified parameters.
   *
   * @param hungerChangeRate  Rate at which hunger increases per step
   * @param hygieneChangeRate Rate at which hygiene decreases per step
   * @param socialChangeRate  Rate at which social decreases per step
   * @param sleepChangeRate   Rate at which sleep decreases per step
   * @param actionBoost       The boost applied when an action is performed
   */
  public HappyStrategy(int hungerChangeRate, int hygieneChangeRate,
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
        // Happy pets get more benefit from feeding
        newHunger = clamp(newHunger - (2 * actionBoost));
        break;
      case PLAY:
        // Playing improves social but increases hunger slightly
        newSocial = clamp(newSocial + actionBoost);
        newHunger = clamp(newHunger + 1);
        break;
      case CLEAN:
        // Cleaning improves hygiene
        newHygiene = clamp(newHygiene + actionBoost);
        break;
      case SLEEP:
        // Sleeping improves sleep
        newSleep = clamp(newSleep + actionBoost);
        break;
    }

    return new HealthStatus(newHunger, newHygiene, newSocial, newSleep);
  }

  @Override
  public HealthStatus applyStep(HealthStatus health) {
    // When happy, natural decay is at the standard rate
    int newHunger = clamp(health.getHunger() + hungerChangeRate);
    int newHygiene = clamp(health.getHygiene() - hygieneChangeRate);
    int newSocial = clamp(health.getSocial() - socialChangeRate);
    int newSleep = clamp(health.getSleep() - sleepChangeRate);

    return new HealthStatus(newHunger, newHygiene, newSocial, newSleep);
  }
}