package donat.udvari.snake.filters

import donat.udvari.snake.model.Direction
import donat.udvari.snake.model.PostMessage
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Service

/**
 * Filtering out the moves where the snake would go out from the board
 */
@Service
@Order(2)
class BorderFilter: DirectionFilter {
    override fun filter(moves: MutableMap<Direction, Int>, message: PostMessage) {
        val xMax = message.board.width
        val yMax = message.board.height
        val myHead = message.you.head
        if (myHead.x == 0) {
            moves.replace(Direction.LEFT, -1)
        }
        if (myHead.y == 0) {
            moves.replace(Direction.DOWN, -1)
        }
        if (myHead.x == xMax - 1) {
            moves.replace(Direction.RIGHT, -1)
        }
        if (myHead.y == yMax -1) {
            moves.replace(Direction.UP, -1)
        }
    }
}