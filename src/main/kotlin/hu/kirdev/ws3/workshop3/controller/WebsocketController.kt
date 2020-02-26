package hu.kirdev.ws3.workshop3.controller

import hu.kirdev.ws3.workshop3.model.MapEntity
import hu.kirdev.ws3.workshop3.packet.*
import hu.kirdev.ws3.workshop3.service.BusinessLogic
import hu.kirdev.ws3.workshop3.service.UserManagerService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.handler.annotation.Header
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.messaging.simp.annotation.SendToUser
import org.springframework.stereotype.Controller

@Controller
class WebsocketController {

    @Autowired
    lateinit var users: UserManagerService

    @Autowired
    lateinit var control: BusinessLogic

    @MessageMapping("/message")
    @SendTo("/topic/chat")
    fun chatMessage(messageRequest: MessageRequestPacket, @Header("simpSessionId") sessionId: String): MessagePacket {
        val user = users.getUser(sessionId)
        return MessagePacket(user.name, MessageType.CHAT_MESSAGE, user.place, messageRequest.message)
    }

    @MessageMapping("/move")
    fun moveRequest(moveRequest: MoveRequest, @Header("simpSessionId") sessionId: String) {
        val user = users.getUser(sessionId)
        user.dX = moveRequest.dX
        user.dY = moveRequest.dY
    }

    @MessageMapping("/reload-map")
    @SendToUser("/topic/map")
    fun reloadMap(reloadMapRequest: ReloadMapRequest, @Header("simpSessionId") sessionId: String): MapEntity {
        val user = users.getUser(sessionId)
        return users.getMapOrDefault(user.place)
    }

}