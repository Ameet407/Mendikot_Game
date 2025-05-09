package com.example.mendikot.model

/**
 * Represents a player in the Mendikot game
 */
data class Player(
    val id: Int,
    var name: String,
    var position: Int = 0,
    var hand: MutableList<Card> = mutableListOf(),
    var teamId: Int = 0
) {
    /**
     * Checks if the player has a card of the given suit
     */
    fun hasSuit(suit: Suit): Boolean {
        return hand.any { it.suit == suit }
    }
    
    /**
     * Returns the valid cards that a player can play based on the lead suit and trump
     * @param leadSuit The suit of the first card played in the trick
     * @param trumpSuit The trump suit for the current game
     * @return List of cards that are valid to play
     */
    fun getPlayableCards(leadSuit: Suit?, trumpSuit: Suit?): List<Card> {
        // If no lead suit (first to play), all cards are valid
        if (leadSuit == null) {
            return hand
        }
        
        // If player has cards of lead suit, they must play one of those
        val cardsOfLeadSuit = hand.filter { it.suit == leadSuit }
        return if (cardsOfLeadSuit.isNotEmpty()) {
            cardsOfLeadSuit
        } else {
            // If player has no cards of lead suit, they can play any card
            hand
        }
    }
    
    /**
     * Plays a card from the player's hand
     * @param card The card to play
     * @return The played card, or null if the card is not in the hand
     */
    fun playCard(card: Card): Card? {
        return if (hand.remove(card)) card else null
    }
    
    /**
     * Adds cards to the player's hand
     */
    fun receiveCards(cards: List<Card>) {
        hand.addAll(cards)
    }
    
    /**
     * Removes all cards from the player's hand
     */
    fun clearHand() {
        hand.clear()
    }
} 