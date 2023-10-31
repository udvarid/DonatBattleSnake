package donat.udvari.snake.filters

import donat.udvari.snake.model.Direction
import donat.udvari.snake.model.PostMessage
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Service

/**
 * Filtering out the direction where the snake would collide with other snakes
 */
@Service
@Order(3)
class BodyObstackleFilter: DirectionFilter {
    override fun filter(moves: MutableMap<Direction, Int>, message: PostMessage) {
        val validDirections = moves.toList().filter { it.second >= 0 }.map { it.first }
        val bodyParts = message.board.snakes.flatMap { it.body }
        val myHead = message.you.head
        for (direction in validDirections) {
            if (bodyParts.contains(myHead.neighbour(direction))) {
                moves.replace(direction, -1)
            }
        }
    }
}