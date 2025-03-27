package pet;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests for the Fox pet class.
 */
public class FoxTest {

  private Fox fox;

  @Before
  public void setUp() {
    fox = new Fox();
  }

  @Test
  public void testInitialState() {
    HealthStatus health = fox.getHealth();
    // Test initial health values
    assertEquals(50, health.getHunger());
    assertEquals(50, health.getHygiene());
    assertEquals(50, health.getSocial());
    assertEquals(50, health.getSleep());
    // Test initial mood - should be HAPPY with starting values
    assertEquals(MoodEnum.HAPPY, fox.getMood());
    // Test that the fox is alive
    assertFalse(fox.isDead());
  }

  @Test
  public void testStep() {
    fox.step();
    HealthStatus health = fox.getHealth();
    // Fox with HappyStrategy has specific rates
    assertEquals(53, health.getHunger()); // 50 + 3 = 53
    assertEquals(48, health.getHygiene()); // 50 - 2 = 48
    assertEquals(48, health.getSocial()); // 50 - 2 = 48
    assertEquals(48, health.getSleep()); // 50 - 2 = 48
  }

  @Test
  public void testFeedInteraction() {
    fox.interactWith(Action.FEED);
    HealthStatus health = fox.getHealth();
    // HappyStrategy with action boost of 8
    assertEquals(34, health.getHunger()); // 50 - (2 * 8) = 34
  }

  @Test
  public void testFoxMoodChangeToSad() {
    // Increase hunger to trigger sad mood
    assertEquals(MoodEnum.HAPPY, fox.getMood());
    assertTrue(fox.moodStrategy instanceof HappyStrategy);
    for (int i = 0; i < 7; i++) {
      fox.step(); // Each step increases hunger by 3
    }
    // Hunger should be 50 + (7 * 3) = 71, which is above the HUNGER_SAD_THRESHOLD (70)
    HealthStatus health = fox.getHealth();
    assertEquals(71, health.getHunger());
    assertEquals(MoodEnum.SAD, fox.getMood());
    assertTrue(fox.moodStrategy instanceof SadStrategy);
  }

  @Test
  public void testFoxSocialThreshold() {
    // Fox has a higher social requirement than snake
    // Reduce social stats
    for (int i = 0; i < 16; i++) {
      // Manually setting health to avoid triggering other thresholds
      // We're just testing the social threshold here
      fox.setHealthStateAndUpdateState(50, 50, 50 - i, 50);

    }

    // At social = 35, it should still be happy (at threshold)
    fox.setHealthStateAndUpdateState(50, 50, 35, 50);
    assertEquals(MoodEnum.HAPPY, fox.getMood());

    // At social = 34, it should become sad
    fox.setHealthStateAndUpdateState(50, 50, 34, 50);
    assertEquals(MoodEnum.SAD, fox.getMood());
  }

  @Test
  public void testFoxDeathThresholds() {
    // Test that fox dies at appropriate thresholds
    DeathThreshold threshold = fox.getDeathThreshold();
    assertEquals(90, threshold.getHungerLimit()); // Higher hunger tolerance
    assertEquals(20, threshold.getHygieneLimit());
    assertEquals(20, threshold.getSocialLimit()); // Higher social requirement than snake
    assertEquals(10, threshold.getSleepLimit());

    // Test death from hunger (should take longer than for other pets)
    for (int i = 0; i < 14; i++) {
      if (!fox.isDead()) {
        fox.step();
      }
    }
    assertTrue(fox.isDead());

    // Test survival with low-ish social
    fox = new Fox(); // Reset
    fox.setHealthStateAndUpdateState(50, 50, 21, 50);// Just above social death threshold
    assertFalse(fox.isDead());

    // Test death with very low social
    fox.setHealthStateAndUpdateState(50, 50, 19, 50);// Below social death threshold
    assertTrue(fox.isDead());
  }

  @Test
  public void testHygieneThresholdForMood() {
    // Fox cares about hygiene for mood

    // First verify fox is happy with normal hygiene
    fox.health = new HealthStatus(50, 50, 50, 50);
    fox.updateMood();
    assertEquals(MoodEnum.HAPPY, fox.getMood());

    // Now lower hygiene to just at threshold
    fox.health = new HealthStatus(50, 35, 50, 50);
    fox.updateMood();
    assertEquals(MoodEnum.HAPPY, fox.getMood());

    // Now below threshold
    fox.health = new HealthStatus(50, 34, 50, 50);
    fox.updateMood();
    assertEquals(MoodEnum.SAD, fox.getMood());
  }

  @Test
  public void testHappyVsSadBehavior() {
    // Compare behavior between happy and sad states

    // Record happy behavior
    Fox happyFox = new Fox();
    happyFox.step();
    HealthStatus happyHealth = happyFox.getHealth();

    // Create sad fox
    Fox sadFox = new Fox();
    sadFox.setMood(MoodEnum.SAD);
    sadFox.step();
    HealthStatus sadHealth = sadFox.getHealth();

    // Sad fox's health should deteriorate faster
    assertTrue(sadHealth.getHunger() > happyHealth.getHunger());
    assertTrue(sadHealth.getHygiene() < happyHealth.getHygiene());
    assertTrue(sadHealth.getSocial() < happyHealth.getSocial());
    assertTrue(sadHealth.getSleep() < happyHealth.getSleep());
  }


}