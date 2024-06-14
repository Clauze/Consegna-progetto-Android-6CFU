package com.example.embchat.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.embchat.data.entity.Chat
import com.example.embchat.data.entity.Message

@Database(entities = [Chat::class, Message::class], version = 1, exportSchema = false)
abstract class ChatRoomDatabase : RoomDatabase(){

    abstract fun chatDao(): ChatDao
    abstract fun messageDao(): MessageDao

    companion object{

        @Volatile
        private var INSTANCE: ChatRoomDatabase? = null

        fun getDatabase(context: Context): ChatRoomDatabase{
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ChatRoomDatabase::class.java,
                    "chat_database"
                ).fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance

                instance
            }
        }


    }
}