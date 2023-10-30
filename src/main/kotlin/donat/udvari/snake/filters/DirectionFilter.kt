package donat.udvari.snake.filters

import donat.udvari.snake.model.Direction
import donat.udvari.snake.model.PostMessage

/**
 * Filter interface for prioritizing the possible directions
 */
interface DirectionFilter {
    fun filter(moves: MutableMap<Direction, Int>, message: PostMessage)
}