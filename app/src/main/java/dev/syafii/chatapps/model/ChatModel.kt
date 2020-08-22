package dev.syafii.chatapps.model

data class ChatModel(
    val sender: String? = null,
    val message: String? = null,
    var firebaseKey: String? = null
)