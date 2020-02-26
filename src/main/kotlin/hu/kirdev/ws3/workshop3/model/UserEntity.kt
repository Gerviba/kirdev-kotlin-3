package hu.kirdev.ws3.workshop3.model

data class UserEntity (
    val sessionId: String,
    val name: String
) {

    var place: String = "index"
    var x: Int = 34
    var y: Int = 29
    var dX: Int = 0
    var dY: Int = 0

}