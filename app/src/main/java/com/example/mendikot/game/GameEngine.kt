package com.example.mendikot.game

import com.example.mendikot.model.Card
import com.example.mendikot.model.GameState
import com.example.mendikot.model.Player
import com.example.mendikot.model.Suit
import com.example.mendikot.model.Team

/**
 * GameEngine handles all the game logic for Mendikot
 */
class GameEngine {

    private var gameState = GameState()
    
    /**
     * Initialize the game with player names
     */
    fun initializeGame(playerNames: List<String>): GameState {
        gameState = gameState.initializeGame(playerNames)
        return gameState
    }
    
    /**
     * Draw cards for team formation
     * @return Map of playerIds to the cards they drew
     */
    fun drawCardsForTeamFormation(): Map<Int, Card> {
        val deck = Card.createDeck().shuffled()
        val playerCardMap = mutableMapOf<Int, Card>()
        
        // Each player draws one card
        for (i in 0 until 4) {
            playerCardMap[i] = deck[i]
        }
        
        return playerCardMap
    }
    
    /**
     * Set teams based on drawn cards
     * Players with higher cards form Team A, lower cards form Team B
     * @param playerCardMap Map of playerIds to the cards they drew
     * @return Map of teamIds to list of playerIds
     */
    fun formTeamsBasedOnCards(playerCardMap: Map<Int, Card>): Map<Int, List<Int>> {
        // Sort players by their card values - highest to lowest
        // Primary sort by rank (Ace is highest at 14, Two is lowest at 2)
        // Secondary sort by suit when ranks are equal
        val sortedPlayers = playerCardMap.entries.sortedByDescending { entry -> 
            // Multiply rank value by 100 to ensure it always takes precedence over suit
            entry.value.rank.value * 100 + entry.value.suit.ordinal 
        }
        
        // First two players (highest cards) go to Team A
        val teamA = listOf(sortedPlayers[0].key, sortedPlayers[1].key)
        
        // Last two players (lowest cards) go to Team B
        val teamB = listOf(sortedPlayers[2].key, sortedPlayers[3].key)
        
        // The dealer is the player with the lowest card (last in the sorted list)
        gameState = gameState.copy(currentDealer = sortedPlayers.last().key)
        
        return mapOf(0 to teamA, 1 to teamB)
    }
    
    /**
     * Deal cards to all players according to Mendikot rules:
     * First round: 5 cards
     * Second round: 4 cards
     * Third round: 4 cards
     */
    fun dealCards() {
        val deck = Card.createDeck().shuffled()
        val players = gameState.players
        
        // Clear all players' hands
        players.forEach { it.clearHand() }
        
        val dealingOrder = generatePlayerOrder(gameState.currentDealer)
        
        // Deal 5 cards in first round
        dealRound(deck.take(20), dealingOrder, 5)
        
        // Deal 4 cards in second round
        dealRound(deck.subList(20, 36), dealingOrder, 4)
        
        // Deal 4 cards in third round
        dealRound(deck.subList(36, 52), dealingOrder, 4)
    }
    
    /**
     * Helper function to deal a round of cards
     */
    private fun dealRound(cards: List<Card>, dealingOrder: List<Int>, cardsPerPlayer: Int) {
        var cardIndex = 0
        
        // Each player gets 'cardsPerPlayer' cards
        for (i in 0 until cardsPerPlayer) {
            for (playerId in dealingOrder) {
                val player = gameState.getPlayer(playerId)
                player?.receiveCards(listOf(cards[cardIndex]))
                cardIndex++
            }
        }
    }
    
    /**
     * Generate the order in which players receive cards (anticlockwise from dealer)
     */
    private fun generatePlayerOrder(dealerId: Int): List<Int> {
        val order = mutableListOf<Int>()
        var currentId = (dealerId + 1) % 4  // Start with player to dealer's right
        
        // Add all 4 players in anticlockwise order
        repeat(4) {
            order.add(currentId)
            currentId = (currentId + 1) % 4
        }
        
        return order
    }
    
    /**
     * Select a trump card and suit
     * @param playerId The player selecting the trump (player to dealer's right)
     * @param card The card selected as trump
     */
    fun selectTrump(playerId: Int, card: Card) {
        // Set trump card and suit
        gameState = gameState.copy(
            trumpCard = card,
            trumpSuit = card.suit,
            trumpRevealed = false
        )
        
        // Remove the trump card from player's hand temporarily
        gameState.getPlayer(playerId)?.playCard(card)
        
        // Set the first player (to dealer's right) to start the first trick
        gameState = gameState.copy(currentTurn = (gameState.currentDealer + 1) % 4)
    }
    
    /**
     * Reveal the trump (happens when a player can't follow suit)
     */
    fun revealTrump() {
        // Put the trump card back in the owner's hand
        val trumpCardOwner = gameState.getPlayer((gameState.currentDealer + 1) % 4)
        gameState.trumpCard?.let { trumpCardOwner?.receiveCards(listOf(it)) }
        
        // Mark trump as revealed
        gameState = gameState.copy(trumpRevealed = true)
    }
    
    /**
     * Play a card in the current trick
     * @param playerId The player playing the card
     * @param card The card being played
     * @return Whether the play was successful
     */
    fun playCard(playerId: Int, card: Card): Boolean {
        // Verify it's the player's turn
        if (playerId != gameState.currentTurn) {
            return false
        }
        
        val player = gameState.getPlayer(playerId) ?: return false
        
        // Check if player needs to follow suit
        val validCards = player.getPlayableCards(gameState.leadSuit, gameState.trumpSuit)
        if (!validCards.contains(card)) {
            return false
        }
        
        // Check if player is unable to follow suit and trump needs to be revealed
        if (gameState.leadSuit != null && !player.hasSuit(gameState.leadSuit!!) && !gameState.trumpRevealed) {
            revealTrump()
        }
        
        // Play the card
        val playedCard = player.playCard(card) ?: return false
        gameState.addCardToTrick(playedCard, playerId)
        
        // Check if trick is complete
        if (gameState.currentTrick.size == 4) {
            completeTrick()
        } else {
            // Move to next player
            gameState = gameState.copy(currentTurn = (playerId + 1) % 4)
        }
        
        return true
    }
    
    /**
     * Complete the current trick and update game state
     */
    private fun completeTrick() {
        // Determine trick winner and update scores
        val winnerId = gameState.completeTrick() ?: return
        
        // Check if all cards have been played (13 tricks)
        if (gameState.getPlayer(0)?.hand?.isEmpty() == true) {
            completeDeal()
            return
        }
        
        // Winner of trick leads next trick
        gameState = gameState.copy(currentTurn = winnerId)
    }
    
    /**
     * Complete the current deal and determine winner
     */
    private fun completeDeal() {
        val teamA = gameState.getTeam(0) ?: return
        val teamB = gameState.getTeam(1) ?: return
        
        // Determine winning team based on Mendikot rules
        val winningTeamId = when {
            // Mendikot - team captures all 4 tens
            teamA.hasMendikot() || teamA.hasWhitewash() -> 0
            teamB.hasMendikot() || teamB.hasWhitewash() -> 1
            
            // Team captures 3 tens
            teamA.tensCollected.size == 3 -> 0
            teamB.tensCollected.size == 3 -> 1
            
            // Both teams capture 2 tens, team with 7+ tricks wins
            teamA.tensCollected.size == 2 && teamB.tensCollected.size == 2 -> 
                if (teamA.tricksWon >= 7) 0 else 1
                
            // Team with more tens wins
            teamA.tensCollected.size > teamB.tensCollected.size -> 0
            else -> 1
        }
        
        // Update scores
        gameState.updateScoreForDeal(winningTeamId)
        
        // Determine if there was a whitewash
        val whitewash = (winningTeamId == 0 && teamA.hasWhitewash()) || 
                        (winningTeamId == 1 && teamB.hasWhitewash())
                        
        // Determine dealer for next deal
        val dealerTeamId = gameState.getTeamIdForPlayer(gameState.currentDealer) ?: 0
        val dealerTeamWon = dealerTeamId == winningTeamId
        
        val nextDealer = gameState.determineNextDealer(dealerTeamWon, whitewash)
        gameState = gameState.copy(currentDealer = nextDealer)
    }
    
    /**
     * Start a new deal
     */
    fun startNewDeal() {
        gameState.resetForNewDeal()
        dealCards()
        
        // Set the first player (to dealer's right) to select trump
        gameState = gameState.copy(currentTurn = (gameState.currentDealer + 1) % 4)
    }
    
    /**
     * Get the current game state
     */
    fun getGameState(): GameState {
        return gameState
    }
} 