package donat.udvari.snake

import donat.udvari.snake.model.Coordinate
import donat.udvari.snake.model.PostMessage

const val HEALTH_WEAK_LIMIT = 75
const val HEALTH_STRONG_LIMIT = 50

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
    return message.board.snakes
        .flatMap {
            val tailCanCut = it.length == it.body.size
            if (tailCanCut) {
                it.body.subList(0, it.body.lastIndex)
            } else {
                it.body
            }
        }
}

fun amITheStrongest(message: PostMessage): Boolean {
    val strongestEnemy = message.board.snakes
        .filterNot { it.head == message.you.head }
        .maxOf { it.length }
    return message.you.length > strongestEnemy
}


enum class Maze {VISITED, UNVISITED, SNAKE}