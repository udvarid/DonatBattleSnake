package donat.udvari.snake.model

import kotlin.math.abs

data class Board (
    val height: Int,
    val width: Int,
    val food: List<Coordinate>,
    val hazards: List<Coordinate>,
    val snakes: List<Snake>
)

data class Coordinate(val x: Int, val y: Int) {
    fun neighbour(direction: Direction): Coordinate =
        when (direction) {
            Direction.UP -> Coordinate(x = this.x, y = this.y + 1)
            Direction.DOWN -> Coordinate(x = this.x, y = this.y - 1)
            Direction.RIGHT -> Coordinate(x = this.x + 1, y = this.y)
            Direction.LEFT -> Coordinate(x = this.x - 1, y = this.y)
        }

    fun distance(other: Coordinate): Int = abs(this.x - other.x) + abs(this.y - other.y)
    fun getNeighbours(): List<Coordinate> =
        listOf(
            Coordinate(this.x + 1, this.y),
            Coordinate(this.x - 1, this.y),
            Coordinate(this.x, this.y + 1),
            Coordinate(this.x, this.y - 1)
        ).filter { it.x >= 0 && it.y >= 0 }
}