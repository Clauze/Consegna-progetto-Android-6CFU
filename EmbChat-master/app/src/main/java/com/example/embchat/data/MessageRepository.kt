package com.example.embchat.data

import androidx.annotation.WorkerThread
import com.example.embchat.data.entity.Message
import kotlinx.coroutines.flow.Flow

class MessageRepository(private val dao: MessageDao) {

    /**
     * Ottiene la lista dei messaggi per un certo numero di telefono
     */
    fun getMessageForChat(chatId: Int): Flow<List<Message>>{
        return dao.getMessages(chatId)
    }

    /**
     * Inserisce un nuovo messaggio
     */
    @WorkerThread
    suspend fun insertMessage(message: Message){
        dao.insertMessage(message)
    }

    /**
     * Elimina un certo numero di messaggi associati a una chat
     */
    @WorkerThread
    suspend fun deleteMessages(numberMessages: Int, chatId: Int){
        dao.deleteMessage(numberMessages, chatId)
    }
}