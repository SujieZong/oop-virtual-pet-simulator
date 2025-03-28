package pet;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests for both HappyStrategy and SadStrategy mood implementations.
 * Tests the behavior of each mood type and transitions between them.
 */
public class MoodStrategiesTest {
  // Test parameters for strategies
  private static final int TEST_HUNGER_RATE = 3;
  private static final int TEST_HYGIENE_RATE = 2;
  private static final int TEST_SOCIAL_RATE = 2;
  private static final int TEST_SLEEP_RATE = 2;
  private static final int TEST_ACTION_BOOST = 7;

  private AbstractMoodStrategy happyStrategy;
  private AbstractMoodStrategy sadStrategy;
  private HealthStatus initialHealth;

  /**
   * Sets up the test environment before each test.
   * Initializes the mood strategies and the initial health status.
   */
  @Before
  public void setUp() {
    // Create test strategies with same parameters for direct comparison
    happyStrategy = new HappyStrategy(
        TEST_HUNGER_RATE, TEST_HYGIENE_RATE, TEST_SOCIAL_RATE, TEST_SLEEP_RATE, TEST_ACTION_BOOST);

    sadStrategy = new SadStrategy(
        TEST_HUNGER_RATE, TEST_HYGIENE_RATE, TEST_SOCIAL_RATE, TEST_SLEEP_RATE, TEST_ACTION_BOOST);

    // Create a middle-range health status for testing
    initialHealth = new HealthStatus(50, 50, 50, 50);
  }

  // Happy strategy tests

  @Test
  public void testHappyStrategyStep() {
    // Test natural decay rates in happy mood
    HealthStatus result = happyStrategy.applyStep(initialHealth);

    // Verify exact decay rates applied
    assertEquals("Happy strategy should increase hunger by exactly the hunger rate",
        53, result.getHunger()); // 50 + 3

    assertEquals("Happy strategy should decrease hygiene by exactly the hygiene rate",
        48, result.getHygiene()); // 50 - 2

    assertEquals("Happy strategy should decrease social by exactly the social rate",
        48, result.getSocial()); // 50 - 2

    assertEquals("Happy strategy should decrease sleep by exactly the sleep rate",
        48, result.getSleep()); // 50 - 2
  }

  @Test
  public void testHappyStrategyFeed() {
    // Test feeding interaction in happy mood
    HealthStatus result = happyStrategy.applyInteraction(initialHealth, Action.FEED);

    // Verify exact feeding effect (2x action boost)
    assertEquals("Happy strategy feeding should decrease hunger by exactly 2x action boost",
        36, result.getHunger()); // 50 - (2 * 7)

    // Other stats should remain unchanged for feeding
    assertEquals(50, result.getHygiene());
    assertEquals(50, result.getSocial());
    assertEquals(50, result.getSleep());
  }

  @Test
  public void testHappyStrategyPlay() {
    // Test playing interaction in happy mood
    HealthStatus result = happyStrategy.applyInteraction(initialHealth, Action.PLAY);

    // Verify exact play effects
    assertEquals("Play should increase social by exactly the action boost",
        57, result.getSocial()); // 50 + 7

    assertEquals("Play should increase hunger by exactly 1",
        51, result.getHunger()); // 50 + 1

    // Other stats should remain unchanged for playing
    assertEquals(50, result.getHygiene());
    assertEquals(50, result.getSleep());
  }

  @Test
  public void testHappyStrategyClean() {
    // Test cleaning interaction in happy mood
    HealthStatus result = happyStrategy.applyInteraction(initialHealth, Action.CLEAN);

    // Verify exact cleaning effect
    assertEquals("Clean should increase hygiene by exactly the action boost",
        57, result.getHygiene()); // 50 + 7

    // Other stats should remain unchanged for cleaning
    assertEquals(50, result.getHunger());
    assertEquals(50, result.getSocial());
    assertEquals(50, result.getSleep());
  }

  @Test
  public void testHappyStrategySleep() {
    // Test sleeping interaction in happy mood
    HealthStatus result = happyStrategy.applyInteraction(initialHealth, Action.SLEEP);

    // Verify exact sleeping effect
    assertEquals("Sleep should increase sleep by exactly the action boost",
        57, result.getSleep()); // 50 + 7

    // Other stats should remain unchanged for sleeping
    assertEquals(50, result.getHunger());
    assertEquals(50, result.getHygiene());
    assertEquals(50, result.getSocial());
  }

  // Sad strategy tests

  @Test
  public void testSadStrategyStep() {
    // Test natural decay rates in sad mood with seeded random for predictability

    // Because SadStrategy uses random, we can only test ranges
    HealthStatus result = sadStrategy.applyStep(initialHealth);

    // Verify exact hunger increase (doesn't use random)
    assertEquals("Sad strategy should increase hunger by exactly the hunger rate",
        53, result.getHunger()); // 50 + 3

    // For random components, check in exact range
    assertTrue("Sad strategy should decrease hygiene by 2-5 points",
        result.getHygiene() >= 45 && result.getHygiene() <= 48); // 50 - (2 + 0-3 random)

    assertTrue("Sad strategy should decrease social by 2-5 points",
        result.getSocial() >= 45 && result.getSocial() <= 48); // 50 - (2 + 0-3 random)

    assertTrue("Sad strategy should decrease sleep by 2-5 points",
        result.getSleep() >= 45 && result.getSleep() <= 48); // 50 - (2 + 0-3 random)
  }

  @Test
  public void testSadStrategyFeed() {
    // Test feeding interaction in sad mood
    HealthStatus result = sadStrategy.applyInteraction(initialHealth, Action.FEED);

    // Verify exact feeding effect (just action boost)
    assertEquals("Sad strategy feeding should decrease hunger by exactly the action boost",
        43, result.getHunger()); // 50 - 7

    // Other stats should remain unchanged for feeding
    assertEquals(50, result.getHygiene());
    assertEquals(50, result.getSocial());
    assertEquals(50, result.getSleep());
  }

  @Test
  public void testSadStrategyPlay() {
    // Test playing interaction in sad mood
    HealthStatus result = sadStrategy.applyInteraction(initialHealth, Action.PLAY);

    // Verify exact play effects
    assertEquals("Sad play should increase social by exactly half action boost",
        53, result.getSocial()); // 50 + (7/2)

    assertEquals("Sad play should increase hunger by exactly 2",
        52, result.getHunger()); // 50 + 2

    // Other stats should remain unchanged for playing
    assertEquals(50, result.getHygiene());
    assertEquals(50, result.getSleep());
  }

  @Test
  public void testSadStrategyClean() {
    // Test cleaning interaction in sad mood
    HealthStatus result = sadStrategy.applyInteraction(initialHealth, Action.CLEAN);

    // Verify exact cleaning effect
    assertEquals("Clean should increase hygiene by exactly the action boost",
        57, result.getHygiene()); // 50 + 7

    // Other stats should remain unchanged for cleaning
    assertEquals(50, result.getHunger());
    assertEquals(50, result.getSocial());
    assertEquals(50, result.getSleep());
  }

  @Test
  public void testSadStrategySleep() {
    // Test sleeping interaction in sad mood
    HealthStatus result = sadStrategy.applyInteraction(initialHealth, Action.SLEEP);

    // Verify exact sleeping effect
    assertEquals("Sleep should increase sleep by exactly the action boost",
        57, result.getSleep()); // 50 + 7

    // Other stats should remain unchanged for sleeping
    assertEquals(50, result.getHunger());
    assertEquals(50, result.getHygiene());
    assertEquals(50, result.getSocial());
  }

  // Value limits tests

  @Test
  public void testStrategyValueLimits() {
    // Create edge case health status
    HealthStatus edgeHealth = new HealthStatus(98, 1, 1, 1);

    // Test that values don't exceed limits after step
    HealthStatus happyResult = happyStrategy.applyStep(edgeHealth);

    // Check exact values with limits applied
    assertEquals("Hunger should be clamped to 100", 100,
        happyResult.getHunger()); // 98 + 3 = 101, clamped to 100
    assertEquals("Hygiene should be clamped to 0", 0,
        happyResult.getHygiene()); // 1 - 2 = -1, clamped to 0
    assertEquals("Social should be clamped to 0", 0,
        happyResult.getSocial()); // 1 - 2 = -1, clamped to 0
    assertEquals("Sleep should be clamped to 0", 0,
        happyResult.getSleep()); // 1 - 2 = -1, clamped to 0
  }

  @Test
  public void testInteractionValueLimits() {
    // Create edge case health statuses
    HealthStatus minHealth = new HealthStatus(0, 0, 0, 0);
    HealthStatus maxHealth = new HealthStatus(100, 95, 95, 95);

    // Test interactions at min values
    HealthStatus feedResult = happyStrategy.applyInteraction(minHealth, Action.FEED);
    assertEquals("Hunger can't go below 0", 0, feedResult.getHunger());

    // Test interactions at max values
    HealthStatus cleanResult = happyStrategy.applyInteraction(maxHealth, Action.CLEAN);
    assertEquals("Hygiene can't exceed 100", 100,
        cleanResult.getHygiene()); // 95 + 7 = 102, clamped to 100

    HealthStatus playResult = happyStrategy.applyInteraction(maxHealth, Action.PLAY);
    assertEquals("Social can't exceed 100", 100,
        playResult.getSocial()); // 95 + 7 = 102, clamped to 100

    HealthStatus sleepResult = happyStrategy.applyInteraction(maxHealth, Action.SLEEP);
    assertEquals("Sleep can't exceed 100", 100,
        sleepResult.getSleep()); // 95 + 7 = 102, clamped to 100
  }

  // Compare happy vs sad strategies

  @Test
  public void testCompareHappyVsSadFeedEffects() {
    HealthStatus happyResult = happyStrategy.applyInteraction(initialHealth, Action.FEED);
    HealthStatus sadResult = sadStrategy.applyInteraction(initialHealth, Action.FEED);

    // Happy: 50 - (2 * 7) = 36, Sad: 50 - 7 = 43
    assertEquals("Happy feeding should reduce hunger to 36", 36, happyResult.getHunger());
    assertEquals("Sad feeding should reduce hunger to 43", 43, sadResult.getHunger());
  }

  @Test
  public void testCompareHappyVsSadPlayEffects() {
    HealthStatus happyResult = happyStrategy.applyInteraction(initialHealth, Action.PLAY);
    HealthStatus sadResult = sadStrategy.applyInteraction(initialHealth, Action.PLAY);

    // Happy: 50 + 7 = 57, Sad: 50 + (7/2) = 53
    assertEquals("Happy playing should increase social to 57", 57, happyResult.getSocial());
    assertEquals("Sad playing should increase social to 53", 53, sadResult.getSocial());

    // Happy: 50 + 1 = 51, Sad: 50 + 2 = 52
    assertEquals("Happy playing should increase hunger to 51", 51, happyResult.getHunger());
    assertEquals("Sad playing should increase hunger to 52", 52, sadResult.getHunger());
  }

  @Test
  public void testCompareHappyVsSadStepEffects() {
    HealthStatus happyResult = happyStrategy.applyStep(initialHealth);

    // Happy: 50 + 3 = 53
    assertEquals("Happy step should increase hunger to 53", 53, happyResult.getHunger());

    // Happy: 50 - 2 = 48
    assertEquals("Happy step should decrease hygiene to 48", 48, happyResult.getHygiene());
    assertEquals("Happy step should decrease social to 48", 48, happyResult.getSocial());
    assertEquals("Happy step should decrease sleep to 48", 48, happyResult.getSleep());

    // For sad mood, we can only check the exact hunger since other attributes use random
    HealthStatus sadResult = sadStrategy.applyStep(initialHealth);
    assertEquals("Sad step should increase hunger to 53", 53, sadResult.getHunger());
  }
}