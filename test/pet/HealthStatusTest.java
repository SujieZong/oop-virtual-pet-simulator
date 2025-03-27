package pet;

import static org.junit.Assert.*;

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
}