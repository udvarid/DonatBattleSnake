package donat.udvari.snake.model

data class InitResponse(
    val apiversion: String,
    val author: String,
    val color: String,
    val head: String,
    val tail: String,
    val version: String
)

data class MoveResponse(val move: String, val shout: String)

enum class Direction(val value: String){
    UP("up"), DOWN("down"), LEFT("left"), RIGHT("right")
}





