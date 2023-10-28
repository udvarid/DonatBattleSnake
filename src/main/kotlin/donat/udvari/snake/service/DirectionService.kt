package donat.udvari.snake.service

import donat.udvari.snake.filters.DirectionFilter
import donat.udvari.snake.model.Direction
import donat.udvari.snake.model.PostMessage
import org.springframework.stereotype.Service

@Service
class DirectionService(val filters: List<DirectionFilter>) {

    fun getDirection(message: PostMessage): Direction {
        val moves: MutableMap<Direction, Int> = Direction.values().associateWith { 1 }.toMutableMap()
        filters.forEach {
            it.filter(moves, message)
        }
        return moves
            .toList()
            .filter { it.second == moves.values.maxOrNull() }
            .map { it.first }
            .random()
    }

}