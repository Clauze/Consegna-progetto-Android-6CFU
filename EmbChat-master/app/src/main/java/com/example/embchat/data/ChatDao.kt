package com.example.embchat.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.embchat.data.entity.Chat
import com.example.embchat.data.entity.Message
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatDao {

    /**
     * Ritorna le chat ordinate per ultimo messaggio (nel caso in cui siano presenti messaggi)
     */
    @Query("SELECT chat_table.* FROM chat_table LEFT JOIN (SELECT chat, MAX(timestamp) AS max_timestamp FROM message_table GROUP BY chat) AS max_messages ON chat_table.chatId = max_messages.chat ORDER BY max_messages.max_timestamp DESC")
    fun getChat(): Flow<List<Chat>>

    /**
     * Inserisce una nuova chat, se già presente non viene inserita
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(chat: Chat)

    /**
     * Ritorna la chat riguardante un certo numero di telefono
     */
    @Query("Select * from chat_table where phone = :phoneNumber")
    suspend fun findChat(phoneNumber:String): Chat?

    /**
     * Elimina una chat
     */
    @Delete
    suspend fun delete(chat:Chat)

}