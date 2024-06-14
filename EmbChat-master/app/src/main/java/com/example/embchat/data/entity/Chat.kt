package com.example.embchat.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "chat_table",
    indices = [Index(value = ["phone"], unique = true)]
)
data class Chat (
    @PrimaryKey(autoGenerate = true)
    val chatId: Int = 0,

    @ColumnInfo(name = "phone")
    val phoneNumber: String,

)