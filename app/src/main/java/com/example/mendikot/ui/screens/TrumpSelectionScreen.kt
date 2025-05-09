package com.example.mendikot.ui.screens

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
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.mendikot.model.Card
import com.example.mendikot.model.Player
import com.example.mendikot.ui.components.PlayerHand

/**
 * Screen for selecting a trump card
 */
@Composable
fun TrumpSelectionScreen(
    currentPlayerId: Int,
    players: List<Player>,
    onSelectTrump: (Int, Card) -> Unit
) {
    val currentPlayer = players.find { it.id == currentPlayerId }
    
    // Privacy protection - player needs to confirm identity
    var showingCards by remember { mutableStateOf(false) }
    var selectedCard by remember { mutableStateOf<Card?>(null) }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Trump Selection",
            color = Color.White,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 24.dp)
        )
        
        if (!showingCards) {
            // Player confirmation screen
            Box(
                modifier = Modifier
                    .fillMaxWidth()
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
                        text = "Pass the device to ${currentPlayer?.name}",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    Button(
                        onClick = { showingCards = true }
                    ) {
                        Text("I am ${currentPlayer?.name}")
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Trump selection instructions
            Text(
                text = "Player to dealer's right must select a trump card.",
                color = Color.White,
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "The selected card will be set aside face down until it needs to be revealed.",
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        } else {
            // Trump selection screen
            Text(
                text = "${currentPlayer?.name}, select a card to be the trump",
                color = Color.White,
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            Spacer(modifier = Modifier.weight(1f))
            
            // Player's hand to choose from
            currentPlayer?.let { player ->
                PlayerHand(
                    cards = player.hand,
                    playableCards = player.hand, // All cards are selectable for trump
                    onCardSelected = { card ->
                        selectedCard = card
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Selected card display
            selectedCard?.let { card ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Selected Trump: ",
                        color = Color.White,
                        fontSize = 16.sp
                    )
                    
                    Spacer(modifier = Modifier.width(8.dp))
                    
                    Text(
                        text = "${card.rank.symbol}${card.suit.symbol}",
                        color = Color.Yellow,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Confirm button
            Button(
                onClick = {
                    selectedCard?.let { card ->
                        onSelectTrump(currentPlayerId, card)
                    }
                },
                enabled = selectedCard != null,
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(56.dp)
            ) {
                Text(
                    text = "Confirm Trump Selection",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
} 