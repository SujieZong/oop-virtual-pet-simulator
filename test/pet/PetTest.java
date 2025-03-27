package pet;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests for the basic Pet class.
 */
public class PetTest {

  private Pet pet;

  @Before
  public void setUp() {
    pet = new Pet();
  }

  @Test
  public void testInitialState() {
    HealthStatus health = pet.getHealth();
    // Test initial health values
    assertEquals(50, health.getHunger());
    assertEquals(50, health.getHygiene());
    assertEquals(50, health.getSocial());
    assertEquals(50, health.getSleep());
    // Test initial mood - should be HAPPY with starting values
    assertEquals(MoodEnum.HAPPY, pet.getMood());

    // Test that the pet is alive
    assertFalse(pet.isDead());
  }

  @Test
  public void testStep() {
    pet.step();
    HealthStatus health = pet.getHealth();
    // Pet starts HAPPY, so should use HappyStrategy rates
    assertEquals(53, health.getHunger());
    assertEquals(48, health.getHygiene());
    assertEquals(48, health.getSocial());
    assertEquals(48, health.getSleep());
  }

  @Test
  public void testFeedInteraction() {
    pet.interactWith(Action.FEED);
    HealthStatus health = pet.getHealth();
    // HappyStrategy: 50 - (2 * 7) = 36
    assertEquals(36, health.getHunger());
    assertEquals(50, health.getHygiene());
    assertEquals(50, health.getSocial());
    assertEquals(50, health.getSleep());
  }

  @Test
  public void testPlayInteraction() {
    pet.interactWith(Action.PLAY);
    HealthStatus health = pet.getHealth();
    assertEquals(51, health.getHunger()); // Hunger +1
    assertEquals(50, health.getHygiene());
    assertEquals(57, health.getSocial()); // Social +7 (action boost)
    assertEquals(50, health.getSleep());
  }

  @Test
  public void testCleanInteraction() {
    pet.interactWith(Action.CLEAN);
    HealthStatus health = pet.getHealth();
    assertEquals(50, health.getHunger());
    assertEquals(57, health.getHygiene()); // Hygiene +7 (action boost)
    assertEquals(50, health.getSocial());
    assertEquals(50, health.getSleep());
  }

  @Test
  public void testSleepInteraction() {
    pet.interactWith(Action.SLEEP);
    HealthStatus health = pet.getHealth();
    assertEquals(50, health.getHunger());
    assertEquals(50, health.getHygiene());
    assertEquals(50, health.getSocial());
    assertEquals(57, health.getSleep()); // Sleep +7 (action boost)
  }

  @Test
  public void testMoodChangeToSad() {
    // Increase hunger to trigger sad mood
    for (int i = 0; i < 6; i++) {
      pet.step(); // Each step increases hunger by 3
    }
    // Hunger should be 50 + (6 * 3) = 68, which is above the HUNGER_SAD_THRESHOLD (65)
    HealthStatus health = pet.getHealth();
    assertEquals(68, health.getHunger());
    assertEquals(MoodEnum.SAD, pet.getMood());
    assertTrue(pet.moodStrategy instanceof SadStrategy);
  }

  @Test
  public void testMoodChangeToHappy() {
    // First make pet sad
    for (int i = 0; i < 6; i++) {
      pet.step();
    }
    assertEquals(MoodEnum.SAD, pet.getMood());

    // Then feed it to make it happy again
    pet.interactWith(Action.FEED);
    // With sad strategy, action boost is 4, so hunger decreases by 4
    HealthStatus health = pet.getHealth();
    assertEquals(64, health.getHunger()); // 68 - 4 = 64, below HUNGER_SAD_THRESHOLD (65)
    assertEquals(MoodEnum.HAPPY, pet.getMood());
    assertTrue(pet.moodStrategy instanceof HappyStrategy);
  }

  @Test
  public void testDeath() {
    // Make pet die from hunger
    for (int i = 0; i < 12; i++) {
      pet.step(); // Increase hunger beyond death threshold
    }

    // Hunger should be around 86, which is over the death threshold
    assertTrue(pet.isDead());
  }

  @Test
  public void testNoChangeAfterDeath() {
    // Kill the pet
    for (int i = 0; i < 12; i++) {
      pet.step();
    }
    assertTrue(pet.isDead());

    // Get current health status
    HealthStatus beforeHealth = pet.getHealth();

    // Try to interact and step
    pet.interactWith(Action.FEED);
    pet.step();

    // Health should remain unchanged
    HealthStatus afterHealth = pet.getHealth();
    assertEquals(beforeHealth.getHunger(), afterHealth.getHunger());
    assertEquals(beforeHealth.getHygiene(), afterHealth.getHygiene());
    assertEquals(beforeHealth.getSocial(), afterHealth.getSocial());
    assertEquals(beforeHealth.getSleep(), afterHealth.getSleep());
  }

  @Test
  public void testHealthValuesBoundedAtMaximum() {
    // Feed pet multiple times to try to reduce hunger below 0
    for (int i = 0; i < 10; i++) {
      pet.interactWith(Action.FEED);
    }

    HealthStatus health = pet.getHealth();
    assertEquals(0, health.getHunger()); // Should be clamped at 0
  }

  @Test
  public void testHealthValuesBoundedAtMinimum() {
    // Step many times to try to increase hunger above 100
    for (int i = 0; i < 20; i++) {
      pet.step();
    }

    // Should die before reaching 100, but we can verify it never exceeds 100
    HealthStatus health = pet.getHealth();
    assertTrue(health.getHunger() <= 100);
  }

  @Test
  public void testManualMoodSetting() {
    pet.setMood(MoodEnum.SAD);
    assertEquals(MoodEnum.SAD, pet.getMood());

    // Test that the strategy changes by checking the step behavior
    pet.step();
    HealthStatus health = pet.getHealth();
    assertEquals(55, health.getHunger()); // SadStrategy: 50 + 5 = 55
  }

  @Test
  public void testGetDeathThreshold() {
    DeathThreshold threshold = pet.getDeathThreshold();
    assertNotNull(threshold);
    // Verify the default values for Pet
    assertEquals(85, threshold.getHungerLimit());
    assertEquals(15, threshold.getHygieneLimit());
    assertEquals(15, threshold.getSocialLimit());
    assertEquals(10, threshold.getSleepLimit());
  }
}
