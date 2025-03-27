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
    return hunger > hungerLimit ||
        hygiene < hygieneLimit ||
        social < socialLimit ||
        sleep < sleepLimit;
  }

  @Override
  public String toString() {
    return "DeathThreshold{" +
        "hungerLimit=" + hungerLimit +
        ", hygieneLimit=" + hygieneLimit +
        ", socialLimit=" + socialLimit +
        ", sleepLimit=" + sleepLimit +
        '}';
  }
}