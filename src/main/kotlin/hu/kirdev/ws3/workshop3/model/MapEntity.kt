package hu.kirdev.ws3.workshop3.model

data class MapEntity(
    var name: String,
    var displayName: String,
    var color: String,
    var foreground: MutableList<String> = mutableListOf(),
    var teleportRegions: MutableList<TeleportRegions> = mutableListOf()
) {

    fun isCoordinateValid(x: Int, y: Int): Boolean {
        return foreground[y][x] != '#'
    }

}

data class TeleportRegions (
    var x: Int,
    var y: Int,
    var target: String,
    var targetX: Int,
    var targetY: Int
)

