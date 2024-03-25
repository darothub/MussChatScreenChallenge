package com.darothub.musschatscreen.data.repository

import com.darothub.musschatscreen.data.entity.MessageEntity
import com.darothub.musschatscreen.model.Message
import kotlinx.coroutines.flow.Flow

interface MessageRepository {
    fun fetchAllMessages(): Flow<List<Message>>
    suspend fun addMessage(messageEntity: MessageEntity)
    suspend fun updateMessage(messageEntity: MessageEntity)
}