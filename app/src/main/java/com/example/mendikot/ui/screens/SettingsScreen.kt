package com.example.mendikot.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mendikot.ui.theme.CardTableColor
import com.example.mendikot.viewmodel.AnimationSpeed
import com.example.mendikot.viewmodel.GamePreferences

/**
 * Screen for adjusting game settings
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    preferences: GamePreferences,
    onUpdatePreferences: (GamePreferences) -> Unit,
    onBack: () -> Unit
) {
    var soundEnabled by remember { mutableStateOf(preferences.soundEnabled) }
    var musicEnabled by remember { mutableStateOf(preferences.musicEnabled) }
    var animationSpeed by remember { mutableStateOf(preferences.animationSpeed) }
    var showHints by remember { mutableStateOf(preferences.showHints) }
    var vibrationEnabled by remember { mutableStateOf(preferences.vibrationEnabled) }
    var hasChanges by remember { mutableStateOf(false) }
    
    // Check for changes in settings
    fun checkForChanges() {
        hasChanges = soundEnabled != preferences.soundEnabled ||
                musicEnabled != preferences.musicEnabled ||
                animationSpeed != preferences.animationSpeed ||
                showHints != preferences.showHints ||
                vibrationEnabled != preferences.vibrationEnabled
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(CardTableColor)
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // Settings cards
            SettingCard(title = "Sound & Haptics") {
                // Sound toggle
                SettingSwitch(
                    title = "Sound Effects",
                    checked = soundEnabled,
                    onCheckedChange = {
                        soundEnabled = it
                        checkForChanges()
                    }
                )
                
                // Music toggle
                SettingSwitch(
                    title = "Background Music",
                    checked = musicEnabled,
                    onCheckedChange = {
                        musicEnabled = it
                        checkForChanges()
                    }
                )
                
                // Vibration toggle
                SettingSwitch(
                    title = "Vibration Feedback",
                    checked = vibrationEnabled,
                    onCheckedChange = {
                        vibrationEnabled = it
                        checkForChanges()
                    }
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Animation speed settings
            SettingCard(title = "Animation Speed") {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .selectableGroup()
                ) {
                    AnimationSpeed.values().forEach { speed ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                                .selectable(
                                    selected = animationSpeed == speed,
                                    onClick = {
                                        animationSpeed = speed
                                        checkForChanges()
                                    },
                                    role = Role.RadioButton
                                )
                                .padding(horizontal = 16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = animationSpeed == speed,
                                onClick = null
                            )
                            
                            Spacer(modifier = Modifier.width(8.dp))
                            
                            Text(
                                text = speed.name.lowercase().capitalize(),
                                color = Color.White
                            )
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Gameplay settings
            SettingCard(title = "Gameplay") {
                SettingSwitch(
                    title = "Show Hints",
                    description = "Display helpful tips during gameplay",
                    checked = showHints,
                    onCheckedChange = {
                        showHints = it
                        checkForChanges()
                    }
                )
            }
            
            Spacer(modifier = Modifier.weight(1f))
            
            // Save button
            if (hasChanges) {
                Button(
                    onClick = {
                        onUpdatePreferences(
                            GamePreferences(
                                soundEnabled = soundEnabled,
                                musicEnabled = musicEnabled,
                                animationSpeed = animationSpeed,
                                showHints = showHints,
                                vibrationEnabled = vibrationEnabled
                            )
                        )
                        hasChanges = false
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null
                    )
                    
                    Spacer(modifier = Modifier.width(8.dp))
                    
                    Text(
                        text = "Save Changes",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

/**
 * A card container for a group of related settings
 */
@Composable
private fun SettingCard(
    title: String,
    content: @Composable () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f)
        ),
        modifier = Modifier.fillMaxWidth()
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
            
            Spacer(modifier = Modifier.height(16.dp))
            
            content()
        }
    }
}

/**
 * A toggle switch setting with title and optional description
 */
@Composable
private fun SettingSwitch(
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    description: String? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                color = Color.White,
                fontSize = 16.sp
            )
            
            if (description != null) {
                Text(
                    text = description,
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 14.sp
                )
            }
        }
        
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
    
    Divider(
        color = Color.White.copy(alpha = 0.1f),
        thickness = 1.dp,
        modifier = Modifier.padding(vertical = 8.dp)
    )
}

private fun String.capitalize(): String {
    return this.replaceFirstChar { it.uppercase() }
} 