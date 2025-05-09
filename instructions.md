# Offline 4-player Android card game.

Complete Game description : 

Mendikot is a 4-player, 2-team card game played in Maharashtra and Gujarat. The main goal of the game is to capture more 10s than the opposing team. Capturing all four 10s results in a Mendikot, which is a superior win. Capturing all 13 tricks in a game is called a Whitewash or 52-card Mendikot.

## Players & Teams- The game involves 4 players, seated anticlockwise. The players are divided into 2 fixed teams, with partners sitting opposite each other. Teams are decided at the start based on the card draw: The 2 highest-ranked cards form Team A. The 2 lowest-ranked cards form Team B.

## Deck-The game uses a standard 52-card deck.The ranks are from high to low: A, K, Q, J, 10, 9, 8, 7, 6, 5, 4, 3, 2. All four suits (hearts, spades, diamonds, clubs) are used. The 10s (10♠, 10♥, 10♦, 10♣) are key scoring cards. 

## Setup-Step 1: Determine First Dealer Each player draws a card from a shuffled deck. The player with the lowest-ranked card becomes the first dealer. 
Step 2: Decide Teams The two players with the highest cards form one team. The two players with the lowest cards form the other team. 
Step 3: Dealing :The dealer shuffles and deals 13 cards to each player-First round: 5 cards,Second round: 4 cards,Third round: 4 cards. All cards are dealt anticlockwise. 
Step 4: Band Hukum (Trump Selection) The player to the dealer’s right selects one card from their hand and places it face down on the table. The suit of this card becomes the trump suit, but it is not revealed yet. The card is set aside temporarily, not used in play until it is revealed. 

## Gameplay- The player to the dealer’s right leads the first card. Players must follow suit if possible. If a player cannot follow suit, then: The face-down trump card is revealed. The suit of that card becomes the trump for the rest of the deal. The trump card is returned to the owner's hand and can be played normally. The player may then play any card (not forced to play a trump). Once the trump is revealed, all tricks from that point onward are subject to trump rules: If trumps are played, the highest trump wins the trick. If no trumps are played, the highest card of the suit led wins. The winner of each trick leads the next one. 

## Winning a Deal Victory is determined as follows: If one team captures 3 or 4 tens, that team wins the deal. If both teams capture 2 tens, the team that wins 7 or more tricks wins the deal. If a team captures all 4 tens, it results in a Mendikot victory. If a team captures all 13 tricks, it results in a 52-card Mendikot or Whitewash. 

## Who Deals Next? If the dealer's team loses, the same dealer continues for the next deal. If the dealer's team loses with a whitewash, the dealer’s partner becomes the dealer for the next deal. If the dealer's team wins, the deal passes anticlockwise to the next player.


# Mendikot Card Game: Core Functionalities

## 1. Game Launch & Main Menu

### 1.1 Launch Screen
- App opens with an animated splash screen showing the Mendikot logo
- Transitions automatically to the Main Menu after 2-3 seconds

### 1.2 Main Menu
- Prominently displays "Create New Game" button 
- Shows "Game Rules" button for instructions
- Includes "Settings" button for game configurations
- Optional "Continue Last Game" button appears if a saved game exists
- Displays attractive card game themed background

## 2. Player Registration

### 2.1 Player Names Input
- Form with 4 text fields for player names
- Default names provided (Player 1, Player 2, etc.) that can be edited
- Clear visual indication of player positions (showing they'll be seated cross-wise as partners)
- Validation to ensure all names are entered (non-empty)
- Option to randomize seating positions

### 2.2 Settings Configuration
- Toggle for sound effects and background music
- Animation speed settings (fast/normal/slow)
- Toggle for hints/help during gameplay
- Option to customize card deck appearance
- Save settings button

### 2.3 Start Game Preparation
- "Start Game" button becomes active once all names are validated
- Visual confirmation that players are ready

## 3. Team Formation & Initial Setup

### 3.1 Card Draw for Teams
- Animated card drawing sequence
- Each player draws a card (device is passed around)
- Private screen for each player with "Draw Card" button
- Transition screens protect player privacy between turns
- Animation reveals all drawn cards simultaneously after all players have drawn
- Clear visual display of team assignments (highest cards vs lowest cards)

### 3.2 Dealer Determination
- Animation highlighting the player with the lowest card as the first dealer
- Visual indication of dealer position (dealer token/marker)
- Clear instructions for first deal

### 3.3 Game Setup Confirmation
- Display of both teams with player names
- Indication of partnerships (partners sitting opposite)
- "Begin First Deal" button
- Option to return to player names if needed

## 4. Core Gameplay

### 4.1 Card Dealing
- Animated dealing sequence showing cards being dealt in the correct pattern (5-4-4)
- Dealer confirmation screen before dealing starts
- Progress indicator during dealing animation
- Option to skip animation

### 4.2 Trump Selection
- Transition screen instructing to pass device to player right of dealer
- Privacy confirmation ("I am [Player Name]") before showing cards
- Display of player's hand with instruction to select trump card
- Visual highlight of selected card before confirmation
- Animation showing trump card being placed face down

### 4.3 Trick Play Mechanics

#### 4.3.1 Turn Management
- Clear indication of whose turn it is to play
- Transition screens between players with privacy protection
- "I am [Player Name]" confirmation button before showing cards
- Option to see the current trick and score during player's turn

#### 4.3.2 Card Play Interface
- Display of player's hand with playable cards highlighted
- Table area showing cards played in current trick
- Indication of lead suit that must be followed
- Visual feedback when attempting to play invalid card
- Animation for playing card onto the table

#### 4.3.3 Trump Revelation
- Special animation when trump needs to be revealed (when player can't follow suit)
- Clear indication of trump suit after revelation
- Visual indicator of trump suit remains visible for remainder of the game

#### 4.3.4 Trick Resolution
- Animation showing winning card in the trick
- Indication of which player/team won the trick
- Special animation for capturing a ten
- Updated score display after each trick
- Brief pause before starting next trick

### 4.4 Game Progress Tracking
- Current score display (tens captured by each team)
- Tricks won by each team counter
- Trump suit indicator (once revealed)
- Current dealer indicator
- Cards remaining in hand counter

## 5. Game Completion & Results

### 5.1 Single Deal Results
- Animation for game completion
- Special animation for Mendikot (all 4 tens captured)
- Extra special animation for Whitewash (all 13 tricks won)
- Clear display of score and winner of the deal
- Statistics for the deal (tricks won, tens captured)

### 5.2 Multi-Deal Tracking
- Cumulative score across multiple deals
- History of deal results (who won each deal)
- Running tally of Mendikots and Whitewashes

### 5.3 Post-Game Options
- "Continue Playing" button (starts new deal with appropriate next dealer)
- "End Game Session" button
- Optional "Save Game Session" button

## 6. Game Continuation & History

### 6.1 Next Dealer Determination
- Automatic determination based on previous game result:
  - If dealer's team lost: Same dealer continues
  - If dealer's team lost with a Whitewash: Dealer's partner becomes dealer
  - If dealer's team won: Next player (anti-clockwise) becomes dealer
- Visual indication of the new dealer

### 6.2 Series Progression
- Continuous tracking of deals within the session
- Updated overall score display before each new deal
- Option to view history of previous deals in the session

### 6.3 Session History
- Record of all deals played in the current session
- Statistics for each team (deals won, tens captured, Mendikots, Whitewashes)
- Player performance statistics

## 7. Game Ending & Return to Menu

### 7.1 End Game Confirmation
- Confirmation dialog when "End Game Session" is selected
- Option to save game statistics before ending

### 7.2 Final Results
- Comprehensive display of session results
- Declaration of overall session winner (team with most deals won)
- Statistics for the entire session
- Option to share results (screenshot or text summary)

### 7.3 Return to Main Menu
- Clear transition back to main menu
- Option to start a new game with same players
- Option to start completely new game

## 8. Additional Features

### 8.1 Game State Persistence
- Automatic saving of game state after each trick
- Option to resume game if app is closed unexpectedly
- Manual save game option in menu

### 8.2 Undo Functionality
- Option to undo last card played (configurable in settings)
- Undo button available during gameplay
- Confirmation required for undo action

### 8.3 Game Rules & Help
- Comprehensive rules section accessible from main menu
- Context-sensitive help during gameplay
- Quick reference for card rankings and scoring

### 8.4 Accessibility Features
- High contrast mode for cards
- Enlarged text option
- Color blind friendly card indicators
- Adjustable animation speed

### 8.5 Device Rotation & Orientation
- Support for both portrait and landscape orientations
- Optimal layout for different device sizes

### 8.6 Sound & Haptics
- Card shuffle and deal sounds
- Card play sound effects
- Special sounds for capturing tens, Mendikot, etc.
- Optional haptic feedback for card plays
- Background music options

### 8.7 User Preferences
- Remember player names between sessions
- Configurable UI theme (light/dark/custom)
- Save user preferences between app launches

## Implementation Priorities

### Phase 1 (Core Gameplay)
- Main menu with Create Game
- Player name entry
- Team formation
- Basic card dealing and play
- Trump selection and revelation
- Basic trick resolution
- Simple end game screen

### Phase 2 (Game Continuity)
- Multiple deal support
- Next dealer determination
- Game session tracking
- Enhanced results screen
- Continue vs End Game options

### Phase 3 (Polish & Additional Features)
- Animations and sound effects
- Undo functionality
- Game state saving
- Settings and configurations
- Help/tutorial system
- Accessibility features


# Tools for Building the Mendikot Android Card Game

Here's a comprehensive list of all the tools, libraries, and frameworks you'll need to build your offline 4-player Mendikot card game for Android:

## Development Environment

1. **Android Studio** - Latest stable version (Hedgehog | 2023.1.1 or newer)
   - Primary IDE for Android development
   - Includes Android SDK, Android Emulator, and Gradle build system

2. **Cursor AI** - As mentioned, you'll be using this AI-powered code editor

3. **Git** - For version control

4. **Java Development Kit (JDK)** - Version 17 (recommended for recent Android development)

## Programming Languages

1. **Kotlin** - Primary language for Android development
   - Kotlin Coroutines for asynchronous programming
   - Kotlin Flow for reactive programming

## Core Android Libraries

1. **Jetpack Compose** - Modern UI toolkit
   - Compose Navigation for screen transitions
   - Compose Animation for card animations and effects
   - Compose Material 3 for design components

2. **Android Architecture Components**
   - ViewModel
   - LiveData / StateFlow
   - Room Database (for game state persistence)
   - DataStore (for settings/preferences)

## Dependency Injection

1. **Dagger Hilt** - For dependency injection and better code organization

## State Management

1. **StateFlow / SharedFlow** - For reactive state management
2. **SavedStateHandle** - For preserving state across configuration changes

## Graphics & Animation

1. **Lottie** - For complex animations like celebrations
2. **Coil** - For efficient image loading (if using custom card assets)
3. **Jetpack Compose Canvas** - For custom card rendering
4. **Accompanist** - Compose extensions for animations and UI components

## Audio

1. **ExoPlayer** - For background music
2. **MediaPlayer** - For simple sound effects

## Testing

1. **JUnit** - For unit testing
2. **Espresso** - For UI testing
3. **Mockito** - For mocking dependencies during testing
4. **Robolectric** - For testing Android components without emulator


   
## Build & Deployment

1. **Gradle** - Build system (included in Android Studio)
2. **R8/ProGuard** - For code optimization and obfuscation
3. **Android App Bundle** - For efficient app distribution

## Version Control

1. **Git** - For version control
2. **GitHub/GitLab/Bitbucket** - For remote repository hosting

## Design Tools

1. **Figma/Adobe XD** - For UI/UX design
2. **Vector Asset Studio** - For creating and importing vector graphics (included in Android Studio)

## Performance Monitoring

1. **Android Profiler** - For monitoring app performance (included in Android Studio)
2. **LeakCanary** - For detecting memory leaks

## Project Structure

1. **Clean Architecture** - For organizing project code
2. **MVVM Pattern** - For UI architecture

## Accessibility

1. **Android Accessibility Suite** - For testing app accessibility
2. **TalkBack** - For screen reader compatibility testing



###Complete Asset Package for Mendikot Card Game
Here's a curated selection of specific assets for each category needed for your Mendikot card game, with direct links where possible:
Playing Card Assets
Card Deck
SVG Cards by David Bellot

Description: Complete vector card deck with classic design
Link: https://github.com/htdebeer/SVG-cards
License: LGPL 2.1
Why this one: Professional quality, vector format (scales perfectly), all cards included, easily customizable in code

User Interface Elements
UI Kit
Mobile Game UI Kit by Kenney

Description: Complete set of game UI elements (buttons, panels, icons)
Link: https://kenney.nl/assets/game-icons
License: CC0 (Public Domain)
Why this one: Clean design, specifically made for games, matches card game aesthetic

Icons
Material Design Icons

Description: Comprehensive icon set for navigation and actions
Link: https://material.io/resources/icons/
License: Apache 2.0
Why this one: Official Android design language, high quality, multiple formats

Game Backgrounds
Table Background
Green Felt Background

Description: Card table texture
Link: https://www.freepik.com/free-photo/green-poker-table-felt-background_7028435.htm
License: Free with attribution
Why this one: Classic card table look, subtle texture, won't distract from gameplay
