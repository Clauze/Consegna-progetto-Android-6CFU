package com.example.embchat.data

import androidx.annotation.WorkerThread
import com.example.embchat.data.entity.Chat
import kotlinx.coroutines.flow.Flow

class ChatRepository(private val chatDao: ChatDao) {

    /**
     * Lista con tutte le chat
     */
    val allChat: Flow<List<Chat>> = chatDao.getChat()

    /**
     * Inserisce un nuovo numero di telefono
     */
    @WorkerThread
    suspend fun insert(chat: Chat){
        chatDao.insert(chat)
    }

    /**
     * Elimina una chat
     */
    @WorkerThread
    suspend fun delete(chat: Chat){
        chatDao.delete(chat)
    }

    /**
     * Ritorna una chat basandosi sul numero di telefono
     */
    @WorkerThread
    suspend fun findChat(phone: String): Chat?{
        return chatDao.findChat(phone)
    }

}