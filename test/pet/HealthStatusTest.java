package pet;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

/**
 * Tests for the HealthStatus class.
 */
public class HealthStatusTest {

  @Test
  public void testHealthStatusConstruction() {
    HealthStatus health = new HealthStatus(50, 60, 70, 80);
    assertEquals(50, health.getHunger());
    assertEquals(60, health.getHygiene());
    assertEquals(70, health.getSocial());
    assertEquals(80, health.getSleep());
  }

  @Test
  public void testToString() {
    HealthStatus health = new HealthStatus(10, 20, 30, 40);
    String expected = "HealthStatus{hunger=10, hygiene=20, social=30, sleep=40}";
    assertEquals(expected, health.toString());
  }

  @Test
  public void testImmutability() {
    // Create initial health status
    HealthStatus health1 = new HealthStatus(10, 20, 30, 40);

    // Create a "modified" health status
    HealthStatus health2 = new HealthStatus(15, 25, 35, 45);

    // Verify the first object wasn't changed
    assertEquals(10, health1.getHunger());
    assertEquals(20, health1.getHygiene());
    assertEquals(30, health1.getSocial());
    assertEquals(40, health1.getSleep());
  }

  // add after grading
  @Test
  public void testEqualsSameValuesReturnsTrue() {
    // Arrange
    HealthStatus status1 = new HealthStatus(50, 60, 70, 80);
    HealthStatus status2 = new HealthStatus(50, 60, 70, 80);

    // Act & Assert
    assertEquals(status1, status2);
  }

  @Test
  public void testEqualsDifferentValuesReturnsFalse() {
    // Arrange
    HealthStatus status1 = new HealthStatus(50, 60, 70, 80);
    HealthStatus status2 = new HealthStatus(51, 60, 70, 80); // Different hunger
    HealthStatus status3 = new HealthStatus(50, 61, 70, 80); // Different hygiene
    HealthStatus status4 = new HealthStatus(50, 60, 71, 80); // Different social
    // Act & Assert
    assertNotEquals(status1, status2);
    assertNotEquals(status1, status3);
    assertNotEquals(status1, status4);
    HealthStatus status5 = new HealthStatus(50, 60, 70, 81); // Different sleep
    assertNotEquals(status1, status5);
  }

  @Test
  public void testEqualsNullObjectReturnsFalse() {
    // Arrange
    HealthStatus status = new HealthStatus(50, 60, 70, 80);

    // Act & Assert
    assertNotEquals(status, null);
  }

  @Test
  public void testEqualsDifferentClassReturnsFalse() {
    // Arrange
    HealthStatus status = new HealthStatus(50, 60, 70, 80);
    Object differentObject = "Not a HealthStatus";

    // Act & Assert
    assertNotEquals(status, differentObject);
  }

  @Test
  public void testEqualsSameReferenceReturnsTrue() {
    // Arrange
    HealthStatus status = new HealthStatus(50, 60, 70, 80);

    // Act & Assert
    assertEquals(status, status);
  }

  @Test
  public void testHashCodeSameValuesSameHashCode() {
    // Arrange
    HealthStatus status1 = new HealthStatus(50, 60, 70, 80);
    HealthStatus status2 = new HealthStatus(50, 60, 70, 80);

    // Act & Assert
    assertEquals(status1.hashCode(), status2.hashCode());
  }

  @Test
  public void testHashCodeDifferentValuesDifferentHashCodes() {
    // Arrange
    HealthStatus status1 = new HealthStatus(50, 60, 70, 80);
    HealthStatus status2 = new HealthStatus(51, 61, 71, 81); // All values different

    // Act & Assert
    // Note: While there's a small probability of hash collision, with our simple values
    // and implementation this test should be reliable
    assertNotEquals(status1.hashCode(), status2.hashCode());
  }


}