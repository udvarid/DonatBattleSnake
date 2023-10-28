package donat.udvari.snake.controller

import donat.udvari.snake.model.InitResponse
import donat.udvari.snake.model.MoveResponse
import donat.udvari.snake.model.PostMessage
import donat.udvari.snake.service.DirectionService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class SnakeController(val directionService: DirectionService) {

    @GetMapping
    fun getInitAnswer(): InitResponse {
        return InitResponse(
            "1",
            "DonatO",
            "#2da7e4",
            "default",
            "default",
            "0.0.1-beta"
        )
    }

    @PostMapping("start")
    fun postStart(@RequestBody message: PostMessage): MoveResponse {
        return MoveResponse(move = "up", shout = "UP!")
    }

    @PostMapping("move")
    fun postMove(@RequestBody message: PostMessage): MoveResponse {
        return MoveResponse(
            move = directionService.getDirection(message).value,
            shout = "AAAA"
        )
    }

    @PostMapping("end")
    fun postEnd(@RequestBody message: PostMessage): MoveResponse {
        return MoveResponse(move = "up", shout = "UP!")
    }

}
