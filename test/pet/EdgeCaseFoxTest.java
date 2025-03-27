package pet;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Tests focused on edge cases and unusual situations specific to the Fox pet.
 */
public class EdgeCaseFoxTest {

  @Test
  public void testFoxHighHungerTolerance() {
    // Fox has a higher hunger death threshold (90) than other pets
    Fox fox = new Fox();

    // Set hunger to just below threshold
    fox.setHealthStateAndUpdateState(89, 50, 50, 50);

    assertFalse(fox.isDead());

    // Set hunger to threshold
    fox.setHealthStateAndUpdateState(90, 50, 50, 50);

    assertFalse(fox.isDead());

    // Set hunger just above threshold
    fox.setHealthStateAndUpdateState(91, 50, 50, 50);

    assertTrue(fox.isDead());
  }

  @Test
  public void testFoxHighSocialNeeds() {
    // Fox has higher social needs than other pets
    Fox fox = new Fox();

    // Test social death threshold (20)
    fox.health = new HealthStatus(50, 50, 21, 50);
    fox.checkDeath();
    assertFalse(fox.isDead());

    fox.health = new HealthStatus(50, 50, 20, 50);
    fox.checkDeath();
    assertFalse(fox.isDead());

    fox.health = new HealthStatus(50, 50, 19, 50);
    fox.checkDeath();
    assertTrue(fox.isDead());

    // Test social sad threshold (35)
    Fox happyFox = new Fox();
    happyFox.health = new HealthStatus(50, 50, 36, 50);
    happyFox.updateMood();
    assertEquals(MoodEnum.HAPPY, happyFox.getMood());

    happyFox.health = new HealthStatus(50, 50, 35, 50);
    happyFox.updateMood();
    assertEquals(MoodEnum.HAPPY, happyFox.getMood());

    happyFox.health = new HealthStatus(50, 50, 34, 50);
    happyFox.updateMood();
    assertEquals(MoodEnum.SAD, happyFox.getMood());
  }

  @Test
  public void testFoxHighHygieneNeeds() {
    // Fox has specific hygiene thresholds
    Fox fox = new Fox();

    // Test hygiene death threshold (20)
    fox.health = new HealthStatus(50, 21, 50, 50);
    fox.checkDeath();
    assertFalse(fox.isDead());

    fox.health = new HealthStatus(50, 20, 50, 50);
    fox.checkDeath();
    assertFalse(fox.isDead());

    fox.health = new HealthStatus(50, 19, 50, 50);
    fox.checkDeath();
    assertTrue(fox.isDead());

    // Test hygiene sad threshold (35)
    Fox happyFox = new Fox();
    happyFox.health = new HealthStatus(50, 36, 50, 50);
    happyFox.updateMood();
    assertEquals(MoodEnum.HAPPY, happyFox.getMood());

    happyFox.health = new HealthStatus(50, 35, 50, 50);
    happyFox.updateMood();
    assertEquals(MoodEnum.HAPPY, happyFox.getMood());

    happyFox.health = new HealthStatus(50, 34, 50, 50);
    happyFox.updateMood();
    assertEquals(MoodEnum.SAD, happyFox.getMood());
  }

  @Test
  public void testFoxMoodChangeDuringPlay() {
    // Fox should get much benefit from play for social
    Fox fox = new Fox();

    // Put fox in a state where it's close to being sad due to low social
    fox.health = new HealthStatus(50, 50, 36, 50);
    fox.updateMood();
    assertEquals(MoodEnum.HAPPY, fox.getMood());

    // Take a step to reduce social below threshold
    fox.step();
    assertEquals(MoodEnum.SAD, fox.getMood());

    // Now play with the fox
    fox.interactWith(Action.PLAY);

    // The play should have improved social enough to make fox happy again
    assertEquals(MoodEnum.HAPPY, fox.getMood());
  }

  @Test
  public void testFoxBoundaryConditions() {
    Fox fox = new Fox();

    // Multiple attributes at threshold simultaneously
    fox.health = new HealthStatus(70, 35, 35, 25);
    fox.updateMood();
    assertEquals(MoodEnum.HAPPY, fox.getMood());

    // Shift one attribute just past threshold
    fox.health = new HealthStatus(71, 35, 35, 25);
    fox.updateMood();
    assertEquals(MoodEnum.SAD, fox.getMood());

    // Restore and shift a different attribute
    fox.health = new HealthStatus(70, 34, 35, 25);
    fox.updateMood();
    assertEquals(MoodEnum.SAD, fox.getMood());

    // Test death threshold boundaries
    fox = new Fox();
    fox.health = new HealthStatus(90, 20, 20, 10);
    fox.checkDeath();
    assertFalse(fox.isDead());

    // Shift one value beyond threshold
    fox.health = new HealthStatus(91, 20, 20, 10);
    fox.checkDeath();
    assertTrue(fox.isDead());
  }

  @Test
  public void testFoxInteractionEffects() {
    Fox fox = new Fox();

    // Test feeding when almost full
    fox.health = new HealthStatus(95, 50, 50, 50);
    fox.interactWith(Action.FEED);
    HealthStatus afterFeed = fox.getHealth();
    // HappyStrategy: 95 - (2 * 8) = 79
    assertEquals(79, afterFeed.getHunger());

    // Test playing when almost dead from hunger
    fox = new Fox();
    fox.health = new HealthStatus(89, 50, 50, 50);
    fox.interactWith(Action.PLAY);
    HealthStatus afterPlay = fox.getHealth();
    // Playing increases hunger: 89 + 1 = 90
    assertEquals(90, afterPlay.getHunger());
    // This should be at the death threshold but not over it
    assertFalse(fox.isDead());

    // One more play should kill the fox due to hunger
    fox.interactWith(Action.PLAY);
    assertTrue(fox.isDead());
  }

  @Test
  public void testFoxSadVsHappyStrategies() {
    // Test different behaviors between happy and sad fox
    Fox happyFox = new Fox();
    happyFox.setMood(MoodEnum.HAPPY);

    Fox sadFox = new Fox();
    sadFox.setMood(MoodEnum.SAD);

    // Feeding should be more effective when happy
    HealthStatus happyBefore = happyFox.getHealth();
    HealthStatus sadBefore = sadFox.getHealth();

    happyFox.interactWith(Action.FEED);
    sadFox.interactWith(Action.FEED);

    int happyHungerReduction = happyBefore.getHunger() - happyFox.getHealth().getHunger();
    int sadHungerReduction = sadBefore.getHunger() - sadFox.getHealth().getHunger();

    assertTrue("Happy fox should get more benefit from feeding",
        happyHungerReduction > sadHungerReduction);
  }
}