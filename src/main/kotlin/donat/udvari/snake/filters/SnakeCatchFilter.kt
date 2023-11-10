package donat.udvari.snake.filters

import donat.udvari.snake.SAME_SNAKE_DISTANCE
import donat.udvari.snake.STRONGER_SNAKE_DISTANCE
import donat.udvari.snake.getEnemySnakes
import donat.udvari.snake.model.Direction
import donat.udvari.snake.model.PostMessage
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Service

/**
    Chasing weaker or run away from stronger snakes
 */
@Service
@Order(8)
class SnakeCatchFilter: DirectionFilter {
    override fun filter(moves: MutableMap<Direction, Int>, message: PostMessage) {
        val validDirections = moves
            .filter { it.value == moves.values.maxOrNull() }
            .map { it.key }
        if (validDirections.size > 1) {
            val myHead = message.you.head
            val mySize = message.you.length
            val enemies = getEnemySnakes(message)
            if (enemies.isNotEmpty()) {
                val enemiesWithDistance = enemies
                    .map { Triple(it.head, it.length, it.head.distance(myHead)) }
                val closestDistance = enemiesWithDistance.minOf { it.third }
                val closestAndStrongestSnake = enemiesWithDistance
                    .filter { it.third == closestDistance }
                    .maxByOrNull { it.second }!!
                val enemyIsStronger = closestAndStrongestSnake.second > mySize
                val enemyIsSame = closestAndStrongestSnake.second == mySize
                val enemyIsWeaker = closestAndStrongestSnake.second < mySize
                for (direction in validDirections) {
                    val possibleDestination = myHead.neighbour(direction)
                    if (enemyIsStronger &&
                        STRONGER_SNAKE_DISTANCE >= closestAndStrongestSnake.third &&
                        closestAndStrongestSnake.third < possibleDestination.distance(closestAndStrongestSnake.first)
                        ||
                        enemyIsSame &&
                        SAME_SNAKE_DISTANCE >= closestAndStrongestSnake.third &&
                        closestAndStrongestSnake.third < possibleDestination.distance(closestAndStrongestSnake.first)
                        ||
                        enemyIsWeaker &&
                        closestAndStrongestSnake.third > possibleDestination.distance(closestAndStrongestSnake.first)) {
                        moves.merge(direction, 1, Int::plus)
                    }
                }
            }

        }
    }
}
