package com.example.mendikot.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mendikot.model.Card
import com.example.mendikot.model.Player
import com.example.mendikot.ui.components.GameTable
import com.example.mendikot.ui.components.PlayerHand
import com.example.mendikot.ui.theme.CardTableColor
import com.example.mendikot.viewmodel.GamePreferences
import com.example.mendikot.viewmodel.GameStatus
import com.example.mendikot.viewmodel.GameUiState

/**
 * Main game play screen that handles all stages of gameplay
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GamePlayScreen(
    gameUiState: GameUiState,
    preferences: GamePreferences,
    onPlayCard: (Int, Card) -> Unit,
    onSelectTrump: (Int, Card) -> Unit,
    onRevealTrump: () -> Unit,
    onDrawCard: () -> Unit,
    onFormTeams: () -> Unit,
    onStartDealing: () -> Unit,
    onStartNewDeal: () -> Unit,
    onReturnToMainMenu: () -> Unit
) {
    // State to control confirmation dialog
    var showExitDialog by remember { mutableStateOf(false) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mendikot") },
                navigationIcon = {
                    IconButton(onClick = { showExitDialog = true }) {
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = "Home"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(CardTableColor)
                .padding(paddingValues)
        ) {
            // Main game content
            when (gameUiState.gameStatus) {
                GameStatus.TEAM_FORMATION -> TeamFormationScreen(
                    players = gameUiState.players,
                    onDrawCard = onDrawCard
                )
                
                GameStatus.SHOWING_DRAWN_CARDS -> DrawnCardsScreen(
                    players = gameUiState.players,
                    drawnCards = gameUiState.drawnCards,
                    onFormTeams = onFormTeams
                )
                
                GameStatus.TEAMS_FORMED -> TeamsFormedScreen(
                    players = gameUiState.players,
                    teams = gameUiState.teams,
                    dealerId = gameUiState.dealerId,
                    onContinue = onStartDealing
                )
                
                GameStatus.DEALING -> DealingScreen(
                    players = gameUiState.players,
                    dealerId = gameUiState.dealerId
                )
                
                GameStatus.SELECTING_TRUMP -> TrumpSelectionScreen(
                    currentPlayerId = gameUiState.currentPlayerId,
                    players = gameUiState.players,
                    onSelectTrump = onSelectTrump
                )
                
                GameStatus.PLAYING_TRICK -> PlayTrickScreen(
                    gameUiState = gameUiState,
                    onPlayCard = onPlayCard,
                    onRevealTrump = onRevealTrump
                )
                
                GameStatus.SHOWING_RESULTS -> ResultsScreen(
                    teams = gameUiState.teams,
                    teamScores = gameUiState.teamScores,
                    onContinue = onStartNewDeal
                )
                
                else -> {
                    // Default screen or loading state
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Loading game...",
                            color = Color.White,
                            fontSize = 24.sp
                        )
                    }
                }
            }
            
            // Game message overlay at the bottom
            if (gameUiState.currentMessage.isNotEmpty()) {
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .background(Color.Black.copy(alpha = 0.6f))
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = gameUiState.currentMessage,
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
        
        // Exit confirmation dialog
        if (showExitDialog) {
            AlertDialog(
                onDismissRequest = { showExitDialog = false },
                title = { Text("Return to Main Menu?", color = Color.Black, fontWeight = FontWeight.Bold) },
                text = { Text("Your current game progress will be lost.", color = Color.Black) },
                confirmButton = {
                    Button(
                        onClick = {
                            showExitDialog = false
                            onReturnToMainMenu()
                        }
                    ) {
                        Text("Yes, Exit")
                    }
                },
                dismissButton = {
                    Button(
                        onClick = { showExitDialog = false },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondary
                        )
                    ) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}

/**
 * Screen to display the player's hand and the game table during trick play
 */
@Composable
fun PlayTrickScreen(
    gameUiState: GameUiState,
    onPlayCard: (Int, Card) -> Unit,
    onRevealTrump: () -> Unit
) {
    // Only show the current player's cards
    val currentPlayer = gameUiState.players.find { it.id == gameUiState.currentPlayerId }
    val playableCards = if (currentPlayer != null) {
        currentPlayer.getPlayableCards(gameUiState.leadSuit, gameUiState.trumpSuit)
    } else {
        emptyList()
    }
    
    // Check if player doesn't have lead suit
    val hasLeadSuit = currentPlayer?.let { player ->
        gameUiState.leadSuit?.let { leadSuit ->
            player.hand.any { it.suit == leadSuit }
        }
    } ?: true
    
    // State to control the trump card popup
    var showTrumpDialog by remember { mutableStateOf(false) }
    
    // If player doesn't have lead suit and it's their turn, show popup
    val shouldShowTrumpPopup = !hasLeadSuit && 
                              gameUiState.leadSuit != null && 
                              !gameUiState.trumpRevealed
    
    LaunchedEffect(currentPlayer?.id, gameUiState.leadSuit) {
        if (shouldShowTrumpPopup) {
            showTrumpDialog = true
        }
    }
    
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Game table in the center
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            GameTable(
                players = gameUiState.players,
                currentTrick = gameUiState.currentTrick,
                currentPlayerId = gameUiState.currentPlayerId,
                dealerId = gameUiState.dealerId,
                trumpSuit = gameUiState.trumpSuit,
                trumpRevealed = gameUiState.trumpRevealed,
                playedCardPlayerIds = gameUiState.playedCardPlayerIds,
                trickCompleted = gameUiState.trickCompleted,
                trickWinnerId = gameUiState.trickWinnerId,
                modifier = Modifier.fillMaxSize()
            )
        }
        
        // Player's hand at the bottom
        if (currentPlayer != null) {
            PlayerHand(
                cards = currentPlayer.hand,
                playableCards = playableCards,
                onCardSelected = { card ->
                    onPlayCard(currentPlayer.id, card)
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
    
    // Trump card reveal popup
    if (showTrumpDialog) {
        AlertDialog(
            onDismissRequest = { showTrumpDialog = false },
            title = { Text("No Lead Suit Cards") },
            text = { 
                Text(
                    "You don't have any cards of the lead suit (${gameUiState.leadSuit?.name}). " +
                    "Would you like to see the trump card?"
                ) 
            },
            confirmButton = {
                Button(
                    onClick = { 
                        onRevealTrump()
                        showTrumpDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF4CAF50)
                    )
                ) {
                    Text("Show Trump")
                }
            },
            dismissButton = {
                Button(
                    onClick = { showTrumpDialog = false },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary
                    )
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}

/**
 * Other screen components will be defined in separate files for clarity
 */ 