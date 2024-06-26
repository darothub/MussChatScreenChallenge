package com.darothub.musschatscreen.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.darothub.musschatscreen.data.entity.MessageEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageDao {
    @Query("SELECT * FROM message")
    fun getAllMessages():Flow<List<MessageEntity>>
    @Insert
    suspend fun addMessage(messageEntity: MessageEntity)
}