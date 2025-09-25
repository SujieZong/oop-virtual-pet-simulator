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
    return String.format("HealthStatus{hunger=%d, hygiene=%d, social=%d, sleep=%d}",
        hunger, hygiene, social, sleep); // originally used concatenation
  }

  // below added after grading

  /**
   * Compares this HealthStatus with another object for equality.
   * Two HealthStatus objects are equal if all their attributes are equal.
   *
   * @param obj the object to compare with
   * @return true if the objects are equal, false otherwise
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }

    HealthStatus that = (HealthStatus) obj;
    return hunger == that.hunger
        &&
        hygiene == that.hygiene
        &&
        social == that.social
        &&
        sleep == that.sleep;
  }

  /**
   * Returns a hash code value for this HealthStatus.
   * The hash code is calculated based on all attributes.
   *
   * @return the hash code value
   */
  @Override
  public int hashCode() {
    int result = 17;
    result = 31 * result + hunger;
    result = 31 * result + hygiene;
    result = 31 * result + social;
    result = 31 * result + sleep;
    return result;
  }
}
