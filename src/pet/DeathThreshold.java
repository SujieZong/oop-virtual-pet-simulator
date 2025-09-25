package pet;

/**
 * Immutable class that encapsulates the threshold values for pet death conditions.
 */
public class DeathThreshold {
  private final int hungerLimit;
  private final int hygieneLimit;
  private final int socialLimit;
  private final int sleepLimit;

  /**
   * Constructs a DeathThreshold with specified values.
   *
   * @param hungerLimit  maximum hunger value before death
   * @param hygieneLimit minimum hygiene value before death
   * @param socialLimit  minimum social value before death
   * @param sleepLimit   minimum sleep value before death
   */
  public DeathThreshold(int hungerLimit, int hygieneLimit, int socialLimit, int sleepLimit) {
    this.hungerLimit = hungerLimit;
    this.hygieneLimit = hygieneLimit;
    this.socialLimit = socialLimit;
    this.sleepLimit = sleepLimit;
  }

  /**
   * Gets the maximum hunger value before the pet dies.
   *
   * @return the maximum hunger threshold
   */
  public int getHungerLimit() {
    return hungerLimit;
  }

  /**
   * Gets the minimum hygiene value before the pet dies.
   *
   * @return the minimum hygiene threshold
   */
  public int getHygieneLimit() {
    return hygieneLimit;
  }

  /**
   * Gets the minimum social value before the pet dies.
   *
   * @return the minimum social threshold
   */
  public int getSocialLimit() {
    return socialLimit;
  }

  /**
   * Gets the minimum sleep value before the pet dies.
   *
   * @return the minimum sleep threshold
   */
  public int getSleepLimit() {
    return sleepLimit;
  }

  /**
   * Checks if the given health status values exceed any death thresholds.
   *
   * @param hunger  current hunger value
   * @param hygiene current hygiene value
   * @param social  current social value
   * @param sleep   current sleep value
   * @return true if any thresholds are crossed (pet should be dead), false otherwise
   */
  public boolean isDeadCondition(int hunger, int hygiene, int social, int sleep) {
    return hunger > hungerLimit
        ||
        hygiene < hygieneLimit
        ||
        social < socialLimit
        ||
        sleep < sleepLimit;
  }

  @Override
  public String toString() {
    return String.format(
        "DeathThreshold{hungerLimit=%d, hygieneLimit=%d, socialLimit=%d, sleepLimit=%d}",
        hungerLimit, hygieneLimit, socialLimit, sleepLimit); // originally used concatenation
  }

  // below added after grading

  /**
   * Compares this DeathThreshold with another object for equality.
   * Two DeathThreshold objects are equal if all their limit values are equal.
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

    DeathThreshold that = (DeathThreshold) obj;
    return hungerLimit == that.hungerLimit
        &&
        hygieneLimit == that.hygieneLimit
        &&
        socialLimit == that.socialLimit
        &&
        sleepLimit == that.sleepLimit;
  }

  /**
   * Returns a hash code value for this DeathThreshold.
   * The hash code is calculated based on all limit values.
   *
   * @return the hash code value
   */
  @Override
  public int hashCode() {
    int result = 17;
    result = 31 * result + hungerLimit;
    result = 31 * result + hygieneLimit;
    result = 31 * result + socialLimit;
    result = 31 * result + sleepLimit;
    return result;
  }
}