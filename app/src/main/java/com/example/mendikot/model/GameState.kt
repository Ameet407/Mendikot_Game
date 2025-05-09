package com.example.mendikot.model

/**
 * Represents the current state of a Mendikot game
 */
data class GameState(
    val players: List<Player> = listOf(),
    val teams: List<Team> = listOf(),
    var currentDealer: Int = -1,
    var currentTurn: Int = -1,
    var trumpSuit: Suit? = null,
    var trumpRevealed: Boolean = false,
    var trumpCard: Card? = null,
    var leadSuit: Suit? = null,
    var currentTrick: MutableList<CardPlay> = mutableListOf(),
    var dealsPlayed: Int = 0,
    var teamScores: MutableMap<Int, Int> = mutableMapOf()
) {
    /**
     * Represents a card played in a trick, with information about who played it
     */
    data class CardPlay(val card: Card, val playerId: Int)
    
    /**
     * Initializes a new game with 4 players and 2 teams
     */
    fun initializeGame(playerNames: List<String>): GameState {
        // Create 4 players with the given names
        val newPlayers = playerNames.mapIndexed { index, name ->
            Player(id = index, name = name, position = index)
        }
        
        // Create 2 teams
        val teamA = Team(id = 0, name = "Team A")
        val teamB = Team(id = 1, name = "Team B")
        
        // Add players to teams (players across from each other are teammates)
        teamA.addPlayer(newPlayers[0])
        teamA.addPlayer(newPlayers[2])
        teamB.addPlayer(newPlayers[1])
        teamB.addPlayer(newPlayers[3])
        
        // Initialize team scores
        teamScores[0] = 0
        teamScores[1] = 0
        
        return this.copy(
            players = newPlayers,
            teams = listOf(teamA, teamB),
            currentDealer = -1, // Will be set during team formation
            currentTurn = -1,
            dealsPlayed = 0
        )
    }
    
    /**
     * Gets a player by their ID
     */
    fun getPlayer(id: Int): Player? {
        return players.find { it.id == id }
    }
    
    /**
     * Gets the team ID of a given player
     */
    fun getTeamIdForPlayer(playerId: Int): Int? {
        return players.find { it.id == playerId }?.teamId
    }
    
    /**
     * Gets a team by its ID
     */
    fun getTeam(id: Int): Team? {
        return teams.find { it.id == id }
    }
    
    /**
     * Gets the team that a player belongs to
     */
    fun getTeamForPlayer(playerId: Int): Team? {
        val teamId = getTeamIdForPlayer(playerId) ?: return null
        return getTeam(teamId)
    }
    
    /**
     * Gets the player who is next to play after the given player
     * Players are seated anticlockwise (0 -> 1 -> 2 -> 3 -> 0)
     */
    fun getNextPlayer(currentPlayerId: Int): Player {
        val nextIndex = (currentPlayerId + 1) % players.size
        return players[nextIndex]
    }
    
    /**
     * Gets the partner of a player (player sitting opposite)
     */
    fun getPartner(playerId: Int): Player? {
        val partner = (playerId + 2) % players.size
        return getPlayer(partner)
    }
    
    /**
     * Determines the winner of the current trick
     */
    fun determineTrickWinner(): Player? {
        if (currentTrick.isEmpty()) return null
        
        // Lead suit is the suit of the first card played
        val firstPlay = currentTrick.first()
        val leadSuit = firstPlay.card.suit
        
        // Find the highest card of the lead suit, or highest trump if trumps were played
        var winningPlay = firstPlay
        
        for (play in currentTrick.drop(1)) {
            val currentCard = play.card
            val winningCard = winningPlay.card
            
            // If a trump was played and current card is a trump, compare only with other trumps
            if (trumpRevealed && currentCard.suit == trumpSuit) {
                // If winning card is not a trump, or current trump is higher, it's the new winner
                if (winningCard.suit != trumpSuit || currentCard.rank.value > winningCard.rank.value) {
                    winningPlay = play
                }
            } 
            // If no trump or current card is not a trump, follow normal rules
            else if (currentCard.suit == leadSuit && (winningCard.suit != trumpSuit)) {
                // Higher card of the lead suit
                if (currentCard.rank.value > winningCard.rank.value) {
                    winningPlay = play
                }
            }
        }
        
        return getPlayer(winningPlay.playerId)
    }
    
    /**
     * Adds a card to the current trick
     */
    fun addCardToTrick(card: Card, playerId: Int) {
        currentTrick.add(CardPlay(card, playerId))
        
        // If this is the first card, set the lead suit
        if (currentTrick.size == 1) {
            leadSuit = card.suit
        }
    }
    
    /**
     * Completes the current trick and updates team scores
     * @return The ID of the player who won the trick
     */
    fun completeTrick(): Int? {
        val winner = determineTrickWinner() ?: return null
        val team = getTeamForPlayer(winner.id) ?: return null
        
        // Collect cards in the trick
        val cards = currentTrick.map { it.card }
        team.collectTrick(cards)
        
        // Reset for next trick
        currentTrick.clear()
        leadSuit = null
        
        return winner.id
    }
    
    /**
     * Gets the current team scores in the format teamId to score mapping
     */
    fun getCurrentTeamScores(): Map<Int, Int> {
        return teamScores
    }
    
    /**
     * Updates the team score for a completed deal
     */
    fun updateScoreForDeal(winningTeamId: Int) {
        val currentScore = teamScores[winningTeamId] ?: 0
        teamScores[winningTeamId] = currentScore + 1
    }
    
    /**
     * Determines the next dealer based on game rules
     */
    fun determineNextDealer(currentDealerTeamWon: Boolean, whitewash: Boolean): Int {
        return when {
            // If dealer's team lost with a whitewash, dealer's partner becomes dealer
            !currentDealerTeamWon && whitewash -> (currentDealer + 2) % 4
            
            // If dealer's team lost, same dealer continues
            !currentDealerTeamWon -> currentDealer
            
            // If dealer's team won, next player anticlockwise becomes dealer
            else -> (currentDealer + 1) % 4
        }
    }
    
    /**
     * Resets the game state for a new deal, but maintains overall scores
     */
    fun resetForNewDeal() {
        // Clear all players' hands
        players.forEach { it.clearHand() }
        
        // Reset team trick counts and tens collected
        teams.forEach { it.resetScore() }
        
        // Reset trump and trick state
        trumpSuit = null
        trumpRevealed = false
        trumpCard = null
        leadSuit = null
        currentTrick.clear()
        
        // Increment deals played
        dealsPlayed++
    }
} 