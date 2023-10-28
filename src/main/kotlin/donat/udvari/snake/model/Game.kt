package donat.udvari.snake.model

data class Game (
    val id: String,
    val ruleset: RuleSet,
    val map: String,
    val timeOut: Int,
    val source: String
)

data class RuleSet(val name: String, val version: String)


