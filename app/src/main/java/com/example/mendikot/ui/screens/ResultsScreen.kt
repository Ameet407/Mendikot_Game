package com.example.mendikot.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mendikot.model.Team
import com.example.mendikot.ui.theme.AccentColor
import com.example.mendikot.ui.theme.TeamAColor
import com.example.mendikot.ui.theme.TeamBColor
import kotlinx.coroutines.delay

/**
 * Screen for showing results after a deal is completed
 */
@Composable
fun ResultsScreen(
    teams: List<Team>,
    teamScores: Map<Int, Int>,
    onContinue: () -> Unit
) {
    var showResults by remember { mutableStateOf(false) }
    var showContinueButton by remember { mutableStateOf(false) }
    
    // Determine the winning team
    val teamA = teams.getOrNull(0)
    val teamB = teams.getOrNull(1)
    
    val teamAScore = teamA?.tensCollected?.size ?: 0
    val teamBScore = teamB?.tensCollected?.size ?: 0
    val teamATricks = teamA?.tricksWon ?: 0
    val teamBTricks = teamB?.tricksWon ?: 0
    
    // Correctly determine the winning team based on Mendikot rules
    val winningTeam = when {
        // Mendikot (all 4 tens) or whitewash (all tricks) wins
        teamA?.hasMendikot() == true || teamA?.hasWhitewash() == true -> teamA
        teamB?.hasMendikot() == true || teamB?.hasWhitewash() == true -> teamB
        
        // Team with 3 tens wins
        teamAScore == 3 -> teamA
        teamBScore == 3 -> teamB
        
        // Tens tie breaker (both have 2 tens)
        teamAScore == 2 && teamBScore == 2 -> {
            // Team with 7+ tricks wins in case of tie
            if (teamATricks >= 7) teamA else teamB
        }
        
        // Team with more tens wins
        teamAScore > teamBScore -> teamA
        else -> teamB
    }
    
    val winningTeamColor = if (winningTeam?.id == 0) TeamAColor else TeamBColor
    
    val hasMendikot = winningTeam?.hasMendikot() == true
    val hasWhitewash = winningTeam?.hasWhitewash() == true
    
    // Special case description for when both teams have 2 tens
    val isTensDrawWithTrickBreaker = teamAScore == 2 && teamBScore == 2
    
    // Animation timing
    LaunchedEffect(Unit) {
        delay(500)
        showResults = true
        delay(1500)
        showContinueButton = true
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Main results
            AnimatedVisibility(
                visible = showResults,
                enter = fadeIn(animationSpec = tween(1000)) +
                        slideInVertically(
                            animationSpec = tween(1000),
                            initialOffsetY = { -it }
                        )
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Victory icon
                    Icon(
                        imageVector = Icons.Default.EmojiEvents,
                        contentDescription = null,
                        tint = AccentColor,
                        modifier = Modifier
                            .size(80.dp)
                            .padding(bottom = 16.dp)
                    )
                    
                    // Victory type
                    Text(
                        text = when {
                            hasWhitewash -> "52-Card Mendikot (Whitewash)!"
                            hasMendikot -> "Mendikot!"
                            isTensDrawWithTrickBreaker -> "Trick Tiebreaker!"
                            else -> "Deal Complete"
                        },
                        color = AccentColor,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    // Winning team
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(winningTeamColor.copy(alpha = 0.2f))
                            .border(2.dp, winningTeamColor, RoundedCornerShape(8.dp))
                            .padding(16.dp)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "${if (winningTeam?.id == 0) "Team A" else "Team B"} Wins!",
                                color = Color.White,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold
                            )
                            
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            // Team stats
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                StatItem(
                                    label = "Tens",
                                    value = "${winningTeam?.tensCollected?.size ?: 0}/4"
                                )
                                
                                StatItem(
                                    label = "Tricks",
                                    value = "${winningTeam?.tricksWon ?: 0}/13"
                                )
                            }
                            
                            // Special message for tiebreaker
                            if (isTensDrawWithTrickBreaker) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Both teams had 2 tens each. Trick count was the tiebreaker!",
                                    color = Color.White,
                                    fontSize = 14.sp,
                                    textAlign = TextAlign.Center
                                )
                            }
                            
                            // Players
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            Text(
                                text = winningTeam?.players?.joinToString(" & ") { it.name } ?: "",
                                color = Color.White,
                                fontSize = 16.sp
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    // Overall scores
                    Text(
                        text = "Overall Score",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        ScoreItem(
                            teamName = "Team A",
                            score = teamScores[0] ?: 0,
                            color = TeamAColor
                        )
                        
                        ScoreItem(
                            teamName = "Team B",
                            score = teamScores[1] ?: 0,
                            color = TeamBColor
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Continue button
            AnimatedVisibility(
                visible = showContinueButton,
                enter = fadeIn(animationSpec = tween(500)) +
                        slideInVertically(
                            animationSpec = tween(500),
                            initialOffsetY = { it }
                        )
            ) {
                Button(
                    onClick = onContinue,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.tertiary,
                        contentColor = Color.Black
                    ),
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .height(56.dp)
                ) {
                    Text(
                        text = "Continue to Next Deal",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

/**
 * Helper component to show a single statistic
 */
@Composable
private fun StatItem(
    label: String,
    value: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = label,
            color = Color.White.copy(alpha = 0.7f),
            fontSize = 14.sp
        )
        
        Text(
            text = value,
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

/**
 * Helper component to show a team's score
 */
@Composable
private fun ScoreItem(
    teamName: String,
    score: Int,
    color: Color
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(color.copy(alpha = 0.2f))
            .border(1.dp, color.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
            .padding(horizontal = 24.dp, vertical = 16.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = teamName,
                color = color,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Text(
                text = "$score",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
} 