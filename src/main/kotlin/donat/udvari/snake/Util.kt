package donat.udvari.snake

import donat.udvari.snake.model.PostMessage

const val HEALTH_WEAK_LIMIT = 75
const val HEALTH_STRONG_LIMIT = 50

fun getInitBoard(message: PostMessage): Array<Array<Maze>> {
    val height = message.board.height
    val width = message.board.width
    val board = Array(height) {Array(width) {Maze.UNVISITED} }
    val bodies = message.board.snakes.flatMap { it.body }
    val myTailCanCut = message.you.length == message.you.body.size
    bodies.let {
        return@let if (myTailCanCut) {
            val myTail = message.you.body.last()
            it.minus(myTail)
        } else {
            it
        }
    }.forEach {
        board[it.y][it.x] = Maze.SNAKE
    }
    return board
}

fun amITheStrongest(message: PostMessage): Boolean {
    val strongestEnemy = message.board.snakes
        .filterNot { it.head == message.you.head }
        .maxOf { it.length }
    return message.you.length > strongestEnemy
}


enum class Maze {VISITED, UNVISITED, SNAKE}