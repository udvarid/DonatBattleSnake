package donat.udvari.snake.filters

import donat.udvari.snake.model.Direction
import donat.udvari.snake.model.PostMessage
import donat.udvari.snake.util.END_GAME_LENGTH_LIMIT
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Service

/**
It coordinates the endGame related filters
 */
@Service
@Order(9)
class EndGameCoordinatorFilter(
    @Qualifier("centrumOrientationFilter") val centrumOrientationFilter: EndGameFilter,
    @Qualifier("morePossibleFurtherStepsFilter") val morePossibleFurtherStepsFilter: EndGameFilter,
    @Qualifier("hunterFilter") val hunterFilter: EndGameFilter
): DirectionFilter {

    override fun filter(moves: MutableMap<Direction, Int>, message: PostMessage) {
        val amIGrownUp = message.you.length >= END_GAME_LENGTH_LIMIT
        val endGameFilters = mutableListOf(morePossibleFurtherStepsFilter, centrumOrientationFilter)
        if (amIGrownUp) {
            endGameFilters.add(0, hunterFilter)
        }
        endGameFilters.forEach {
            it.filter(moves, message)
        }
    }

}