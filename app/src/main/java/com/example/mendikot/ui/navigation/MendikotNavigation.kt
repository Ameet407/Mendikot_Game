package com.example.mendikot.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.mendikot.ui.screens.GamePlayScreen
import com.example.mendikot.ui.screens.PlayerRegistrationScreen
import com.example.mendikot.ui.screens.RulesScreen
import com.example.mendikot.ui.screens.SettingsScreen
import com.example.mendikot.ui.screens.SplashScreen
import com.example.mendikot.ui.screens.StartScreen
import com.example.mendikot.viewmodel.GameViewModel

/**
 * Main navigation routes for the Mendikot game
 */
sealed class MendikotScreen(val route: String) {
    object Splash : MendikotScreen("splash")
    object Start : MendikotScreen("start")
    object PlayerRegistration : MendikotScreen("player_registration")
    object GamePlay : MendikotScreen("gameplay")
    object Settings : MendikotScreen("settings")
    object Rules : MendikotScreen("rules")
}

/**
 * Main navigation setup for the Mendikot game
 */
@Composable
fun MendikotNavigation(
    navController: NavHostController,
    startDestination: String = MendikotScreen.Splash.route
) {
    val gameViewModel: GameViewModel = viewModel()
    val gameUiState by gameViewModel.uiState.collectAsState()
    val preferences by gameViewModel.preferences.collectAsState()
    
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(MendikotScreen.Splash.route) {
            SplashScreen(
                onSplashComplete = {
                    navController.navigate(MendikotScreen.Start.route) {
                        popUpTo(MendikotScreen.Splash.route) { inclusive = true }
                    }
                }
            )
        }
        
        composable(MendikotScreen.Start.route) {
            StartScreen(
                onNewGameClick = {
                    navController.navigate(MendikotScreen.PlayerRegistration.route)
                },
                onSettingsClick = {
                    navController.navigate(MendikotScreen.Settings.route)
                },
                onRulesClick = {
                    navController.navigate(MendikotScreen.Rules.route)
                }
            )
        }
        
        composable(MendikotScreen.PlayerRegistration.route) {
            PlayerRegistrationScreen(
                onStartGame = { playerNames ->
                    gameViewModel.initializeGame(playerNames)
                    navController.navigate(MendikotScreen.GamePlay.route) {
                        popUpTo(MendikotScreen.Start.route)
                    }
                },
                onBack = {
                    navController.popBackStack()
                }
            )
        }
        
        composable(MendikotScreen.GamePlay.route) {
            GamePlayScreen(
                gameUiState = gameUiState,
                preferences = preferences,
                onPlayCard = { playerId, card ->
                    gameViewModel.playCard(playerId, card)
                },
                onSelectTrump = { playerId, card ->
                    gameViewModel.selectTrump(playerId, card)
                },
                onRevealTrump = {
                    gameViewModel.revealTrumpCard()
                },
                onDrawCard = {
                    gameViewModel.drawCardsForTeamFormation()
                },
                onFormTeams = {
                    gameViewModel.formTeamsOnClick()
                },
                onStartDealing = {
                    gameViewModel.startDealing()
                },
                onStartNewDeal = {
                    gameViewModel.startNewDeal()
                },
                onReturnToMainMenu = {
                    gameViewModel.resetGame()
                    navController.navigate(MendikotScreen.Start.route) {
                        popUpTo(MendikotScreen.GamePlay.route) { inclusive = true }
                    }
                }
            )
        }
        
        composable(MendikotScreen.Settings.route) {
            SettingsScreen(
                preferences = preferences,
                onUpdatePreferences = { newPreferences ->
                    gameViewModel.updatePreferences(newPreferences)
                },
                onBack = {
                    navController.popBackStack()
                }
            )
        }
        
        composable(MendikotScreen.Rules.route) {
            RulesScreen(
                onBack = {
                    navController.popBackStack()
                }
            )
        }
    }
} 