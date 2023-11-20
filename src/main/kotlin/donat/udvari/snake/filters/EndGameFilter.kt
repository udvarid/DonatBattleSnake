package donat.udvari.snake.filters

import donat.udvari.snake.model.Direction
import donat.udvari.snake.model.PostMessage

/**
 * Filter interface for Endgame phase
 */
interface EndGameFilter {
    fun filter(moves: MutableMap<Direction, Int>, message: PostMessage)
}