package com.example.mendikot.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mendikot.game.GameEngine
import com.example.mendikot.model.Card
import com.example.mendikot.model.GameState
import com.example.mendikot.model.Player
import com.example.mendikot.model.Suit
import com.example.mendikot.model.Team
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay

/**
 * ViewModel that manages the game state and UI interactions
 */
class GameViewModel : ViewModel() {

    private val gameEngine = GameEngine()
    
    // Game UI state
    private val _uiState = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()
    
    // Preferences
    private val _preferences = MutableStateFlow(GamePreferences())
    val preferences: StateFlow<GamePreferences> = _preferences.asStateFlow()
    
    /**
     * Initialize a new game
     */
    fun initializeGame(playerNames: List<String>) {
        val gameState = gameEngine.initializeGame(playerNames)
        
        _uiState.update { currentState ->
            currentState.copy(
                gameStatus = GameStatus.TEAM_FORMATION,
                players = gameState.players,
                teams = gameState.teams,
                currentMessage = "Draw cards to determine teams"
            )
        }
    }
    
    /**
     * Draw cards for team formation
     */
    fun drawCardsForTeamFormation() {
        val playerCardMap = gameEngine.drawCardsForTeamFormation()
        
        _uiState.update { currentState ->
            currentState.copy(
                drawnCards = playerCardMap,
                gameStatus = GameStatus.SHOWING_DRAWN_CARDS,
                currentMessage = "Cards drawn! Click 'Form Teams' to continue."
            )
        }
        
        // We no longer automatically form teams after a delay
        // The user must click the "Form Teams" button
    }
    
    /**
     * Form teams when the user clicks the button in the DrawnCardsScreen
     */
    fun formTeamsOnClick() {
        val drawnCards = _uiState.value.drawnCards
        if (drawnCards.isNotEmpty()) {
            formTeams(drawnCards)
        }
    }
    
    /**
     * Form teams based on drawn cards
     */
    private fun formTeams(playerCardMap: Map<Int, Card>) {
        val teamMap = gameEngine.formTeamsBasedOnCards(playerCardMap)
        val gameState = gameEngine.getGameState()
        
        _uiState.update { currentState ->
            currentState.copy(
                teams = gameState.teams,
                dealerId = gameState.currentDealer,
                gameStatus = GameStatus.TEAMS_FORMED,
                currentMessage = "Teams formed! ${gameState.getPlayer(gameState.currentDealer)?.name} is the dealer."
            )
        }
    }
    
    /**
     * Start dealing cards
     */
    fun startDealing() {
        gameEngine.dealCards()
        val gameState = gameEngine.getGameState()
        
        _uiState.update { currentState ->
            currentState.copy(
                players = gameState.players,
                gameStatus = GameStatus.SELECTING_TRUMP,
                currentPlayerId = (gameState.currentDealer + 1) % 4,
                currentMessage = "Cards dealt! ${gameState.getPlayer((gameState.currentDealer + 1) % 4)?.name} select trump card."
            )
        }
    }
    
    /**
     * Select the trump card
     */
    fun selectTrump(playerId: Int, card: Card) {
        gameEngine.selectTrump(playerId, card)
        val gameState = gameEngine.getGameState()
        
        _uiState.update { currentState ->
            currentState.copy(
                trumpCard = card,
                trumpSuit = card.suit,
                gameStatus = GameStatus.PLAYING_TRICK,
                currentPlayerId = gameState.currentTurn,
                currentMessage = "${gameState.getPlayer(gameState.currentTurn)?.name}'s turn to play."
            )
        }
    }
    
    /**
     * Play a card
     */
    fun playCard(playerId: Int, card: Card) {
        val success = gameEngine.playCard(playerId, card)
        if (!success) return
        
        val gameState = gameEngine.getGameState()
        
        // Check if this is the last card of the last trick (final trick of the game)
        val isLastCardOfGame = gameState.players.all { it.hand.isEmpty() }
        
        // Check if trick is complete (4 cards played) but not yet animated
        val trickComplete = (gameState.currentTrick.isEmpty() && 
                           _uiState.value.currentTrick.size == 3 && 
                           _uiState.value.playedCardPlayerIds.size == 3) ||
                           // For the last trick of the game, check if we just played the 4th card
                           (isLastCardOfGame && _uiState.value.currentTrick.size == 3)
        
        // We need to manually determine the trick winner for animation
        val trickWinnerId = if (trickComplete) gameState.currentTurn else -1
        
        // Update UI based on current game state
        _uiState.update { currentState ->
            // For any trick (including the last trick), we want to show the animation before moving forward
            val updatedStatus = if (trickComplete) {
                // Keep in PLAYING_TRICK state until animation completes
                GameStatus.PLAYING_TRICK
            } else if (gameState.players.all { it.hand.isEmpty() } && 
                       gameState.currentTrick.isEmpty() && 
                       !currentState.trickCompleted) {
                GameStatus.DEAL_COMPLETE
            } else {
                GameStatus.PLAYING_TRICK
            }
            
            // Update current trick
            val updatedTrick = if (gameState.currentTrick.isEmpty() && trickComplete) {
                // If trick completed but we need to animate it, keep the cards visible
                currentState.currentTrick + card
            } else if (isLastCardOfGame && currentState.currentTrick.size == 3) {
                // For the last trick of the game, manually add the final card
                currentState.currentTrick + card
            } else {
                gameState.currentTrick.map { it.card }
            }
            
            // Update card player IDs
            val updatedPlayedCardIds = if (gameState.currentTrick.isEmpty() && trickComplete) {
                // If trick completed but we need to animate it, keep the card owners visible
                currentState.playedCardPlayerIds + playerId
            } else if (isLastCardOfGame && currentState.currentTrick.size == 3) {
                // For the last trick of the game, manually add the final card's player
                currentState.playedCardPlayerIds + playerId
            } else {
                gameState.currentTrick.map { it.playerId }
            }
            
            val message = when {
                trickComplete -> "${gameState.getPlayer(trickWinnerId)?.name} won the trick and leads next."
                gameState.currentTrick.isEmpty() -> "${gameState.getPlayer(gameState.currentTurn)?.name} won the trick and leads next."
                else -> "${gameState.getPlayer(gameState.currentTurn)?.name}'s turn to play."
            }
            
            currentState.copy(
                players = gameState.players,
                currentTrick = updatedTrick,
                playedCardPlayerIds = updatedPlayedCardIds,
                currentPlayerId = gameState.currentTurn,
                trumpRevealed = gameState.trumpRevealed,
                trumpSuit = gameState.trumpSuit,
                leadSuit = gameState.leadSuit,
                gameStatus = updatedStatus,
                teams = gameState.teams,
                currentMessage = message,
                trickCompleted = trickComplete,
                trickWinnerId = if (trickComplete) trickWinnerId else -1
            )
        }
        
        // Handle trick animation if trick completed
        if (_uiState.value.trickCompleted) {
            viewModelScope.launch {
                // Allow time for the trick animation to play out
                delay(3500)
                
                // Clear the trick after animation completes
                _uiState.update { currentState ->
                    // If this was the last trick, now we can move to DEAL_COMPLETE
                    val newStatus = if (isLastCardOfGame) {
                        GameStatus.DEAL_COMPLETE
                    } else {
                        currentState.gameStatus
                    }
                    
                    currentState.copy(
                        currentTrick = emptyList(),
                        playedCardPlayerIds = emptyList(),
                        trickCompleted = false,
                        trickWinnerId = -1,
                        gameStatus = newStatus
                    )
                }
                
                // If this was the last trick and now we're moving to deal complete, show results
                if (isLastCardOfGame) {
                    // Give a moment to see the empty table before showing results
                    delay(1000)
                    showDealResults()
                }
            }
        }
        
        // If deal is complete (but not from the last trick path which has its own handling)
        // This might happen in edge cases or if game state changes directly to DEAL_COMPLETE
        if (_uiState.value.gameStatus == GameStatus.DEAL_COMPLETE && !isLastCardOfGame && !_uiState.value.trickCompleted) {
            viewModelScope.launch {
                // Simulating a delay to show results
                delay(2000)
                showDealResults()
            }
        }
    }
    
    /**
     * Show the results of the completed deal
     */
    private fun showDealResults() {
        val gameState = gameEngine.getGameState()
        val teamA = gameState.getTeam(0)
        val teamB = gameState.getTeam(1)
        
        val teamAScore = teamA?.tensCollected?.size ?: 0
        val teamBScore = teamB?.tensCollected?.size ?: 0
        val teamATricks = teamA?.tricksWon ?: 0
        val teamBTricks = teamB?.tricksWon ?: 0
        
        // Correctly determine the winning team based on Mendikot rules
        val winningTeamId = when {
            // Mendikot (all 4 tens) or whitewash (all tricks) wins
            teamA?.hasMendikot() == true || teamA?.hasWhitewash() == true -> 0
            teamB?.hasMendikot() == true || teamB?.hasWhitewash() == true -> 1
            
            // Team with 3 tens wins
            teamAScore == 3 -> 0
            teamBScore == 3 -> 1
            
            // Tens tie breaker (both have 2 tens)
            teamAScore == 2 && teamBScore == 2 -> 
                if (teamATricks >= 7) 0 else 1
            
            // Team with more tens wins
            teamAScore > teamBScore -> 0
            else -> 1
        }
        
        val winningTeam = gameState.getTeam(winningTeamId)
        val winningTeamTens = if (winningTeamId == 0) teamAScore else teamBScore
        val winningTeamTricks = if (winningTeamId == 0) teamATricks else teamBTricks
        
        val isTensDrawWithTrickBreaker = teamAScore == 2 && teamBScore == 2
        
        val winningMessage = when {
            winningTeam?.hasMendikot() == true -> "Team ${winningTeam.name} scored a Mendikot!"
            winningTeam?.hasWhitewash() == true -> "Team ${winningTeam.name} scored a 52-card Mendikot (Whitewash)!"
            isTensDrawWithTrickBreaker -> "Team ${winningTeam?.name} won with $winningTeamTricks tricks (both teams had 2 tens)."
            else -> "Team ${winningTeam?.name} won the deal with $winningTeamTens tens."
        }
        
        _uiState.update { currentState ->
            currentState.copy(
                gameStatus = GameStatus.SHOWING_RESULTS,
                currentMessage = winningMessage,
                teamScores = gameState.getCurrentTeamScores()
            )
        }
    }
    
    /**
     * Start a new deal
     */
    fun startNewDeal() {
        gameEngine.startNewDeal()
        val gameState = gameEngine.getGameState()
        
        _uiState.update { currentState ->
            currentState.copy(
                players = gameState.players,
                currentTrick = emptyList(),
                playedCardPlayerIds = emptyList(),
                trumpCard = null,
                trumpSuit = null,
                trumpRevealed = false,
                dealerId = gameState.currentDealer,
                gameStatus = GameStatus.DEALING,
                currentMessage = "${gameState.getPlayer(gameState.currentDealer)?.name} is dealing."
            )
        }
        
        // Simulate dealing animation
        viewModelScope.launch {
            kotlinx.coroutines.delay(1500)
            _uiState.update { currentState ->
                currentState.copy(
                    gameStatus = GameStatus.SELECTING_TRUMP,
                    currentPlayerId = (gameState.currentDealer + 1) % 4,
                    currentMessage = "${gameState.getPlayer((gameState.currentDealer + 1) % 4)?.name} select trump card."
                )
            }
        }
    }
    
    /**
     * Update game settings
     */
    fun updatePreferences(newPreferences: GamePreferences) {
        _preferences.update { newPreferences }
    }
    
    /**
     * Reset the game to start a new session
     */
    fun resetGame() {
        _uiState.update { GameUiState() }
    }
    
    /**
     * Reveals the trump card to the player
     */
    fun revealTrumpCard() {
        val gameState = gameEngine.getGameState()
        if (!gameState.trumpRevealed) {
            gameEngine.revealTrump()
            
            _uiState.update { currentState ->
                currentState.copy(
                    trumpRevealed = true,
                    currentMessage = "Trump card revealed: ${gameState.trumpSuit?.name}"
                )
            }
        }
    }
}

/**
 * Represents the UI state of the game
 */
data class GameUiState(
    val gameStatus: GameStatus = GameStatus.NOT_STARTED,
    val players: List<Player> = emptyList(),
    val teams: List<Team> = emptyList(),
    val currentPlayerId: Int = -1,
    val dealerId: Int = -1,
    val currentTrick: List<Card> = emptyList(),
    val playedCardPlayerIds: List<Int> = emptyList(),
    val trumpCard: Card? = null,
    val trumpSuit: Suit? = null,
    val trumpRevealed: Boolean = false,
    val leadSuit: Suit? = null,
    val teamScores: Map<Int, Int> = emptyMap(),
    val drawnCards: Map<Int, Card> = emptyMap(),
    val currentMessage: String = "",
    val trickCompleted: Boolean = false,   // Flag to indicate a trick is complete and should be animated
    val trickWinnerId: Int = -1            // The player who won the last trick
)

/**
 * Represents game settings and preferences
 */
data class GamePreferences(
    val soundEnabled: Boolean = true,
    val musicEnabled: Boolean = true,
    val animationSpeed: AnimationSpeed = AnimationSpeed.NORMAL,
    val showHints: Boolean = true,
    val vibrationEnabled: Boolean = true
)

/**
 * Possible animation speeds
 */
enum class AnimationSpeed {
    FAST, NORMAL, SLOW
}

/**
 * Represents the various states of gameplay
 */
enum class GameStatus {
    NOT_STARTED,
    TEAM_FORMATION,
    SHOWING_DRAWN_CARDS,
    TEAMS_FORMED,
    DEALING,
    SELECTING_TRUMP,
    PLAYING_TRICK,
    DEAL_COMPLETE,
    SHOWING_RESULTS,
    GAME_OVER
} 