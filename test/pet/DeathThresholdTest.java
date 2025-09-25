package pet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
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

  // add after grading
  @Test
  public void testEquals_SameValues_ReturnsTrue() {
    // Arrange
    DeathThreshold threshold1 = new DeathThreshold(85, 15, 15, 10);
    DeathThreshold threshold2 = new DeathThreshold(85, 15, 15, 10);

    // Act & Assert
    assertEquals(threshold1, threshold2);
  }

  @Test
  public void testEquals_DifferentValues_ReturnsFalse() {
    // Arrange
    DeathThreshold threshold1 = new DeathThreshold(85, 15, 15, 10);
    DeathThreshold threshold2 = new DeathThreshold(90, 15, 15, 10); // Different hungerLimit
    DeathThreshold threshold3 = new DeathThreshold(85, 20, 15, 10); // Different hygieneLimit
    DeathThreshold threshold4 = new DeathThreshold(85, 15, 20, 10); // Different socialLimit
    assertNotEquals(threshold1, threshold2);
    assertNotEquals(threshold1, threshold3);
    assertNotEquals(threshold1, threshold4);
    DeathThreshold threshold5 = new DeathThreshold(85, 15, 15, 15); // Different sleepLimit
    assertNotEquals(threshold1, threshold5);
  }

  @Test
  public void testEqualsNullObjectReturnsFalse() {
    // Arrange
    DeathThreshold threshold = new DeathThreshold(85, 15, 15, 10);

    // Act & Assert
    assertNotEquals(threshold, null);
  }

  @Test
  public void testEqualsDifferentClassReturnsFalse() {
    // Arrange
    DeathThreshold threshold = new DeathThreshold(85, 15, 15, 10);
    Object differentObject = "Not a DeathThreshold";

    // Act & Assert
    assertNotEquals(threshold, differentObject);
  }

  @Test
  public void testEqualsSameReferenceReturnsTrue() {
    // Arrange
    DeathThreshold threshold = new DeathThreshold(85, 15, 15, 10);

    // Act & Assert
    assertEquals(threshold, threshold);
  }

  @Test
  public void testHashCodeSameValuesSameHashCode() {
    // Arrange
    DeathThreshold threshold1 = new DeathThreshold(85, 15, 15, 10);
    DeathThreshold threshold2 = new DeathThreshold(85, 15, 15, 10);

    // Act & Assert
    assertEquals(threshold1.hashCode(), threshold2.hashCode());
  }

  @Test
  public void testHashCodeDifferentValuesDifferentHashCodes() {
    // Arrange
    DeathThreshold threshold1 = new DeathThreshold(85, 15, 15, 10);
    DeathThreshold threshold2 = new DeathThreshold(90, 20, 20, 15); // All values different

    // Act & Assert
    assertNotEquals(threshold1.hashCode(), threshold2.hashCode());
  }
}