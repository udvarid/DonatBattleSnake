package donat.udvari.snake.filters

import donat.udvari.snake.getBodies
import donat.udvari.snake.model.Direction
import donat.udvari.snake.model.PostMessage
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Service

/**
Choosing the destination with more further free cells to move
 */
@Service
@Order(9)
class MorePossibleFurtherStepsFilter: DirectionFilter {
    override fun filter(moves: MutableMap<Direction, Int>, message: PostMessage) {
        val validDirections = moves
            .filter { it.value == moves.values.maxOrNull() }
            .map { it.key }
        if (validDirections.size > 1) {
            val myHead = message.you.head
            val bodies = getBodies(message)
            val height = message.board.height
            val width = message.board.width

            val weightedDirections = validDirections
                .map { direction -> Pair(direction, myHead.neighbour(direction).getNeighbours()
                    .filter { it.x < width && it.y < height }
                    .filterNot { bodies.contains(it) }
                    .count()) }

            weightedDirections
                .filter { pair -> pair.second == weightedDirections.maxOf { it.second } }
                .map { it.first }
                .let {
                    if (it.size == 1) {
                        moves.merge(it[0], 1, Int::plus)
                    }
                }
        }
    }
}