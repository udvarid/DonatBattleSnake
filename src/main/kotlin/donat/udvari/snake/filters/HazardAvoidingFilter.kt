package donat.udvari.snake.filters

import donat.udvari.snake.model.Direction
import donat.udvari.snake.model.PostMessage
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Service

/**
    Avoiding - with prioritize decrease - the obstacles
 */
@Service
@Order(4)
class HazardAvoidingFilter: DirectionFilter {
    override fun filter(moves: MutableMap<Direction, Int>, message: PostMessage) {
        if (message.board.hazards.isNotEmpty()) {
            val validDirections = moves.toList().filter { it.second >= 0 }.map { it.first }
            if (validDirections.size > 1) {
                val hazards = message.board.hazards
                val myHead = message.you.head
                for (direction in validDirections) {
                    if (hazards.contains(myHead.neighbour(direction))) {
                        moves.merge(direction, 1, Int::minus)
                    }
                }
            }
        }
    }
}