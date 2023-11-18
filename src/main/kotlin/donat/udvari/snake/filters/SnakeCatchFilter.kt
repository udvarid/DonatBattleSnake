package donat.udvari.snake.filters

import donat.udvari.snake.util.SAME_SNAKE_DISTANCE
import donat.udvari.snake.util.STRONGER_SNAKE_DISTANCE
import donat.udvari.snake.util.getEnemySnakes
import donat.udvari.snake.model.Direction
import donat.udvari.snake.model.PostMessage
import donat.udvari.snake.util.getPath
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
                    .map { Triple(it.head, it.length, getPath(start = myHead, goal = it.head, message = message, removeHeads = true)) }
                    .filter { it.third.isNotEmpty() }
                if (enemiesWithDistance.isNotEmpty()) {
                    val closestDistance = enemiesWithDistance.minOf { it.third.size }
                    val closestAndStrongestSnake = enemiesWithDistance
                        .filter { it.third.size == closestDistance }
                        .maxByOrNull { it.second }!!
                    val enemyIsStronger = closestAndStrongestSnake.second > mySize
                    val enemyIsSame = closestAndStrongestSnake.second == mySize
                    val enemyIsWeaker = closestAndStrongestSnake.second < mySize

                    if (enemyIsWeaker) {
                        for (direction in validDirections) {
                            val possibleDestination = myHead.neighbour(direction)
                            val nextStep = closestAndStrongestSnake.third[1]
                            if (possibleDestination == nextStep) {
                                moves.merge(direction, 1, Int::plus)
                            }
                        }
                    } else if (enemyIsStronger && STRONGER_SNAKE_DISTANCE >= closestAndStrongestSnake.third.size ||
                        enemyIsSame && SAME_SNAKE_DISTANCE >= closestAndStrongestSnake.third.size) {
                            val areaOfEnemy = getPath(start = closestAndStrongestSnake.first, message = message)
                            val farthestCoordinateFromEnemy = areaOfEnemy.maxByOrNull { it.distance(closestAndStrongestSnake.first) }
                            farthestCoordinateFromEnemy?.let {
                                val fleePath = getPath(start = myHead, goal = it, message = message, removeHeads = true)
                                if (fleePath.isNotEmpty()) {
                                    val nextStep = fleePath[1]
                                    for (direction in validDirections) {
                                        val possibleDestination = myHead.neighbour(direction)
                                        if (possibleDestination == nextStep) {
                                            moves.merge(direction, 1, Int::plus)
                                        }
                                    }
                                } else {
                                    for (direction in validDirections) {
                                        val possibleDestination = myHead.neighbour(direction)
                                        if (closestAndStrongestSnake.third.size < possibleDestination.distance(closestAndStrongestSnake.first)) {
                                            moves.merge(direction, 1, Int::plus)
                                        }
                                    }
                                }
                            }
                    }
                }
            }

        }
    }
}
