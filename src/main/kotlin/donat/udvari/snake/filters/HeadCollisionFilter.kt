package donat.udvari.snake.filters

import donat.udvari.snake.model.Direction
import donat.udvari.snake.model.PostMessage
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Service

/**
Avoiding or attracting head collision possibilities
 */
@Service
@Order(6)
class HeadCollisionFilter: DirectionFilter {
    override fun filter(moves: MutableMap<Direction, Int>, message: PostMessage) {
        val validDirections = moves.toList().filter { it.second >= 0 }.map { it.first }
        if (validDirections.size > 1) {
            val myHead = message.you.head
            val mySize = message.you.length
            val enemiesWithHeadAndSize = message.board.snakes
                .filter { it.head != myHead }
                .map { Pair(it.head, it.length) }
            for (direction in validDirections) {
                val possibleDestination = myHead.neighbour(direction)
                val coordinatesToCheck = possibleDestination.getNeighbours().minus(myHead)
                var isThereWeakSnake = false
                var isThereMiddleSnake = false
                var isThereStrongSnake = false
                for (coordinateToCheck in coordinatesToCheck) {
                    enemiesWithHeadAndSize.forEach {
                        if (it.first == coordinateToCheck) {
                            if (it.second > mySize) {
                                isThereStrongSnake = true
                            } else if(it.second == mySize) {
                                isThereMiddleSnake = true
                            } else {
                                isThereWeakSnake = true
                            }
                        }
                    }
                }
                if (isThereStrongSnake) {
                    moves.merge(direction, 2, Int::minus)
                } else if (isThereMiddleSnake) {
                    moves.merge(direction, 1, Int::minus)
                } else if (isThereWeakSnake) {
                    moves.merge(direction, 1, Int::plus)
                }
            }
        }
    }
}