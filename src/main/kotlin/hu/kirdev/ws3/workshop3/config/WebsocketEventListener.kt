package hu.kirdev.ws3.workshop3.config

import hu.kirdev.ws3.workshop3.service.BusinessLogic
import hu.kirdev.ws3.workshop3.service.UserManagerService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.event.EventListener
import org.springframework.messaging.simp.stomp.StompHeaderAccessor
import org.springframework.messaging.support.GenericMessage
import org.springframework.stereotype.Component
import org.springframework.web.socket.messaging.SessionConnectEvent
import org.springframework.web.socket.messaging.SessionConnectedEvent
import org.springframework.web.socket.messaging.SessionDisconnectEvent
import org.springframework.web.socket.messaging.SessionSubscribeEvent

@Component
class WebsocketEventListener {

    @Autowired
    lateinit var users: UserManagerService

    @Autowired
    lateinit var system: BusinessLogic

    @EventListener
    fun handleConnect(event: SessionConnectEvent) {
        println("Connected: ${event.message}")
    }

    @EventListener
    fun handleConnected(event: SessionConnectedEvent) {
        val headerAccessor = StompHeaderAccessor.wrap(event.message)
        println("Connected: ${event.message}")

        var username = decodeNativeHeader(headerAccessor, "username")

        if (username == null) {
            username = "UNNAMED"
        }

        val user = users.getOrCreateUser(headerAccessor.sessionId!!, username)

        println("Received a new web socket connection by user: '${headerAccessor.sessionId}' -> ${user.name}")
        system.sendBroadcastSystemMessage("User ${user.name} joined the chat")
    }

    @EventListener
    fun handleDisconnect(event: SessionDisconnectEvent) {
        val headerAccessor = StompHeaderAccessor.wrap(event.message)
        println("Disconnected user with sessionId: $headerAccessor.sessionId")
    }

    @EventListener
    fun handleSubscribe(event: SessionSubscribeEvent) {
        println("Subscribe: $event.message")
    }

    fun decodeNativeHeader(headerAccessor: StompHeaderAccessor, header: String): String? {
        return try {
            val nativeHeaders = decodeNativeHeaders(headerAccessor)
            nativeHeaders.get(header)?.get(0)
        } catch (e: Exception) {
            null
        }
    }

    fun decodeNativeHeaders(headerAccessor: StompHeaderAccessor): Map<String, List<String>> {
        val simpConnectMessage = headerAccessor.messageHeaders["simpConnectMessage"] as GenericMessage<Object>?
        checkNotNull(simpConnectMessage)
        val nativeHeaders = simpConnectMessage.headers["nativeHeaders"] as Map<String, List<String>>?
        checkNotNull( nativeHeaders)
        return nativeHeaders
    }

}
