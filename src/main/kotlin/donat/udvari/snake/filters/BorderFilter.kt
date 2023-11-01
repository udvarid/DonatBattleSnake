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
            moves.remove(Direction.LEFT)
        }
        if (myHead.y == 0) {
            moves.remove(Direction.DOWN)
        }
        if (myHead.x == xMax - 1) {
            moves.remove(Direction.RIGHT)
        }
        if (myHead.y == yMax -1) {
            moves.remove(Direction.UP)
        }
    }
}