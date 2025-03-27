package pet;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests for the Snake pet class.
 */
public class SnakeTest {

  private Snake snake;

  @Before
  public void setUp() {
    snake = new Snake();
  }

  @Test
  public void testInitialState() {
    HealthStatus health = snake.getHealth();
    // Test initial health values
    assertEquals(50, health.getHunger());
    assertEquals(50, health.getHygiene());
    assertEquals(50, health.getSocial());
    assertEquals(50, health.getSleep());
    // Test initial mood - should be HAPPY with starting values
    assertEquals(MoodEnum.HAPPY, snake.getMood());
    // Test that the snake is alive
    assertFalse(snake.isDead());
  }

  @Test
  public void testStep() {
    snake.step();
    HealthStatus health = snake.getHealth();
    // Snake with HappyStrategy has different rates
    assertEquals(52, health.getHunger()); // 50 + 2 = 52
    assertEquals(49, health.getHygiene()); // 50 - 1 = 49
    assertEquals(49, health.getSocial()); // 50 - 1 = 49
    assertEquals(49, health.getSleep()); // 50 - 1 = 49
  }

  @Test
  public void testFeedInteraction() {
    snake.interactWith(Action.FEED);
    HealthStatus health = snake.getHealth();
    // HappyStrategy with action boost of 10
    assertEquals(30, health.getHunger()); // 50 - (2 * 10) = 30
  }

  @Test
  public void testSnakeMoodChangeToSad() {
    // Increase hunger to trigger sad mood
    for (int i = 0; i < 5; i++) {
      snake.step(); // Each step increases hunger by 2
    }
    // Hunger should be 50 + (5 * 2) = 60, which is at the HUNGER_SAD_THRESHOLD (60), still happy
    HealthStatus health = snake.getHealth();
    assertEquals(60, health.getHunger());
    assertEquals(MoodEnum.HAPPY, snake.getMood());
    assertTrue(snake.moodStrategy instanceof HappyStrategy);
    snake.step();
    health = snake.getHealth();
    assertEquals(62, health.getHunger());
    assertEquals(MoodEnum.SAD, snake.getMood());
    assertTrue(snake.moodStrategy instanceof SadStrategy);
  }

  @Test
  public void testSnakeSocialThreshold() {
    // Snake has a much lower social requirement than other pets
    // Reduce social stats
    for (int i = 0; i < 41; i++) {
      // Manually setting health to avoid triggering other thresholds
      // We're just testing the social threshold here
      snake.setHealthStateAndUpdateState(50, 50, 50 - i, 50);
    }
    // At social = 10, it should still be happy (just at the threshold)
    snake.setHealthStateAndUpdateState(50, 50, 10, 50);
    assertEquals(MoodEnum.HAPPY, snake.getMood());

    // At social = 9, it should become sad
    snake.setHealthStateAndUpdateState(50, 50, 9, 50);
    assertEquals(MoodEnum.SAD, snake.getMood());
  }

  @Test
  public void testSnakeDeathThresholds() {
    // Test that snake dies at appropriate thresholds
    DeathThreshold threshold = snake.getDeathThreshold();
    assertEquals(80, threshold.getHungerLimit());
    assertEquals(15, threshold.getHygieneLimit());
    assertEquals(5, threshold.getSocialLimit()); // Very low social requirement
    assertEquals(5, threshold.getSleepLimit());

    // Test death from hunger
    for (int i = 0; i < 16; i++) {
      if (!snake.isDead()) {
        snake.step();
      }
    }
    assertTrue(snake.isDead());

    // Test survival with low social
    snake = new Snake(); // Reset
    snake.setHealthStateAndUpdateState(50, 50, 5, 50); // Just above social death threshold
    assertFalse(snake.isDead());

    // Test death with very low social
    snake.setHealthStateAndUpdateState(50, 50, 4, 50); // Below social death threshold
    assertTrue(snake.isDead());
  }

  @Test
  public void testHappyVsSadBehavior() {
    // Compare behavior between happy and sad states

    // Record happy behavior
    Snake happySnake = new Snake();
    happySnake.step();
    HealthStatus happyHealth = happySnake.getHealth();

    // Create sad snake
    Snake sadSnake = new Snake();
    sadSnake.setMood(MoodEnum.SAD);
    sadSnake.step();
    HealthStatus sadHealth = sadSnake.getHealth();

    // Sad snake's health should deteriorate faster
    assertTrue(sadHealth.getHunger() > happyHealth.getHunger());
    assertTrue(sadHealth.getHygiene() < happyHealth.getHygiene());
    assertTrue(sadHealth.getSocial() < happyHealth.getSocial());
    assertTrue(sadHealth.getSleep() < happyHealth.getSleep());
  }

  @Test
  public void testMissingHygieneInMoodCheck() {
    // Note: The Snake's updateMood doesn't check hygiene, unlike Fox
    // This test verifies that behavior

    // First verify snake is happy with normal hygiene
    snake.setHealthStateAndUpdateState(50, 50, 50, 50);
    assertEquals(MoodEnum.HAPPY, snake.getMood());

    // Now drastically lower hygiene - should still be happy if other stats are good
    snake.setHealthStateAndUpdateState(50, 5, 50, 50);
    assertEquals(MoodEnum.HAPPY, snake.getMood());

    // Verify that low hygiene still triggers death
    snake.checkDeath();
    assertTrue(snake.isDead());
  }
}