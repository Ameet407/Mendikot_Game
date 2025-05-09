package com.example.mendikot.model

/**
 * Represents a standard playing card with a suit and rank
 */
data class Card(val suit: Suit, val rank: Rank) {
    /**
     * Checks if this card is a ten of any suit (important for scoring in Mendikot)
     */
    val isTen: Boolean
        get() = rank == Rank.TEN

    /**
     * Returns the resource ID for this card's image
     * Card naming convention: rank_of_suit (lowercase)
     * e.g., "ace_of_hearts", "10_of_spades"
     */
    fun getImageResourceName(): String {
        val rankName = when (rank) {
            Rank.ACE -> "ace"
            Rank.KING -> "king"
            Rank.QUEEN -> "queen"
            Rank.JACK -> "jack"
            Rank.TEN -> "10"
            Rank.NINE -> "9"
            Rank.EIGHT -> "8"
            Rank.SEVEN -> "7"
            Rank.SIX -> "6"
            Rank.FIVE -> "5"
            Rank.FOUR -> "4"
            Rank.THREE -> "3"
            Rank.TWO -> "2"
        }
        
        val suitName = when (suit) {
            Suit.HEARTS -> "hearts"
            Suit.SPADES -> "spades"
            Suit.DIAMONDS -> "diamonds"
            Suit.CLUBS -> "clubs"
        }
        
        return "${rankName}_of_${suitName}"
    }

    override fun toString(): String {
        return "${rank.symbol}${suit.symbol}"
    }

    companion object {
        /**
         * Creates a standard 52-card deck
         */
        fun createDeck(): List<Card> {
            val deck = mutableListOf<Card>()
            
            for (suit in Suit.values()) {
                for (rank in Rank.values()) {
                    deck.add(Card(suit, rank))
                }
            }
            
            return deck
        }
    }
}

/**
 * Represents the four suits in a standard deck
 */
enum class Suit(val symbol: String) {
    HEARTS("♥"),
    SPADES("♠"),
    DIAMONDS("♦"),
    CLUBS("♣")
}

/**
 * Represents the thirteen ranks in a standard deck
 * Ordered from highest (ACE) to lowest (TWO) according to Mendikot rules
 */
enum class Rank(val symbol: String, val value: Int) {
    ACE("A", 14),
    KING("K", 13),
    QUEEN("Q", 12),
    JACK("J", 11),
    TEN("10", 10),
    NINE("9", 9),
    EIGHT("8", 8),
    SEVEN("7", 7),
    SIX("6", 6),
    FIVE("5", 5),
    FOUR("4", 4),
    THREE("3", 3),
    TWO("2", 2)
} 