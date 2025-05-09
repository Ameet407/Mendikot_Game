package com.example.mendikot.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mendikot.model.Card
import com.example.mendikot.model.Player
import com.example.mendikot.model.Team
import com.example.mendikot.ui.components.PlayingCard
import com.example.mendikot.ui.theme.TeamAColor
import com.example.mendikot.ui.theme.TeamBColor
import kotlinx.coroutines.delay

/**
 * Screen for team formation where players draw cards
 */
@Composable
fun TeamFormationScreen(
    players: List<Player>,
    onDrawCard: () -> Unit
) {
    // Privacy protection - player needs to confirm identity
    var confirmingPlayer by remember { mutableStateOf(0) }
    var showCards by remember { mutableStateOf(false) }
    var buttonScale by remember { mutableStateOf(1f) }
    
    // Create a pulsing animation for the button
    val infiniteTransition = rememberInfiniteTransition(label = "ButtonPulse")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(800),
            repeatMode = RepeatMode.Reverse
        ),
        label = "ScalePulse"
    )
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Team Formation",
            color = Color.White,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 24.dp)
        )
        
        Text(
            text = if (showCards) {
                "Pass the device to ${players.getOrNull(confirmingPlayer)?.name ?: "next player"}"
            } else {
                "Each player will draw a card to determine teams"
            },
            color = Color.White,
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        // Add explicit instruction
        if (!showCards) {
            Text(
                text = "👇 Click the button below to continue 👇",
                color = Color(0xFFFFA500), // Orange color for visibility
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }
        
        if (showCards) {
            // Player confirmation screen
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White.copy(alpha = 0.1f))
                    .border(1.dp, Color.White.copy(alpha = 0.3f), RoundedCornerShape(16.dp))
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(48.dp)
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text(
                        text = "I am ${players.getOrNull(confirmingPlayer)?.name}",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    Button(
                        onClick = {
                            if (confirmingPlayer < 3) {
                                confirmingPlayer++
                            } else {
                                // All players have confirmed, proceed to draw cards
                                onDrawCard()
                                showCards = false
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.tertiary,
                            contentColor = Color.Black
                        )
                    ) {
                        Text("Confirm & Draw Card")
                    }
                }
            }
        } else {
            // Draw card instruction with animated button
            Button(
                onClick = { showCards = true },
                modifier = Modifier
                    .scale(scale)
                    .height(56.dp)
                    .fillMaxWidth(0.8f)
                    .shadow(
                        elevation = 8.dp,
                        shape = RoundedCornerShape(8.dp),
                        ambientColor = Color(0xFFFFA500),
                        spotColor = Color(0xFFFFA500)
                    ),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.tertiary,
                    contentColor = Color.Black
                )
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                    
                    Spacer(modifier = Modifier.width(8.dp))
                    
                    Text(
                        text = "Start Drawing Cards",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

/**
 * Screen showing the cards each player drew
 */
@Composable
fun DrawnCardsScreen(
    players: List<Player>,
    drawnCards: Map<Int, Card>,
    onFormTeams: () -> Unit
) {
    var showAllCards by remember { mutableStateOf(false) }
    var showContinueButton by remember { mutableStateOf(false) }
    var showTeamPreview by remember { mutableStateOf(false) }
    
    // Animate the display of all cards after a short delay
    LaunchedEffect(drawnCards) {
        delay(1000)
        showAllCards = true
        delay(1500)
        showTeamPreview = true
        delay(1000)
        showContinueButton = true
    }
    
    // Sort players by card rank for display purposes
    val playersSortedByCard = if (drawnCards.isNotEmpty()) {
        players.sortedByDescending { player -> 
            drawnCards[player.id]?.let { card -> 
                card.rank.value * 100 + card.suit.ordinal 
            } ?: 0
        }
    } else {
        players
    }
    
    // Determine the dealer (lowest card)
    val dealerPlayerId = if (drawnCards.isNotEmpty()) {
        playersSortedByCard.lastOrNull()?.id
    } else {
        null
    }
    
    // Create scroll state
    val scrollState = rememberScrollState()
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 80.dp), // Add padding at bottom for the button
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                text = "Cards Drawn",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 24.dp)
            )
            
            // Use a scrollable container for the content
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(scrollState),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Show each player's drawn card
                playersSortedByCard.forEachIndexed { index, player ->
                    drawnCards[player.id]?.let { card ->
                        // Determine which team this player will be on
                        val teamColor = when {
                            index < 2 -> TeamAColor // First two (highest cards) are Team A
                            else -> TeamBColor      // Last two (lowest cards) are Team B
                        }
                        
                        // Determine if this player is the dealer (lowest card)
                        val isDealer = player.id == dealerPlayerId
                        
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Player name
                            Row(
                                modifier = Modifier.weight(1f),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = player.name,
                                    color = Color.White,
                                    fontSize = 16.sp
                                )
                                
                                // Show dealer indicator if applicable
                                if (isDealer && showTeamPreview) {
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Box(
                                        modifier = Modifier
                                            .size(20.dp)
                                            .clip(RoundedCornerShape(4.dp))
                                            .background(Color.White),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "D",
                                            color = Color.Black,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                            
                            // Team indicator
                            AnimatedVisibility(
                                visible = showTeamPreview,
                                enter = fadeIn(animationSpec = tween(500))
                            ) {
                                Text(
                                    text = if (index < 2) "Team A" else "Team B",
                                    color = teamColor,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(end = 8.dp)
                                )
                            }
                            
                            // Card
                            AnimatedVisibility(
                                visible = showAllCards,
                                enter = fadeIn(animationSpec = tween(500)) + 
                                        slideInVertically(
                                            animationSpec = tween(500),
                                            initialOffsetY = { it }
                                        )
                            ) {
                                PlayingCard(
                                    card = card,
                                    modifier = Modifier
                                        .width(80.dp)
                                        .padding(horizontal = 8.dp)
                                )
                            }
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Team formation explanation
                AnimatedVisibility(
                    visible = showTeamPreview,
                    enter = fadeIn(animationSpec = tween(1000))
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.Black.copy(alpha = 0.3f))
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "Team Formation Rules:",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        
                        Text(
                            text = "• Players with 2 highest cards → Team A",
                            color = TeamAColor,
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center
                        )
                        
                        Text(
                            text = "• Players with 2 lowest cards → Team B",
                            color = TeamBColor,
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center
                        )
                        
                        Text(
                            text = "• Player with lowest card is the dealer",
                            color = Color.White,
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
        
        // Continue button - positioned at the bottom of the screen
        AnimatedVisibility(
            visible = showContinueButton,
            enter = fadeIn(animationSpec = tween(500)) + 
                    slideInVertically(
                        animationSpec = tween(500),
                        initialOffsetY = { it / 2 }
                    ),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp)
        ) {
            Button(
                onClick = { onFormTeams() },
                modifier = Modifier
                    .height(56.dp)
                    .fillMaxWidth(0.8f)
                    .shadow(
                        elevation = 8.dp,
                        shape = RoundedCornerShape(8.dp),
                        ambientColor = Color(0xFFFFA500),
                        spotColor = Color(0xFFFFA500)
                    ),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4CAF50),
                    contentColor = Color.White
                )
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Form Teams",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Spacer(modifier = Modifier.width(8.dp))
                    
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null
                    )
                }
            }
        }
    }
}

/**
 * Screen showing the teams that have been formed
 */
@Composable
fun TeamsFormedScreen(
    players: List<Player>,
    teams: List<Team>,
    dealerId: Int,
    onContinue: () -> Unit
) {
    val dealer = players.find { it.id == dealerId }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Teams Formed",
            color = Color.White,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 24.dp)
        )
        
        // Team A
        val teamA = teams.getOrNull(0)
        if (teamA != null) {
            TeamDisplay(
                team = teamA,
                teamColor = TeamAColor,
                teamName = "Team A"
            )
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Team B
        val teamB = teams.getOrNull(1)
        if (teamB != null) {
            TeamDisplay(
                team = teamB,
                teamColor = TeamBColor,
                teamName = "Team B"
            )
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Dealer announcement
        dealer?.let {
            Text(
                text = "${dealer.name} will be the dealer",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Continue button
        Button(
            onClick = onContinue,
            modifier = Modifier
                .height(56.dp)
                .fillMaxWidth(0.8f),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.tertiary,
                contentColor = Color.Black
            )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Begin Dealing",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                
                Spacer(modifier = Modifier.width(8.dp))
                
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null
                )
            }
        }
    }
}

/**
 * Helper component to display a team and its players
 */
@Composable
private fun TeamDisplay(
    team: Team,
    teamColor: Color,
    teamName: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(teamColor.copy(alpha = 0.2f))
            .border(1.dp, teamColor.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
            .padding(16.dp)
    ) {
        Text(
            text = teamName,
            color = teamColor,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            team.players.forEach { player ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = teamColor,
                        modifier = Modifier.size(24.dp)
                    )
                    
                    Text(
                        text = player.name,
                        color = Color.White,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}

/**
 * Screen shown during card dealing animation
 */
@Composable
fun DealingScreen(
    players: List<Player>,
    dealerId: Int
) {
    val dealer = players.find { it.id == dealerId }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Dealing Cards",
            color = Color.White,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 24.dp)
        )
        
        Text(
            text = "${dealer?.name ?: "Dealer"} is dealing the cards",
            color = Color.White,
            fontSize = 18.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 32.dp)
        )
        
        // Simple animation could be added here
        Text(
            text = "5-4-4 pattern",
            color = Color.White.copy(alpha = 0.7f),
            fontSize = 14.sp
        )
    }
} 