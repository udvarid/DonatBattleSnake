package donat.udvari.snake.filters

import donat.udvari.snake.model.Direction
import donat.udvari.snake.model.PostMessage
import donat.udvari.snake.util.*
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Service

/**
    Collection food when energy is low
 */
@Service
@Order(7)
class CollectingFoodFilter: DirectionFilter {
    override fun filter(moves: MutableMap<Direction, Int>, message: PostMessage) {
        val extraLimit = if (message.you.length < END_GAME_LENGTH_LIMIT) 15 else 0
        val healthLimit = (if (amITheStrongest(message)) HEALTH_STRONG_LIMIT else HEALTH_WEAK_LIMIT) + extraLimit
        val weakestExtra = if (amITheWeakest(message)) 1 else 0
        if (message.you.health < healthLimit) {
            val validDirections = moves.toList().filter { it.second >= 0 }.map { it.first }
            if (validDirections.size > 1) {
                val myHead = message.you.head
                val food = message.board.food
                if (food.isNotEmpty()) {
                    val isItRoyale = message.game.map == "royale"
                    val hazardsToAvoid = if (isItRoyale) message.board.hazards else emptyList()
                    val closestPath = food.map { getPath(start = myHead, goal = it, message = message, coordinatesToAvoid = hazardsToAvoid) }
                        .filter { it.isNotEmpty() }
                        .minByOrNull { it.size }
                    closestPath?.let {
                        val nextStep = it[1]
                        for (direction in validDirections) {
                            val possibleDestination = myHead.neighbour(direction)
                            if (possibleDestination == nextStep) {
                                moves.merge(direction, 1 + weakestExtra, Int::plus)
                            }
                        }
                    }
                }
            }
        }
    }
}