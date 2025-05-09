package com.example.mendikot.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mendikot.model.Card
import com.example.mendikot.model.Player
import com.example.mendikot.model.Suit
import com.example.mendikot.ui.theme.CardTableColor
import com.example.mendikot.ui.theme.SuitBlack
import com.example.mendikot.ui.theme.SuitRed
import com.example.mendikot.ui.theme.TeamAColor
import com.example.mendikot.ui.theme.TeamBColor
import kotlinx.coroutines.delay

/**
 * Displays the game table with player positions and the current trick
 */
@Composable
fun GameTable(
    players: List<Player>,
    currentTrick: List<Card>,
    currentPlayerId: Int,
    dealerId: Int,
    trumpSuit: Suit?,
    trumpRevealed: Boolean,
    playedCardPlayerIds: List<Int> = emptyList(),
    trickCompleted: Boolean = false,
    trickWinnerId: Int = -1,
    modifier: Modifier = Modifier
) {
    // Animation states
    var isAnimatingTrickCompletion by remember { mutableStateOf(false) }
    var showVictoryMessage by remember { mutableStateOf(false) }
    
    // Start animation when a trick is completed
    LaunchedEffect(trickCompleted) {
        if (trickCompleted && !isAnimatingTrickCompletion) {
            // Wait a moment for the last card to be visible
            delay(1000)
            
            // Begin card movement animation
            isAnimatingTrickCompletion = true
            
            // Show victory message after cards start moving
            delay(600)
            showVictoryMessage = true
            
            // Animation will continue until GameViewModel clears the trick
            // which will reset this component with empty trick
        }
    }
    
    // Reset animation states when trick is cleared
    LaunchedEffect(currentTrick.size) {
        if (currentTrick.isEmpty()) {
            isAnimatingTrickCompletion = false
            showVictoryMessage = false
        }
    }
    
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(CardTableColor)
            .border(1.dp, Color.Black.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
            .padding(8.dp)
    ) {
        // Center trick area
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            // Trump suit indicator
            if (trumpSuit != null && trumpRevealed) {
                TrumpSuitIndicator(trumpSuit = trumpSuit)
            }
            
            // Current trick
            CurrentTrick(
                currentTrick = currentTrick,
                playedCardPlayerIds = playedCardPlayerIds,
                trickWinnerId = if (isAnimatingTrickCompletion) trickWinnerId else -1,
                isAnimating = isAnimatingTrickCompletion,
                modifier = Modifier.fillMaxSize()
            )
        }
        
        // Player indicators
        PlayerIndicator(
            player = players.getOrNull(0),
            isCurrentPlayer = currentPlayerId == 0,
            isDealer = dealerId == 0,
            position = PlayerPosition.TOP,
            isWinner = trickWinnerId == 0 && showVictoryMessage,
            modifier = Modifier.align(Alignment.TopCenter)
        )
        
        PlayerIndicator(
            player = players.getOrNull(1),
            isCurrentPlayer = currentPlayerId == 1,
            isDealer = dealerId == 1,
            position = PlayerPosition.LEFT,
            isWinner = trickWinnerId == 1 && showVictoryMessage,
            modifier = Modifier.align(Alignment.CenterStart)
        )
        
        PlayerIndicator(
            player = players.getOrNull(2),
            isCurrentPlayer = currentPlayerId == 2,
            isDealer = dealerId == 2,
            position = PlayerPosition.BOTTOM,
            isWinner = trickWinnerId == 2 && showVictoryMessage,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
        
        PlayerIndicator(
            player = players.getOrNull(3),
            isCurrentPlayer = currentPlayerId == 3,
            isDealer = dealerId == 3,
            position = PlayerPosition.RIGHT,
            isWinner = trickWinnerId == 3 && showVictoryMessage,
            modifier = Modifier.align(Alignment.CenterEnd)
        )
        
        // Victory message overlay (positioned on top of everything)
        if (showVictoryMessage && trickWinnerId >= 0) {
            val winner = players.find { it.id == trickWinnerId }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.8f))
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "🏆 Trick Won! 🏆",
                        color = Color(0xFFFFD700), // Gold color
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Text(
                        text = "${winner?.name ?: "Player"} won the trick",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    Text(
                        text = "and will lead next!",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

/**
 * Positions of players around the table
 */
enum class PlayerPosition {
    TOP, RIGHT, BOTTOM, LEFT
}

/**
 * Displays a player indicator at one of the positions around the table
 */
@Composable
private fun PlayerIndicator(
    player: Player?,
    isCurrentPlayer: Boolean,
    isDealer: Boolean,
    position: PlayerPosition,
    isWinner: Boolean = false,
    modifier: Modifier = Modifier
) {
    if (player == null) return
    
    val teamColor = if (player.teamId == 0) TeamAColor else TeamBColor
    val backgroundColor = when {
        isWinner -> Color(0xFFFFD700) // Gold color for winner
        isCurrentPlayer -> teamColor
        else -> teamColor.copy(alpha = 0.6f)
    }
    
    Box(modifier = modifier.padding(8.dp)) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Draw turn indicator if it's this player's turn
            if (isCurrentPlayer && !isWinner) {
                Icon(
                    imageVector = if (position == PlayerPosition.BOTTOM) 
                        Icons.Default.ArrowUpward else Icons.Default.ArrowDownward,
                    contentDescription = "Current Turn",
                    tint = Color.Yellow,
                    modifier = Modifier.size(24.dp)
                )
                
                Spacer(modifier = Modifier.height(4.dp))
            }
            
            // Winner star indicator
            if (isWinner) {
                Text(
                    text = "⭐",
                    fontSize = 24.sp,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
            }
            
            // Player indicator
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(backgroundColor)
                    .padding(horizontal = 12.dp, vertical = 6.dp)
                    .scale(if (isWinner) 1.1f else 1.0f) // Scale up the winner's name
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = if (isWinner) Color.Black else Color.White,
                    modifier = Modifier.size(16.dp)
                )
                
                Spacer(modifier = Modifier.width(4.dp))
                
                Text(
                    text = player.name,
                    color = if (isWinner) Color.Black else Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                
                if (isDealer) {
                    Spacer(modifier = Modifier.width(4.dp))
                    
                    // Dealer indicator
                    Box(
                        modifier = Modifier
                            .size(16.dp)
                            .clip(CircleShape)
                            .background(Color.White),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "D",
                            color = Color.Black,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

/**
 * Displays the cards currently in play for the trick
 */
@Composable
private fun CurrentTrick(
    currentTrick: List<Card>,
    playedCardPlayerIds: List<Int>,
    trickWinnerId: Int = -1,
    isAnimating: Boolean = false,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        // Create a map of player ID to played card
        val cardsByPlayerId = mutableMapOf<Int, Card>()
        
        // Get all played cards by player ID
        currentTrick.forEachIndexed { index, card ->
            val playerId = playedCardPlayerIds.getOrNull(index)
            if (playerId != null) {
                cardsByPlayerId[playerId] = card
            }
        }
        
        // If a card was just played, add emphasis to it
        val lastPlayerId = playedCardPlayerIds.lastOrNull()
        val lastCardJustPlayed = lastPlayerId != null && currentTrick.size == 4 && !isAnimating
        
        // Top position card (Player 0)
        cardsByPlayerId[0]?.let { card ->
            val shouldAnimate = isAnimating && trickWinnerId != 0
            val isLastPlayed = lastPlayerId == 0 && lastCardJustPlayed
            val targetAlignment = when (trickWinnerId) {
                0 -> Alignment.TopCenter
                1 -> Alignment.CenterStart
                2 -> Alignment.BottomCenter
                3 -> Alignment.CenterEnd
                else -> Alignment.TopCenter
            }
            
            PlayingCard(
                card = card,
                isAnimating = shouldAnimate,
                targetAlignment = targetAlignment,
                highlighted = isLastPlayed,
                modifier = Modifier
                    .align(if (shouldAnimate) targetAlignment else Alignment.TopCenter)
                    .padding(top = if (!shouldAnimate) 40.dp else 0.dp)
                    .width(70.dp)
                    .scale(if (isLastPlayed) 1.1f else 1f)
            )
        }
        
        // Left position card (Player 1)
        cardsByPlayerId[1]?.let { card ->
            val shouldAnimate = isAnimating && trickWinnerId != 1
            val isLastPlayed = lastPlayerId == 1 && lastCardJustPlayed
            val targetAlignment = when (trickWinnerId) {
                0 -> Alignment.TopCenter
                1 -> Alignment.CenterStart
                2 -> Alignment.BottomCenter
                3 -> Alignment.CenterEnd
                else -> Alignment.CenterStart
            }
            
            PlayingCard(
                card = card,
                isAnimating = shouldAnimate,
                targetAlignment = targetAlignment,
                highlighted = isLastPlayed,
                modifier = Modifier
                    .align(if (shouldAnimate) targetAlignment else Alignment.CenterStart)
                    .padding(start = if (!shouldAnimate) 40.dp else 0.dp)
                    .width(70.dp)
                    .scale(if (isLastPlayed) 1.1f else 1f)
            )
        }
        
        // Bottom position card (Player 2)
        cardsByPlayerId[2]?.let { card ->
            val shouldAnimate = isAnimating && trickWinnerId != 2
            val isLastPlayed = lastPlayerId == 2 && lastCardJustPlayed
            val targetAlignment = when (trickWinnerId) {
                0 -> Alignment.TopCenter
                1 -> Alignment.CenterStart
                2 -> Alignment.BottomCenter
                3 -> Alignment.CenterEnd
                else -> Alignment.BottomCenter
            }
            
            PlayingCard(
                card = card,
                isAnimating = shouldAnimate,
                targetAlignment = targetAlignment,
                highlighted = isLastPlayed,
                modifier = Modifier
                    .align(if (shouldAnimate) targetAlignment else Alignment.BottomCenter)
                    .padding(bottom = if (!shouldAnimate) 40.dp else 0.dp)
                    .width(70.dp)
                    .scale(if (isLastPlayed) 1.1f else 1f)
            )
        }
        
        // Right position card (Player 3)
        cardsByPlayerId[3]?.let { card ->
            val shouldAnimate = isAnimating && trickWinnerId != 3
            val isLastPlayed = lastPlayerId == 3 && lastCardJustPlayed
            val targetAlignment = when (trickWinnerId) {
                0 -> Alignment.TopCenter
                1 -> Alignment.CenterStart
                2 -> Alignment.BottomCenter
                3 -> Alignment.CenterEnd
                else -> Alignment.CenterEnd
            }
            
            PlayingCard(
                card = card,
                isAnimating = shouldAnimate,
                targetAlignment = targetAlignment,
                highlighted = isLastPlayed,
                modifier = Modifier
                    .align(if (shouldAnimate) targetAlignment else Alignment.CenterEnd)
                    .padding(end = if (!shouldAnimate) 40.dp else 0.dp)
                    .width(70.dp)
                    .scale(if (isLastPlayed) 1.1f else 1f)
            )
        }
    }
}

/**
 * Displays the trump suit indicator
 */
@Composable
private fun TrumpSuitIndicator(
    trumpSuit: Suit,
    modifier: Modifier = Modifier
) {
    val isRed = trumpSuit == Suit.HEARTS || trumpSuit == Suit.DIAMONDS
    val suitColor = if (isRed) SuitRed else SuitBlack
    
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(Color.White.copy(alpha = 0.8f))
            .padding(8.dp)
    ) {
        Text(
            text = "Trump",
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp,
            color = Color.Black
        )
        
        Text(
            text = trumpSuit.symbol,
            fontSize = 24.sp,
            color = suitColor,
            textAlign = TextAlign.Center
        )
    }
} 