package com.darothub.musschatscreen.data.repository

import com.darothub.musschatscreen.data.dao.MessageDao
import com.darothub.musschatscreen.data.entity.MessageEntity
import com.darothub.musschatscreen.model.Message
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MessageRepositoryImpl(val dao: MessageDao) : MessageRepository {
    override fun fetchAllMessages(): Flow<List<Message>>  =
        dao.getAllMessages().map { it.toMessageList() }

    override suspend fun addMessage(messageEntity: MessageEntity) = dao.addMessage(messageEntity)

    private fun List<MessageEntity>.toMessageList(): List<Message> = this.map { it.toMessage() }

    private fun MessageEntity.toMessage(): Message = Message(
        id = id!!,
        sender = sender,
        content = content,
        timestamp = timestamp,
        hasTail = hasTail
    )
}