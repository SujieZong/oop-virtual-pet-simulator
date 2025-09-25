package pet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests for the Snake pet type.
 * Tests initialization, interactions, health changes, mood transitions,
 * and death conditions specific to Snake.
 */
public class SnakeTest {
  // Constants from Snake class
  private static final int HAPPY_HUNGER_RATE = 2;
  private static final int HAPPY_HYGIENE_RATE = 1;
  private static final int HAPPY_SOCIAL_RATE = 1;
  private static final int HAPPY_SLEEP_RATE = 1;
  private static final int HAPPY_ACTION_BOOST = 10;

  private static final int SAD_HUNGER_RATE = 4;
  private static final int SAD_HYGIENE_RATE = 2;
  private static final int SAD_SOCIAL_RATE = 2;
  private static final int SAD_SLEEP_RATE = 2;
  private static final int SAD_ACTION_BOOST = 3;

  private Snake snake;

  @Before
  public void setUp() {
    snake = new Snake();
  }

  // Initialization tests

  @Test
  public void testSnakeInitialization() {
    // Verify initial health values
    assertEquals("Initial hunger should be 50", 50, snake.getHealth().getHunger());
    assertEquals("Initial hygiene should be 50", 50, snake.getHealth().getHygiene());
    assertEquals("Initial social should be 50", 50, snake.getHealth().getSocial());
    assertEquals("Initial sleep should be 50", 50, snake.getHealth().getSleep());

    // Verify initial mood (should be HAPPY with default values)
    assertEquals("Initial mood should be HAPPY", MoodEnum.HAPPY, snake.getMood());

    // Verify snake is alive
    assertFalse("Snake should be alive initially", snake.isDead());
  }

  @Test
  public void testSnakeDeathThresholds() {
    DeathThreshold threshold = snake.getDeathThreshold();

    // Verify threshold values based on Snake constants
    assertEquals("Hunger limit should be 80", 80, threshold.getHungerLimit());
    assertEquals("Hygiene limit should be 15", 15, threshold.getHygieneLimit());
    assertEquals("Social limit should be 5", 5, threshold.getSocialLimit());
    assertEquals("Sleep limit should be 5", 5, threshold.getSleepLimit());
  }

  // Interaction tests in HAPPY mood

  @Test
  public void testFeedingInHappyMood() {
    // Ensure snake is in happy mood
    snake.setMood(MoodEnum.HAPPY);
    snake.setHealthStateAndUpdateState(50, 50, 50, 50);

    // Feed the snake
    snake.interactWith(Action.FEED);

    // Verify exact hunger decrease (2 * action boost)
    assertEquals("Feeding in happy mood should decrease hunger by 2 * action boost",
        30, snake.getHealth().getHunger()); // 50 - (2 * 10)
  }

  @Test
  public void testPlayingInHappyMood() {
    // Ensure snake is in happy mood
    snake.setMood(MoodEnum.HAPPY);
    snake.setHealthStateAndUpdateState(50, 50, 50, 50);

    // Play with the snake
    snake.interactWith(Action.PLAY);

    // Verify exact play effects
    assertEquals("Playing in happy mood should increase social by action boost",
        60, snake.getHealth().getSocial()); // 50 + 10

    assertEquals("Playing should increase hunger by 1",
        51, snake.getHealth().getHunger()); // 50 + 1
  }

  @Test
  public void testCleaningInHappyMood() {
    // Ensure snake is in happy mood
    snake.setMood(MoodEnum.HAPPY);
    snake.setHealthStateAndUpdateState(50, 50, 50, 50);

    // Clean the snake
    snake.interactWith(Action.CLEAN);

    // Verify exact cleaning effect
    assertEquals("Cleaning in happy mood should increase hygiene by action boost",
        60, snake.getHealth().getHygiene()); // 50 + 10
  }

  @Test
  public void testSleepingInHappyMood() {
    // Ensure snake is in happy mood
    snake.setMood(MoodEnum.HAPPY);
    snake.setHealthStateAndUpdateState(50, 50, 50, 50);

    // Put snake to sleep
    snake.interactWith(Action.SLEEP);

    // Verify exact sleeping effect
    assertEquals("Sleeping in happy mood should increase sleep by action boost",
        60, snake.getHealth().getSleep()); // 50 + 10
  }

  // Interaction tests in SAD mood

  @Test
  public void testFeedingInSadMood() {
    // Set snake to sad mood

    snake.setHealthStateAndUpdateState(50, 50, 50, 50);
    snake.setMood(MoodEnum.SAD);

    // Feed the snake
    snake.interactWith(Action.FEED);

    // Verify exact hunger decrease (action boost)
    assertEquals("Feeding in sad mood should decrease hunger by action boost",
        47, snake.getHealth().getHunger()); // 50 - 3
  }

  @Test
  public void testPlayingInSadMood() {
    // Set snake to sad mood

    snake.setHealthStateAndUpdateState(50, 50, 50, 50);
    snake.setMood(MoodEnum.SAD);

    // Play with the snake
    snake.interactWith(Action.PLAY);

    // Verify exact play effects
    assertEquals("Playing in sad mood should increase social by half action boost",
        51, snake.getHealth().getSocial()); // 50 + (3/2) = 51 (integer division)

    assertEquals("Playing in sad mood should increase hunger by 2",
        52, snake.getHealth().getHunger()); // 50 + 2
  }

  @Test
  public void testCleaningInSadMood() {
    // Set snake to sad mood

    snake.setHealthStateAndUpdateState(50, 50, 50, 50);
    snake.setMood(MoodEnum.SAD);

    // Clean the snake
    snake.interactWith(Action.CLEAN);

    // Verify exact cleaning effect
    assertEquals("Cleaning in sad mood should increase hygiene by action boost",
        53, snake.getHealth().getHygiene()); // 50 + 3
  }

  @Test
  public void testSleepingInSadMood() {
    // Set snake to sad mood

    snake.setHealthStateAndUpdateState(50, 50, 50, 50);
    snake.setMood(MoodEnum.SAD);

    // Put snake to sleep
    snake.interactWith(Action.SLEEP);

    // Verify exact sleeping effect
    assertEquals("Sleeping in sad mood should increase sleep by action boost",
        53, snake.getHealth().getSleep()); // 50 + 3
  }

  // Time step tests

  @Test
  public void testStepChangesInHappyMood() {
    // Ensure snake is in happy mood
    snake.setMood(MoodEnum.HAPPY);
    snake.setHealthStateAndUpdateState(50, 50, 50, 50);

    // Apply time step
    snake.step();

    // Verify exact health changes
    assertEquals("Hunger should increase by exactly the happy hunger rate",
        52, snake.getHealth().getHunger()); // 50 + 2

    assertEquals("Hygiene should decrease by exactly the happy hygiene rate",
        49, snake.getHealth().getHygiene()); // 50 - 1

    assertEquals("Social should decrease by exactly the happy social rate",
        49, snake.getHealth().getSocial()); // 50 - 1

    assertEquals("Sleep should decrease by exactly the happy sleep rate",
        49, snake.getHealth().getSleep()); // 50 - 1
  }

  @Test
  public void testStepChangesInSadMood() {
    // Set snake to sad mood

    snake.setHealthStateAndUpdateState(50, 50, 50, 50);
    snake.setMood(MoodEnum.SAD);

    // Apply time step
    snake.step();

    // Verify exact hunger increase
    assertEquals("Hunger should increase by exactly the sad hunger rate",
        54, snake.getHealth().getHunger()); // 50 + 4

    // For other stats, we can only check ranges due to random component
    assertTrue("Hygiene should decrease by 2-5 points",
        snake.getHealth().getHygiene() >= 45
            &&
            snake.getHealth().getHygiene() <= 48); // 50 - (2 + 0-3 random)

    assertTrue("Social should decrease by 2-5 points",
        snake.getHealth().getSocial() >= 45
            &&
            snake.getHealth().getSocial() <= 48); // 50 - (2 + 0-3 random)

    assertTrue("Sleep should decrease by 2-5 points",
        snake.getHealth().getSleep() >= 45
            &&
            snake.getHealth().getSleep() <= 48); // 50 - (2 + 0-3 random)
  }

  // Mood transition tests - Note: Snake mood is unaffected by hygiene unlike other pets

  @Test
  public void testMoodTransitionToSad() {
    // Start in happy mood
    snake.setMood(MoodEnum.HAPPY);
    assertEquals(MoodEnum.HAPPY, snake.getMood());

    // Set health to trigger sad mood
    // Hunger > HUNGER_SAD_THRESHOLD (60)
    snake.setHealthStateAndUpdateState(61, 50, 50, 50);

    // Force mood update
    snake.step();

    // Verify mood changed to SAD
    assertEquals("Mood should change to SAD", MoodEnum.SAD, snake.getMood());
  }

  @Test
  public void testMoodTransitionToHappy() {
    // Start in sad mood
    snake.setMood(MoodEnum.SAD);
    assertEquals(MoodEnum.SAD, snake.getMood());

    // Set health to happy levels
    snake.setHealthStateAndUpdateState(55, 50, 50, 50);

    // Force mood update
    snake.step();

    // Verify mood changed to HAPPY
    assertEquals("Mood should change to HAPPY", MoodEnum.HAPPY, snake.getMood());
  }

  @Test
  public void testAllSadMoodTriggers() {
    // Test each trigger for sad mood individually

    // 1. High hunger
    snake.setHealthStateAndUpdateState(61, 50, 50, 50); // Hunger > 60 threshold
    snake.step();
    assertEquals("High hunger should trigger sad mood", MoodEnum.SAD, snake.getMood());

    // Reset to happy
    snake.setHealthStateAndUpdateState(50, 50, 50, 50);
    snake.step();

    // 2. Low social - Snake has lower social threshold than other pets
    snake.setHealthStateAndUpdateState(50, 50, 9, 50); // Social < 10 threshold
    snake.step();
    assertEquals("Low social should trigger sad mood", MoodEnum.SAD, snake.getMood());

    // Reset to happy
    snake.setHealthStateAndUpdateState(50, 50, 50, 50);
    snake.step();

    // 3. Low sleep
    snake.setHealthStateAndUpdateState(50, 50, 50, 29); // Sleep < 30 threshold
    snake.step();
    assertEquals("Low sleep should trigger sad mood", MoodEnum.SAD, snake.getMood());

    // 4. Verify hygiene doesn't affect snake mood
    snake.setHealthStateAndUpdateState(50, 20, 50, 50); // Low hygiene
    snake.step();
    assertEquals("Low hygiene should NOT trigger sad mood for snake", MoodEnum.HAPPY,
        snake.getMood());
  }

  // Death condition tests

  @Test
  public void testDeathByHunger() {
    // Set hunger above threshold
    snake.setHealthStateAndUpdateState(81, 50, 50, 50); // > 80 hunger limit

    // Force update
    snake.step();

    // Verify snake is dead
    assertTrue("Snake should die when hunger exceeds threshold", snake.isDead());
  }

  @Test
  public void testDeathByLowHygiene() {
    // Set hygiene below threshold
    snake.setHealthStateAndUpdateState(50, 14, 50, 50); // < 15 hygiene limit

    // Force update
    snake.step();

    // Verify snake is dead
    assertTrue("Snake should die when hygiene is below threshold", snake.isDead());
  }

  @Test
  public void testDeathByLowSocial() {
    // Set social below threshold - Snake has very low social limit
    snake.setHealthStateAndUpdateState(50, 50, 4, 50); // < 5 social limit

    // Force update
    snake.step();

    // Verify snake is dead
    assertTrue("Snake should die when social is below threshold", snake.isDead());
  }

  @Test
  public void testDeathByLowSleep() {
    // Set sleep below threshold - Snake has very low sleep limit
    snake.setHealthStateAndUpdateState(50, 50, 50, 4); // < 5 sleep limit

    // Force update
    snake.step();

    // Verify snake is dead
    assertTrue("Snake should die when sleep is below threshold", snake.isDead());
  }

  // No interactions when dead

  @Test
  public void testNoInteractionsWhenDead() {
    // Kill the snake
    snake.setHealthStateAndUpdateState(81, 50, 50, 50); // > hunger limit
    snake.step();
    assertTrue("Snake should be dead", snake.isDead());

    // Get current health values
    final int initialHunger = snake.getHealth().getHunger();
    final int initialHygiene = snake.getHealth().getHygiene();
    final int initialSocial = snake.getHealth().getSocial();
    final int initialSleep = snake.getHealth().getSleep();

    // Try interactions
    snake.interactWith(Action.FEED);
    snake.interactWith(Action.PLAY);
    snake.interactWith(Action.CLEAN);
    snake.interactWith(Action.SLEEP);

    // Verify health didn't change
    assertEquals("Dead snake's hunger shouldn't change", initialHunger,
        snake.getHealth().getHunger());
    assertEquals("Dead snake's hygiene shouldn't change", initialHygiene,
        snake.getHealth().getHygiene());
    assertEquals("Dead snake's social shouldn't change", initialSocial,
        snake.getHealth().getSocial());
    assertEquals("Dead snake's sleep shouldn't change", initialSleep, snake.getHealth().getSleep());
  }

  @Test
  public void testNoStepChangeWhenDead() {
    // Kill the snake
    snake.setHealthStateAndUpdateState(81, 50, 50, 50); // > hunger limit
    snake.step();
    assertTrue("Snake should be dead", snake.isDead());

    // Get current health values
    final int initialHunger = snake.getHealth().getHunger();
    final int initialHygiene = snake.getHealth().getHygiene();
    final int initialSocial = snake.getHealth().getSocial();
    final int initialSleep = snake.getHealth().getSleep();

    // Try step
    snake.step();

    // Verify health didn't change
    assertEquals("Dead snake's hunger shouldn't change", initialHunger,
        snake.getHealth().getHunger());
    assertEquals("Dead snake's hygiene shouldn't change", initialHygiene,
        snake.getHealth().getHygiene());
    assertEquals("Dead snake's social shouldn't change", initialSocial,
        snake.getHealth().getSocial());
    assertEquals("Dead snake's sleep shouldn't change", initialSleep, snake.getHealth().getSleep());
  }

  // Edge cases

  @Test
  public void testMinMaxHealthValues() {
    // Test minimum values
    snake.setHealthStateAndUpdateState(0, 0, 0, 0);


    snake.revive();
    snake.step();

    // Verify exact minimum values are respected
    assertEquals("Hygiene can't go below 0", 0, snake.getHealth().getHygiene());
    assertEquals("Social can't go below 0", 0, snake.getHealth().getSocial());
    assertEquals("Sleep can't go below 0", 0, snake.getHealth().getSleep());
    assertEquals("Hunger should increase by exactly the sad hunger rate", 4,
        snake.getHealth().getHunger());
    snake.revive();
    snake.setMood(MoodEnum.HAPPY);
    snake.interactWith(Action.FEED);
    assertEquals("Hunger can't go below 0", 0, snake.getHealth().getHunger());

    // Test maximum values
    snake.setHealthStateAndUpdateState(50, 95, 95, 95);
    snake.setMood(MoodEnum.HAPPY);

    // Try interactions that would increase values
    snake.interactWith(Action.CLEAN);
    snake.interactWith(Action.PLAY);
    snake.interactWith(Action.SLEEP);

    // Verify exact maximum values are respected
    assertEquals("Hygiene can't exceed 100", 100, snake.getHealth().getHygiene());
    assertEquals("Social can't exceed 100", 100, snake.getHealth().getSocial());
    assertEquals("Sleep can't exceed 100", 100, snake.getHealth().getSleep());

    snake.setHealthStateAndUpdateState(99, 95, 95, 95);
    snake.revive();
    snake.step();
    assertEquals("Hunger can't exceed 100", 100, snake.getHealth().getHunger());


  }

  @Test
  public void testBoundaryConditions() {
    // Test exact boundary conditions for snake thresholds

    // 1. Test exact hunger threshold boundary (60)
    snake.setHealthStateAndUpdateState(60, 50, 50, 50); // Exactly at threshold

    assertEquals("Hunger exactly at threshold (60) should not trigger sad mood", MoodEnum.HAPPY,
        snake.getMood());

    snake.setHealthStateAndUpdateState(61, 50, 50, 50); // Just above threshold

    assertEquals("Hunger just above threshold (61) should trigger sad mood", MoodEnum.SAD,
        snake.getMood());

    // 2. Test exact social threshold boundary (10)
    snake.setHealthStateAndUpdateState(50, 50, 10, 50); // Exactly at threshold

    assertEquals("Social exactly at threshold (10) should not trigger sad mood", MoodEnum.HAPPY,
        snake.getMood());

    snake.setHealthStateAndUpdateState(50, 50, 9, 50); // Just below threshold

    assertEquals("Social just below threshold (9) should trigger sad mood", MoodEnum.SAD,
        snake.getMood());

    // 3. Test exact sleep threshold boundary (30)
    snake.setHealthStateAndUpdateState(50, 50, 50, 30); // Exactly at threshold

    assertEquals("Sleep exactly at threshold (30) should not trigger sad mood", MoodEnum.HAPPY,
        snake.getMood());

    snake.setHealthStateAndUpdateState(50, 50, 50, 29); // Just below threshold

    assertEquals("Sleep just below threshold (29) should trigger sad mood", MoodEnum.SAD,
        snake.getMood());
  }

  @Test
  public void testMultipleInteractions() {
    // Test multiple interactions in sequence
    snake.setMood(MoodEnum.HAPPY);
    snake.setHealthStateAndUpdateState(50, 50, 50, 50);

    // Feeding multiple times
    snake.interactWith(Action.FEED);
    snake.interactWith(Action.FEED);

    assertEquals("Hunger should decrease to 10 after two feedings", 10,
        snake.getHealth().getHunger());
    // 50 - (2*10) - (2*10) = 10

    // Reset and test multiple play interactions
    snake.setHealthStateAndUpdateState(50, 50, 50, 50);

    // Play multiple times
    snake.interactWith(Action.PLAY);
    snake.interactWith(Action.PLAY);
    snake.interactWith(Action.PLAY);

    assertEquals("Social should hit max (100) after three plays", 80,
        snake.getHealth().getSocial());
    // 50 + 10 + 10 + 10 = 80

    assertEquals("Hunger should increase after plays", 53, snake.getHealth().getHunger());
    // 50 + 1 + 1 + 1 = 53
  }

  @Test
  public void testMultipleMoodTransitions() {
    // Test repeated mood transitions

    // Start happy
    snake.setMood(MoodEnum.HAPPY);
    snake.setHealthStateAndUpdateState(50, 50, 50, 50);
    assertEquals(MoodEnum.HAPPY, snake.getMood());

    // Make sad by hunger
    snake.setHealthStateAndUpdateState(70, 50, 50, 50);
    snake.step();
    assertEquals(MoodEnum.SAD, snake.getMood());

    // Make happy again
    snake.setHealthStateAndUpdateState(50, 50, 50, 50);
    snake.step();
    assertEquals(MoodEnum.HAPPY, snake.getMood());

    // Make sad by social
    snake.setHealthStateAndUpdateState(50, 50, 5, 50);
    snake.step();
    assertEquals(MoodEnum.SAD, snake.getMood());

    // Make happy again
    snake.setHealthStateAndUpdateState(50, 50, 50, 50);
    snake.step();
    assertEquals(MoodEnum.HAPPY, snake.getMood());

    // Make sad by multiple factors
    snake.setHealthStateAndUpdateState(70, 50, 5, 20);
    snake.step();
    assertEquals(MoodEnum.SAD, snake.getMood());
  }

  @Test
  public void testAllHappyMoodTriggers() {
    // Test each trigger for happy mood individually

    snake.setHealthStateAndUpdateState(61, 50, 50, 50);
    assertEquals("High hunger should trigger sad mood", MoodEnum.SAD, snake.getMood());
    snake.interactWith(Action.FEED); // Reset hunger to 50
    assertEquals("Low hunger should trigger happy mood", MoodEnum.HAPPY, snake.getMood());


    snake.setHealthStateAndUpdateState(50, 50, 9, 50);
    assertEquals("Low social should trigger sad mood", MoodEnum.SAD, snake.getMood());
    snake.interactWith(Action.PLAY); // Reset social to 50
    assertEquals("High social should trigger happy mood", MoodEnum.HAPPY, snake.getMood());


    snake.setHealthStateAndUpdateState(50, 50, 50, 29);
    assertEquals("Low sleep should trigger sad mood", MoodEnum.SAD, snake.getMood());
    snake.interactWith(Action.SLEEP); // Reset sleep to 50
    assertEquals("High sleep should trigger happy mood", MoodEnum.HAPPY, snake.getMood());

    snake.setHealthStateAndUpdateState(50, 20, 50, 50);
    assertEquals("Low hygiene should NOT trigger sad mood for snake", MoodEnum.HAPPY,
        snake.getMood());
    snake.interactWith(Action.CLEAN);
    assertEquals("High hygiene should trigger happy mood", MoodEnum.HAPPY, snake.getMood());
  }
}