package pet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for the Fox pet type.
 * Tests initialization, interactions, health changes, mood transitions,
 * and death conditions specific to Fox.
 */
public class FoxTest {
  // Constants from Fox class
  private static final int HAPPY_HUNGER_RATE = 3;
  private static final int HAPPY_HYGIENE_RATE = 2;
  private static final int HAPPY_SOCIAL_RATE = 2;
  private static final int HAPPY_SLEEP_RATE = 2;
  private static final int HAPPY_ACTION_BOOST = 8;

  private static final int SAD_HUNGER_RATE = 5;
  private static final int SAD_HYGIENE_RATE = 3;
  private static final int SAD_SOCIAL_RATE = 3;
  private static final int SAD_SLEEP_RATE = 3;
  private static final int SAD_ACTION_BOOST = 4;

  private Fox fox;

  @Before
  public void setUp() {
    fox = new Fox();
  }

  // Initialization tests

  @Test
  public void testFoxInitialization() {
    // Verify initial health values
    assertEquals("Initial hunger should be 50", 50, fox.getHealth().getHunger());
    assertEquals("Initial hygiene should be 50", 50, fox.getHealth().getHygiene());
    assertEquals("Initial social should be 50", 50, fox.getHealth().getSocial());
    assertEquals("Initial sleep should be 50", 50, fox.getHealth().getSleep());

    // Verify initial mood (should be HAPPY with default values)
    assertEquals("Initial mood should be HAPPY", MoodEnum.HAPPY, fox.getMood());

    // Verify fox is alive
    assertFalse("Fox should be alive initially", fox.isDead());
  }

  @Test
  public void testFoxDeathThresholds() {
    DeathThreshold threshold = fox.getDeathThreshold();

    // Verify threshold values based on Fox constants
    assertEquals("Hunger limit should be 90", 90, threshold.getHungerLimit());
    assertEquals("Hygiene limit should be 20", 20, threshold.getHygieneLimit());
    assertEquals("Social limit should be 20", 20, threshold.getSocialLimit());
    assertEquals("Sleep limit should be 10", 10, threshold.getSleepLimit());
  }

  // Interaction tests in HAPPY mood

  @Test
  public void testFeedingInHappyMood() {
    // Ensure fox is in happy mood
    fox.setMood(MoodEnum.HAPPY);
    fox.setHealthStateAndUpdateState(50, 50, 50, 50);

    // Feed the fox
    fox.interactWith(Action.FEED);

    // Verify exact hunger decrease (2 * action boost)
    assertEquals("Feeding in happy mood should decrease hunger by 2 * action boost",
        34, fox.getHealth().getHunger()); // 50 - (2 * 8)
  }

  @Test
  public void testPlayingInHappyMood() {
    // Ensure fox is in happy mood
    fox.setMood(MoodEnum.HAPPY);
    fox.setHealthStateAndUpdateState(50, 50, 50, 50);

    // Play with the fox
    fox.interactWith(Action.PLAY);

    // Verify exact play effects
    assertEquals("Playing in happy mood should increase social by action boost",
        58, fox.getHealth().getSocial()); // 50 + 8

    assertEquals("Playing should increase hunger by 1",
        51, fox.getHealth().getHunger()); // 50 + 1
  }

  @Test
  public void testCleaningInHappyMood() {
    // Ensure fox is in happy mood
    fox.setMood(MoodEnum.HAPPY);
    fox.setHealthStateAndUpdateState(50, 50, 50, 50);

    // Clean the fox
    fox.interactWith(Action.CLEAN);

    // Verify exact cleaning effect
    assertEquals("Cleaning in happy mood should increase hygiene by action boost",
        58, fox.getHealth().getHygiene()); // 50 + 8
  }

  @Test
  public void testSleepingInHappyMood() {
    // Ensure fox is in happy mood
    fox.setMood(MoodEnum.HAPPY);
    fox.setHealthStateAndUpdateState(50, 50, 50, 50);

    // Put fox to sleep
    fox.interactWith(Action.SLEEP);

    // Verify exact sleeping effect
    assertEquals("Sleeping in happy mood should increase sleep by action boost",
        58, fox.getHealth().getSleep()); // 50 + 8
  }

  // Interaction tests in SAD mood

  @Test
  public void testFeedingInSadMood() {
    // Set fox to sad mood

    fox.setHealthStateAndUpdateState(50, 50, 50, 50);
    fox.setMood(MoodEnum.SAD);
    // Feed the fox
    fox.interactWith(Action.FEED);

    // Verify exact hunger decrease (action boost)
    assertEquals("Feeding in sad mood should decrease hunger by action boost",
        46, fox.getHealth().getHunger()); // 50 - 4
  }

  @Test
  public void testPlayingInSadMood() {
    // Set fox to sad mood

    fox.setHealthStateAndUpdateState(50, 50, 50, 50);
    fox.setMood(MoodEnum.SAD);

    // Play with the fox
    fox.interactWith(Action.PLAY);

    // Verify exact play effects
    assertEquals("Playing in sad mood should increase social by half action boost",
        52, fox.getHealth().getSocial()); // 50 + (4/2)

    assertEquals("Playing in sad mood should increase hunger by 2",
        52, fox.getHealth().getHunger()); // 50 + 2
  }

  // Time step tests

  @Test
  public void testStepChangesInHappyMood() {
    // Ensure fox is in happy mood
    fox.setMood(MoodEnum.HAPPY);
    fox.setHealthStateAndUpdateState(50, 50, 50, 50);

    // Apply time step
    fox.step();

    // Verify exact health changes
    assertEquals("Hunger should increase by exactly the happy hunger rate",
        53, fox.getHealth().getHunger()); // 50 + 3

    assertEquals("Hygiene should decrease by exactly the happy hygiene rate",
        48, fox.getHealth().getHygiene()); // 50 - 2

    assertEquals("Social should decrease by exactly the happy social rate",
        48, fox.getHealth().getSocial()); // 50 - 2

    assertEquals("Sleep should decrease by exactly the happy sleep rate",
        48, fox.getHealth().getSleep()); // 50 - 2
  }

  @Test
  public void testStepChangesInSadMood() {
    // Set fox to sad mood

    fox.setHealthStateAndUpdateState(50, 50, 50, 50);
    fox.setMood(MoodEnum.SAD);

    // Apply time step
    fox.step();

    // Verify exact hunger increase
    assertEquals("Hunger should increase by exactly the sad hunger rate",
        55, fox.getHealth().getHunger()); // 50 + 5

    // For other stats, we can only check ranges due to random component
    assertTrue("Hygiene should decrease by 3-6 points",
        fox.getHealth().getHygiene() >= 44
            &&
            fox.getHealth().getHygiene() <= 47); // 50 - (3 + 0-3 random)

    assertTrue("Social should decrease by 3-6 points",
        fox.getHealth().getSocial() >= 44
            &&
            fox.getHealth().getSocial() <= 47); // 50 - (3 + 0-3 random)

    assertTrue("Sleep should decrease by 3-6 points",
        fox.getHealth().getSleep() >= 44
            &&
            fox.getHealth().getSleep() <= 47); // 50 - (3 + 0-3 random)
  }

  // Mood transition tests

  @Test
  public void testMoodTransitionToSad() {
    // Start in happy mood
    fox.setMood(MoodEnum.HAPPY);
    assertEquals(MoodEnum.HAPPY, fox.getMood());

    // Set health to trigger sad mood
    // Hunger > HUNGER_SAD_THRESHOLD (70)
    fox.setHealthStateAndUpdateState(71, 50, 50, 50);

    // Force mood update
    fox.step();

    // Verify mood changed to SAD
    assertEquals("Mood should change to SAD", MoodEnum.SAD, fox.getMood());
  }

  @Test
  public void testMoodTransitionToHappy() {
    // Start in sad mood
    fox.setMood(MoodEnum.SAD);
    assertEquals(MoodEnum.SAD, fox.getMood());

    // Set health to happy levels
    fox.setHealthStateAndUpdateState(65, 50, 50, 50);

    // Force mood update
    fox.step();

    // Verify mood changed to HAPPY
    assertEquals("Mood should change to HAPPY", MoodEnum.HAPPY, fox.getMood());
  }

  @Test
  public void testAllSadMoodTriggers() {
    // Test each trigger for sad mood individually

    // 1. High hunger
    fox.setHealthStateAndUpdateState(71, 50, 50, 50); // Hunger > 70 threshold
    fox.step();
    assertEquals("High hunger should trigger sad mood", MoodEnum.SAD, fox.getMood());

    // Reset to happy
    fox.setHealthStateAndUpdateState(50, 50, 50, 50);
    fox.step();

    // 2. Low hygiene
    fox.setHealthStateAndUpdateState(50, 34, 50, 50); // Hygiene < 35 threshold
    fox.step();
    assertEquals("Low hygiene should trigger sad mood", MoodEnum.SAD, fox.getMood());

    // Reset to happy
    fox.setHealthStateAndUpdateState(50, 50, 50, 50);
    fox.step();

    // 3. Low social
    fox.setHealthStateAndUpdateState(50, 50, 34, 50); // Social < 35 threshold
    fox.step();
    assertEquals("Low social should trigger sad mood", MoodEnum.SAD, fox.getMood());

    // Reset to happy
    fox.setHealthStateAndUpdateState(50, 50, 50, 50);
    fox.step();

    // 4. Low sleep
    fox.setHealthStateAndUpdateState(50, 50, 50, 24); // Sleep < 25 threshold
    fox.step();
    assertEquals("Low sleep should trigger sad mood", MoodEnum.SAD, fox.getMood());
  }

  // Death condition tests

  @Test
  public void testDeathByHunger() {
    // Set hunger above threshold
    fox.setHealthStateAndUpdateState(91, 50, 50, 50); // > 90 hunger limit

    // Force update
    fox.step();

    // Verify fox is dead
    assertTrue("Fox should die when hunger exceeds threshold", fox.isDead());
  }

  @Test
  public void testDeathByLowHygiene() {
    // Set hygiene below threshold
    fox.setHealthStateAndUpdateState(50, 19, 50, 50); // < 20 hygiene limit

    // Force update
    fox.step();

    // Verify fox is dead
    assertTrue("Fox should die when hygiene is below threshold", fox.isDead());
  }

  @Test
  public void testDeathByLowSocial() {
    // Set social below threshold
    fox.setHealthStateAndUpdateState(50, 50, 19, 50); // < 20 social limit

    // Force update
    fox.step();

    // Verify fox is dead
    assertTrue("Fox should die when social is below threshold", fox.isDead());
  }

  @Test
  public void testDeathByLowSleep() {
    // Set sleep below threshold
    fox.setHealthStateAndUpdateState(50, 50, 50, 9); // < 10 sleep limit

    // Force update
    fox.step();

    // Verify fox is dead
    assertTrue("Fox should die when sleep is below threshold", fox.isDead());
  }

  // No interactions when dead

  @Test
  public void testNoInteractionsWhenDead() {
    // Kill the fox
    fox.setHealthStateAndUpdateState(91, 50, 50, 50); // > hunger limit
    fox.step();
    assertTrue("Fox should be dead", fox.isDead());

    // Get current health values
    int initialHunger = fox.getHealth().getHunger();
    fox.interactWith(Action.FEED);
    assertEquals("Dead fox's hunger shouldn't change", initialHunger, fox.getHealth().getHunger());

    int initialHygiene = fox.getHealth().getHygiene();
    fox.interactWith(Action.CLEAN);
    assertEquals("Dead fox's hygiene shouldn't change", initialHygiene,
        fox.getHealth().getHygiene());

    int initialSocial = fox.getHealth().getSocial();
    fox.interactWith(Action.PLAY);
    assertEquals("Dead fox's social shouldn't change", initialSocial, fox.getHealth().getSocial());

    int initialSleep = fox.getHealth().getSleep();
    fox.interactWith(Action.SLEEP);
    assertEquals("Dead fox's sleep shouldn't change", initialSleep, fox.getHealth().getSleep());
    // Verify health didn't change
  }

  @Test
  public void testNoStepChangeWhenDead() {
    // Kill the fox
    fox.setHealthStateAndUpdateState(91, 50, 50, 50); // > hunger limit
    fox.step();
    assertTrue("Fox should be dead", fox.isDead());

    // Get current health values
    final int initialHunger = fox.getHealth().getHunger();
    final int initialHygiene = fox.getHealth().getHygiene();
    final int initialSocial = fox.getHealth().getSocial();
    final int initialSleep = fox.getHealth().getSleep();
    fox.step(); // Try step
    assertEquals("Dead fox's hunger shouldn't change", initialHunger, fox.getHealth().getHunger());
    assertEquals("Dead fox's hygiene shouldn't change", initialHygiene,
        fox.getHealth().getHygiene());
    assertEquals("Dead fox's social shouldn't change", initialSocial, fox.getHealth().getSocial());
    assertEquals("Dead fox's sleep shouldn't change", initialSleep, fox.getHealth().getSleep());
  }
  // Edge cases

  @Test
  public void testMinMaxHealthValues() {
    // Test minimum values
    fox.setHealthStateAndUpdateState(0, 0, 0, 0);

    fox.revive();
    fox.step();

    // Verify exact minimum values are respected
    assertEquals("Hygiene can't go below 0", 0, fox.getHealth().getHygiene());
    assertEquals("Social can't go below 0", 0, fox.getHealth().getSocial());
    assertEquals("Sleep can't go below 0", 0, fox.getHealth().getSleep());
    assertEquals("Hunger increase", 5,
        fox.getHealth().getHunger());


    // Test maximum values
    Fox fox1 = new Fox();
    fox1.setHealthStateAndUpdateState(1, 95, 95, 95);


    // Try interactions that would increase values

    fox1.interactWith(Action.CLEAN);
    fox1.interactWith(Action.PLAY);
    fox1.interactWith(Action.FEED);
    fox1.interactWith(Action.SLEEP);


    // Verify exact maximum values are respected
    assertEquals("Hygiene can't exceed 100", 100, fox1.getHealth().getHygiene());
    assertEquals("Social can't exceed 100", 100, fox1.getHealth().getSocial());
    assertEquals("Sleep can't exceed 100", 100, fox1.getHealth().getSleep());
    assertEquals("Hunger can't go below 0", 0, fox1.getHealth().getHunger());

    fox1.setHealthStateAndUpdateState(99, 95, 95, 95);
    fox1.revive();
    fox1.step();
    assertEquals("Hunger can't exceed 100", 100, fox1.getHealth().getHunger());


  }


  @Test
  public void testExtendedMoodChanges() {
    // Test specific mood changes with multiple scenarios

    // 1. Start happy

    fox.setHealthStateAndUpdateState(50, 50, 50, 50);
    assertEquals(MoodEnum.HAPPY, fox.getMood());

    // 2. Set values just above/below thresholds to test exact transition points

    // Just over hunger threshold (70)
    fox.setHealthStateAndUpdateState(71, 50, 50, 50);

    assertEquals("Hunger = 71 should trigger sad mood", MoodEnum.SAD, fox.getMood());

    // Just under hunger threshold (70)
    fox.setHealthStateAndUpdateState(69, 50, 50, 50);

    assertEquals("Hunger = 69 should not trigger sad mood", MoodEnum.HAPPY, fox.getMood());

    // Just under hygiene threshold (35)
    fox.setHealthStateAndUpdateState(50, 34, 50, 50);

    assertEquals("Hygiene = 34 should trigger sad mood", MoodEnum.SAD, fox.getMood());

    // Just over hygiene threshold (35)
    fox.setHealthStateAndUpdateState(50, 36, 50, 50);

    assertEquals("Hygiene = 36 should not trigger sad mood", MoodEnum.HAPPY, fox.getMood());
  }

  @Test
  public void testBoundaryConditions() {
    // Test exact boundary conditions for fox thresholds

    // 1. Test exact hunger threshold boundary (70)
    fox.setHealthStateAndUpdateState(70, 50, 50, 50); // Exactly at threshold

    assertEquals("Hunger exactly at threshold (70) should not trigger sad mood", MoodEnum.HAPPY,
        fox.getMood());

    fox.setHealthStateAndUpdateState(71, 50, 50, 50); // Just above threshold

    assertEquals("Hunger just above threshold (71) should trigger sad mood", MoodEnum.SAD,
        fox.getMood());

    // 2. Test exact hygiene threshold boundary (35)
    fox.setHealthStateAndUpdateState(50, 35, 50, 50); // Exactly at threshold

    assertEquals("Hygiene exactly at threshold (35) should not trigger sad mood", MoodEnum.HAPPY,
        fox.getMood());

    fox.setHealthStateAndUpdateState(50, 34, 50, 50); // Just below threshold

    assertEquals("Hygiene just below threshold (34) should trigger sad mood", MoodEnum.SAD,
        fox.getMood());

    // 3. Test exact social threshold boundary (35)
    fox.setHealthStateAndUpdateState(50, 50, 35, 50); // Exactly at threshold

    assertEquals("Social exactly at threshold (35) should not trigger sad mood", MoodEnum.HAPPY,
        fox.getMood());

    fox.setHealthStateAndUpdateState(50, 50, 34, 50); // Just below threshold

    assertEquals("Social just below threshold (34) should trigger sad mood", MoodEnum.SAD,
        fox.getMood());

    // 4. Test exact sleep threshold boundary (25)
    fox.setHealthStateAndUpdateState(50, 50, 50, 25); // Exactly at threshold

    assertEquals("Sleep exactly at threshold (25) should not trigger sad mood", MoodEnum.HAPPY,
        fox.getMood());

    fox.setHealthStateAndUpdateState(50, 50, 50, 24); // Just below threshold

    assertEquals("Sleep just below threshold (24) should trigger sad mood", MoodEnum.SAD,
        fox.getMood());
  }

  @Test
  public void testCleaningInSadMood() {
    // Set fox to sad mood
    fox.setHealthStateAndUpdateState(50, 50, 50, 50);
    fox.setMood(MoodEnum.SAD);

    // Clean the fox
    fox.interactWith(Action.CLEAN);

    // Verify exact cleaning effect
    assertEquals("Cleaning in sad mood should increase hygiene by action boost",
        54, fox.getHealth().getHygiene()); // 50 + 4
  }

  @Test
  public void testSleepingInSadMood() {
    // Set fox to sad mood
    fox.setHealthStateAndUpdateState(50, 50, 50, 50);
    fox.setMood(MoodEnum.SAD);

    // Put fox to sleep
    fox.interactWith(Action.SLEEP);

    // Verify exact sleeping effect
    assertEquals("Sleeping in sad mood should increase sleep by action boost",
        54, fox.getHealth().getSleep()); // 50 + 4
  }

  @Test
  public void testAllHappyMoodTriggers() {
    // Test each trigger for happy mood individually

    // 1. Low hunger
    fox.setHealthStateAndUpdateState(71, 50, 50, 50);
    assertEquals("High hunger should trigger sad mood", MoodEnum.SAD, fox.getMood());
    fox.interactWith(Action.FEED); // Reset hunger to 50
    assertEquals("Low hunger should trigger happy mood", MoodEnum.HAPPY, fox.getMood());


    // 2. High hygiene
    fox.setHealthStateAndUpdateState(50, 34, 50, 50);
    assertEquals("Low hygiene should trigger sad mood", MoodEnum.SAD, fox.getMood());
    fox.interactWith(Action.CLEAN); // Reset hygiene to 50
    assertEquals("High hygiene should trigger happy mood", MoodEnum.HAPPY, fox.getMood());


    // 3. High social
    fox.setHealthStateAndUpdateState(50, 50, 34, 50); // Social >= 35 threshold
    assertEquals("Low social should trigger sad mood", MoodEnum.SAD, fox.getMood());
    fox.interactWith(Action.PLAY); // Reset social to 50
    assertEquals("High social should trigger happy mood", MoodEnum.HAPPY, fox.getMood());


    // 4. High sleep
    fox.setHealthStateAndUpdateState(50, 50, 50, 24); // Sleep >= 25 threshold
    assertEquals("Low sleep should trigger sad mood", MoodEnum.SAD, fox.getMood());
    fox.interactWith(Action.SLEEP); // Reset sleep to 50
    assertEquals("High sleep should trigger happy mood", MoodEnum.HAPPY, fox.getMood());
  }
}