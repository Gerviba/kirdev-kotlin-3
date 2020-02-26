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

    fun sendBroadcastSystemMessage(message: String) {
    }

    private fun sendMessageTo(user: UserEntity, channel: String, payload: Any) {
        println("Sending packet to ${user.sessionId} (channel: ${channel}, payload: ${payload})")
    }

    private fun sendBroadcastMessage(channel: String, payload: Any) {
        println("Sending broadcast to channel: ${channel}, payload: ${payload}")
    }

    @Scheduled(fixedRate = 250)
    fun gameLoop() {
    }

    @Scheduled(fixedRate = 1000, initialDelay = 50)
    fun movementUpdate() {
    }

}
