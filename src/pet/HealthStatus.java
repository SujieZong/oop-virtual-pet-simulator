package pet;

/**
 * HealthStatus is an immutable class encapsulating the
 * pet's health-related attributes.
 */
public class HealthStatus {
  private final int hunger;
  private final int hygiene;
  private final int social;
  private final int sleep;

  /**
   * Constructs a new HealthStatus with the specified values.
   *
   * @param hunger  the hunger level
   * @param hygiene the hygiene level
   * @param social  the social level
   * @param sleep   the sleep level
   */
  public HealthStatus(int hunger, int hygiene, int social, int sleep) {
    this.hunger = hunger;
    this.hygiene = hygiene;
    this.social = social;
    this.sleep = sleep;
  }

  /**
   * Gets the pet's current hunger level.
   *
   * @return the pet's current hunger level
   */
  public int getHunger() {
    return hunger;
  }

  /**
   * Gets the pet's current hygiene level.
   *
   * @return the pet's current hygiene level
   */
  public int getHygiene() {
    return hygiene;
  }

  /**
   * Gets the pet's current social level.
   *
   * @return the pet's current social level
   */
  public int getSocial() {
    return social;
  }

  /**
   * Gets the pet's current sleep level.
   *
   * @return the pet's current sleep level
   */
  public int getSleep() {
    return sleep;
  }

  @Override
  public String toString() {
    return "HealthStatus{"
        +
        "hunger=" + hunger
        +
        ", hygiene=" + hygiene
        +
        ", social=" + social
        +
        ", sleep=" + sleep
        +
        '}';
  }
}
