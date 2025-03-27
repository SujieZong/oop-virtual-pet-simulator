package pet;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Tests for the different strategy implementations.
 */
public class StrategyTest {

  @Test
  public void testHappyStrategyStep() {
    MoodStrategy happyStrategy = new HappyStrategy(3, 2, 1, 2, 7);
    HealthStatus initialHealth = new HealthStatus(50, 50, 50, 50);

    HealthStatus newHealth = happyStrategy.applyStep(initialHealth);

    // Verify the expected changes
    assertEquals(53, newHealth.getHunger());    // 50 + 3
    assertEquals(48, newHealth.getHygiene());   // 50 - 2
    assertEquals(49, newHealth.getSocial());    // 50 - 1
    assertEquals(48, newHealth.getSleep());     // 50 - 2
  }

  @Test
  public void testHappyStrategyInteractions() {
    MoodStrategy happyStrategy = new HappyStrategy(3, 2, 1, 2, 7);
    HealthStatus initialHealth = new HealthStatus(50, 50, 50, 50);

    // Test FEED action
    HealthStatus afterFeed = happyStrategy.applyInteraction(initialHealth, Action.FEED);
    assertEquals(36, afterFeed.getHunger());    // 50 - (2 * 7)
    assertEquals(50, afterFeed.getHygiene());
    assertEquals(50, afterFeed.getSocial());
    assertEquals(50, afterFeed.getSleep());

    // Test PLAY action
    HealthStatus afterPlay = happyStrategy.applyInteraction(initialHealth, Action.PLAY);
    assertEquals(51, afterPlay.getHunger());    // 50 + 1
    assertEquals(50, afterPlay.getHygiene());
    assertEquals(57, afterPlay.getSocial());    // 50 + 7
    assertEquals(50, afterPlay.getSleep());

    // Test CLEAN action
    HealthStatus afterClean = happyStrategy.applyInteraction(initialHealth, Action.CLEAN);
    assertEquals(50, afterClean.getHunger());
    assertEquals(57, afterClean.getHygiene());  // 50 + 7
    assertEquals(50, afterClean.getSocial());
    assertEquals(50, afterClean.getSleep());

    // Test SLEEP action
    HealthStatus afterSleep = happyStrategy.applyInteraction(initialHealth, Action.SLEEP);
    assertEquals(50, afterSleep.getHunger());
    assertEquals(50, afterSleep.getHygiene());
    assertEquals(50, afterSleep.getSocial());
    assertEquals(57, afterSleep.getSleep());    // 50 + 7
  }

  @Test
  public void testSadStrategyStep() {
    MoodStrategy sadStrategy = new SadStrategy(5, 3, 2, 3, 4);
    HealthStatus initialHealth = new HealthStatus(50, 50, 50, 50);

    HealthStatus newHealth = sadStrategy.applyStep(initialHealth);

    // Verify the changes are greater than the base rates
    // Note: SadStrategy has random components, so we can't test exact values
    assertTrue(newHealth.getHunger() >= 55);    // At least 50 + 5
    assertTrue(newHealth.getHygiene() <= 47);   // At most 50 - 3
    assertTrue(newHealth.getSocial() <= 48);    // At most 50 - 2
    assertTrue(newHealth.getSleep() <= 47);     // At most 50 - 3
  }

  @Test
  public void testSadStrategyInteractions() {
    MoodStrategy sadStrategy = new SadStrategy(5, 3, 2, 3, 4);
    HealthStatus initialHealth = new HealthStatus(50, 50, 50, 50);

    // Test FEED action
    HealthStatus afterFeed = sadStrategy.applyInteraction(initialHealth, Action.FEED);
    assertEquals(46, afterFeed.getHunger());    // 50 - 4 (less effective than happy)
    assertEquals(50, afterFeed.getHygiene());
    assertEquals(50, afterFeed.getSocial());
    assertEquals(50, afterFeed.getSleep());

    // Test PLAY action
    HealthStatus afterPlay = sadStrategy.applyInteraction(initialHealth, Action.PLAY);
    assertEquals(52, afterPlay.getHunger());    // 50 + 2 (more hunger increase than happy)
    assertEquals(50, afterPlay.getHygiene());
    assertEquals(52, afterPlay.getSocial());    // 50 + (4/2) = 52 (less social benefit)
    assertEquals(50, afterPlay.getSleep());

    // Test CLEAN action
    HealthStatus afterClean = sadStrategy.applyInteraction(initialHealth, Action.CLEAN);
    assertEquals(50, afterClean.getHunger());
    assertEquals(54, afterClean.getHygiene());  // 50 + 4
    assertEquals(50, afterClean.getSocial());
    assertEquals(50, afterClean.getSleep());

    // Test SLEEP action
    HealthStatus afterSleep = sadStrategy.applyInteraction(initialHealth, Action.SLEEP);
    assertEquals(50, afterSleep.getHunger());
    assertEquals(50, afterSleep.getHygiene());
    assertEquals(50, afterSleep.getSocial());
    assertEquals(54, afterSleep.getSleep());    // 50 + 4
  }

  @Test
  public void testValueBoundaryHandling() {
    // Test that values are properly clamped at boundaries
    MoodStrategy happyStrategy = new HappyStrategy(10, 10, 10, 10, 20);

    // Test clamping at the maximum
    HealthStatus maxHealth = new HealthStatus(95, 50, 50, 50);
    HealthStatus afterStep = happyStrategy.applyStep(maxHealth);
    assertEquals(100, afterStep.getHunger());  // Should be clamped at 100

    // Test clamping at the minimum
    HealthStatus minHealth = new HealthStatus(50, 5, 5, 5);
    HealthStatus afterStep2 = happyStrategy.applyStep(minHealth);
    assertEquals(0, afterStep2.getHygiene());  // Should be clamped at 0
    assertEquals(0, afterStep2.getSocial());   // Should be clamped at 0
    assertEquals(0, afterStep2.getSleep());    // Should be clamped at 0

    // Test interaction clamping
    HealthStatus lowHunger = new HealthStatus(5, 50, 50, 50);
    HealthStatus afterFeed = happyStrategy.applyInteraction(lowHunger, Action.FEED);
    assertEquals(0, afterFeed.getHunger());    // 5 - (2 * 20) would be < 0, clamp at 0
  }
}