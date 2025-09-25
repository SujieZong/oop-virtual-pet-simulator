# Virtual Pet Simulator

## About/Overview

The Virtual Pet Simulator is a Java application that allows users to select and take care of different types of virtual
pets. Using the Model-View-Controller (MVC) architecture pattern, the application simulates the life cycle of a pet that
requires care across four dimensions: hunger, hygiene, social needs, and sleep. The pet's mood changes based on these
attributes, and different moods result in different behaviors. If any of the pet's needs are neglected beyond a
threshold, the pet will die.

This program simulates the responsibility of pet ownership in a fun and engaging way, with a colorful user interface and
different pet options, each with unique health value settings.

## List of Features

- **Multiple Pet Types**: Choose between three different pet types (Mysterious Pet, Fox, and Snake), each with unique
  thresholds and behavior characteristics.
- **Health Status Tracking**: Monitor four key health attributes of your pet:
    - Hunger
    - Hygiene
    - Social
    - Sleep
- **Mood System**: Pet's mood (Happy or Sad) affects how quickly its needs deteriorate and how it responds to
  interactions.
- **Interactive Care Actions**: Care for your pet through four different interactions:
    - Feed
    - Play
    - Clean
    - Sleep
- **Real-time Updates**: Health status and mood updates in real-time with visual cues.
- **Visual Feedback**: Pet images that change based on mood and current activity.
- **Automatic Deterioration**: Pet's needs automatically deteriorate over time, requiring regular attention.
- **Death Mechanics**: Pet will die if any health attribute crosses a critical threshold.
- **Modern UI**: Clean, colorful interface with buttons and displays.
- **Step Indicator**: Visual indicator when a time step occurs.

## How To Run

### Running the JAR File

1. Ensure you have Java Runtime Environment (JRE) version 8 or higher installed.
2. Open a terminal or command prompt.
3. Navigate to res/pet directory where click the JAR file or run the following command:
    ```bash
    java -jar VirtualPetSimulator.jar
    ```

### Required Arguments

The application does not require any command-line arguments to run.

## How to Use the Program

1. **Select a Pet Type**: When the application starts, you will be presented with a selection screen. Choose one of the
   three pet types:
    - Mysterious Pet
    - Fox
    - Snake

2. **Monitor Your Pet's Status**: After selecting a pet, the main game screen will appear. Here you can monitor:
    - Health Statistics (top panel): Shows numerical values for Hunger, Hygiene, Social, and Sleep.
    - Mood (bottom panel): Displays "HAPPY" or "SAD" depending on your pet's current mood.
    - Pet Image (center): Visual representation of your pet that changes based on mood.

3. **Interact with Your Pet**: Use the buttons at the bottom of the screen to care for your pet:
    - **Feed**: Reduces hunger. The pet will react differently based on its current mood.
    - **Play**: Improves social needs but might increase hunger.
    - **Clean**: Improves hygiene.
    - **Sleep**: Improves sleep.
    - **Step**: Manually advance time (note that time also advances automatically).

4. **Understand Your Pet's Needs**:
    - Hunger increases over time. Keep it below the threshold to keep your pet happy.
    - Hygiene decreases over time. Keep it above the threshold.
    - Social needs decrease over time. Keep them above the threshold.
    - Sleep decreases over time. Keep it above the threshold.
    - Different pet types have different thresholds and behaviors.

5. **Watch for Mood Changes**: Your pet's mood affects how quickly its needs change and how it responds to your actions:
    - Happy pets respond better to interactions and their needs deteriorate more slowly.
    - Sad pets respond less to interactions and their needs deteriorate more quickly.

6. **Game Over**: If any of your pet's needs cross a critical threshold, your pet will die. You'll see a death screen
   and then be returned to the pet selection screen to start again.

## Design/Model Changes


### Version 1.0 to 2.0

- **Multiple Pet Types**: Extended from a single pet type to three different types, each with unique characteristics.
- **Abstract Classes**: Created AbstractPet and AbstractMoodStrategy classes to allow for more flexibility and code
  reuse across different pet types.

### Version 2.0 to 3.0 (Current)

- **Enhanced UI**: Improved the user interface with styling, custom fonts, and images.
- **Action Images**: Added visual feedback for interactions through temporary action images.
- **Step Indicator**: Added visual indication when a time step occurs.
- **Death Screen**: Added a death screen with a restart option.
- **MVC Separation**: Refined the separation between model, view, and controller for better maintenance and
  extensibility.

## Assumptions

- The application assumes a single pet is active at any time.
- The application assumes all health attributes start at a default value of 50 (half-full).
- The application assumes the user has a display capable of supporting the GUI.
- The application assumes the necessary image files are available in the correct directory structure.
- The application assumes attribute values are bounded between 0 and 100.
- The application assumes time steps are uniform.

## Limitations

- The program does not save pet state between sessions. Once the application is closed, all progress is lost.
- The program does not allow customization of pet attributes beyond the predefined types.
- The program does not allow adjustment of the automatic step interval (fixed at 5 seconds).
- The program does not support networked or multiplayer functionality.
- The animation system is simple and does not support complex animation sequences.
- The program does not include sound effects or background music.
- There is no way to pause the game - the pet's needs continue to deteriorate over time.

## Citations

No external resources were used in the development of this application beyond standard Java libraries. All code, design
patterns, and implementation approaches were developed based on fundamental object-oriented programming principles and
the Java Swing framework for GUI components.
