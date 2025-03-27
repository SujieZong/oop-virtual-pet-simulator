package pet;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Tests focused on edge cases and unusual situations specific to the Snake pet.
 */
public class EdgeCaseSnakeTest {

  @Test
  public void testSnakeLowSocialNeeds() {
    // Snake has extremely low social needs (5)
    Snake snake = new Snake();

    // Test social death threshold (5)
    snake.setHealthStateAndUpdateState(50, 50, 6, 50);
    assertFalse(snake.isDead());

    snake.setHealthStateAndUpdateState(50, 50, 5, 50);
    assertFalse(snake.isDead());

    snake.setHealthStateAndUpdateState(50, 50, 4, 50);
    assertTrue(snake.isDead());

    // Test social sad threshold (10)
    Snake happySnake = new Snake();
    happySnake.setHealthStateAndUpdateState(50, 50, 11, 50);
    assertEquals(MoodEnum.HAPPY, happySnake.getMood());

    happySnake.setHealthStateAndUpdateState(50, 50, 10, 50);
    assertEquals(MoodEnum.HAPPY, happySnake.getMood());

    happySnake.setHealthStateAndUpdateState(50, 50, 9, 50);
    assertEquals(MoodEnum.SAD, happySnake.getMood());
  }

  @Test
  public void testSnakeHygieneNotAffectingMood() {
    // Snake's updateMood() doesn't check hygiene (unlike Fox)
    Snake snake = new Snake();

    // Set very low hygiene but other stats are good
    snake.setHealthStateAndUpdateState(50, 5, 50, 50);

    // Snake should still be happy despite low hygiene
    assertEquals(MoodEnum.HAPPY, snake.getMood());

    // But hygiene still affects death
    snake.setHealthStateAndUpdateState(50, 15, 50, 50);
    assertFalse(snake.isDead());

    snake.setHealthStateAndUpdateState(50, 14, 50, 50);
    assertTrue(snake.isDead());
  }

  @Test
  public void testSnakeLowerHungerTolerance() {
    // Snake has a lower hunger tolerance (80) than the Fox (90)
    Snake snake = new Snake();

    // Set hunger to just below threshold
    snake.setHealthStateAndUpdateState(79, 50, 50, 50);
    assertFalse(snake.isDead());

    // Set hunger to threshold
    snake.setHealthStateAndUpdateState(80, 50, 50, 50);
    assertFalse(snake.isDead());

    // Set hunger just above threshold
    snake.setHealthStateAndUpdateState(81, 50, 50, 50);
    assertTrue(snake.isDead());
  }

  @Test
  public void testSnakeSleepEfficiency() {
    // Snake has a lower sleep death threshold (5) but gets sad below 30
    Snake snake = new Snake();

    // Test sleep death threshold
    snake.setHealthStateAndUpdateState(50, 50, 50, 6);
    assertFalse(snake.isDead());

    snake.setHealthStateAndUpdateState(50, 50, 50, 5);
    assertFalse(snake.isDead());

    snake.setHealthStateAndUpdateState(50, 50, 50, 4);
    assertTrue(snake.isDead());

    // Test sleep sad threshold (30)
    Snake happySnake = new Snake();
    happySnake.setHealthStateAndUpdateState(50, 50, 50, 31);
    assertEquals(MoodEnum.HAPPY, happySnake.getMood());

    happySnake.setHealthStateAndUpdateState(50, 50, 50, 30);
    assertEquals(MoodEnum.HAPPY, happySnake.getMood());

    happySnake.setHealthStateAndUpdateState(50, 50, 50, 29);
    assertEquals(MoodEnum.SAD, happySnake.getMood());

    // This means snake can be sad from sleep (below 30) but
    // still far from death (below 5)
  }

  @Test
  public void testSnakeMultipleThresholds() {
    Snake snake = new Snake();

    // Multiple attributes at threshold simultaneously
    snake.setHealthStateAndUpdateState(60, 50, 10, 30);
    assertEquals(MoodEnum.HAPPY, snake.getMood());

    // Shift one attribute just past threshold
    snake.setHealthStateAndUpdateState(61, 50, 10, 30);
    assertEquals(MoodEnum.SAD, snake.getMood());

    // Restore and shift a different attribute
    snake.setHealthStateAndUpdateState(60, 50, 9, 30);
    assertEquals(MoodEnum.SAD, snake.getMood());

    // Restore and shift a third attribute
    snake.setHealthStateAndUpdateState(60, 50, 10, 29);
    assertEquals(MoodEnum.SAD, snake.getMood());

    // Test death threshold boundaries
    snake = new Snake();
    snake.setHealthStateAndUpdateState(80, 15, 5, 5);
    assertFalse(snake.isDead());

    // Shift one value beyond threshold
    snake.setHealthStateAndUpdateState(81, 15, 5, 5);
    assertTrue(snake.isDead());
  }

  @Test
  public void testSnakeHungerIncreasesSlowly() {
    // Snake's hunger should increase more slowly than other pets
    Snake snake = new Snake();
    Pet pet = new Pet();
    Fox fox = new Fox();

    // All start at hunger = 50

    // Take 10 steps
    for (int i = 0; i < 10; i++) {
      snake.step();
      pet.step();
      fox.step();
    }

    // Compare final hunger values
    assertTrue("Snake's hunger should increase more slowly than generic pet",
        snake.getHealth().getHunger() < pet.getHealth().getHunger());
    assertTrue("Snake's hunger should increase more slowly than fox",
        snake.getHealth().getHunger() < fox.getHealth().getHunger());
  }

  @Test
  public void testSnakeSurviveWithMinimalCare() {
    // Snake should be able to survive with minimal care due to slow decay
    Snake snake = new Snake();
    Pet pet = new Pet();

    // Care pattern: feed only every 5 steps
    for (int i = 0; i < 25; i++) {
      snake.step();
      pet.step();

      if (i % 5 == 0) {
        snake.interactWith(Action.FEED);
        pet.interactWith(Action.FEED);
      }
    }

    // Snake should still be alive with this minimal feeding
    assertFalse(snake.isDead());

    // Generic pet might be dead or close to death with same care
    if (!pet.isDead()) {
      assertTrue("Generic pet should be closer to death than snake with same care",
          pet.getHealth().getHunger() > snake.getHealth().getHunger());
    }
  }

  @Test
  public void testSnakeLargerMealsLessOften() {
    // Snake should get more benefit from feeding
    Snake snake = new Snake();
    Pet pet = new Pet();

    // Feed once
    snake.interactWith(Action.FEED);
    pet.interactWith(Action.FEED);

    // Check hunger reduction
    int snakeHungerAfterFeed = snake.getHealth().getHunger();
    int petHungerAfterFeed = pet.getHealth().getHunger();

    assertTrue("Snake should get more hunger reduction from feeding",
        snakeHungerAfterFeed < petHungerAfterFeed);
  }
}