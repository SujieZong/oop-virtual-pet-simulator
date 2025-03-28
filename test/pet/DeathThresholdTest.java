package pet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Tests for the DeathThreshold class.
 */
public class DeathThresholdTest {

  @Test
  public void testDeathThresholdConstruction() {
    DeathThreshold threshold = new DeathThreshold(85, 15, 10, 5);
    assertEquals(85, threshold.getHungerLimit());
    assertEquals(15, threshold.getHygieneLimit());
    assertEquals(10, threshold.getSocialLimit());
    assertEquals(5, threshold.getSleepLimit());
  }

  @Test
  public void testToString() {
    DeathThreshold threshold = new DeathThreshold(85, 15, 10, 5);
    String expected =
        "DeathThreshold{hungerLimit=85, hygieneLimit=15, socialLimit=10, sleepLimit=5}";
    assertEquals(expected, threshold.toString());
  }

  @Test
  public void testIsDeadCondition() {
    DeathThreshold threshold = new DeathThreshold(85, 15, 10, 5);

    // All values safe
    assertFalse(threshold.isDeadCondition(50, 50, 50, 50));

    // Each value at the threshold (still safe)
    assertFalse(threshold.isDeadCondition(85, 50, 50, 50)); // Hunger at limit
    assertFalse(threshold.isDeadCondition(50, 15, 50, 50)); // Hygiene at limit
    assertFalse(threshold.isDeadCondition(50, 50, 10, 50)); // Social at limit
    assertFalse(threshold.isDeadCondition(50, 50, 50, 5));  // Sleep at limit

    // Each value past the threshold (should trigger death)
    assertTrue(threshold.isDeadCondition(86, 50, 50, 50)); // Hunger too high
    assertTrue(threshold.isDeadCondition(50, 14, 50, 50)); // Hygiene too low
    assertTrue(threshold.isDeadCondition(50, 50, 9, 50));  // Social too low
    assertTrue(threshold.isDeadCondition(50, 50, 50, 4));  // Sleep too low

    // Multiple thresholds crossed
    assertTrue(threshold.isDeadCondition(90, 10, 5, 2));
  }
}