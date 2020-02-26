package hu.kirdev.ws3.workshop3.service

import hu.kirdev.ws3.workshop3.model.UserEntity
import hu.kirdev.ws3.workshop3.packet.MapPositions
import hu.kirdev.ws3.workshop3.packet.MessagePacket
import hu.kirdev.ws3.workshop3.packet.MessageType
import hu.kirdev.ws3.workshop3.packet.UserPosition
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.simp.SimpMessageHeaderAccessor
import org.springframework.messaging.simp.SimpMessageType
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

@Service
class BusinessLogic {

    @Autowired
    lateinit var users: UserManagerService

    @Autowired
    lateinit var outgoing: SimpMessagingTemplate

    fun sendBroadcastSystemMessage(message: String) {
        sendBroadcastMessage("/topic/chat", MessagePacket("SYSTEM", MessageType.SYSTEM_MESSAGE, "GLOBAL", message))
    }

    private fun sendMessageTo(user: UserEntity, channel: String, payload: Any) {
//        println("Sending packet to ${user.sessionId} (channel: ${channel}, payload: ${payload})")
        val headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE)
        with (headerAccessor) {
            sessionId = user.sessionId
            setLeaveMutable(true)
        }
        outgoing.convertAndSendToUser(user.sessionId, channel, payload, headerAccessor.messageHeaders)
    }

    private fun sendBroadcastMessage(channel: String, payload: Any) {
//        println("Sending broadcast to channel: ${channel}, payload: ${payload}")
        outgoing.convertAndSend(channel, payload)
    }

    @Scheduled(fixedRate = 250)
    fun gameLoop() {
        val positions = HashMap<String, MutableList<UserPosition>>()

        users.getAll().forEach { user ->
            val map = users.getMapOrDefault(user.place)

            positions.computeIfAbsent(map.name) { mutableListOf() }
                    .add(UserPosition(user.name, x = user.x, y = user.y))
        }

        users.getAll().forEach { user ->
            sendMessageTo(user,"/topic/positions", MapPositions(user.place, positions[user.place]!!))
        }
    }

    @Scheduled(fixedRate = 1000, initialDelay = 50)
    fun movementUpdate() {
        users.getAll().forEach { user ->
            val map = users.getMapOrDefault(user.place)
            val newX = user.x + user.dX
            val newY = user.y + user.dY

            if (map.isCoordinateValid(newX, newY)) {
                user.x = newX
                user.y = newY
            }
        }
    }

}
