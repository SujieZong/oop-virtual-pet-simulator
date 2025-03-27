package pet;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Tests comparing different pet types to ensure they have unique behaviors.
 */
public class ComparisonTest {

  @Test
  public void testPetTypeInitialBehavior() {
    // Create one of each pet type
    Pet genericPet = new Pet();
    Snake snake = new Snake();
    Fox fox = new Fox();

    // All should start with the same initial health
    assertEquals(50, genericPet.getHealth().getHunger());
    assertEquals(50, snake.getHealth().getHunger());
    assertEquals(50, fox.getHealth().getHunger());

    assertEquals(50, genericPet.getHealth().getHygiene());
    assertEquals(50, snake.getHealth().getHygiene());
    assertEquals(50, fox.getHealth().getHygiene());

    assertEquals(50, genericPet.getHealth().getSocial());
    assertEquals(50, snake.getHealth().getSocial());
    assertEquals(50, fox.getHealth().getSocial());

    assertEquals(50, genericPet.getHealth().getSleep());
    assertEquals(50, snake.getHealth().getSleep());
    assertEquals(50, fox.getHealth().getSleep());
    assertEquals(50, genericPet.getHealth().getSleep());

    // All should start HAPPY
    assertEquals(MoodEnum.HAPPY, genericPet.getMood());
    assertEquals(MoodEnum.HAPPY, snake.getMood());
    assertEquals(MoodEnum.HAPPY, fox.getMood());
  }

  @Test
  public void testPetTypeStepBehavior() {
    // Create one of each pet type
    Pet genericPet = new Pet();
    Snake snake = new Snake();
    Fox fox = new Fox();

    // Apply a step to each
    genericPet.step();
    snake.step();
    fox.step();

    // Check hunger increase rates differ
    assertEquals(53, genericPet.getHealth().getHunger()); // 50 + 3
    assertEquals(52, snake.getHealth().getHunger());      // 50 + 2
    assertEquals(53, fox.getHealth().getHunger());        // 50 + 3

    // Check hygiene decrease rates differ
    assertEquals(48, genericPet.getHealth().getHygiene()); // 50 - 2
    assertEquals(49, snake.getHealth().getHygiene());      // 50 - 1 (slower for snake)
    assertEquals(48, fox.getHealth().getHygiene());        // 50 - 2


  }

  @Test
  public void testPetTypeFeedBehavior() {
    // Create one of each pet type
    Pet genericPet = new Pet();
    Snake snake = new Snake();
    Fox fox = new Fox();

    // Feed each pet
    genericPet.interactWith(Action.FEED);
    snake.interactWith(Action.FEED);
    fox.interactWith(Action.FEED);

    // Check feed effects differ
    assertEquals(36, genericPet.getHealth().getHunger()); // 50 - (2 * 7) = 36
    assertEquals(30, snake.getHealth().getHunger());      // 50 - (2 * 10) = 30 (better for snake)
    assertEquals(34, fox.getHealth().getHunger());        // 50 - (2 * 8) = 34
  }

  @Test
  public void testDeathThresholdDifferences() {
    // Get thresholds from each pet type
    DeathThreshold genericThreshold = new Pet().getDeathThreshold();
    DeathThreshold snakeThreshold = new Snake().getDeathThreshold();
    DeathThreshold foxThreshold = new Fox().getDeathThreshold();

    // Compare hunger thresholds
    assertEquals(85, genericThreshold.getHungerLimit());
    assertEquals(80, snakeThreshold.getHungerLimit());  // Snake dies sooner from hunger
    assertEquals(90, foxThreshold.getHungerLimit());    // Fox can tolerate more hunger

    // Compare social thresholds
    assertEquals(15, genericThreshold.getSocialLimit());
    assertEquals(5, snakeThreshold.getSocialLimit());   // Snake needs little social
    assertEquals(20, foxThreshold.getSocialLimit());    // Fox needs more social
  }

  @Test
  public void testSadMoodTriggers() {
    // Create pets with specific health to test mood thresholds
    Pet genericPet = new Pet();
    Snake snake = new Snake();
    Fox fox = new Fox();

    // Test hunger thresholds for sad mood (different for each pet)

    // Generic pet: 65 is the threshold
    genericPet.setHealthStateAndUpdateState(64, 50, 50, 50);
    assertEquals(MoodEnum.HAPPY, genericPet.getMood());

    genericPet.setHealthStateAndUpdateState(65, 50, 50, 50);
    assertEquals(MoodEnum.HAPPY, genericPet.getMood());

    genericPet.setHealthStateAndUpdateState(66, 50, 50, 50);
    assertEquals(MoodEnum.SAD, genericPet.getMood());

    // Snake: 60 is the threshold
    snake.setHealthStateAndUpdateState(59, 50, 50, 50);
    assertEquals(MoodEnum.HAPPY, snake.getMood());

    snake.setHealthStateAndUpdateState(60, 50, 50, 50);
    assertEquals(MoodEnum.HAPPY, snake.getMood());

    snake.setHealthStateAndUpdateState(61, 50, 50, 50);
    assertEquals(MoodEnum.SAD, snake.getMood());

    // Fox: 70 is the threshold
    fox.setHealthStateAndUpdateState(69, 50, 50, 50);
    assertEquals(MoodEnum.HAPPY, fox.getMood());

    fox.setHealthStateAndUpdateState(70, 50, 50, 50);
    assertEquals(MoodEnum.HAPPY, fox.getMood());

    fox.setHealthStateAndUpdateState(71, 50, 50, 50);
    assertEquals(MoodEnum.SAD, fox.getMood());
  }

  @Test
  public void testLongTermBehaviorDifferences() {
    // Create one of each pet type
    Pet genericPet = new Pet();
    Snake snake = new Snake();
    Fox fox = new Fox();

    // Run for 10 steps
    for (int i = 0; i < 10; i++) {
      genericPet.step();
      snake.step();
      fox.step();
    }

    // Snake should have lower hunger increase over time
    assertTrue(snake.getHealth().getHunger() < genericPet.getHealth().getHunger());
    assertTrue(snake.getHealth().getHunger() < fox.getHealth().getHunger());

    // Create new pets and feed occasionally
    genericPet = new Pet();
    snake = new Snake();
    fox = new Fox();

    // Run steps and feed every 3rd step
    for (int i = 0; i < 15; i++) {
      genericPet.step();
      snake.step();
      fox.step();

      if (i % 3 == 0) {
        genericPet.interactWith(Action.FEED);
        snake.interactWith(Action.FEED);
        fox.interactWith(Action.FEED);
      }
    }

    // Check which pet is happier after this regimen
    assertEquals(MoodEnum.HAPPY,
        snake.getMood()); // Snake should stay happy with this feeding schedule
  }
}
