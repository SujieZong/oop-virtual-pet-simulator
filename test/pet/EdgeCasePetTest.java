package pet;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Tests focused on edge cases and unusual situations.
 */
public class EdgeCasePetTest {

  @Test
  public void testZeroHealthValues() {
    // Create a pet with some values at zero
    Pet pet = new Pet();
    pet.setHealthStateAndUpdateState(0, 0, 0, 0);

    // Perform step - values should increase/decrease from zero
    pet.step();
    HealthStatus health = pet.getHealth();

    assertEquals(0, health.getHunger());

    // Can't go below zero
    assertEquals(0, health.getHygiene());
    assertEquals(0, health.getSocial());
    assertEquals(0, health.getSleep());

    // Pet should be in SAD mood and may be dead
    assertEquals(MoodEnum.SAD, pet.getMood());
    assertTrue(pet.isDead());
  }

  @Test
  public void testMaxHealthValues() {
    // Create a pet with max values
    Pet pet = new Pet();
    pet.health = new HealthStatus(100, 100, 100, 100);

    // Perform step - hunger increases, others decrease
    pet.step();
    HealthStatus health = pet.getHealth();

    // Can't go above 100
    assertEquals(100, health.getHunger());

    // Others should decrease from 100
    assertEquals(98, health.getHygiene());
    assertEquals(98, health.getSocial());
    assertEquals(98, health.getSleep());
  }

  @Test
  public void testSetMoodOnDeadPet() {
    // Create a pet and kill it
    Pet pet = new Pet();
    pet.health = new HealthStatus(100, 0, 0, 0); // Should be dead
    pet.checkDeath();
    assertTrue(pet.isDead());

    // Initial mood
    MoodEnum initialMood = pet.getMood();

    // Try to change mood
    pet.setMood(initialMood == MoodEnum.HAPPY ? MoodEnum.SAD : MoodEnum.HAPPY);

    // Mood should not change
    assertEquals(initialMood, pet.getMood());
  }

  @Test
  public void testMoodStrategyChangeAfterMoodSet() {
    // Create a pet and record its hunger increase rate
    Pet pet = new Pet();
    pet.step();
    int happyHungerIncrease = pet.getHealth().getHunger() - 50;

    // Reset and set to SAD mood
    pet = new Pet();
    pet.setMood(MoodEnum.SAD);

    // Check that hunger now increases at the sad rate
    pet.step();
    int sadHungerIncrease = pet.getHealth().getHunger() - 50;

    // Sad strategy should increase hunger faster
    assertTrue(sadHungerIncrease > happyHungerIncrease);
  }

  @Test
  public void testInteractionEffectsAtBoundaries() {
    // Create a pet at the boundary
    Pet pet = new Pet();

    // Set hunger to nearly max and feed
    pet.setHealthStateAndUpdateState(85, 50, 50, 50);
    assertEquals(MoodEnum.SAD, pet.getMood());
    pet.interactWith(Action.FEED);
    assertEquals(81, pet.getHealth().getHunger());

    // Set hunger to nearly min and feed
    pet.setHealthStateAndUpdateState(5, 50, 50, 50);
    pet.interactWith(Action.FEED);
    assertEquals(0, pet.getHealth().getHunger());

    // Set hygiene to nearly min and clean
    pet.setHealthStateAndUpdateState(50, 15, 50, 50);
    pet.interactWith(Action.CLEAN);
    assertEquals(19, pet.getHealth().getHygiene());
  }

  @Test
  public void testAlivePetBecomesDeadThenInteract() {
    // Create a pet at the edge of death
    Pet pet = new Pet();
    pet.setHealthStateAndUpdateState(84, 50, 50, 50);// Just below death threshold
    assertFalse(pet.isDead());

    // Take a step to bring it over threshold
    pet.step();
    HealthStatus healthBeforeDeath = pet.getHealth();
    assertEquals(89, healthBeforeDeath.getHunger()); // 84 + 3 = 87, which is over 85 threshold
    assertTrue(pet.isDead());

    // Try to interact and verify no change
    pet.interactWith(Action.FEED);
    HealthStatus healthAfterInteraction = pet.getHealth();

    // Health should not change after death
    assertEquals(healthBeforeDeath.getHunger(), healthAfterInteraction.getHunger());
    assertEquals(healthBeforeDeath.getHygiene(), healthAfterInteraction.getHygiene());
    assertEquals(healthBeforeDeath.getSocial(), healthAfterInteraction.getSocial());
    assertEquals(healthBeforeDeath.getSleep(), healthAfterInteraction.getSleep());
  }

  @Test
  public void testRandomizationInSadStrategy() {
    // The SadStrategy has random components in its applyStep method
    // Let's test that it produces different results on multiple calls

    SadStrategy strategy = new SadStrategy(3, 2, 2, 2, 5);
    HealthStatus baseHealth = new HealthStatus(50, 50, 50, 50);

    HealthStatus result1 = strategy.applyStep(baseHealth);
    HealthStatus result2 = strategy.applyStep(baseHealth);
    HealthStatus result3 = strategy.applyStep(baseHealth);

    // With random components, it's extremely unlikely all three results would be identical
    // At least one of these assertions should be true
    boolean hygieneDiffers = result1.getHygiene() != result2.getHygiene() ||
        result2.getHygiene() != result3.getHygiene() ||
        result1.getHygiene() != result3.getHygiene();

    boolean socialDiffers = result1.getSocial() != result2.getSocial() ||
        result2.getSocial() != result3.getSocial() ||
        result1.getSocial() != result3.getSocial();

    boolean sleepDiffers = result1.getSleep() != result2.getSleep() ||
        result2.getSleep() != result3.getSleep() ||
        result1.getSleep() != result3.getSleep();

    assertTrue(hygieneDiffers || socialDiffers || sleepDiffers);
  }
}