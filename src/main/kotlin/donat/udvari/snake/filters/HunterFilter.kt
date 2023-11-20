package donat.udvari.snake.filters

import donat.udvari.snake.model.Direction
import donat.udvari.snake.model.PostMessage
import donat.udvari.snake.util.SnakeHunt
import donat.udvari.snake.util.getEnemySnakes
import donat.udvari.snake.util.getPath
import org.springframework.stereotype.Service

/**
    It will make steps to put enemies into trap
 */
@Service("hunterFilter")
class HunterFilter: EndGameFilter {
    override fun filter(moves: MutableMap<Direction, Int>, message: PostMessage) {
        val validDirections = moves
            .filter { it.value == moves.values.maxOrNull() }
            .map { it.key }
        if (validDirections.size > 1) {
            val myHead = message.you.head
            val enemiesWithFreedom = getEnemySnakes(message)
                .map { SnakeHunt(it.head, it.length, getPath(start = it.head, message = message).size) }
                .filter { it.length * 1.5 < it.origFreedom }
            for (direction in validDirections) {
                val possibleDestination = myHead.neighbour(direction)
                val successHunting = enemiesWithFreedom
                    .map { it.copy(newFreedom = getPath(
                        start = it.head,
                        message = message,
                        coordinatesToAvoid = listOf(possibleDestination)).size) }
                    .filter { it.origFreedom > it.newFreedom!! + 1 }
                if (successHunting.isNotEmpty()) {
                    moves.merge(direction, 1, Int::plus)
                }
            }
        }
    }
}