package com.darothub.musschatscreen.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.darothub.musschatscreen.model.Sender

@Entity(tableName = "message")
data class MessageEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Long?= null,
    val sender: Sender,
    val content: String,
    val timestamp: Long,
    val hasTail: Boolean = false
)