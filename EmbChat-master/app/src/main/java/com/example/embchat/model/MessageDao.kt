package com.example.embchat.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.embchat.model.entity.Message
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageDao {
    /**
     * Ritorna la lista dei messaggi per una certa chat ordinati per timestamp
     */
    @Query("Select * from message_table where Chat = :chatId order by Timestamp")
    fun getMessages(chatId:Int): Flow<List<Message>>

    /**
     * Inserise un nuovo messaggio
     */
    @Insert
    suspend fun insertMessage(message: Message)

    /**
     * Elimina gli ultimi n messaggi della chat per ordine cronologico
     */
    @Query("Delete from message_table where messageId in (select messageId from message_table where Chat = :chatId order by Timestamp DESC limit :n)")
    suspend fun deleteMessage(n: Int, chatId: Int)
}