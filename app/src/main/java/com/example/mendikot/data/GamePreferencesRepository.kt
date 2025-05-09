package com.example.mendikot.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.mendikot.viewmodel.AnimationSpeed
import com.example.mendikot.viewmodel.GamePreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "mendikot_preferences")

/**
 * Repository for accessing and storing game preferences
 */
class GamePreferencesRepository(private val context: Context) {

    // Preference keys
    private object PreferencesKeys {
        val SOUND_ENABLED = booleanPreferencesKey("sound_enabled")
        val MUSIC_ENABLED = booleanPreferencesKey("music_enabled")
        val ANIMATION_SPEED = stringPreferencesKey("animation_speed")
        val SHOW_HINTS = booleanPreferencesKey("show_hints")
        val VIBRATION_ENABLED = booleanPreferencesKey("vibration_enabled")
        val PLAYER1_NAME = stringPreferencesKey("player1_name")
        val PLAYER2_NAME = stringPreferencesKey("player2_name")
        val PLAYER3_NAME = stringPreferencesKey("player3_name")
        val PLAYER4_NAME = stringPreferencesKey("player4_name")
    }

    /**
     * Get the flow of game preferences
     */
    val gamePreferencesFlow: Flow<GamePreferences> = context.dataStore.data
        .map { preferences ->
            GamePreferences(
                soundEnabled = preferences[PreferencesKeys.SOUND_ENABLED] ?: true,
                musicEnabled = preferences[PreferencesKeys.MUSIC_ENABLED] ?: true,
                animationSpeed = getAnimationSpeed(preferences[PreferencesKeys.ANIMATION_SPEED]),
                showHints = preferences[PreferencesKeys.SHOW_HINTS] ?: true,
                vibrationEnabled = preferences[PreferencesKeys.VIBRATION_ENABLED] ?: true
            )
        }
    
    /**
     * Get the saved player names
     */
    val savedPlayerNames: Flow<List<String>> = context.dataStore.data
        .map { preferences ->
            listOf(
                preferences[PreferencesKeys.PLAYER1_NAME] ?: "Player 1",
                preferences[PreferencesKeys.PLAYER2_NAME] ?: "Player 2",
                preferences[PreferencesKeys.PLAYER3_NAME] ?: "Player 3",
                preferences[PreferencesKeys.PLAYER4_NAME] ?: "Player 4"
            )
        }
    
    /**
     * Update game preferences
     */
    suspend fun updateGamePreferences(preferences: GamePreferences) {
        context.dataStore.edit { prefs ->
            prefs[PreferencesKeys.SOUND_ENABLED] = preferences.soundEnabled
            prefs[PreferencesKeys.MUSIC_ENABLED] = preferences.musicEnabled
            prefs[PreferencesKeys.ANIMATION_SPEED] = preferences.animationSpeed.name
            prefs[PreferencesKeys.SHOW_HINTS] = preferences.showHints
            prefs[PreferencesKeys.VIBRATION_ENABLED] = preferences.vibrationEnabled
        }
    }
    
    /**
     * Save player names
     */
    suspend fun savePlayerNames(playerNames: List<String>) {
        context.dataStore.edit { prefs ->
            playerNames.getOrNull(0)?.let { prefs[PreferencesKeys.PLAYER1_NAME] = it }
            playerNames.getOrNull(1)?.let { prefs[PreferencesKeys.PLAYER2_NAME] = it }
            playerNames.getOrNull(2)?.let { prefs[PreferencesKeys.PLAYER3_NAME] = it }
            playerNames.getOrNull(3)?.let { prefs[PreferencesKeys.PLAYER4_NAME] = it }
        }
    }
    
    /**
     * Helper function to convert a string to animation speed
     */
    private fun getAnimationSpeed(value: String?): AnimationSpeed {
        return when (value) {
            AnimationSpeed.FAST.name -> AnimationSpeed.FAST
            AnimationSpeed.SLOW.name -> AnimationSpeed.SLOW
            else -> AnimationSpeed.NORMAL
        }
    }
} 