package donat.udvari.snake.filters

import donat.udvari.snake.model.Direction
import donat.udvari.snake.model.PostMessage
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Service

/**
 * Filtering out the only invalid move: backward
 */
@Service
@Order(1)
class HeadNeckFilter: DirectionFilter {
    override fun filter(moves: MutableMap<Direction, Int>, message: PostMessage) {
        val me = message.you
        if (me.length > 1) {
            val head = me.body[0]
            val neck = me.body[1]
            if (head.x == neck.x && head.y > neck.y) {
                moves.replace(Direction.DOWN, -1)
            } else if (head.x == neck.x && head.y < neck.y) {
                moves.replace(Direction.UP, -1)
            } else if (head.x > neck.x && head.y == neck.y) {
                moves.replace(Direction.LEFT, -1)
            } else if (head.x < neck.x && head.y == neck.y) {
                moves.replace(Direction.RIGHT, -1)
            }
        }
    }
}