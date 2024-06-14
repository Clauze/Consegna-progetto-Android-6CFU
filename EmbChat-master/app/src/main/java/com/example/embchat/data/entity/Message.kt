package com.example.embchat.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "message_table", foreignKeys = [ForeignKey(entity = Chat::class, parentColumns = arrayOf("chatId"), childColumns = arrayOf("Chat"), onDelete = ForeignKey.CASCADE)])
data class Message (

    @PrimaryKey(autoGenerate = true)
    val messageId: Long =0,

    @ColumnInfo(name = "Chat")
    val chat: Int,

    @ColumnInfo(name = "Text")
    val text : String,

    @ColumnInfo(name = "sentByMe")
    val sentByMe : Boolean,

    @ColumnInfo(name = "Timestamp")
    val timestamp : Long

)