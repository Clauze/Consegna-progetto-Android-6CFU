package com.example.embchat.model

import android.app.Application

class ChatApplication : Application() {
    val database by lazy { ChatRoomDatabase.getDatabase(this) }
    val repositoryChat by lazy { ChatRepository(database.chatDao()) }
    val repositoryMessage by lazy { MessageRepository(database.messageDao()) }
}