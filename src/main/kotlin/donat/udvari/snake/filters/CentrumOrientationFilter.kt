package donat.udvari.snake.filters

import donat.udvari.snake.model.Coordinate
import donat.udvari.snake.model.Direction
import donat.udvari.snake.model.PostMessage
import donat.udvari.snake.util.getPath
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Service

/**
When snake has more possibilities, it goes to the center
 */
@Service("centrumOrientationFilter")
class CentrumOrientationFilter: EndGameFilter {
    override fun filter(moves: MutableMap<Direction, Int>, message: PostMessage) {
        val validDirections = moves
            .filter { it.value == moves.values.maxOrNull() }
            .map { it.key }
        if (validDirections.size > 1) {
            val myHead = message.you.head
            val center = Coordinate(message.board.width / 2, message.board.height / 2)
            val weightedDirections = validDirections
                .map { direction -> Pair(direction, getPath(myHead.neighbour(direction), center, message).size) }
                .filter { it.second > 0 }

            weightedDirections
                .filter { pair -> pair.second == weightedDirections.minOf { it.second } }
                .map { it.first }
                .let {
                    if (it.size == 1) {
                        moves.merge(it[0], 1, Int::plus)
                    }
                }
        }
    }
}