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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mendikot.ui.theme.CardTableColor
import com.example.mendikot.ui.theme.OnBackgroundColor
import com.example.mendikot.ui.theme.TeamAColor
import com.example.mendikot.ui.theme.TeamBColor

/**
 * Screen for entering the names of the 4 players
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerRegistrationScreen(
    onStartGame: (List<String>) -> Unit,
    onBack: () -> Unit
) {
    var player1Name by remember { mutableStateOf("Player 1") }
    var player2Name by remember { mutableStateOf("Player 2") }
    var player3Name by remember { mutableStateOf("Player 3") }
    var player4Name by remember { mutableStateOf("Player 4") }
    var buttonEnabled by remember { mutableStateOf(true) }
    
    val focusManager = LocalFocusManager.current
    
    // Validate that all player names are filled
    LaunchedEffect(player1Name, player2Name, player3Name, player4Name) {
        buttonEnabled = player1Name.isNotBlank() && 
                player2Name.isNotBlank() && 
                player3Name.isNotBlank() && 
                player4Name.isNotBlank()
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Player Registration") },
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
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Enter Player Names",
                    color = OnBackgroundColor,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 24.dp)
                )
                
                Text(
                    text = "Players will be seated cross-wise as partners after team formation",
                    color = OnBackgroundColor.copy(alpha = 0.8f),
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 24.dp)
                )
                
                // Player name inputs
                PlayerNameInputField(
                    label = "Player 1",
                    value = player1Name,
                    onValueChange = { player1Name = it },
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                PlayerNameInputField(
                    label = "Player 2",
                    value = player2Name,
                    onValueChange = { player2Name = it },
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                PlayerNameInputField(
                    label = "Player 3",
                    value = player3Name,
                    onValueChange = { player3Name = it },
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                PlayerNameInputField(
                    label = "Player 4",
                    value = player4Name,
                    onValueChange = { player4Name = it },
                    onNext = { focusManager.clearFocus() }
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Diagram showing how players will be seated
                SeatingDiagram()
                
                // Use fixed spacing instead of weight which can push button off screen
                Spacer(modifier = Modifier.height(24.dp))
                
                // Random button
                IconButton(
                    onClick = { 
                        // Reset to default names
                        player1Name = "Player 1"
                        player2Name = "Player 2"
                        player3Name = "Player 3"
                        player4Name = "Player 4"
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Reset Names",
                        tint = OnBackgroundColor
                    )
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Instruction for button
                Text(
                    text = "👇 Tap to Start Game 👇",
                    color = Color(0xFFFFA500), // Orange color for visibility
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Start Game button
                Button(
                    onClick = { 
                        onStartGame(listOf(player1Name, player2Name, player3Name, player4Name)) 
                    },
                    enabled = buttonEnabled,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF4CAF50), // Bright green
                        contentColor = Color.White
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .shadow(
                            elevation = 8.dp,
                            shape = RoundedCornerShape(8.dp),
                            ambientColor = Color(0xFF4CAF50),
                            spotColor = Color(0xFF4CAF50)
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
                            text = "Start Game",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PlayerNameInputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    onNext: () -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        leadingIcon = { 
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null
            )
        },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Next
        ),
        keyboardActions = KeyboardActions(
            onNext = { onNext() }
        ),
        modifier = Modifier.fillMaxWidth(),
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = Color.White.copy(alpha = 0.1f),
            focusedContainerColor = Color.White.copy(alpha = 0.2f),
            unfocusedTextColor = Color.White,
            focusedTextColor = Color.White
        )
    )
}

@Composable
private fun SeatingDiagram() {
    Box(
        modifier = Modifier
            .padding(vertical = 8.dp)
            .size(160.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.3f))
            .border(
                width = 1.dp,
                color = Color.White.copy(alpha = 0.3f),
                shape = RoundedCornerShape(8.dp)
            )
    ) {
        // Vertical lines connecting teams
        Box(
            modifier = Modifier
                .height(100.dp)
                .width(2.dp)
                .align(Alignment.Center)
                .background(Color.White.copy(alpha = 0.3f))
        )
        
        // Horizontal lines connecting teams
        Box(
            modifier = Modifier
                .width(100.dp)
                .height(2.dp)
                .align(Alignment.Center)
                .background(Color.White.copy(alpha = 0.3f))
        )
        
        // Player positions
        PlayerPositionItem(
            text = "Player 1",
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 4.dp),
            teamColor = TeamAColor
        )
        
        PlayerPositionItem(
            text = "Player 2",
            modifier = Modifier
                .align(Alignment.CenterStart) 
                .padding(start = 4.dp),
            teamColor = TeamBColor
        )
        
        PlayerPositionItem(
            text = "Player 3",
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 4.dp),
            teamColor = TeamAColor
        )
        
        PlayerPositionItem(
            text = "Player 4",
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 4.dp),
            teamColor = TeamBColor
        )
    }
}

@Composable
private fun PlayerPositionItem(
    text: String,
    modifier: Modifier = Modifier,
    teamColor: Color
) {
    Box(
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(teamColor.copy(alpha = 0.7f))
                .padding(horizontal = 12.dp, vertical = 6.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(14.dp)
            )
            
            Spacer(modifier = Modifier.width(2.dp))
            
            Text(
                text = text,
                color = Color.White,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
} 