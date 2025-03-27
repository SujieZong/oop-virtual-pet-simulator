package pet;

/**
 * Abstract base class for all pet types.
 * Implements common functionality while allowing specific pet types to customize behavior.
 */
public abstract class AbstractPet implements PetInterface {

  // Initial health value for all pets
  private static final int INITIAL_HEALTH_VALUE = 50;

  // Encapsulated health status
  protected HealthStatus health;

  // Pet state
  protected MoodEnum currentMood;
  protected AbstractMoodStrategy moodStrategy;
  protected DeathThreshold threshold;
  protected boolean isDead;

  /**
   * Creates a new pet with default health values.
   */
  public AbstractPet() {
    // Initialize health with default values
    this.health = new HealthStatus(
        INITIAL_HEALTH_VALUE,
        INITIAL_HEALTH_VALUE,
        INITIAL_HEALTH_VALUE,
        INITIAL_HEALTH_VALUE
    );


    // Subclasses must initialize threshold
    initializeThreshold();

    // Determine initial mood based on health values
    updateMood();

    // Set initial strategy based on determined mood
    updateMoodStrategy();
    checkDeath();
  }

  /**
   * Initialize the death threshold values.
   * This must be implemented by subclasses.
   */
  protected abstract void initializeThreshold();

  @Override
  public void step() {
    if (isDead) {
      return; // No state changes if the pet is dead
    }

    // Apply the strategy to update the health values directly
    this.health = moodStrategy.applyStep(health);
    updateMood();
    updateMoodStrategy();
    // Check for death
    checkDeath();


  }

  @Override
  public void interactWith(Action action) {
    if (isDead) {
      return; // No interactions if the pet is dead
    }

    // Apply the strategy to handle the interaction directly on the pet's attributes
    this.health = moodStrategy.applyInteraction(health, action);
    updateMood();
    updateMoodStrategy();

    // Check for death
    checkDeath();


  }

  @Override
  public HealthStatus getHealth() {
    return health;
  }

  @Override
  public MoodEnum getMood() {
    return currentMood;
  }

  @Override
  public void setMood(MoodEnum mood) {
    if (!isDead && this.currentMood != mood) {
      this.currentMood = mood;
      updateMoodStrategy();
    }
  }

  /**
   * Checks if the pet's health has crossed any death thresholds.
   */
  protected void checkDeath() {
    if (threshold.isDeadCondition(
        health.getHunger(),
        health.getHygiene(),
        health.getSocial(),
        health.getSleep())) {
      isDead = true;
    } else {
      isDead = false;
    }
  }

  /**
   * Updates the mood strategy based on the current mood.
   * Each pet type must implement this to set the appropriate strategy.
   */
  protected abstract void updateMoodStrategy();
  //keep this abstract to give subclass flexibility to choose mood
  // strategy based on different criteria

  /**
   * Updates the pet's mood based on its current health.
   * Each pet type must implement this to determine mood changes.
   */
  protected abstract void updateMood();
  // keep this abstract to give subclass flexibility to choose different mood strategies

  /**
   * Checks if the pet is dead.
   *
   * @return true if the pet is dead, false otherwise
   */
  public boolean isDead() {
    return isDead;
  }


  public DeathThreshold getDeathThreshold() {
    return threshold;
  }

  /**
   * Sets the pet's health state to the provided values and updates mood and death status.
   * NOTE: This method is intended for testing purposes only.
   *
   * @param hunger  the new hunger value
   * @param hygiene the new hygiene value
   * @param social  the new social value
   * @param sleep   the new sleep value
   */
  protected void setHealthStateAndUpdateState(int hunger, int hygiene, int social, int sleep) {
    this.health = new HealthStatus(hunger, hygiene, social, sleep);
    updateMood();
    updateMoodStrategy();
    // Check for death
    checkDeath();


  }
}