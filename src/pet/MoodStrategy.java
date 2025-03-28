package pet;

/**
 * Interface defining the strategy for a pet's mood.
 * Different moods will have different implementations of this strategy.
 */
public interface MoodStrategy {

  /**
   * Applies the effects of an interaction to the pet based on the current mood.
   *
   * @param health The current health status of the pet
   * @param action The action being performed
   * @return The new health status after the interaction
   */
  HealthStatus applyInteraction(HealthStatus health, Action action);

  /**
   * Applies the natural changes that occur each step based on the pet's mood.
   *
   * @param health The current health status of the pet
   * @return The new health status after a step passes
   */
  HealthStatus applyStep(HealthStatus health);


}