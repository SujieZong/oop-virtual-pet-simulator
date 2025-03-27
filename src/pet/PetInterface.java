package pet;

/**
 * The PetInterface defines the minimal set of methods
 * required for any Pet implementation in the virtual pet game.
 */
public interface PetInterface {
  /**
   * Advances the pet's internal state by one unit of time,
   * decreasing health stats as needed.
   */
  void step();

  /**
   * Applies an interaction (e.g., feeding, playing, cleaning, sleeping) to the pet
   * which may alter its internal state and mood.
   *
   * @param action the type of interaction to apply
   */
  void interactWith(Action action);

  /**
   * Returns an immutable HealthStatus object containing the pet's health data.
   *
   * @return the pet's current HealthStatus
   */
  HealthStatus getHealth();

  /**
   * Returns the current mood of the pet.
   *
   * @return the current MoodEnum of the pet
   */
  MoodEnum getMood();

  /**
   * Manually sets the pet's mood.
   *
   * @param mood the new mood of the pet
   */
  void setMood(MoodEnum mood);

  /**
   * Checks if the pet is dead.
   *
   * @return true if the pet is dead, false otherwise
   */
  boolean isDead();

  /**
   * Returns the death thresholds for this pet.
   * These values indicate when the pet will die based on its health status.
   *
   * @return the DeathThreshold object containing limit values
   */
  DeathThreshold getDeathThreshold();
}