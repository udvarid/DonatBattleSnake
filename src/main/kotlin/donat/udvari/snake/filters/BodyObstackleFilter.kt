package donat.udvari.snake.filters

import donat.udvari.snake.util.getBodies
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
        if (validDirections.size > 1) {
            val bodyParts = getBodies(message)
            val myHead = message.you.head
            for (direction in validDirections) {
                if (bodyParts.contains(myHead.neighbour(direction))) {
                    moves.remove(direction)
                }
            }
        }
    }
}