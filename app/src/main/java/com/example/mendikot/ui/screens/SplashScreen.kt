package com.example.mendikot.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mendikot.R
import kotlinx.coroutines.delay

/**
 * Splash screen displayed when the app is launched
 */
@Composable
fun SplashScreen(
    onSplashComplete: () -> Unit
) {
    var visible by remember { mutableStateOf(false) }
    var logoVisible by remember { mutableStateOf(false) }
    
    // Animation values
    val scaleValue by animateFloatAsState(
        targetValue = if (logoVisible) 1f else 0.5f,
        animationSpec = tween(
            durationMillis = 1000,
            easing = FastOutSlowInEasing
        ),
        label = "scale"
    )
    
    // Show splash screen for a short time
    LaunchedEffect(key1 = true) {
        visible = true
        delay(300)
        logoVisible = true
        delay(2000)
        visible = false
        delay(300)
        onSplashComplete()
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        AnimatedVisibility(
            visible = visible,
            enter = fadeIn(animationSpec = tween(300)),
            exit = fadeOut(animationSpec = tween(300))
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Use card images to create a simple logo
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_foreground),
                    contentDescription = "Logo",
                    modifier = Modifier
                        .size(200.dp)
                        .scale(scaleValue)
                        .alpha(if (logoVisible) 1f else 0f)
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    text = "MENDIKOT",
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .scale(scaleValue)
                        .alpha(if (logoVisible) 1f else 0f)
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = "Card Game",
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 16.sp,
                    modifier = Modifier
                        .scale(scaleValue)
                        .alpha(if (logoVisible) 1f else 0f)
                )
            }
        }
    }
} 