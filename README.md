# Mendikot Card Game

A modern Android implementation of the traditional Mendikot card game, built with Jetpack Compose and following MVVM architecture patterns.

<p align="center">
  <img src="app/src/main/res/mipmap-xxxhdpi/ic_launcher_round.webp" alt="Mendikot Logo" width="120"/>
</p>

## About the Game

Mendikot is a traditional trick-taking card game for 4 players divided into two teams. The game involves strategy, skill, and a bit of luck. The main goal is to collect "tens" (cards with a value of 10) and winning tricks to score points.

### Key Game Features:

- Team formation via drawing cards
- 13 trick-based gameplay with a trump suit
- Special winning conditions like Mendikot (collecting all 4 tens)
- Dealer rotation mechanics
- Traditional scoring system

### Winning Conditions:

The winning team is determined in this order:
1. **Mendikot**: A team that collects all 4 tens wins automatically
2. **Whitewash**: A team that wins all 13 tricks gets a perfect score
3. **3 Tens**: A team that collects 3 tens wins the deal
4. **Tiebreaker**: If both teams collect 2 tens each, the team with 7 or more tricks wins
5. **Most Tens**: Otherwise, the team with more tens wins the deal

## Screenshots

<p align="center">
  <img src="screenshots/home screen.png" alt="Home Screen" width="250"/>
  <img src="screenshots/player registration screen.png" alt="Player Registration" width="250"/>
  <img src="screenshots/team formation screen.png" alt="Team Formation" width="250"/>
</p>

<p align="center">
  <img src="screenshots/game table screen.png" alt="Game Table" width="250"/>
  <img src="screenshots/result screen.png" alt="Results Screen" width="250"/>
</p>

## Features

- ✨ Beautiful UI built with Material 3 design principles
- 🎮 Complete implementation of Mendikot card game rules
- 🎭 Smooth animations for card dealing, trick winning, and game flow
- 👥 Support for 4 players (pass & play style)
- 📱 Responsive design that works on various screen sizes
- 🔊 Sound effects and visual feedback
- 📖 In-game rules reference

## Project Structure

The project follows the MVVM (Model-View-ViewModel) architecture pattern:

```
com.example.mendikot/
├── data/           # Data persistence and preferences
├── game/           # Game engine and logic
│   └── GameEngine.kt  # Core game mechanics
├── model/          # Data models and entities
│   ├── Card.kt     # Playing card representation
│   ├── GameState.kt # Current game state
│   ├── Player.kt   # Player information
│   ├── Team.kt     # Team formation and scoring
│   └── Suit.kt     # Card suit enums
├── ui/             # User interface components
│   ├── components/ # Reusable UI components
│   │   ├── GameTable.kt    # Game table display
│   │   └── PlayingCard.kt  # Card rendering
│   ├── screens/    # Main app screens
│   │   ├── GamePlayScreen.kt      # Main gameplay UI
│   │   └── TeamFormationScreens.kt # Team formation screens
│   └── theme/      # App theming and styling
├── viewmodel/      # ViewModels for state management
│   └── GameViewModel.kt # Game state and UI interactions
└── MainActivity.kt  # Application entry point
```

## Technologies Used

- **Language:** Kotlin
- **UI Framework:** Jetpack Compose
- **Architecture:** MVVM (Model-View-ViewModel)
- **Navigation:** Jetpack Navigation Compose
- **State Management:** StateFlow and Compose State
- **Dependencies:**
  - Jetpack Compose Material 3
  - Lifecycle ViewModel
  - Navigation Compose
  - Coil for image loading
  - Lottie for animations
  - Media3 for audio
  - DataStore for preferences

## Getting Started

### Prerequisites

- Android Studio Hedgehog (2023.1.1) or newer
- Android SDK 34 or newer
- JDK 11 or newer

### Building and Running the Project

1. Clone the repository:
   ```bash
   git clone https://github.com/Ameet407/Mendikot_Game.git
   ```

2. Open the project in Android Studio:
   - Launch Android Studio
   - Select "Open an existing Android Studio project"
   - Navigate to the cloned repository and select it

3. Sync the project with Gradle files:
   - Android Studio should automatically sync the project
   - If it doesn't, select "File > Sync Project with Gradle Files"

4. Build the project:
   - Select "Build > Make Project" or use the shortcut (Ctrl+F9 on Windows/Linux, Cmd+F9 on macOS)

5. Run the app:
   - Connect an Android device or use an emulator
   - Select "Run > Run 'app'" or use the shortcut (Shift+F10 on Windows/Linux, Ctrl+R on macOS)

### Running Tests

Execute the automated tests by right-clicking on the test directory and selecting "Run Tests" or by using:

```bash
./gradlew test
```

## Contributing

Contributions are welcome! If you'd like to contribute to this project, please follow these steps:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Author

Developed by Ameet Kumar Mishra

