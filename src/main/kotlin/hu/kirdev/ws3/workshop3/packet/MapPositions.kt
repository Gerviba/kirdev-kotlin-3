package hu.kirdev.ws3.workshop3.packet

data class UserPosition(val name: String, val x: Int, val y: Int)

data class MapPositions(
        val mapName: String,
        val positions: List<UserPosition>
)