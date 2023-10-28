package donat.udvari.snake.model

data class PostMessage (
    val game: Game,
    val turn: Int,
    val board: Board,
    val you: Snake
)
