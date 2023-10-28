package donat.udvari.snake.model

data class Board (
    val height: Int,
    val width: Int,
    val food: List<Coordinate>,
    val hazards: List<Coordinate>,
    val snakes: List<Snake>
)

data class Coordinate(val x: Int, val y: Int)