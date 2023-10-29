package donat.udvari.snake.model

data class Game (
    val id: String,
    val ruleset: RuleSet,
    val map: String,
    val timeout: Int,
    val source: String
)

data class RuleSet(val name: String, val version: String, val settings: RuleSetSettings?)

data class RuleSetSettings(
    val foodSpawnChance: Int?,
    val minimumFood: Int?,
    val hazardDamagePerTurn: Int?,
    val royale: RoyaleSettings?,
    val squad: SquadSettings?
)

data class RoyaleSettings(val shrinkEveryNTurns: Int)

data class SquadSettings(
    val allowBodyCollisions: Boolean,
    val sharedElimination: Boolean,
    val sharedHealth: Boolean,
    val sharedLength: Boolean
)