package com.example.mendikot.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.mendikot.model.Card
import com.example.mendikot.model.Suit

/**
 * Displays a player's hand of cards in a horizontal scrollable row
 */
@Composable
fun PlayerHand(
    cards: List<Card>,
    onCardSelected: (Card) -> Unit,
    playableCards: List<Card> = emptyList(),
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(150.dp)
            .padding(horizontal = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        if (cards.isEmpty()) {
            // Show empty slots if no cards
            LazyRow(
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy((-16).dp)
            ) {
                items(5) {
                    EmptyCardSlot(
                        modifier = Modifier
                            .height(120.dp)
                            .zIndex(it.toFloat())
                    )
                }
            }
        } else {
            // Show player's cards
            LazyRow(
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy((-30).dp)
            ) {
                items(cards) { card ->
                    val isPlayable = playableCards.contains(card)
                    
                    PlayingCard(
                        card = card,
                        highlighted = isPlayable,
                        onClick = { 
                            if (isPlayable) {
                                onCardSelected(card)
                            }
                        },
                        modifier = Modifier
                            .height(120.dp)
                            .zIndex(if (isPlayable) 10f else 1f)
                    )
                }
            }
        }
    }
}

/**
 * Displays a player's cards in a compact way, used for opponents' hands
 */
@Composable
fun OpponentHand(
    cardCount: Int,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        // Only show card backs for opponents
        LazyRow(
            contentPadding = PaddingValues(horizontal = 4.dp),
            horizontalArrangement = Arrangement.spacedBy((-40).dp)
        ) {
            items(cardCount) { index ->
                val mockCard = Card(Suit.SPADES, com.example.mendikot.model.Rank.ACE) // Doesn't matter since it's face down
                SmallPlayingCard(
                    card = mockCard,
                    faceDown = true,
                    modifier = Modifier.zIndex(index.toFloat())
                )
            }
        }
    }
} 