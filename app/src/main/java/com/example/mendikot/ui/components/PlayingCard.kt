package com.example.mendikot.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.mendikot.model.Card
import com.example.mendikot.model.Suit
import com.example.mendikot.ui.theme.CardBorderColor
import com.example.mendikot.ui.theme.SuitBlack
import com.example.mendikot.ui.theme.SuitRed

/**
 * Displays a playing card with suit and rank
 */
@Composable
fun PlayingCard(
    card: Card,
    modifier: Modifier = Modifier,
    onClick: ((Card) -> Unit)? = null,
    faceDown: Boolean = false,
    highlighted: Boolean = false,
    isAnimating: Boolean = false,
    targetAlignment: Alignment = Alignment.Center
) {
    val shape = RoundedCornerShape(8.dp)
    val isRed = card.suit == Suit.HEARTS || card.suit == Suit.DIAMONDS
    val suitColor = if (isRed) SuitRed else SuitBlack
    
    // Visual properties based on state
    val borderWidth = if (highlighted) 3.dp else 1.dp
    val borderColor = if (highlighted) Color.Yellow else CardBorderColor
    val backgroundColor = if (faceDown) Color(0xFF1A237E) else Color.White
    
    // Animation for card collection
    val scale by animateFloatAsState(
        targetValue = if (isAnimating) 0.7f else 1f,
        animationSpec = tween(durationMillis = 800),
        label = "cardScale"
    )
    
    val zIndex by animateFloatAsState(
        targetValue = if (isAnimating) 10f else 1f,
        animationSpec = tween(durationMillis = 800),
        label = "cardZIndex"
    )
    
    // Glow effect for animated cards
    val shadowElevation = if (isAnimating) 8.dp else 0.dp
    val shadowColor = if (isAnimating) Color.Yellow.copy(alpha = 0.5f) else Color.Transparent
    
    Box(
        modifier = modifier
            .scale(scale)
            .zIndex(zIndex)
            .aspectRatio(0.7f)
            .clip(shape)
            .background(backgroundColor)
            .border(
                width = if (isAnimating) 2.dp else borderWidth,
                color = if (isAnimating) Color.Yellow else borderColor,
                shape = shape
            )
            .then(
                if (onClick != null) Modifier.clickable { onClick(card) } else Modifier
            )
    ) {
        if (!faceDown) {
            // Card content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(4.dp)
            ) {
                // Top-left corner
                Box(
                    modifier = Modifier
                        .padding(1.dp)
                        .align(Alignment.Start)
                ) {
                    Text(
                        text = card.rank.symbol,
                        fontWeight = FontWeight.Bold,
                        color = suitColor,
                        fontSize = 16.sp
                    )
                }
                
                // Center suit symbol
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = card.suit.symbol,
                        color = suitColor,
                        fontSize = 32.sp,
                        textAlign = TextAlign.Center
                    )
                }
                
                // Bottom-right corner
                Box(
                    modifier = Modifier
                        .padding(1.dp)
                        .align(Alignment.End)
                ) {
                    Text(
                        text = card.rank.symbol,
                        fontWeight = FontWeight.Bold,
                        color = suitColor,
                        fontSize = 16.sp
                    )
                }
            }
        } else {
            // Card back design
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
                    .background(Color(0xFF0D47A1), RoundedCornerShape(4.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "M",
                    color = Color(0xFF2196F3),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

/**
 * Displays a smaller version of a playing card
 */
@Composable
fun SmallPlayingCard(
    card: Card,
    modifier: Modifier = Modifier,
    onClick: ((Card) -> Unit)? = null,
    faceDown: Boolean = false,
    highlighted: Boolean = false
) {
    PlayingCard(
        card = card,
        modifier = modifier.size(50.dp),
        onClick = onClick,
        faceDown = faceDown,
        highlighted = highlighted
    )
}

/**
 * Displays an empty card slot (placeholder for cards)
 */
@Composable
fun EmptyCardSlot(
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(8.dp)
    
    Box(
        modifier = modifier
            .aspectRatio(0.7f)
            .clip(shape)
            .background(Color.White.copy(alpha = 0.1f))
            .border(1.dp, CardBorderColor.copy(alpha = 0.3f), shape)
    )
} 