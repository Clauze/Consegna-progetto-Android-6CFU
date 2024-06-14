package com.example.embchat.serializable

import java.io.Serializable

data class ChatDetail (
    val chatId: Int,

    val phoneNumber: String
) : Serializable