package donat.udvari.snake

import donat.udvari.snake.model.*
import donat.udvari.snake.util.getPath
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class SnakeApplicationTests {

	@Test
	fun contextLoads() {
	}

	@Test
	fun pathFinderTest() {
		val mySnake = getSnake()
		val game = getGame()
		val board = getBoard(mySnake)
		val testMessage = PostMessage(game, 10, board, mySnake)

		val startCoordinate = Coordinate(3, 5)
		val goalCoordinate = Coordinate(7, 5)
		val path = getPath(startCoordinate, goalCoordinate, testMessage)
		assertEquals(11, path.size)
	}

	private fun getBoard(snake: Snake): Board =
		Board(
			height = 10,
			width = 10,
			food = emptyList(),
			hazards = emptyList(),
			snakes = listOf(snake)
		)

	private fun getGame(): Game =
		Game(
			id = "id",
			ruleset = RuleSet("","",null),
			map = "map",
			timeout = 303,
			source = "source"
	)

	private fun getBody(): List<Coordinate> =
		listOf(
			Coordinate(5, 2),
			Coordinate(5, 3),
			Coordinate(5, 4),
			Coordinate(5, 5),
			Coordinate(5, 6),
			Coordinate(5, 7),
			Coordinate(5, 8),
		)

	private fun getSnake(): Snake =
		Snake(
			id = "id",
			name = "snake",
			health = 78,
			body = getBody(),
			latency = "latency",
			head = Coordinate(5,2),
			length = 7,
			shout = "shout",
			squad = "squad",
			customizations = Customization("","","")
		)
}
