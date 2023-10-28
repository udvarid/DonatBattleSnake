package donat.udvari.snake.model

data class Snake(
    val id: String,
    val name: String,
    val health: Int,
    val body: List<Coordinate>,
    val latency: String,
    val head: Coordinate,
    val length: Int,
    val shout: String,
    val squad: String,
    val customizations: Customization
)

data class Customization(
    val color: String,
    val head: String,
    val tail: String
)
