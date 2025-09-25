package pet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests for the generic Pet class.
 * Tests initialization, interactions, health changes, mood transitions,
 * and death conditions.
 */
public class PetTest {
  // Constants from Pet class
  private static final int HAPPY_HUNGER_RATE = 3;
  private static final int HAPPY_HYGIENE_RATE = 2;
  private static final int HAPPY_SOCIAL_RATE = 2;
  private static final int HAPPY_SLEEP_RATE = 2;
  private static final int HAPPY_ACTION_BOOST = 7;

  private static final int SAD_HUNGER_RATE = 5;
  private static final int SAD_HYGIENE_RATE = 3;
  private static final int SAD_SOCIAL_RATE = 3;
  private static final int SAD_SLEEP_RATE = 3;
  private static final int SAD_ACTION_BOOST = 4;

  private Pet pet;

  @Before
  public void setUp() {
    pet = new Pet();
  }

  // Initialization tests

  @Test
  public void testPetInitialization() {
    // Verify initial health values
    assertEquals("Initial hunger should be 50", 50, pet.getHealth().getHunger());
    assertEquals("Initial hygiene should be 50", 50, pet.getHealth().getHygiene());
    assertEquals("Initial social should be 50", 50, pet.getHealth().getSocial());
    assertEquals("Initial sleep should be 50", 50, pet.getHealth().getSleep());

    // Verify initial mood (should be HAPPY with default values)
    assertEquals("Initial mood should be HAPPY", MoodEnum.HAPPY, pet.getMood());

    // Verify pet is alive
    assertFalse("Pet should be alive initially", pet.isDead());
  }

  @Test
  public void testPetDeathThresholds() {
    DeathThreshold threshold = pet.getDeathThreshold();

    // Verify threshold values based on Pet constants
    assertEquals("Hunger limit should be 85", 85, threshold.getHungerLimit());
    assertEquals("Hygiene limit should be 15", 15, threshold.getHygieneLimit());
    assertEquals("Social limit should be 15", 15, threshold.getSocialLimit());
    assertEquals("Sleep limit should be 10", 10, threshold.getSleepLimit());
  }

  // Interaction tests in HAPPY mood

  @Test
  public void testFeedingInHappyMood() {
    // Ensure pet is in happy mood
    pet.setMood(MoodEnum.HAPPY);
    pet.setHealthStateAndUpdateState(50, 50, 50, 50);

    // Feed the pet
    pet.interactWith(Action.FEED);

    // Verify exact hunger decrease (2 * action boost)
    assertEquals("Feeding in happy mood should decrease hunger by 2 * action boost",
        36, pet.getHealth().getHunger()); // 50 - (2 * 7)
  }

  @Test
  public void testPlayingInHappyMood() {
    // Ensure pet is in happy mood
    pet.setMood(MoodEnum.HAPPY);
    pet.setHealthStateAndUpdateState(50, 50, 50, 50);

    // Play with the pet
    pet.interactWith(Action.PLAY);

    // Verify exact play effects
    assertEquals("Playing in happy mood should increase social by action boost",
        57, pet.getHealth().getSocial()); // 50 + 7

    assertEquals("Playing should increase hunger by 1",
        51, pet.getHealth().getHunger()); // 50 + 1
  }

  @Test
  public void testCleaningInHappyMood() {
    // Ensure pet is in happy mood
    pet.setMood(MoodEnum.HAPPY);
    pet.setHealthStateAndUpdateState(50, 50, 50, 50);

    // Clean the pet
    pet.interactWith(Action.CLEAN);

    // Verify exact cleaning effect
    assertEquals("Cleaning in happy mood should increase hygiene by action boost",
        57, pet.getHealth().getHygiene()); // 50 + 7
  }

  @Test
  public void testSleepingInHappyMood() {
    // Ensure pet is in happy mood
    pet.setMood(MoodEnum.HAPPY);
    pet.setHealthStateAndUpdateState(50, 50, 50, 50);

    // Put pet to sleep
    pet.interactWith(Action.SLEEP);

    // Verify exact sleeping effect
    assertEquals("Sleeping in happy mood should increase sleep by action boost",
        57, pet.getHealth().getSleep()); // 50 + 7
  }

  // Interaction tests in SAD mood

  @Test
  public void testFeedingInSadMood() {
    // Set pet to sad mood

    pet.setHealthStateAndUpdateState(50, 50, 50, 50);
    pet.setMood(MoodEnum.SAD);

    // Feed the pet
    pet.interactWith(Action.FEED);

    // Verify exact hunger decrease (action boost)
    assertEquals("Feeding in sad mood should decrease hunger by action boost",
        46, pet.getHealth().getHunger()); // 50 - 4
  }

  @Test
  public void testPlayingInSadMood() {
    // Set pet to sad mood

    pet.setHealthStateAndUpdateState(50, 50, 50, 50);
    pet.setMood(MoodEnum.SAD);

    // Play with the pet
    pet.interactWith(Action.PLAY);

    // Verify exact play effects
    assertEquals("Playing in sad mood should increase social by half action boost",
        52, pet.getHealth().getSocial()); // 50 + (4/2)

    assertEquals("Playing in sad mood should increase hunger by 2",
        52, pet.getHealth().getHunger()); // 50 + 2
  }

  // Time step tests

  @Test
  public void testStepChangesInHappyMood() {
    // Ensure pet is in happy mood
    pet.setMood(MoodEnum.HAPPY);
    pet.setHealthStateAndUpdateState(50, 50, 50, 50);

    // Apply time step
    pet.step();

    // Verify exact health changes
    assertEquals("Hunger should increase by exactly the happy hunger rate",
        53, pet.getHealth().getHunger()); // 50 + 3

    assertEquals("Hygiene should decrease by exactly the happy hygiene rate",
        48, pet.getHealth().getHygiene()); // 50 - 2

    assertEquals("Social should decrease by exactly the happy social rate",
        48, pet.getHealth().getSocial()); // 50 - 2

    assertEquals("Sleep should decrease by exactly the happy sleep rate",
        48, pet.getHealth().getSleep()); // 50 - 2
  }

  @Test
  public void testStepChangesInSadMood() {
    // Set pet to sad mood

    pet.setHealthStateAndUpdateState(50, 50, 50, 50);
    pet.setMood(MoodEnum.SAD);
    // Apply time step
    pet.step();

    // Verify exact hunger increase
    assertEquals("Hunger should increase by exactly the sad hunger rate",
        55, pet.getHealth().getHunger()); // 50 + 5

    // For other stats, we can only check ranges due to random component
    assertTrue("Hygiene should decrease by 3-6 points",
        pet.getHealth().getHygiene() >= 44
            &&
            pet.getHealth().getHygiene() <= 47); // 50 - (3 + 0-3 random)

    assertTrue("Social should decrease by 3-6 points",
        pet.getHealth().getSocial() >= 44
            &&
            pet.getHealth().getSocial() <= 47); // 50 - (3 + 0-3 random)

    assertTrue("Sleep should decrease by 3-6 points",
        pet.getHealth().getSleep() >= 44
            &&
            pet.getHealth().getSleep() <= 47); // 50 - (3 + 0-3 random)
  }

  // Mood transition tests

  @Test
  public void testMoodTransitionToSad() {
    // Start in happy mood
    pet.setMood(MoodEnum.HAPPY);
    assertEquals(MoodEnum.HAPPY, pet.getMood());

    // Set health to trigger sad mood
    // Hunger > HUNGER_SAD_THRESHOLD (65)
    pet.setHealthStateAndUpdateState(66, 50, 50, 50);

    // Force mood update
    pet.step();

    // Verify mood changed to SAD
    assertEquals("Mood should change to SAD", MoodEnum.SAD, pet.getMood());
  }

  @Test
  public void testMoodTransitionToHappy() {
    // Start in sad mood
    pet.setMood(MoodEnum.SAD);
    assertEquals(MoodEnum.SAD, pet.getMood());

    // Set health to happy levels
    pet.setHealthStateAndUpdateState(60, 50, 50, 50);

    // Force mood update
    pet.step();

    // Verify mood changed to HAPPY
    assertEquals("Mood should change to HAPPY", MoodEnum.HAPPY, pet.getMood());
  }

  @Test
  public void testAllSadMoodTriggers() {
    // Test each trigger for sad mood individually

    // 1. High hunger
    pet.setHealthStateAndUpdateState(66, 50, 50, 50); // Hunger > 65 threshold
    pet.step();
    assertEquals("High hunger should trigger sad mood", MoodEnum.SAD, pet.getMood());

    // Reset to happy
    pet.setHealthStateAndUpdateState(50, 50, 50, 50);
    pet.step();

    // 2. Low hygiene
    pet.setHealthStateAndUpdateState(50, 29, 50, 50); // Hygiene < 30 threshold
    pet.step();
    assertEquals("Low hygiene should trigger sad mood", MoodEnum.SAD, pet.getMood());

    // Reset to happy
    pet.setHealthStateAndUpdateState(50, 50, 50, 50);
    pet.step();

    // 3. Low social
    pet.setHealthStateAndUpdateState(50, 50, 24, 50); // Social < 25 threshold
    pet.step();
    assertEquals("Low social should trigger sad mood", MoodEnum.SAD, pet.getMood());

    // Reset to happy
    pet.setHealthStateAndUpdateState(50, 50, 50, 50);
    pet.step();

    // 4. Low sleep
    pet.setHealthStateAndUpdateState(50, 50, 50, 24); // Sleep < 25 threshold
    pet.step();
    assertEquals("Low sleep should trigger sad mood", MoodEnum.SAD, pet.getMood());
  }

  // Death condition tests

  @Test
  public void testDeathByHunger() {
    // Set hunger above threshold
    pet.setHealthStateAndUpdateState(86, 50, 50, 50); // > 85 hunger limit

    // Force update
    pet.step();

    // Verify pet is dead
    assertTrue("Pet should die when hunger exceeds threshold", pet.isDead());
  }

  @Test
  public void testDeathByLowHygiene() {
    // Set hygiene below threshold
    pet.setHealthStateAndUpdateState(50, 14, 50, 50); // < 15 hygiene limit

    // Force update
    pet.step();

    // Verify pet is dead
    assertTrue("Pet should die when hygiene is below threshold", pet.isDead());
  }

  @Test
  public void testDeathByLowSocial() {
    // Set social below threshold
    pet.setHealthStateAndUpdateState(50, 50, 14, 50); // < 15 social limit

    // Force update
    pet.step();

    // Verify pet is dead
    assertTrue("Pet should die when social is below threshold", pet.isDead());
  }

  @Test
  public void testDeathByLowSleep() {
    // Set sleep below threshold
    pet.setHealthStateAndUpdateState(50, 50, 50, 9); // < 10 sleep limit

    // Force update
    pet.step();

    // Verify pet is dead
    assertTrue("Pet should die when sleep is below threshold", pet.isDead());
  }

  // No interactions when dead

  @Test
  public void testNoInteractionsWhenDead() {
    // Kill the pet
    pet.setHealthStateAndUpdateState(86, 50, 50, 50); // > hunger limit
    pet.step();
    assertTrue("Pet should be dead", pet.isDead());

    // Get current health values
    final int initialHunger = pet.getHealth().getHunger();
    final int initialHygiene = pet.getHealth().getHygiene();
    final int initialSocial = pet.getHealth().getSocial();
    final int initialSleep = pet.getHealth().getSleep();

    // Try interactions
    pet.interactWith(Action.FEED);
    pet.interactWith(Action.PLAY);
    pet.interactWith(Action.CLEAN);
    pet.interactWith(Action.SLEEP);


    // Verify health didn't change
    assertEquals("Dead pet's hunger shouldn't change", initialHunger, pet.getHealth().getHunger());
    assertEquals("Dead pet's hygiene shouldn't change", initialHygiene,
        pet.getHealth().getHygiene());
    assertEquals("Dead pet's social shouldn't change", initialSocial, pet.getHealth().getSocial());

    assertEquals("Dead pet's sleep shouldn't change", initialSleep, pet.getHealth().getSleep());
  }

  @Test
  public void testNoStepChangeWhenDead() {
    // Kill the pet
    pet.setHealthStateAndUpdateState(86, 50, 50, 50); // > hunger limit
    pet.step();
    assertTrue("Pet should be dead", pet.isDead());

    // Get current health values
    int initialHunger = pet.getHealth().getHunger();
    int initialHygiene = pet.getHealth().getHygiene();
    final int initialSocial = pet.getHealth().getSocial();
    final int initialSleep = pet.getHealth().getSleep();
    // Try step
    pet.step();

    // Verify health didn't change
    assertEquals("Dead pet's hunger shouldn't change", initialHunger, pet.getHealth().getHunger());
    assertEquals("Dead pet's hygiene shouldn't change", initialHygiene,
        pet.getHealth().getHygiene());

    assertEquals("Dead pet's social shouldn't change", initialSocial, pet.getHealth().getSocial());

    assertEquals("Dead pet's sleep shouldn't change", initialSleep, pet.getHealth().getSleep());
  }

  // Edge cases

  @Test
  public void testMinMaxHealthValues() {
    // Test minimum values
    pet.setHealthStateAndUpdateState(0, 0, 0, 0);

    // Try to further reduce values with a sad mood
    pet.setMood(MoodEnum.SAD);
    pet.revive();

    pet.step();

    // Verify exact minimum values are respected
    assertEquals("Hygiene can't go below 0", 0, pet.getHealth().getHygiene());
    assertEquals("Social can't go below 0", 0, pet.getHealth().getSocial());
    assertEquals("Sleep can't go below 0", 0, pet.getHealth().getSleep());
    assertEquals("Hunger should increase by exactly the sad hunger rate", 5,
        pet.getHealth().getHunger());

    // Test maximum values
    pet.setHealthStateAndUpdateState(50, 95, 95, 95);
    pet.setMood(MoodEnum.HAPPY);

    // Try interactions that would increase values
    pet.interactWith(Action.CLEAN);
    pet.interactWith(Action.PLAY);
    pet.interactWith(Action.SLEEP);

    // Verify exact maximum values are respected
    assertEquals("Hygiene can't exceed 100", 100,
        pet.getHealth().getHygiene()); // 95 + 7 = 102, clamped to 100
    assertEquals("Social can't exceed 100", 100,
        pet.getHealth().getSocial()); // 95 + 7 = 102, clamped to 100
    assertEquals("Sleep can't exceed 100", 100,
        pet.getHealth().getSleep()); // 95 + 7 = 102, clamped to 100

    pet.setHealthStateAndUpdateState(99, 95, 95, 95);
    pet.revive();
    pet.step();
    assertEquals("Hunger can't exceed 100", 100,
        pet.getHealth().getHunger());


  }

  @Test
  public void testMultipleInteractions() {
    // Test multiple interactions in sequence
    pet.setMood(MoodEnum.HAPPY);
    pet.setHealthStateAndUpdateState(50, 50, 50, 50);

    // Feeding multiple times - 50 - (2*7) - (2*7) = 22
    pet.interactWith(Action.FEED);
    pet.interactWith(Action.FEED);

    assertEquals("Hunger should decrease by 2*7 twice", 22, pet.getHealth().getHunger());

    // Reset and test play, clean, sleep
    pet.setHealthStateAndUpdateState(50, 50, 50, 50);

    pet.interactWith(Action.PLAY);
    pet.interactWith(Action.CLEAN);
    pet.interactWith(Action.SLEEP);

    assertEquals("Social should increase by action boost", 57, pet.getHealth().getSocial());
    assertEquals("Hygiene should increase by action boost", 57, pet.getHealth().getHygiene());
    assertEquals("Sleep should increase by action boost", 57, pet.getHealth().getSleep());
    assertEquals("Hunger should increase by 1 from play", 51, pet.getHealth().getHunger());
  }

  @Test
  public void testBoundaryConditions() {
    // Test exact boundary conditions for pet thresholds

    // 1. Test exact hunger threshold boundary (65)
    pet.setHealthStateAndUpdateState(65, 50, 50, 50); // Exactly at threshold

    assertEquals("Hunger exactly at threshold (65) should not trigger sad mood", MoodEnum.HAPPY,
        pet.getMood());

    pet.setHealthStateAndUpdateState(66, 50, 50, 50); // Just above threshold

    assertEquals("Hunger just above threshold (66) should trigger sad mood", MoodEnum.SAD,
        pet.getMood());

    // 2. Test exact hygiene threshold boundary (30)
    pet.setHealthStateAndUpdateState(50, 30, 50, 50); // Exactly at threshold

    assertEquals("Hygiene exactly at threshold (30) should not trigger sad mood", MoodEnum.HAPPY,
        pet.getMood());

    pet.setHealthStateAndUpdateState(50, 29, 50, 50); // Just below threshold

    assertEquals("Hygiene just below threshold (29) should trigger sad mood", MoodEnum.SAD,
        pet.getMood());

    // 3. Test exact social threshold boundary (25)
    pet.setHealthStateAndUpdateState(50, 50, 25, 50); // Exactly at threshold

    assertEquals("Social exactly at threshold (25) should not trigger sad mood", MoodEnum.HAPPY,
        pet.getMood());

    pet.setHealthStateAndUpdateState(50, 50, 24, 50); // Just below threshold

    assertEquals("Social just below threshold (24) should trigger sad mood", MoodEnum.SAD,
        pet.getMood());

    // 4. Test exact sleep threshold boundary (25)
    pet.setHealthStateAndUpdateState(50, 50, 50, 25); // Exactly at threshold

    assertEquals("Sleep exactly at threshold (25) should not trigger sad mood", MoodEnum.HAPPY,
        pet.getMood());

    pet.setHealthStateAndUpdateState(50, 50, 50, 24); // Just below threshold

    assertEquals("Sleep just below threshold (24) should trigger sad mood", MoodEnum.SAD,
        pet.getMood());
  }

  @Test
  public void testCleaningInSadMood() {
    // Set pet to sad mood
    pet.setHealthStateAndUpdateState(50, 50, 50, 50);
    pet.setMood(MoodEnum.SAD);

    // Clean the pet
    pet.interactWith(Action.CLEAN);

    // Verify exact cleaning effect
    assertEquals("Cleaning in sad mood should increase hygiene by action boost",
        54, pet.getHealth().getHygiene()); // 50 + 4
  }

  @Test
  public void testSleepingInSadMood() {
    // Set pet to sad mood
    pet.setHealthStateAndUpdateState(50, 50, 50, 50);
    pet.setMood(MoodEnum.SAD);

    // Put pet to sleep
    pet.interactWith(Action.SLEEP);

    // Verify exact sleeping effect
    assertEquals("Sleeping in sad mood should increase sleep by action boost",
        54, pet.getHealth().getSleep()); // 50 + 4
  }

  @Test
  public void testAllHappyMoodTriggers() {
    // Test each trigger for happy mood individually


    pet.setHealthStateAndUpdateState(66, 50, 50, 50);
    assertEquals("High hunger should trigger sad mood", MoodEnum.SAD, pet.getMood());
    pet.interactWith(Action.FEED);
    assertEquals("Low hunger should trigger happy mood", MoodEnum.HAPPY, pet.getMood());


    pet.setHealthStateAndUpdateState(50, 29, 50, 50);
    assertEquals("Low hygiene should trigger sad mood", MoodEnum.SAD, pet.getMood());
    pet.interactWith(Action.CLEAN);
    assertEquals("High hygiene should trigger happy mood", MoodEnum.HAPPY, pet.getMood());


    pet.setHealthStateAndUpdateState(50, 50, 24, 50);
    assertEquals("Low social should trigger sad mood", MoodEnum.SAD, pet.getMood());
    pet.interactWith(Action.PLAY);
    assertEquals("High social should trigger happy mood", MoodEnum.HAPPY, pet.getMood());


    pet.setHealthStateAndUpdateState(50, 50, 50, 24);
    assertEquals("Low sleep should trigger sad mood", MoodEnum.SAD, pet.getMood());
    pet.interactWith(Action.SLEEP);
    assertEquals("High sleep should trigger happy mood", MoodEnum.HAPPY, pet.getMood());
  }
}

