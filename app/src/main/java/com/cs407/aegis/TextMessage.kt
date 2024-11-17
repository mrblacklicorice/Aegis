package com.cs407.aegis

data class TextMessage(
    val id: String,
    val text: String,
    val isSent: Boolean  // true if this message was sent by the user
)

