package donat.udvari.snake

import donat.udvari.snake.model.Coordinate
import donat.udvari.snake.model.PostMessage
import donat.udvari.snake.model.Snake

const val HEALTH_WEAK_LIMIT = 75
const val HEALTH_STRONG_LIMIT = 50
const val STRONGER_SNAKE_DISTANCE = 4
const val SAME_SNAKE_DISTANCE = 3

fun getInitBoard(message: PostMessage): Array<Array<Maze>> {
    val height = message.board.height
    val width = message.board.width
    val board = Array(height) {Array(width) {Maze.UNVISITED} }
    val bodies = getBodies(message)
    bodies.forEach {
        board[it.y][it.x] = Maze.SNAKE
    }
    return board
}

fun getBodies(message: PostMessage): List<Coordinate> {
    val isItConstrictor = message.game.ruleset.name == "constrictor"
    return message.board.snakes
        .flatMap {
            val tailCanCut = it.length == it.body.size
            if (!isItConstrictor && tailCanCut) {
                it.body.subList(0, it.body.lastIndex)
            } else {
                it.body
            }
        }
}

fun amITheStrongest(message: PostMessage): Boolean {
    val strongestEnemy = getEnemySnakes(message)
        .maxOf { it.length }
    return message.you.length > strongestEnemy
}

fun amITheWeakest(message: PostMessage): Boolean {
    val weakestEnemy = getEnemySnakes(message)
        .minOf { it.length }
    return message.you.length <= weakestEnemy
}

fun getEnemySnakes(message: PostMessage): List<Snake> =
    message.board.snakes.filterNot { it.head == message.you.head }

enum class Maze {VISITED, UNVISITED, SNAKE}