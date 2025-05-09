package com.example.mendikot.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mendikot.ui.theme.CardTableColor

/**
 * Screen displaying the rules of the Mendikot card game
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RulesScreen(
    onBack: () -> Unit
) {
    val scrollState = rememberScrollState()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Game Rules") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
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
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(scrollState)
            ) {
                Text(
                    text = "Mendikot Card Game Rules",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Introduction
                RuleSection(
                    title = "Introduction",
                    content = "Mendikot is a 4-player, 2-team card game played in Maharashtra and Gujarat. " +
                            "The main goal of the game is to capture more 10s than the opposing team. " +
                            "Capturing all four 10s results in a Mendikot, which is a superior win. " +
                            "Capturing all 13 tricks in a game is called a Whitewash or 52-card Mendikot."
                )
                
                // Players & Teams
                RuleSection(
                    title = "Players & Teams",
                    content = "The game involves 4 players, seated anticlockwise. " +
                            "The players are divided into 2 fixed teams, with partners sitting opposite each other. " +
                            "Teams are decided at the start based on the card draw: " +
                            "The 2 highest-ranked cards form Team A. The 2 lowest-ranked cards form Team B."
                )
                
                // Deck
                RuleSection(
                    title = "Deck",
                    content = "The game uses a standard 52-card deck. " +
                            "The ranks are from high to low: A, K, Q, J, 10, 9, 8, 7, 6, 5, 4, 3, 2. " +
                            "All four suits (hearts, spades, diamonds, clubs) are used. " +
                            "The 10s (10♠, 10♥, 10♦, 10♣) are key scoring cards."
                )
                
                // Setup
                RuleSection(
                    title = "Setup",
                    content = "Step 1: Determine First Dealer - Each player draws a card from a shuffled deck. " +
                            "The player with the lowest-ranked card becomes the first dealer.\n\n" +
                            "Step 2: Decide Teams - The two players with the highest cards form one team. " +
                            "The two players with the lowest cards form the other team.\n\n" +
                            "Step 3: Dealing - The dealer shuffles and deals 13 cards to each player:\n" +
                            "- First round: 5 cards\n" +
                            "- Second round: 4 cards\n" +
                            "- Third round: 4 cards\n" +
                            "All cards are dealt anticlockwise.\n\n" +
                            "Step 4: Band Hukum (Trump Selection) - The player to the dealer's right selects one card " +
                            "from their hand and places it face down on the table. The suit of this card becomes the trump suit, " +
                            "but it is not revealed yet. The card is set aside temporarily, not used in play until it is revealed."
                )
                
                // Gameplay
                RuleSection(
                    title = "Gameplay",
                    content = "The player to the dealer's right leads the first card. " +
                            "Players must follow suit if possible. " +
                            "If a player cannot follow suit, then:\n\n" +
                            "- The face-down trump card is revealed.\n" +
                            "- The suit of that card becomes the trump for the rest of the deal.\n" +
                            "- The trump card is returned to the owner's hand and can be played normally.\n" +
                            "- The player may then play any card (not forced to play a trump).\n\n" +
                            "Once the trump is revealed, all tricks from that point onward are subject to trump rules:\n" +
                            "- If trumps are played, the highest trump wins the trick.\n" +
                            "- If no trumps are played, the highest card of the suit led wins.\n" +
                            "The winner of each trick leads the next one."
                )
                
                // Winning a Deal
                RuleSection(
                    title = "Winning a Deal",
                    content = "Victory is determined as follows:\n\n" +
                            "- If one team captures 3 or 4 tens, that team wins the deal.\n" +
                            "- If both teams capture 2 tens, the team that wins 7 or more tricks wins the deal.\n" +
                            "- If a team captures all 4 tens, it results in a Mendikot victory.\n" +
                            "- If a team captures all 13 tricks, it results in a 52-card Mendikot or Whitewash."
                )
                
                // Who Deals Next
                RuleSection(
                    title = "Who Deals Next",
                    content = "- If the dealer's team loses, the same dealer continues for the next deal.\n" +
                            "- If the dealer's team loses with a whitewash, the dealer's partner becomes the dealer for the next deal.\n" +
                            "- If the dealer's team wins, the deal passes anticlockwise to the next player."
                )
                
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

/**
 * A section of rules with a title and content
 */
@Composable
private fun RuleSection(
    title: String,
    content: String
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f)
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            
            Divider(
                color = Color.White.copy(alpha = 0.3f),
                thickness = 1.dp,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            
            Text(
                text = content,
                fontSize = 14.sp,
                color = Color.White,
                lineHeight = 20.sp
            )
        }
    }
} 