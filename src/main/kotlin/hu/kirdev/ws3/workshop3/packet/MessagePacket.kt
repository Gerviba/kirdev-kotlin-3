package hu.kirdev.ws3.workshop3.packet

enum class MessageType {
    CHAT_MESSAGE,
    SYSTEM_MESSAGE
}

data class MessagePacket (
    val sender: String,
    val type: MessageType,
    val tag: String,
    val message: String
)