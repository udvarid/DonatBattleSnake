package donat.udvari.snake.util

import donat.udvari.snake.model.Coordinate
import donat.udvari.snake.model.PostMessage
import java.util.*

fun getPath(start: Coordinate, goal: Coordinate? = null, message: PostMessage, removeHeads: Boolean = false): List<Coordinate> {
    val bodies = getBodies(message, removeHeads)
    val paths: MutableMap<Coordinate, Coordinate> = mutableMapOf()
    val myQueue: Queue<Coordinate> = LinkedList()
    val height = message.board.height
    val width = message.board.width

    start.getNeighbours()
        .filter { it.x < width && it.y < height }
        .filterNot { bodies.contains(it) }
        .forEach {
            myQueue.add(it)
            paths[it] = start
        }

    while (myQueue.isNotEmpty() && !paths.containsKey(goal)) {
        val currentElement = myQueue.poll()
        currentElement.getNeighbours()
            .asSequence()
            .filter { it.x < width && it.y < height }
            .filterNot { bodies.contains(it) }
            .filterNot { myQueue.toList().contains(it) }
            .filterNot { paths.containsKey(it) }
            .forEach {
                myQueue.add(it)
                paths[it] = currentElement
            }
    }

    if (goal != null && paths.containsKey(goal)) {
        val myPath: MutableList<Coordinate> = mutableListOf()
        myPath.add(goal)
        var current = paths[goal]
        while (!myPath.contains(start)) {
            myPath.add(current!!)
            current = paths[current]
        }
        myPath.reverse()
        return myPath
    }

    if (goal == null) {
        return paths.keys.toList()
    }

    return emptyList()
}
