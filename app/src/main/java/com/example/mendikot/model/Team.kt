package com.example.mendikot.model

/**
 * Represents a team of two players in the Mendikot game
 */
data class Team(
    val id: Int,
    val name: String,
    val players: MutableList<Player> = mutableListOf(),
    var tensCollected: MutableList<Card> = mutableListOf(),
    var tricksWon: Int = 0
) {
    /**
     * Add a player to this team
     */
    fun addPlayer(player: Player) {
        players.add(player)
        player.teamId = id
    }
    
    /**
     * Tracks a won trick, including collecting any tens in the trick
     */
    fun collectTrick(trick: List<Card>) {
        tricksWon++
        
        // Add any tens to the collected list
        tensCollected.addAll(trick.filter { it.isTen })
    }
    
    /**
     * Checks if the team has a Mendikot (all 4 tens)
     */
    fun hasMendikot(): Boolean {
        return tensCollected.size == 4
    }
    
    /**
     * Checks if the team has a Whitewash (all 13 tricks)
     */
    fun hasWhitewash(): Boolean {
        return tricksWon == 13
    }
    
    /**
     * Resets the team's score for a new deal
     */
    fun resetScore() {
        tensCollected.clear()
        tricksWon = 0
    }
} 