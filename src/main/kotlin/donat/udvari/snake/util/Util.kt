package donat.udvari.snake.util

import donat.udvari.snake.model.Coordinate
import donat.udvari.snake.model.PostMessage
import donat.udvari.snake.model.Snake

const val HEALTH_WEAK_LIMIT = 75
const val HEALTH_STRONG_LIMIT = 50
const val STRONGER_SNAKE_DISTANCE = 5
const val SAME_SNAKE_DISTANCE = 4
const val END_GAME_LENGTH_LIMIT = 10
const val FREEDOM_MULTIPLICATOR = 1.25

fun getInitBoard(message: PostMessage): Array<Array<Maze>> {
    val height = message.board.height
    val width = message.board.width
    val board = Array(height) {Array(width) { Maze.UNVISITED } }
    val bodies = getBodies(message)
    bodies.forEach {
        board[it.y][it.x] = Maze.SNAKE
    }
    return board
}

fun getBodies(message: PostMessage, removeHeads: Boolean = false): List<Coordinate> {
    val isItConstrictor = message.game.ruleset.name == "constrictor"
    val result = message.board.snakes
        .flatMap {
            val tailCanCut = it.length == it.body.size
            if (!isItConstrictor && tailCanCut) {
                it.body.subList(0, it.body.lastIndex)
            } else {
                it.body
            }
        }
        .distinct()
    return if (removeHeads) {
        val myHead = message.you.head
        val enemyHeads = message.board.snakes.map { it.head }.filterNot { it == myHead }.toSet()
        result.minus(enemyHeads)
    } else {
        result
    }
}

fun amITheStrongest(message: PostMessage): Boolean {
    val strongestEnemy = getEnemySnakes(message)
        .maxOfOrNull { it.length }
    return if (strongestEnemy == null) true else message.you.length > strongestEnemy
}

fun amITheWeakest(message: PostMessage): Boolean {
    val weakestEnemy = getEnemySnakes(message)
        .minOfOrNull { it.length }
    return if (weakestEnemy == null) false else message.you.length <= weakestEnemy
}

fun getEnemySnakes(message: PostMessage): List<Snake> =
    message.board.snakes.filterNot { it.head == message.you.head }

enum class Maze {VISITED, UNVISITED, SNAKE}

data class SnakeHunt(val head: Coordinate, val length: Int, val origFreedom: Int, val newFreedom: Int? = null)