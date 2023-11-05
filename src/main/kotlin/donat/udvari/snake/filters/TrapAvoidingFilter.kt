package donat.udvari.snake.filters

import donat.udvari.snake.Maze
import donat.udvari.snake.getInitBoard
import donat.udvari.snake.model.Direction
import donat.udvari.snake.model.PostMessage
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Service

/**
Avoiding moves where the snake could be trapped
 */
@Service
@Order(5)
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
                } else if (numberOfCells <= mySize * 2) {
                    moves.merge(direction, 1, Int::minus)
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