package donat.udvari.snake.filters

import donat.udvari.snake.model.Direction
import donat.udvari.snake.model.PostMessage

interface DirectionFilter {
    fun filter(moves: MutableMap<Direction, Int>, message: PostMessage)
}