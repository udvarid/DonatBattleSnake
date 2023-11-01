package donat.udvari.snake.filters

import donat.udvari.snake.model.Coordinate
import donat.udvari.snake.model.Direction
import donat.udvari.snake.model.PostMessage
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Service

/**
Avoiding moves where the snake could be trapped
 */
@Service
@Order(7)
class TrapAvoidingFilter: DirectionFilter {
    override fun filter(moves: MutableMap<Direction, Int>, message: PostMessage) {
        val validDirections = moves.toList().filter { it.second >= 0 }.map { it.first }
        if (validDirections.size > 1) {
            val board = getInitBoard(message)
            val myHead = message.you.head
            val mySize = message.you.length
            for (direction in validDirections) {
                val possibleDirection = myHead.neighbour(direction)
                clearVisited(board)
                val numberOfCells = calculateFreeCells(board, possibleDirection.y, possibleDirection.x)
                if (numberOfCells <= mySize) {
                    moves.merge(direction, 2, Int::minus)
                }
            }
        }
    }
}

private fun calculateFreeCells(board: Array<Array<Maze>>, y: Int, x: Int): Int {
    val currentCell = board[y][x]
    if (currentCell != Maze.UNVISITED) {
        return 0
    }
    board[y][x] = Maze.VISITED
    val maxY = board.indices
    val maxX = board[0].indices
    val possibleFurtherCells = listOf(Pair(y - 1, x), Pair(y + 1, x), Pair(y, x - 1), Pair(y, x + 1))
        .filter { it.first in maxY && it.second in maxX }
    return 1 + possibleFurtherCells.sumOf { calculateFreeCells(board, it.first, it.second) }
}


private fun clearVisited(board: Array<Array<Maze>>) {
    for (y in board.indices) {
        for (x in board[0].indices) {
            if (board[y][x] == Maze.VISITED) {
                board[y][x] = Maze.UNVISITED
            }
        }
    }
}

private fun getInitBoard(message: PostMessage): Array<Array<Maze>> {
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

enum class Maze {VISITED, UNVISITED, SNAKE}