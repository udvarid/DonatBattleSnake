package donat.udvari.snake.filters

import donat.udvari.snake.ENERGY_LIMIT
import donat.udvari.snake.model.Direction
import donat.udvari.snake.model.PostMessage
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Service

/**
    Collection food when energy is low
 */
@Service
@Order(5)
class CollectingFoodFilter: DirectionFilter {
    override fun filter(moves: MutableMap<Direction, Int>, message: PostMessage) {
        if (message.you.health < ENERGY_LIMIT) {
            val validDirections = moves.toList().filter { it.second != -1 }.map { it.first }
            val myHead = message.you.head
            val food = message.board.food
            if (food.isNotEmpty()) {
                val closestFood = food.map { Pair(it, it.distance(myHead))}.minByOrNull { it.second }!!
                for (direction in validDirections) {
                    val possibleDestination = myHead.neighbour(direction)
                    if (closestFood.second > possibleDestination.distance(closestFood.first)) {
                        moves.merge(direction, 1, Int::plus)
                    }
                }
            }
        }
    }
}