package com.example.mendikot.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mendikot.R
import com.example.mendikot.ui.theme.AccentColor
import com.example.mendikot.ui.theme.BackgroundColor
import com.example.mendikot.ui.theme.CardTableColor

/**
 * Start screen displayed after the splash screen
 */
@Composable
fun StartScreen(
    onNewGameClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onRulesClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(CardTableColor)
    ) {
        // Background decoration - cards scattered
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = null,
            modifier = Modifier
                .size(400.dp)
                .alpha(0.1f)
                .align(Alignment.Center)
        )
        
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Game logo
            Text(
                text = "MENDIKOT",
                fontSize = 44.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            
            Text(
                text = "Card Game",
                fontSize = 20.sp,
                color = Color.White.copy(alpha = 0.8f),
                modifier = Modifier.padding(bottom = 40.dp)
            )
            
            // Main button - Create New Game
            MainMenuButton(
                text = "Create New Game",
                onClick = onNewGameClick
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Secondary buttons
            SecondaryMenuButton(
                text = "Game Rules",
                onClick = onRulesClick
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            SecondaryMenuButton(
                text = "Settings",
                onClick = onSettingsClick
            )
        }
    }
}

@Composable
private fun MainMenuButton(
    text: String,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = AccentColor,
            contentColor = Color.Black
        ),
        modifier = Modifier
            .width(240.dp)
            .height(60.dp)
            .clip(RoundedCornerShape(30.dp))
    ) {
        Text(
            text = text,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun SecondaryMenuButton(
    text: String,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = Color.White
        ),
        modifier = Modifier
            .width(200.dp)
            .height(50.dp)
            .clip(RoundedCornerShape(25.dp))
    ) {
        Text(
            text = text,
            fontSize = 16.sp
        )
    }
} 